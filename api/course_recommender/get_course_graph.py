import requests
from bs4 import BeautifulSoup
import re
import json 

COURSE_LIST_URL = "https://ucalendar.uwaterloo.ca/2223/COURSE/course-ECE.html"
COURSE_CODE_REGEX = "(?:[A-Z]{2,5})(?:\/[A-Z]{2,5})* +[0-9][0-9][0-9][A-C|L]?"
UNFORMATTED_SEPERATION_REGEX = "(?:[A-Z]{2,5}) (?:\d+)(?:{}\s*\d+(?:[A-C|L])?)*"
COURSE_CODE_REGEX_2 = "(?:[A-Z]{2,5}) (?:\d+)(?:,\s*\d+(?:[A-C|L])?)*"
OR_REGEX = "(?:[A-Z]{2,5}) (?:\d+)(?: (?:or)\s*\d+(?:[A-C|L])?)*"
BRACKET_REGEX = "\(.*\)"


def parse_seperated_course_names(regex, string):
    courses = []
    course_codes_2 = re.findall(regex, string)
    for course_code in course_codes_2:
        if ',' in course_code:
            codes = course_code.split(",")    
            letters = codes[0].split(" ")[0]
            for code in codes[1:]:
                courses.append(f"{letters}{code}")
            courses.append(codes[0])
    return courses


def parse_course_list(url):
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html5lib')
    courses = []
    centers = soup.find_all('center')
    divs = [c.find_all('div')[0] for c in centers]
    
    for div in divs:
        course_divs = div.find_all('div')
        course = []
        for course_div in course_divs:
            course.append(course_div.text)
        del course[3]
        del course[1]
        course = [c for c in course if c.strip() and '[' not in c]
        courses.append(course)
    return courses
        
def parse_antireqs(string, split=True):
    if split:
        string = string.split(":")[1].strip()
    course_codes = re.findall(COURSE_CODE_REGEX, string)

    courses = []
    for course_code in course_codes:
        if '/' in course_code:
            codes = course_code.split('/')
            num = codes[-1].split(" ")[-1]
            for code in codes[:-1]:
                courses.append(f"{code} {num}")
            courses.append(codes[-1])
        else:
            courses.append(course_code)
    courses += parse_seperated_course_names(COURSE_CODE_REGEX_2, string)
    courses = list(set(courses))
    return courses

def parse_prereqs(string):
    string = string.split(":")[1].strip()
    courses = parse_antireqs(string, split=False)
    ourses = parse_antireqs(string, split=False)
    ourses_2 = parse_seperated_course_names(OR_REGEX, string)
    or_courses = set(ourses+ourses_2)

    courses = set(courses) - or_courses
    res = {"or_courses": list(or_courses), "and_courses": list(courses)}
    return res

def parse_course_graph(courses):
    antireq = {}
    prereq = {}
    for course in courses: 
        course_code = re.findall(COURSE_CODE_REGEX, course[0])[0]
        for c in course:
            if 'Antireq' in c:
                antireqs = parse_antireqs(c)
                antireq[course_code] = antireqs
            if 'Prereq' in c:
                prereqs = parse_prereqs(c)
                prereq[course_code] = prereqs
    
    return prereq, antireq


if __name__ == '__main__':
    course_list = parse_course_list(COURSE_LIST_URL)
    prereq, antireq = parse_course_graph(course_list)

    with open('json_folder/antireq.json', 'w') as f:
        json.dump(antireq, f, indent=2)
    with open('json_folder/prereq.json', 'w') as f:
        json.dump(prereq, f, indent=2)