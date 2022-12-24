package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.Downloads
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.domain.mainAc.HomeRepository
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: HomeRepository
): ViewModel() {
    private val _getAllPost = MutableLiveData<UiState<ArrayList<Post>>>()
    val getAllPost :LiveData<UiState<ArrayList<Post>>>
        get() = _getAllPost
    fun getAllPost(){
        _getAllPost.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.getAllPost { list->
                _getAllPost.value = list
            }
        }
    }

    private val _getClickedLikeUsers = MutableLiveData<UiState<ArrayList<Likes>>>()
    val getClickedLikeUsers:LiveData<UiState<ArrayList<Likes>>>
        get() = _getClickedLikeUsers
    fun getClickedLikeUsers(randomKey:String){
        _getClickedLikeUsers.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            repository.getClickedLikeUsers(randomKey){ list->
                _getClickedLikeUsers.value = list
            }
        }
    }

    private val _addLike= MutableLiveData<UiState<String>>()
    val addLike:LiveData<UiState<String>>
        get()=_addLike
    fun addLike(key:String, likes: Likes){
        _addLike.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.plusLike(key, likes){
                _addLike.value=it
            }
        }
    }

    private val _addDownload = MutableLiveData<UiState<String>>()
    val addDownload:LiveData<UiState<String>>
        get()=_addDownload
    fun addDownload(key:String, downloads: Downloads){
        _addDownload.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.plusDown(key, downloads){
                _addDownload.value=it
            }
        }
    }
}