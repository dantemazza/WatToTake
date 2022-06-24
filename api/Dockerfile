FROM python:3.8
EXPOSE 5000
RUN apt-get update && apt-get -y install qpdf poppler-utils && apt-get install -y build-essential libpoppler-cpp-dev pkg-config python-dev
RUN apt-get install -y libqpdf-dev
RUN apt-get install -y tesseract-ocr
RUN apt-get install -y ghostscript
COPY requirements.txt .
RUN pip3 install --upgrade pip
RUN pip3 install -r requirements.txt
WORKDIR /opt/api
ENV PYTHONPATH .