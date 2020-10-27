curl -v orion:1026/v2/subscriptions -s -S -H 'Content-Type: application/json' -d @- <<EOF 
{
  "description": "A subscription to get info about ReqFlightPrediction1",
  "subject": {
    "entities": [
      {
        "id": "ReqFlightPrediction1",
        "type": "flight"
      }
    ],
    "condition": {
      "attrs": [
        "FlightNum",
        "DepDelay",
        "Carrier",
        "FlightDate",
        "Origin",
        "Dest",
        "Distance",
        "DayOfYear",
        "DayOfMonth",
        "DayOfWeek",
        "Timestamp",
        "predictionId",  
        "socketId" 
      ]
    }
  },
  "notification": {
    "http": {
      "url": "http://spark-master:9001"
    },
    "attrs": [
      "FlightNum",
        "DepDelay",
        "Carrier",
        "FlightDate",
        "Origin",
        "Dest",
        "Distance",
        "DayOfYear",
        "DayOfMonth",
        "DayOfWeek",
        "Timestamp",
        "predictionId",
        "socketId" 
    ]
  },
  "expires": "2040-01-01T14:00:00.00Z",
  "throttling": 5
}
EOF

curl -v orion:1026/v2/subscriptions -s -S -H 'Content-Type: application/json' -d @- <<EOF 
{
  "description": "A subscription to get info about ResFlightPrediction1",
  "subject": {
    "entities": [
      {
        "id": "ResFlightPrediction1",
        "type": "flight"
      }
    ],
    "condition": {
      "attrs": [
        "socketId",
        "predictionId",
        "predictionValue"
      ]
    }
  },
  "notification": {
    "http": {
      "url": "http://web:5000/flights/delays/response"
    },
    "attrs": [
      "socketId",
      "predictionId",
      "predictionValue"
    ]
  },
  "expires": "2040-01-01T14:00:00.00Z",
  "throttling": 5
}
EOF

curl -v orion:1026/v2/subscriptions -s -S -H 'Content-Type: application/json' -d @- <<EOF 
{
  "description": "A subscription to get info about ResFlightPrediction1",
  "subject": {
    "entities": [
      {
        "id": "ResFlightPrediction1",
        "type": "flight"
      }
    ],
    "condition": {
      "attrs": [
        "socketId",
        "predictionId",
        "predictionValue"
      ]
    }
  },
  "notification": {
    "http": {
      "url": "http://draco:5050/v2/notify"
    },
    "attrs": [
      "socketId",
      "predictionId",
      "predictionValue"
    ]
  },
  "expires": "2040-01-01T14:00:00.00Z",
  "throttling": 5
}
EOF
