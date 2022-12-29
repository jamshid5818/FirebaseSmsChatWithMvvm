package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.random

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Sms
import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.random.RandomChatRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomChatViewModel @Inject constructor(
    val repository: RandomChatRepository
): ViewModel(){
    private val _findNewPerson = MutableLiveData<UiState<String>>()
    val findNewPerson : LiveData<UiState<String>>
        get() = _findNewPerson
    fun getNewPerson(findGender:String,sharedGender:String){
        _findNewPerson.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.findFriend(findGender,sharedGender){ newPerson->
                _findNewPerson.value=newPerson
            }
        }
    }
    private val _randomChat = MutableLiveData<UiState<String>>()
    val randomChat :LiveData<UiState<String>>
        get() = _randomChat
    fun sendSms(firebaseKey:String,sms: Sms){
        _randomChat.value = UiState.Loading(true)
        viewModelScope.launch {
            repository.addNewChat(firebaseKey, sms){
                _randomChat.value = it
            }
        }
    }
    private val _findPersonInfo = MutableLiveData<UiState<UserInfo>>()
    val findPersonInfo :LiveData<UiState<UserInfo>>
        get()=_findPersonInfo
    fun getFindUserInfo(findUserKey: String){
        _findPersonInfo.value = UiState.Loading(true)
        viewModelScope.launch {
            repository.getFindUserInfo(findUserKey){
                _findPersonInfo.value=it
            }
        }
    }

    private val _getAllSms = MutableLiveData<UiState<ArrayList<Sms>>>()
    val getAllSms :LiveData<UiState<ArrayList<Sms>>>
        get() = _getAllSms
    fun getAllSms(key:String){
        _getAllSms.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.getRandomChatAllSms(key){
                _getAllSms.value=it
            }
        }
    }
}