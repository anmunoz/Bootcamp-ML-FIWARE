FROM python:3

WORKDIR /usr/src/app

COPY ./requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
MKDIR /resource
WORKDIR /resource
RUN wget https://downloads.apache.org/spark/spark-3.0.1/spark-3.0.1-bin-hadoop2.7.tgz && tar -xvf spark-3.0.1-bin-hadoop2.7.tgz

RUN ls

CMD [ "python", "/resources/train_spark_mllib_model.py ." ]