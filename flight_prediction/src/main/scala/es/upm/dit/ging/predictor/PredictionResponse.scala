package es.upm.dit.ging.predictor

case class PredictionResponse (socketId: String, predictionId: String, predictionValue: String) {
  override def toString :String = s"""{
  "socketId": { "value": "${socketId}", "type": "String"},
  "predictionId": { "value":"${predictionId}", "type": "String"},
  "predictionValue": { "value":${predictionValue}, "type": "Integer"}
  }""".trim()
}