package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.global

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Sms
import jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.global.GlobalRepository
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
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
    private val _sendSms = MutableLiveData<UiState<ArrayList<String>>>()
    val sendSms : LiveData<UiState<ArrayList<String>>>
        get() = _sendSms
    fun sendSms(email:String,smsText:String,gender:String,unixTime:Long,imageUri: Uri){
        _sendSms.value=UiState.Loading(true)
        viewModelScope.launch {
            repository.sendSms(email = email,smsText=smsText,gender=gender,unixTime=unixTime,imageUri){
                _sendSms.value=it
            }
        }
    }
}