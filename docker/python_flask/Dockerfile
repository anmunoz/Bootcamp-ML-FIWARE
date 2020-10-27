### 1. Get Linux
FROM python:3.7-slim

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && \
    apt-get install -y  gcc python3-dev wget curl
COPY ./requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

ENV PROJECT_HOME=./
EXPOSE 5000

#### 
CMD ["python", "test.py"]