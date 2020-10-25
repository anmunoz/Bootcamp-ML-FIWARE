package es.upm.dit.ging.predictor

import java.sql.Timestamp
import java.sql.Date

case class PredictionRequest(origin: String,
                             flightNumber: String,
                             dayOfWeek: Int,
                             dayOfYear: Int,
                             dayOfMonth: Int,
                             dest: String,
                             depDelay: Double,
                             prediction: String,
                             timestamp: Timestamp,
                             flightDate: Date,
                             carrier: String,
                             uuid: String,
                             distance: Double,
                             carrierIndex: Double,
                             originIndex: Double,
                             destIndex: Double,
                             routeIndex: Double
                            )
