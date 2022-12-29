package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState

interface PersonRepostiroy {
    fun getPosts(emailAddress:String,result: (UiState<ArrayList<Post>>) -> Unit)
    fun getProfileData(emailAddress:String,result: (UiState<UserInfo>) -> Unit)
    fun logout(result: () -> Unit)
}