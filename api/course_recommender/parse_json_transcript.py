import json
from collections import defaultdict

### CONSTANTS ###

COURSE_NAME = "Course"
ELECTIVE_TYPE = "ElectiveType"
NUM_REQUIRED = "NumRequired"
ATTEMPTED = "Attempted"
EARNED = "Earned"
DESCRIPTION = "Description"
GRADE = "Grade"
FOURTH_YEAR_TE = "4th year TE"
LIST_1_NSE = "List 1 NSE"
LIST_2_NSE = "List 2 NSE"
LIST_A_CSE = "List A CSE"
LIST_B_CSE = "List B CSE"
LIST_C_CSE = "List C CSE"
LIST_D_CSE = "List D CSE"
LIST_ABCD_CSE ="List A/B/C/D CSE"

##################

#parse json and output number of courses left to take
def parse_json(requirements_json, transcript_json, courses_json):
    '''
    input: requirments json file path, transcript json file path, course json file path
    output: number of TE, CSE, NSE left to take
    '''

    #load transcript
    with open(transcript_json) as f:
        transcriptDict = json.load(f)

    #load requirements
    requirementsDict = {}
    with open(requirements_json) as f:
        requirementsList = json.load(f)
    for requirement in requirementsList:
        requirementsDict[requirement[ELECTIVE_TYPE]] = int(requirement[NUM_REQUIRED])

    #load courses
    courseDict = {}
    with open(courses_json) as f:
        courseList = json.load(f)
    for course in courseList:
        courseDict[course[COURSE_NAME]] = course[ELECTIVE_TYPE]

    #count number of each elective type taken, store in counterDict
    counterDict = initialize_dict()
    for course in transcriptDict:
        if pass_or_fail(course):
            course_type = lookup_course(course, courseDict)
            counterDict[course_type] += 1
    print(requirementsDict)
    print(counterDict)

    print("Number of TEs left:" + str(check_TE(requirementsDict, counterDict)))
    print("Number of [List1NSE, List2NSE] left:" + str(check_NSE(requirementsDict, counterDict)))
    print("Number of Total CSEs, List C CSE, List D CSE left:" + str(check_CSE(requirementsDict, counterDict)))
    
    return

#determine whether course was passed or failed
def pass_or_fail(course):
    '''
    input: course entry in dictionary
    output: True or False, True = passed
    '''
    return True if course[ATTEMPTED] == course[EARNED] else False

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
        raise Exception("course name doesn't exist in list of available courses")

def check_TE(requirements, taken_courses):
    diff = requirements[FOURTH_YEAR_TE] - taken_courses[FOURTH_YEAR_TE]
    return diff if requirements[FOURTH_YEAR_TE] > taken_courses[FOURTH_YEAR_TE] else 0

def check_NSE(requirements, taken_courses):
    List1diff = requirements[LIST_1_NSE] - taken_courses[LIST_1_NSE]
    List2diff = requirements[LIST_2_NSE] - taken_courses[LIST_2_NSE]
    if List1diff > 0 and List2diff > 0:
        return [[List1diff, List2diff]]
    elif List1diff > 0:
        return [[List1diff, 0]]
    else:
        #List 1 NSE => List 2 NSE
        return [[0, List2diff], [List2diff, 0]]

def check_CSE(requirements, taken_courses):
    ListCdiff = requirements[LIST_C_CSE] - taken_courses[LIST_C_CSE]
    ListDdiff = requirements[LIST_D_CSE] - taken_courses[LIST_D_CSE]
    ListABCDdiff = requirements[LIST_ABCD_CSE] - taken_courses[LIST_A_CSE] 
    - taken_courses[LIST_B_CSE] - taken_courses[LIST_C_CSE] - taken_courses[LIST_D_CSE]

    #2 List D CSE => 1 List C CSE
    if ListDdiff <= -2:
        ListDdiff += 2
        ListCdiff -= 1

    return ListABCDdiff, ListCdiff, ListDdiff

def initialize_dict():
    emptyDict = defaultdict(int) 
    emptyDict[FOURTH_YEAR_TE] 
    emptyDict[LIST_1_NSE]
    emptyDict[LIST_2_NSE]
    emptyDict[LIST_A_CSE]
    emptyDict[LIST_B_CSE]
    emptyDict[LIST_C_CSE]
    emptyDict[LIST_D_CSE]
    emptyDict[LIST_ABCD_CSE]
    return emptyDict



if __name__ == '__main__':
    requirements = './json_folder/requirements.json'
    transcript = './json_folder/transcript.json'
    courses = './json_folder/courses.json'
    parse_json(requirements, transcript, courses)