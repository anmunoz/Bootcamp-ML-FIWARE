# FIWARE Machine Learning - Flight Predictor example

### Prerequisites

* Docker
* Docker Compose
* Python 3
* PIP 3

* Clone this project
```shell
git clone https://github.com/ging/Bootcamp-ML-FIWARE
cd Bootcamp-ML-FIWARE
```

* Run the whole scenario
```shell
python3 deploy-scenario.py
```

* Open browser in :
``` 
http://localhost:5000/flights/delays/predict_flights
```
* Fill the form 

Enter a nonzero departure delay, an ISO-formatted date (I used 2016-12-25, which was in the future at the time I was writing this), a valid carrier code (use AA or DL if you don’t know one), an origin and destination (my favorite is ATL → SFO)

* Predict!
