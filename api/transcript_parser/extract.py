import os
import re
import pdfplumber
import json
import ocrmypdf
import PyPDF2
import io
from uuid import uuid4

def read_pdfplumber(file_name):
    with pdfplumber.open(file_name) as pdf:
        pages = [page.extract_text() for page in pdf.pages]
    return pages

def ocr(file_path, save_path):
    ocrmypdf.ocr(file_path, save_path)

def fan_out(file):
    UPLOAD_FOLDER = "uploads"
    if not os.path.exists(UPLOAD_FOLDER):
        os.mkdir(UPLOAD_FOLDER)
    file_uuid = uuid4()
    file_path = f"{UPLOAD_FOLDER}/{file_uuid}.pdf"
    file.save(file_path)
    file.close()
    return file_path

def transcript_to_json(file_name):
    SCHEMA = ["Course", "Description", "Attempted", "Earned", "Grade"]
    COURSE_CODE_REGEX = "[A-Z]{3,5} +[0-9][0-9][0-9]"
    OCR_PDF_PATH = f"{file_name[:-4]}-ocr.pdf"

    courses = []
    ocr(file_name, OCR_PDF_PATH)
    texts = read_pdfplumber(OCR_PDF_PATH)
    for text in texts:
        lines = text.splitlines()
        for line in lines:
            course_code = re.findall(COURSE_CODE_REGEX, line)
            if course_code:
                course = {}
                # Course
                course_code = ' '.join(course_code[0].split())
                course[SCHEMA[0]] = course_code
                courses.append(course)

                # Description
                line = re.sub(COURSE_CODE_REGEX, '', line)
                description = line.split("0.")[0].strip()
                description = ' '.join(description.split())
                course[SCHEMA[1]] = description

                #Attempted, Earned, Grade
                if "0." in line:
                    nums = "0." + line.split("0.", 1)[1]
                    nums = nums.split()
                    course[SCHEMA[2]] = nums[0]
                    course[SCHEMA[3]] = nums[1]
                    course[SCHEMA[4]] = nums[2]
    return courses

if __name__ == "__main__":
    print(transcript_to_json("../test_data/SSR_TSRPT.pdf"))