package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.random

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.domain.chatAc.random.RandomChatRepository
import jx.lessons.firebasesmschatwithmvvm.domain.mainAc.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomChatViewModel @Inject constructor(
    val repository: RandomChatRepository
): ViewModel(){
    private val _getNewPerson = MutableLiveData<UiState<String>>()
    val getNewPerson : LiveData<UiState<String>>
        get() = _getNewPerson
    fun getNewPerson(findGender:String, unixTime:Long){
        _getNewPerson.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.addNewChat(findGender,unixTime){newPerson->
                _getNewPerson.value=newPerson
            }
        }
    }
}