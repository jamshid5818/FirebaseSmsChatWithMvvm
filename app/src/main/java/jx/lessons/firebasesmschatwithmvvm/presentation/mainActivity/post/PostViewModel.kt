package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc.PostRepository
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    val repository: PostRepository
):ViewModel() {
    private val _uploadPost = MutableLiveData<UiState<String>>()
    val uploadPost :LiveData<UiState<String>>
        get() = _uploadPost
    fun upLoadPost(
        post:Post,
        imageUri: Uri
    ){
      _uploadPost.value = UiState.Loading(true)
      viewModelScope.launch(Dispatchers.Main){
          repository.upLoadPost(
              post = post,
              imageUri = imageUri
          ){
              _uploadPost.value = it
          }
      }
    }
}