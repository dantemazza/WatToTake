import requests
from bs4 import BeautifulSoup
import re
import json

URL = "https://ugradcalendar.uwaterloo.ca/page/ENG-Computer-Engineering"
CSE_URL = "https://ugradcalendar.uwaterloo.ca/page/ENG-BASc-and-BSE-Complementary-Studies-Engineering"

COURSE_CODE_REGEX = "[A-Z]{2,5} +[0-9][0-9][0-9][A-C|L]?"

LIST_A = "List A â€“ Impact Courses"
LIST_B = "List B â€“ Engineering Economics Courses"
LIST_C = "List C â€“ Humanities and Social Sciences Courses"
LIST_D = "List D â€“ Other Permissible Complementary Studies Courses"
NSE = "Natural Science Electives"
NOT_TE = {"Notes", "Work-term Reflections", "Complementary Studies Electives", "Ethics Requirement", "Natural Science Electives"}

LIST_A_JSON_PATH = "./json_folder/list_a_courses.json"
LIST_B_JSON_PATH = "./json_folder/list_b_courses.json"
LIST_C_JSON_PATH = "./json_folder/list_c_courses.json"
LIST_D_JSON_PATH = "./json_folder/list_d_courses.json"
NSE_PATH = "./json_folder/nse_courses.json"
TE_PATH = "./json_folder/te_courses.json"

def parse_TE(url):
    course_dict = {}
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html5lib')
    headers = soup.find_all(re.compile('^h4$'))
    for header in headers:
        for sib in header.find_next_siblings():
            if sib.name=="h4":
                break
            else:
                if header.text not in NOT_TE and sib.name=="ul":
                    for line in sib.text.splitlines():
                        course_name = re.findall(COURSE_CODE_REGEX, line)
                        if course_name:
                            # Course name
                            course_name = course_name[0]
                            # Description
                            line = re.sub(COURSE_CODE_REGEX, '', line)
                            description = line.split("0.")[0].strip()
                            description = ' '.join(description.split())
                            course_dict[course_name] = description
    if course_dict:
        with open(TE_PATH, 'w') as fp:
            json.dump(course_dict, fp, indent=2)

def parse_NSE(url):
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html5lib')
    headers = soup.find_all(re.compile('^h4$'))
    for header in headers:
        for sib in header.find_next_siblings():
            if sib.name=="h4":
                break
            else:
                if header.text == NSE and sib.name=="ul":

                    save_course_info_NSE(sib.text.splitlines(), NSE_PATH)

def save_course_info_NSE(text, json_path):
    course_dict = {}
    for line in text:
        if bool(re.search('and', line)):
            lines = line.split(" and ")
            for l in lines:
                course_name = re.findall(COURSE_CODE_REGEX, l)
                if course_name:
                    course_name = course_name[0]
                    # Description
                    l = re.sub(COURSE_CODE_REGEX, '', l)
                    description = l.split("0.")[0].strip()
                    description = ' '.join(description.split())
                    course_dict[course_name] = description
        else:
            # Course name
            course_name = re.findall(COURSE_CODE_REGEX, line)
            if course_name:
                course_name = course_name[0]
                # Description
                line = re.sub(COURSE_CODE_REGEX, '', line)
                description = line.split("0.")[0].strip()
                description = ' '.join(description.split())
                course_dict[course_name] = description

    if course_dict:
        with open(json_path, 'w') as fp:
            json.dump(course_dict, fp, indent=2)

def parse_CSE(url):
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html5lib')
    soup.prettify()
    headers = soup.find_all(re.compile('^h4$'))
    for header in headers:
        for sib in header.find_next_siblings():
            if sib.name=="h4":
                break
            else:
               # text = sib.text
                if header.text == LIST_A and sib.name=="ul":
                    print("here", header.text, sib.name)
                    save_course_info_A_B(sib.text.splitlines(), LIST_A_JSON_PATH)
                if header.text == LIST_B and sib.name=="ul":
                    print("here", header.text, sib.name)
                    save_course_info_A_B(sib.text.splitlines(), LIST_B_JSON_PATH)
                if header.text == LIST_C:
                    print("here", header.text, sib.name)
                    save_course_info_C(sib.text.splitlines(), LIST_C_JSON_PATH)
                if header.text == LIST_D:
                    print("here", header.text, sib.name)
                    save_course_info_D(sib.text.splitlines(), LIST_D_JSON_PATH)
                    
def save_course_info_A_B(text, json_path):
    course_dict = {}
    for line in text:
        course_name = re.findall(COURSE_CODE_REGEX, line)
        if course_name:
            # Course name
            course_name = course_name[0]
            # Description
            line = re.sub(COURSE_CODE_REGEX, '', line)
            description = line.split("0.")[0].strip()
            description = ' '.join(description.split())
            course_dict[course_name] = description
    if course_dict:
        with open(json_path, 'w') as fp:
            json.dump(course_dict, fp, indent=2)

def save_course_info_C(text, json_path):
    course_dict = {}
    for line in text:
        if bool(re.search('except', line)):
            course_names = re.findall(COURSE_CODE_REGEX, line)
            if course_names:
                course_prefix = re.findall('[A-Z]{2,5}', course_names[0])
                course_dict[course_prefix[0]] = course_names
        elif bool(re.search('All', line)):
            course_names = re.findall('[A-Z]{2,5}', line)
            if course_names:
                course_prefix = course_names[0]
                course_dict[course_prefix] = []  
        else:
            course_names = re.findall(COURSE_CODE_REGEX, line)
            for course_name in course_names:
                course_dict[course_name] = ""
    if course_dict:
        with open(json_path, 'w') as fp:
            json.dump(course_dict, fp, indent=2)

def save_course_info_D(text, json_path):
    course_dict = {}
    for line in text:
        if not bool(re.search('except', line)):
            course_names = re.findall(COURSE_CODE_REGEX, line)
            for course_name in course_names:
                course_dict[course_name] = ""
    if course_dict:
        with open(json_path, 'w') as fp:
            json.dump(course_dict, fp, indent=2)

if __name__ == '__main__':
    parse_TE(URL)
    parse_NSE(URL)
    parse_CSE(CSE_URL)