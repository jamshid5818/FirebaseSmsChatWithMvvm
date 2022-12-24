package jx.lessons.firebasesmschatwithmvvm.domain.chatAc.random

import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface RandomChatRepository {
    fun findFriend(findGender:String,sharedGender:String, result: (UiState<String>) -> Unit)
    fun addNewChat(fireBaseKey:String,sms: Sms, result: (UiState<String>) -> Unit)
    fun getFindUserInfo(findUserkey: String, result: (UiState<UserInfo>) -> Unit)
    fun getRandomChatAllSms(key:String,result: (UiState<ArrayList<Sms>>) -> Unit)
}