package jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.global

import android.net.Uri
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Sms
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState

interface GlobalRepository {
    fun sendSms(email:String,smsText:String,gender:String,unixTime:Long,imageUri: Uri,result: (UiState<String>) -> Unit)
    fun getAllSms(result: (UiState<ArrayList<Sms>>) -> Unit)
}