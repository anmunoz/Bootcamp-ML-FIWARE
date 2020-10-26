curl -X POST 'http://orion:1026/v2/entities' -H 'Content-Type: application/json'  -d @- <<EOF 
{
        "FlightNum": {
            "type": "int",
            "value": 15,
            "metadata": {}
        },
        "DepDelay": {
            "type": "Double",
            "value": 5.0,
            "metadata": {}
        },
        "Carrier": {
            "type": "String",
            "value": "AA",
            "metadata": {}
        },
        "FlightDate": {
            "type": "Date",
            "value": "2016-12-25",
            "metadata": {}
        },
        "Origin": {
            "type": "String",
            "value": "ATL",
            "metadata": {}
        },
        "Dest": {
            "type": "String",
            "value": "SFO",
            "metadata": {}
        },
        "Distance": {
            "type": "double",
            "value":2139.0,
            "metadata": {}
        },
        "DayOfYear": {
            "type": "int",
            "value":360,
            "metadata": {}
        },
        "DayOfMonth": {
            "type": "int",
            "value":25,
            "metadata": {}
        },
        "DayOfWeek": {
            "type": "int",
            "value":6,
            "metadata": {}
        },
        "Timestamp": {
            "type": "String",
            "value":"2020-10-23 13:19:04.527142",
            "metadata": {}
        },
        "predictionId": {
            "type": "String",
            "value":"3ba647df-25e6-4031-b16b-fa39b868c282",
            "metadata": {}
        },
        "socketId": {
            "type": "String",
            "value":"nn",
            "metadata": {}
        },
        "type": "flight",
        "id": "ReqFlightPrediction1"
}
EOF

curl -X POST 'http://orion:1026/v2/entities' -H 'Content-Type: application/json' -d @- <<EOF 
{
  "socketId": { "value": "AA", "type": "String"},
  "predictionId": { "value":"BB", "type": "String"},
  "predictionValue": { "value":0, "type": "Integer"},
        "type": "flight",
        "id": "ResFlightPrediction1"
}
EOF
