import os
import time 

MYDIR = ("./models/spark_random_forest_classifier.flight_delays.5.0.bin/metadata")
data = ("./data/simple_flight_delay_features.jsonl.bz2")
distances = ("./data/origin_dest_distances.jsonl")

CHECK_DATA = os.path.exists(data and distances)
CHECK_FOLDER = os.path.isdir(MYDIR)

if not CHECK_DATA:
	print('Downloading the data...')
	os.system('./resources/download_data.sh')
if not CHECK_FOLDER:
    print('Start training containers')
    os.system('docker-compose -f docker-compose-training.yml up')

while not os.path.isdir(MYDIR):
	print('Still training...')
	time.sleep(30)

if CHECK_FOLDER:
    os.system('docker-compose -f docker-compose-training.yml down')
    time.sleep(3)
    print('Start prediction containers')
    os.system('docker-compose -f docker-compose-prediction.yml up')




