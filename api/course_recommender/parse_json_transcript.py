import json
from collections import defaultdict
from lib2to3.pgen2.pgen import DFAState

from pydantic import Json

### CONSTANTS ###

COURSE_NAME = "Course"
ELECTIVE_TYPE = "ElectiveType"
NUM_REQUIRED = "NumRequired"
ATTEMPTED = "Attempted"
EARNED = "Earned"
DESCRIPTION = "Description"
GRADE = "Grade"
FOURTH_YEAR_TE = "4th year TE"
NSE = "NSE"
LIST_A_CSE = "List A CSE"
LIST_B_CSE = "List B CSE"
LIST_C_CSE = "List C CSE"
LIST_D_CSE = "List D CSE"
LIST_ABCD_CSE = "List A/B/C/D CSE"
COURSE_JSON = "./json_folder/courses.json"
REQUIREMENT_JSON = "./json_folder/requirements.json"
##################

#parse json and output number of courses left to take
def get_recommendations(transcript_json, requirements_json=REQUIREMENT_JSON, courses_json=COURSE_JSON):
    '''
    input: requirments json file path, transcript json file path, course json file path
    output: number of TE, CSE, NSE left to take
    '''

    #load transcript
    transcriptDict = {}
    with open(transcript_json) as f:
        transcriptList = json.load(f)
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
    with open(courses_json) as f:
        courseList = json.load(f)
    for course in courseList:
        courseDict[course[COURSE_NAME]] = course[ELECTIVE_TYPE]

    #count number of each elective type taken, store in counterDict
    counterDict = initialize_dict()
    for course in transcriptDict.keys():
        if pass_or_fail(transcriptDict[course]):
            course_type = lookup_course(transcriptDict[course], courseDict)
            counterDict[course_type] += 1
        
    returnDict = {}
    #get te requirements and recommendations
    te = check_E(requirementsDict, counterDict, FOURTH_YEAR_TE)
    returnDict["TE Requirements"] = str(te)
    returnDict["TE Recommendations"] = str(recommend_E(transcriptDict, courseDict, te, FOURTH_YEAR_TE))
    #get nse requirements and recommendations
    nse = check_E(requirementsDict, counterDict, NSE)
    returnDict["NSE Requirements"] = str(nse)
    returnDict["NSE Recommendations"] = str(recommend_E(transcriptDict, courseDict, nse, NSE))
    #get cse requirements and recommendations
    cse = check_CSE(requirementsDict, counterDict)
    returnDict["CSE Requirements"] = str(cse)
    returnDict["CSE Recommendations"] = str(recommend_CSE(transcriptDict, courseDict, cse))
    print(json.dumps(returnDict))
    return json.dumps(returnDict)

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

def check_E(requirements, taken_courses, elective_type):
    diff = requirements[elective_type] - taken_courses[elective_type]
    return diff if requirements[elective_type] > taken_courses[elective_type] else 0

def check_CSE(requirements, taken_courses):
    ListCdiff = requirements[LIST_C_CSE] - taken_courses[LIST_C_CSE]
    ListABCDdiff = requirements[LIST_ABCD_CSE] - taken_courses[LIST_A_CSE] 
    - taken_courses[LIST_B_CSE] - taken_courses[LIST_C_CSE] - taken_courses[LIST_D_CSE]

    #2 List D CSE => 1 List C CSE
    if taken_courses[LIST_D_CSE] >= 2:
        ListCdiff -= 1

    return [ListABCDdiff, ListCdiff]

def recommend_E(taken_courses, total_courses, num_left, course_type):
    recommendations = []
    for course in total_courses:
        if num_left > 0 and course not in taken_courses and total_courses[course] == course_type:
            print(str(course))
            recommendations.append(course)
            num_left -= 1
    return recommendations

def recommend_CSE(taken_courses, total_courses, num_left):
    listCLeft = num_left[0]
    totalLeft = num_left[1]
    listCRecommendations = []
    totalRecommendations = []
    for course in total_courses:
        if (listCLeft > 0 or totalLeft > 0) and course not in taken_courses:
            if listCLeft > 0 and total_courses[course] == LIST_C_CSE:
                print(str(course))
                listCRecommendations.append(course)
                if totalLeft > 0:
                    totalRecommendations.append(course) 
                    totalLeft -= 1
                listCLeft -= 1
            elif totalLeft > 0 and (total_courses[course] == LIST_A_CSE or 
            total_courses[course] == LIST_B_CSE or total_courses[course] == LIST_D_CSE):
                print(str(course))
                totalRecommendations.append(course)
                totalLeft -= 1
    return [listCRecommendations, totalRecommendations]

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



if __name__ == '__main__':
    transcript = './json_folder/transcript.json'
    get_recommendations(transcript)