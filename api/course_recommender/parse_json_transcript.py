import json
from collections import defaultdict
import re
import random

### CONSTANTS ###

COURSE_NAME = "Course"
ELECTIVE_TYPE = "ElectiveType"
NUM_REQUIRED = "NumRequired"
ATTEMPTED = "Attempted"
EARNED = "Earned"
DESCRIPTION = "Description"
GRADE = "Grade"
FOURTH_YEAR_TE = "4th year TEs"
NSE = "NSEs"
LIST_A_CSE = "List A CSEs"
LIST_B_CSE = "List B CSEs"
LIST_C_CSE = "List C CSEs"
LIST_D_CSE = "List D CSEs"
LIST_ABCD_CSE = "List A/B/C/D CSEs"
LIST_A_COURSE_JSON = "/opt/api/course_recommender/json_folder/list_a_courses.json"
LIST_B_COURSE_JSON = "/opt/api/course_recommender/json_folder/list_b_courses.json"
LIST_C_COURSE_JSON = "/opt/api/course_recommender/json_folder/list_c_courses.json"
LIST_D_COURSE_JSON = "/opt/api/course_recommender/json_folder/list_d_courses.json"
NSE_COURSE_JSON = "/opt/api/course_recommender/json_folder/nse_courses.json"
TE_COURSE_JSON = "/opt/api/course_recommender/json_folder/te_courses.json"
REQUIREMENT_JSON = "/opt/api/course_recommender/json_folder/requirements.json"
FOURTH_YEAR_COURSE_CODE_REGEX = "[A-Z]{2,5} +[4][0-9][0-9][A-C|L]?"
COURSE_CODE_REGEX = "[A-Z]{2,5} +[0-9][0-9][0-9][A-C|L]?"
##################

