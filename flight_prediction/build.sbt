name := "flight_prediction"

version := "0.1"

scalaVersion := "2.12.9"

val sparkVersion = "3.0.1"

mainClass in Compile := Some("scala.MakePrediction")

resolvers ++= Seq(
  "apache-snapshots" at "https://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.1"

)
