### 1. Get Linux
FROM python:3.7-slim
ENV DEBIAN_FRONTEND=noninteractive

RUN mkdir -p /usr/share/man/man1 /usr/share/man/man2

RUN apt-get update && \
apt-get install -y --no-install-recommends \
        openjdk-11-jre

# Prints installed java version, just for checking

RUN apt-get -y install gcc python3-dev wget
RUN java --version
### 3. Get Python, PIP
COPY ./requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

####
#### OPTIONAL : 4. SET JAVA_HOME environment variable, uncomment the line below if you need it

RUN update-alternatives --config java
RUN wget https://downloads.apache.org/spark/spark-3.0.1/spark-3.0.1-bin-hadoop2.7.tgz && tar -xvf spark-3.0.1-bin-hadoop2.7.tgz

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

#### 
CMD ["python", "test.py"]