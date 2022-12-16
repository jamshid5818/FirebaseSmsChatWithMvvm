package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.repository.chatAc.global.GlobalRepository
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalChatViewModel @Inject constructor(
    val repository: GlobalRepository
): ViewModel() {
    private val _getAllSms = MutableLiveData<UiState<ArrayList<Sms>>>()
    val getAllSms : LiveData<UiState<ArrayList<Sms>>>
        get() = _getAllSms
    fun getAllSms(){
        _getAllSms.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.getAllSms {list->
                _getAllSms.value=list
            }
        }
    }
    private val _sendSms = MutableLiveData<UiState<String>>()
    val sendSms : LiveData<UiState<String>>
        get() = _sendSms
    fun sendSms(email:String,smsText:String,gender:String,unixTime:Long){
        _sendSms.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.sendSms(email = email,smsText=smsText,gender=gender,unixTime=unixTime){
                _sendSms.value=it
            }
        }
    }
}