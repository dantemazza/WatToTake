import requests
from bs4 import BeautifulSoup
import re
COURSE_LIST_URL = "https://ucalendar.uwaterloo.ca/2223/COURSE/course-ECE.html"
COURSE_CODE_REGEX = "([A-Z]{2,5})(\/[A-Z]{2,5})* +[0-9][0-9][0-9][A-C]?"
# def parse_TE(url):
#     r = requests.get(url)
#     soup = BeautifulSoup(r.content, 'html5lib')
#     print(soup.prettify())

# def parse_NSE(url):
#     r = requests.get(url) 
#     soup = BeautifulSoup(r.content, 'html5lib')
#     print(soup.prettify())
    
# def parse_CSE(url):
#     r = requests.get(url)
#     soup = BeautifulSoup(r.content, 'html5lib')
#     soup.prettify()
#     headers = soup.find_all(re.compile('^h4$'))
#     print(headers[0].text)
#     for header in headers:
#         for sib in header.find_next_siblings():
#             if sib.name=="h4":
#                 break
#             else:
#                 print(sib.text)
#                 print(re.find(sib.text))
#         break

def parse_course_list(url):
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html5lib')
    # print(soup.prettify())
    #body.main.center.div.div
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
        
def parse_antireqs(string):
    string = string.split(":")[1].strip()
    print(string)

def parse_prereqs(string):
    string = string.split(":")[1].strip()
    print(string)

def parse_course_graph(courses):
    for course in courses: 
        course_code = re.findall(COURSE_CODE_REGEX, course[0])[0]
        for c in course:
            if 'Antireq' in c:
                antireqs = parse_antireqs(c)
            if 'Prereq' in c:
                # prereqs = parse_prereqs(c)
                pass
    antireq = {}
    prereq = {}
    return prereq, antireq


if __name__ == '__main__':
    course_list = parse_course_list(COURSE_LIST_URL)
    prereq, antireq = parse_course_graph(course_list)