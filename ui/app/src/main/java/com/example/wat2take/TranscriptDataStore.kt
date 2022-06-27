package com.example.wat2take

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TranscriptDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("courseList")
        const val DEFAULT_COURSES_VAL = "[]"
        val COURSE_LIST_KEY = stringPreferencesKey("courses")
    }

    //get course list
    val getCourseList: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[COURSE_LIST_KEY] ?: DEFAULT_COURSES_VAL;
        }

    //save course list into datastore
    suspend fun saveCourseList() {
        val courseList: String = "[{\n" +
                "  \"id\": 0,\n" +
                "  \"Course\": \"ECE240\",\n" +
                "  \"grade\": 51.2,\n" +
                "  \"Description\": \"Circuits 2\"\n" +
                "},\n" +
                "{\n" +
                "  \"id\": 2,\n" +
                "  \"Course\": \"ECE160\",\n" +
                "  \"grade\": 62.3,\n" +
                "  \"Description\": \"Electromagnetic Physics\"\n" +
                "}\n" +
                "]"
        context.dataStore.edit { it.clear() }
        context.dataStore.edit { preferences ->
            preferences[COURSE_LIST_KEY] = courseJson
        }
    }

    val courseJson = "[{\n" +
            "\t'Course': 'CHE 100',\n" +
            "\t'Description': 'Chemical  Engineering  Concepts  1',\n" +
            "\t'Attempted': '0.75',\n" +
            "\t'Earned': '0.75',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'CHE 102',\n" +
            "\t'Description': 'Chemistry for  Engineers',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'MATH 115',\n" +
            "\t'Description': 'Linear Algebra for Engineering',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'MATH 116',\n" +
            "\t'Description': 'Calculus  1  for Engineering',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'PHYS 115',\n" +
            "\t'Description': 'Mechanics',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 106',\n" +
            "\t'Description': 'Electricity and  Magnetism',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 108',\n" +
            "\t'Description': 'Discrete  Mathematics and  Logic  1',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 124',\n" +
            "\t'Description': 'Digital  Circuits and  Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 140',\n" +
            "\t'Description': 'Linear Circuits',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 192',\n" +
            "\t'Description': 'Engineering  Economics and  Impact on  Society',\n" +
            "\t'Attempted': '0.25',\n" +
            "\t'Earned': '0.25',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'MATH 119',\n" +
            "\t'Description': 'Calculus 2 for Engineering',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 150',\n" +
            "\t'Description': 'Fundamentals of  Programming',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 109',\n" +
            "\t'Description': 'Materials Chemistry for Engineers',\n" +
            "\t'Attempted': '0.25',\n" +
            "\t'Earned': '0.25',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 204',\n" +
            "\t'Description': 'Numerical  Methods',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 205',\n" +
            "\t'Description': 'Advanced  Calculus  1  for  Electrical  and  Computer',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 222',\n" +
            "\t'Description': 'Digital  Computers',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 240',\n" +
            "\t'Description': 'Electronic Circuits  1',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 250',\n" +
            "\t'Description': 'Algorithms and  Data Structures',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'WKRPT 201',\n" +
            "\t'Description': 'Work-term  Report',\n" +
            "\t'Attempted': '0.13',\n" +
            "\t'Earned': '0.13',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 203',\n" +
            "\t'Description': 'Probability Theory and  Statistics  1',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 207',\n" +
            "\t'Description': 'Signals and  Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 208',\n" +
            "\t'Description': 'Discrete  Mathematics and  Logic 2',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 224',\n" +
            "\t'Description': 'Embedded  Microprocessor Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 252',\n" +
            "\t'Description': 'Systems  Programming  and Concurrency',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ITAL 101',\n" +
            "\t'Description': 'Introduction to  Italian  Language  1',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'WKRPT 301',\n" +
            "\t'Description': 'Work-term  Report',\n" +
            "\t'Attempted': '0.13',\n" +
            "\t'Earned': '0.13',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 298',\n" +
            "\t'Description': 'Instrumentation  and  Prototyping  Laboratory',\n" +
            "\t'Attempted': '0.25',\n" +
            "\t'Earned': '0.25',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 318',\n" +
            "\t'Description': 'Communication  Systems  1',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 327',\n" +
            "\t'Description': 'Digital  Hardware  Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 350',\n" +
            "\t'Description': 'Real-Time Operating  Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 380',\n" +
            "\t'Description': 'Analog  Control  Systems',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'WKRPT 401',\n" +
            "\t'Description': 'Work-term  Report',\n" +
            "\t'Attempted': '0.13',\n" +
            "\t'Earned': '0.13',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'CLAS 104',\n" +
            "\t'Description': 'Classical  Mythology',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 307',\n" +
            "\t'Description': 'Probability Theory and  Statistics 2',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 320',\n" +
            "\t'Description': 'Computer Architecture',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 351',\n" +
            "\t'Description': 'Compilers',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 358',\n" +
            "\t'Description': 'Computer Networks',\n" +
            "\t'Attempted': '0.50',\n" +
            "\t'Earned': '0.50',\n" +
            "\t'Grade': 'REDACTED'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 452',\n" +
            "\t'Description': 'Software  Design  and Architectures'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 454',\n" +
            "\t'Description': 'Distributed  Computing'\n" +
            "}, {\n" +
            "\t'Course': 'ECE 498',\n" +
            "\t'Description': 'A  Engineering  Design  Project'\n" +
            "}, {\n" +
            "\t'Course': 'ECON 101',\n" +
            "\t'Description': 'Introduction to  Microeconomics'\n" +
            "}, {\n" +
            "\t'Course': 'REC 100',\n" +
            "\t'Description': 'Introduction to the  Study of  Recreation  and  Leisure'\n" +
            "}]"

}