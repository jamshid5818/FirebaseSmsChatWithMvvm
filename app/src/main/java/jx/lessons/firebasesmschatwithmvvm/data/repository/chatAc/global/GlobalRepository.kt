package jx.lessons.firebasesmschatwithmvvm.data.repository.chatAc.global

import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface GlobalRepository {
    fun sendSms(email:String,smsText:String,gender:String,unixTime:Long,result: (UiState<String>) -> Unit)
    fun getAllSms(result: (UiState<ArrayList<Sms>>) -> Unit)
}