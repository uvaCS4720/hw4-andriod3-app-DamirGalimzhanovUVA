package edu.nd.pmcburne.hello

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class MainUIState(
    val counterValue: Int
)

class MainViewModel(
    private val dao: PlacemarkDao
) : ViewModel() {

    var allTags = mutableStateListOf<Tag>()
    var allPlacemarks = mutableStateListOf<Placemark>()
    var selectedTag by mutableStateOf("core")


    fun selectTag(tag: String){
        selectedTag = tag

    }

    fun fetchAllTags(){

        viewModelScope.launch{
            val tags = dao.getTags().distinctBy { it.tagName }.sortedBy { it.tagName }
            allTags.clear()
            allTags.addAll(tags)
            println("We are able to get the tags in viewModel $allTags")
        }

//        runBlocking {
//            val deferredAllTags = async() {
//                dao.getTags()
//            }
//            newTags = deferredAllTags.await()
//        }
//        println("We are able to get the tags in viewModel $allTags")
    }

    fun fetchPlacemarks(){
        viewModelScope.launch{
            val placemarks = dao.getPlacemarksWithTag(selectedTag)
            println("We are able to get the placemarks in viewModel $placemarks")
            allPlacemarks.clear()
            allPlacemarks.addAll(placemarks)
            println("We are able to get the placemarks in viewModel $allPlacemarks")
        }
    }


}