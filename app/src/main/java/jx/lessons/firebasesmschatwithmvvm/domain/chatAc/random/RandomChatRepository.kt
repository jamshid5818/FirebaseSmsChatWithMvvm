package jx.lessons.firebasesmschatwithmvvm.domain.chatAc.random

import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface RandomChatRepository {
    fun addNewChat(findGender:String, unixTime:Long, result: (UiState<String>) -> Unit)
}