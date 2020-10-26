#! /bin/bash -eu

/spark/bin/spark-submit --jars ./flight_prediction/flight_prediction/lib/orion.spark.connector-1.2.2.jar --class  es.upm.dit.ging.predictor.MakePrediction --master  spark://spark-master:7077 --deploy-mode client ./flight_prediction/flight_prediction/target/scala-2.12/flight_prediction_2.12-0.1.jar --conf "spark.driver.extraJavaOptions=-Dlog4jspark.root.logger=WARN,console"


