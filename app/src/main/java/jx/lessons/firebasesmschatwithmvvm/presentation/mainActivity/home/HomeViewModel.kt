package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.repository.mainAc.HomeRepository
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
        private val _setLikePost = MutableLiveData<UiState<Boolean>>()
        val setLikePost:LiveData<UiState<Boolean>>
            get() = _setLikePost

    fun setLikePost(list:ArrayList<Likes>,randomKey: String, email:String){
        var position = 0
        _setLikePost.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.PlusLike(list,randomKey,email){isLiked->
                _setLikePost.value = isLiked
            }
        }
    }
        private val _setMinusLike = MutableLiveData<UiState<Boolean>>()
        val setMinusLike:LiveData<UiState<Boolean>>
            get() = _setMinusLike

    fun setMinusLike(email: String, randomKey: String){
        _setMinusLike.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.MinusLike(email, randomKey){
                _setMinusLike.value = it
            }
        }
    }
}