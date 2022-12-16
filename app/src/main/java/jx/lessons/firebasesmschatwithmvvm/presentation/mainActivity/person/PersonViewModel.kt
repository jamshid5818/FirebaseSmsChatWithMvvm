package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.repository.mainAc.PersonRepostiroy
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    val repository: PersonRepostiroy
): ViewModel() {
    private val _getUserPosts = MutableLiveData<UiState<ArrayList<Post>>>()
    val getUserPosts: LiveData<UiState<ArrayList<Post>>>
        get() = _getUserPosts

    fun getUserPosts(emailAddress: String) {
        _getUserPosts.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main) {
            repository.getPosts(emailAddress) {list->
                _getUserPosts.value=list
            }
        }
    }

    private val _getUserInfo = MutableLiveData<UiState<UserInfo>>()
    val getUserInfo:LiveData<UiState<UserInfo>>
        get() = _getUserInfo

    fun getUserInfo(emailAddressFireKey: String){
        _getUserInfo.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.getProfileData(emailAddressFireKey){
                _getUserInfo.value = it
            }
        }
    }

    fun firebasePathgmail(email:String):String{
        var email2 =""
        email.forEachIndexed { index, letter ->
            if (letter.isLetter() || letter.isDigit()) {
                email2+=email[index]
            }
        }
        return email2
    }
}