#parse json and output number of courses left to take
def get_recommendations(transcript_json, requirements_json=REQUIREMENT_JSON, list_a_courses_json=LIST_A_COURSE_JSON,
                        list_b_courses_json=LIST_B_COURSE_JSON, list_c_courses_json=LIST_C_COURSE_JSON, list_d_courses_json=LIST_D_COURSE_JSON,
                        nse_courses_json=NSE_COURSE_JSON, te_courses_json=TE_COURSE_JSON):
    '''
    input: requirments json file path, transcript json file path, course json file path
    output: number of TE, CSE, NSE left to take
    '''

    #load transcript
    transcriptDict = {}
    transcriptList = transcript_json
    for course in transcriptList:
        transcriptDict[course[COURSE_NAME]] = course
    
    #load requirements
    requirementsDict = {}
    with open(requirements_json) as f:
        requirementsList = json.load(f)
    for requirement in requirementsList:
        requirementsDict[requirement[ELECTIVE_TYPE]] = int(requirement[NUM_REQUIRED])

    #load courses
    courseDict = {}
    cseDict = {}
    listacseDict = {}
    listbcseDict = {}
    listccseDict = {}
    listdcseDict = {}
    nseDict = {}
    teDict = {}
    with open(list_a_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        listacseDict[key] = value
        cseDict[key] = value

    with open(list_b_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        listbcseDict[key] = value
        cseDict[key] = value

    with open(list_c_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        listccseDict[key] = value
        cseDict[key] = value
    
    with open(list_d_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        listdcseDict[key] = value
        cseDict[key] = value

    with open(nse_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        nseDict[key] = value

    with open(te_courses_json) as f:
        courseList = json.load(f)
    for key, value in courseList.items():
        courseDict[key] = value
        teDict[key] = value

    
    returnDict = {}
    returnList = []
    #get te requirements and recommendations
    teReturnDict = {}
    te = check_num_TE(requirementsDict[FOURTH_YEAR_TE], transcriptDict, teDict)
    teReturnDict["Requirement_Name"] = FOURTH_YEAR_TE
    teReturnDict["Num_Requirements"] = str(te)
    teReturnDict["Courses"] = recommend_TE(transcriptDict, teDict, te)
    returnList.append(teReturnDict)
    #get nse requirements and recommendations
    nseReturnDict = {}
    nse = check_num_NSE(requirementsDict[NSE], transcriptDict, nseDict)
    nseReturnDict["Requirement_Name"] = NSE
    nseReturnDict["Num_Requirements"] = str(nse)
    nseReturnDict["Courses"] = recommend_NSE(transcriptDict, nseDict, nse)
    returnList.append(nseReturnDict)
    #get cse requirements and recommendations
    listccseReturnDict = {}
    totalcseReturnDict = {}
    listCCSE,  totalCSE = check_num_CSE(requirementsDict[LIST_ABCD_CSE], requirementsDict[LIST_C_CSE], transcriptDict, cseDict, listccseDict, listdcseDict)
    listCRecommendations, totalRecommendations = recommend_CSE(transcriptDict, cseDict, listccseDict, listCCSE, totalCSE)
    listccseReturnDict["Requirement_Name"] = LIST_C_CSE
    listccseReturnDict["Num_Requirements"] = str(listCCSE)
    listccseReturnDict["Courses"] = listCRecommendations
    totalcseReturnDict["Requirement_Name"] = LIST_ABCD_CSE
    totalcseReturnDict["Num_Requirements"] = str(totalCSE)
    totalcseReturnDict["Courses"] = totalRecommendations
    returnList.append(listccseReturnDict)
    returnList.append(totalcseReturnDict)
    returnDict["recommendations"] = returnList
    print(returnDict)
    return returnDict

#determine whether course was passed or failed
def pass_or_fail(course):
    '''
    input: course entry in dictionary
    output: True or False, True = passed
    '''
    if ATTEMPTED in course.keys():
        return True if course[ATTEMPTED] == course[EARNED] else False
    else:
        return True

#return elective type of course
def lookup_course(course, course_lookup_dict):
    '''
    input: single course JSON, course lookup dictionary
    output: elective type of course
    '''
    course_name = course[COURSE_NAME]
    if course_name in course_lookup_dict:
        return course_lookup_dict[course_name]
    else:
        return ""

def check_num_TE(num_required, taken_dict, te_dict):
    diff = num_required
    for course in taken_dict:
        if bool(re.search(FOURTH_YEAR_COURSE_CODE_REGEX, course)):
            if course in te_dict:
                diff -= 1
    return diff

def check_num_NSE(num_required, taken_dict, te_dict):
    diff = num_required
    for course in taken_dict:
        if bool(re.search(COURSE_CODE_REGEX, course)):
            if course in te_dict:
                diff -= 1
    return diff

#check_num_CSE(requirementsDict[LIST_ABCD_CSE], requirementsDict[LIST_C_CSE], transcriptDict, cseDict, listccseDict)
def check_num_CSE(total_reqs, c_reqs, taken_courses, cse_courses, list_c_courses, list_d_courses):
    ListCdiff = c_reqs
    ListABCDdiff = total_reqs
    c_count = all_except_check(list_c_courses, taken_courses)
    count = 0
    for course in taken_courses:
        if course in list_c_courses:
            ListCdiff -= 1
        elif course in cse_courses:
            ListABCDdiff -= 1
        if course in list_d_courses:
            count += 1
    if count >= 2:
        ListCdiff -= 1
    ListCdiff = ListCdiff - c_count
    return ListCdiff, ListABCDdiff 

def recommend_TE(taken_courses, te_courses, num):
    courses = []
    for course in te_courses:
        prereq_good = prereq_check(taken_courses, course)
        if not prereq_good:
            print("Cannot take " + course)
        if course not in taken_courses and bool(re.search(FOURTH_YEAR_COURSE_CODE_REGEX, course)) and prereq_good:
            courses.append({"course_code": course, "course_title": te_courses[course]})
    random.shuffle(courses)
    courses = courses[:num]
    return courses

def recommend_NSE(taken_courses, nse_courses, num):
    courses = []
    for course in nse_courses:
        if course not in taken_courses and bool(re.search(COURSE_CODE_REGEX, course)) and prereq_check(taken_courses, course):
            courses.append({"course_code": course, "course_title": nse_courses[course]})
    random.shuffle(courses)
    courses = courses[:num]
    return courses

def recommend_CSE(taken_courses, cse_courses, list_c_cse_courses , listCLeft, totalLeft):
    listCRecommendations = []
    totalRecommendations = []
    for course in cse_courses:
        if course not in taken_courses:
            prereq_good =  prereq_check(taken_courses, course)
            if not prereq_good:
                print("Cannot take " + course)
            if course in list_c_cse_courses and prereq_good:
                listCRecommendations.append({"course_code": course, "course_title": list_c_cse_courses[course]})
            else:
                totalRecommendations.append({"course_code": course, "course_title": cse_courses[course]})
    random.shuffle(listCRecommendations)
    listCRecommendations = listCRecommendations[:listCLeft]
    random.shuffle(totalRecommendations)
    totalRecommendations = totalRecommendations[:totalLeft]
    return listCRecommendations, totalRecommendations

def initialize_dict():
    emptyDict = defaultdict(int) 
    emptyDict[FOURTH_YEAR_TE] 
    emptyDict[NSE]
    emptyDict[LIST_A_CSE]
    emptyDict[LIST_B_CSE]
    emptyDict[LIST_C_CSE]
    emptyDict[LIST_D_CSE]
    emptyDict[LIST_ABCD_CSE]
    return emptyDict

def all_except_check(course_dict, taken_courses, ):
    total = 0
    for course in taken_courses:  
        letters = course.split()[0]
        nums = course.split()[1]
        if letters in course_dict:
            excluded = set(course_dict[letters])
            if nums not in excluded:
                total += 1
    return total 

def prereq_check(taken_courses, course_code):
    with open('./json_folder/antireq.json') as f:
        antireqs = json.load(f)
    with open('./json_folder/prereq.json') as f:
        prereqs = json.load(f)
    
    if course_code not in prereqs:
        return True
    
    required_prereqs = prereqs[course_code]
    valid_antireqs = []
    for course in taken_courses:
        if course in antireqs:
            valid_antireqs.extend(antireqs[course])
    valid_antireqs = set(valid_antireqs)
    and_prereqs = required_prereqs["and_courses"]
    or_prereqs = required_prereqs["or_courses"]
    for course in and_prereqs:
        if course not in taken_courses and course not in valid_antireqs:
            return False
    result = False if or_prereqs else True
    for course in or_prereqs:
        if course in taken_courses or course in valid_antireqs:
            result = True
            break
    return result
    


if __name__ == '__main__':
    transcript = './json_folder/transcript.json'
    with open(transcript) as f: 
        transcriptList = json.load(f)
    get_recommendations(transcriptList, "./json_folder/requirements.json", "./json_folder/list_a_courses.json", "./json_folder/list_b_courses.json",
    "./json_folder/list_c_courses.json", "./json_folder/list_d_courses.json", "./json_folder/nse_courses.json",
    "./json_folder/te_courses.json")