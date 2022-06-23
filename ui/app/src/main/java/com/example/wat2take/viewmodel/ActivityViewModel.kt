package com.example.wat2take.viewmodel

import com.example.wat2take.repository.ImplRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wat2take.Course
import com.example.wat2take.CourseList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val implRepository: ImplRepository): ViewModel() {
    var courses : MutableLiveData<String> = MutableLiveData(null)

    var courseList : MutableLiveData<CourseList> = MutableLiveData()

    fun saveData() {
        viewModelScope.launch(Dispatchers.IO) {
            val courseListJson: String = "[{\n" +
                    "  \"id\": 0,\n" +
                    "  \"name\": \"ECE240\",\n" +
                    "  \"grade\": 51.2,\n" +
                    "  \"title\": \"Circuits 2\"\n" +
                    "},\n" +
                    "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"name\": \"ECE160\",\n" +
                    "  \"grade\": 62.3,\n" +
                    "  \"title\": \"Electromagnetic Physics\"\n" +
                    "}\n" +
                    "]"
            implRepository.saveCourseList(
                CourseList(
                    courses = courseListJson!!
                )
            )
        }
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            implRepository.getCourseList().collect {
                courseList.postValue(it)
            }
        }
    }
}