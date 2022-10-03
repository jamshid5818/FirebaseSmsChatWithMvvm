package jx.lessons.firebasesmschatwithmvvm.data.repository

import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface PersonRepostiroy {
    fun getPosts(emailAddress:String,result: (UiState<ArrayList<Post>>) -> Unit)
    fun getProfileData(emailAddress:String,result: (UiState<UserInfo>) -> Unit)
}