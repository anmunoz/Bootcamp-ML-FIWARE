package es.upm.dit.ging.predictor

import java.sql.Timestamp
import java.sql.Date
import org.apache.spark.ml.classification.RandomForestClassificationModel
import org.apache.spark.ml.feature.{Bucketizer, StringIndexerModel, VectorAssembler}
import org.apache.spark.sql.functions.{concat, from_json, lit}
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.fiware.cosmos.orion.spark.connector.{ContentType, HTTPMethod, OrionReceiver, OrionSink, OrionSinkObject}

object MakePrediction {
  final val URL_CB = "http://orion:1026/v2/entities/ResFlightPrediction1/attrs"
  final val CONTENT_TYPE = ContentType.JSON
  final val METHOD = HTTPMethod.PATCH
  final val BASE_PATH = "./flight_prediction"

  def main(args: Array[String]): Unit = {
    println("Fligth predictor starting...")

    val spark = SparkSession
      .builder
      .appName("StructuredNetworkWordCount")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    //Load the arrival delay bucketizer
    val base_path= BASE_PATH
    val arrivalBucketizerPath = "%s/models/arrival_bucketizer_2.0.bin".format(base_path)
    print(arrivalBucketizerPath.toString())
    val arrivalBucketizer = Bucketizer.load(arrivalBucketizerPath)
    val columns= Seq("Carrier","Origin","Dest","Route")

    //Load all the string field vectorizer pipelines into a dict
    val stringIndexerModelPath =  columns.map(n=> ("%s/models/string_indexer_model_"
      .format(base_path)+"%s.bin".format(n)).toSeq)
    val stringIndexerModel = stringIndexerModelPath.map{n => StringIndexerModel.load(n.toString)}
    val stringIndexerModels  = (columns zip stringIndexerModel).toMap

    // Load the numeric vector assembler
    val vectorAssemblerPath = "%s/models/numeric_vector_assembler.bin".format(base_path)
    val vectorAssembler = VectorAssembler.load(vectorAssemblerPath)

    // Load the classifier model
    val randomForestModelPath = "%s/models/spark_random_forest_classifier.flight_delays.5.0.bin".format(
      base_path)
    val rfc = RandomForestClassificationModel.load(randomForestModelPath)

    val ssc = new StreamingContext(spark.sparkContext, Seconds(5))

    val eventStream = ssc.receiverStream(new OrionReceiver(9001))
    val processedDataStream = eventStream
      .flatMap(event => event.entities)
      .map(ent => {println(ent)
        val origin = ent.attrs("Origin").value.toString
        println(origin+"kkkkkkkkkkkkkkkkkkk")
        val flightNumber = ent.attrs("FlightNum").value.toString
        val dayOfWeek = ent.attrs("DayOfWeek").value.toString.toInt
        val dayOfYear = ent.attrs("DayOfYear").value.toString.toInt
        val dayOfMonth = ent.attrs("DayOfMonth").value.toString.toInt
        val dest = ent.attrs("Dest").value.toString
        val depDelay = ent.attrs("DepDelay").value.toString.toDouble
        val timestamp = Timestamp.valueOf(ent.attrs("Timestamp").value.toString)
        val flightDate =   Date.valueOf(ent.attrs("FlightDate").value.toString)
        val carrier = ent.attrs("Carrier").value.toString
        val uuid = ent.attrs("predictionId").value.toString
        val distance = ent.attrs("Distance").value.toString.toDouble
        val socketId = ent.attrs("socketId").value.toString

        PredictionRequest(origin, flightNumber, dayOfWeek, dayOfYear, dayOfMonth, dest, depDelay,"",timestamp,flightDate,carrier,
        uuid,distance,0,0,0,0,socketId)
      })

    val predictionDataStream = processedDataStream
      .transform(rdd => {
          val df = rdd.toDF("Origin","FlightNum","DayOfWeek","DayOfYear"
            ,"DayOfMonth","Dest","DepDelay","Prediction","Timestamp"
            ,"FlightDate","Carrier","UUID","Distance","Carrier_index"
            ,"Origin_index","Dest_index","Route_index","socketId")
          df.printSchema()
          df.show()

          // DataFrame for Vectorizing string fields with the corresponding pipeline for that column
          val flightFlattenedDf = df.selectExpr("Origin",
            "DayOfWeek","DayOfYear","DayOfMonth","Dest",
            "DepDelay","Timestamp","FlightDate",
            "Carrier","UUID","Distance")
          flightFlattenedDf.printSchema()
          flightFlattenedDf.show()


          val predictionRequestsWithRouteMod = flightFlattenedDf.withColumn(
            "Route",
            concat(
              flightFlattenedDf("Origin"),
              lit('-'),
              flightFlattenedDf("Dest")
            )
          )

          // Dataframe for Vectorizing numeric columns
           val flightFlattenedDf2 = df.selectExpr("Origin",
             "DayOfWeek","DayOfYear","DayOfMonth","Dest",
             "DepDelay","Timestamp","FlightDate",
             "Carrier","UUID","Distance",
             "cast('Carrier_index' as double) Carrier_index" ,"cast('Origin_index' as double) Origin_index",
             "cast('Dest_index' as double) Dest_index","cast('Route_index' as double) Route_index","socketId")

           val predictionRequestsWithRouteMod2 = flightFlattenedDf2.withColumn(
             "Route",
             concat(
               flightFlattenedDf2("Origin"),
               lit('-'),
               flightFlattenedDf2("Dest")
             )
           )

           // Vectorize string fields with the corresponding pipeline for that column
           // Turn category fields into categoric feature vectors, then drop intermediate fields
           val predictionRequestsWithRoute = stringIndexerModel.map(n=>n.transform(predictionRequestsWithRouteMod))

            //Vectorize numeric columns: DepDelay, Distance and index columns
           val vectorizedFeatures = vectorAssembler.setHandleInvalid("keep").transform(predictionRequestsWithRouteMod2)

            // Drop the individual index columns
            val finalVectorizedFeatures = vectorizedFeatures
              .drop("'Carrier_index'")
              .drop("'Origin_index'")
              .drop("'Dest_index'")
              .drop("'Route_index'")

          // Inspect the finalized features
            finalVectorizedFeatures.printSchema()


            // Make the prediction
            val predictions = rfc.transform(finalVectorizedFeatures)
              .drop("Features_vec")

            // Drop the features vector and prediction metadata to give the original fields
            val finalPredictions = predictions.drop("indices")
              .drop("values")
              .drop("rawPrediction")
              .drop("probability")

            // Inspect the output
            finalPredictions.printSchema()
            finalPredictions.show()

        finalPredictions.toJavaRDD })
        .map(pred=>
        PredictionResponse(
          pred.get(15).toString,
          pred.get(9).toString,
          pred.get(17).toString
        ))
      // Convert the output to an OrionSinkObject and send to Context Broker
        val sinkDataStream = predictionDataStream
        .map(res => OrionSinkObject(res.toString, URL_CB, CONTENT_TYPE, METHOD))

    // Add Orion Sink
    OrionSink.addSink(sinkDataStream)
    sinkDataStream.print()
    predictionDataStream.print()
    ssc.start()
    ssc.awaitTermination()
  }

}