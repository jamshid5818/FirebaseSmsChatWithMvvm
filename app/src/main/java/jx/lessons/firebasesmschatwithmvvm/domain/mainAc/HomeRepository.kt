package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import jx.lessons.firebaseSmsChatWithMvvm.data.model.Downloads
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Likes
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState

interface HomeRepository {
    fun getAllPost(result: (UiState<ArrayList<Post>>) -> Unit)
    fun getClickedLikeUsers(randomKey:String, result: (UiState<ArrayList<Likes>>) -> Unit)
    fun plusLike(key:String,likes: Likes, result: (UiState<String>) -> Unit)
    fun plusDown(key:String, downloads: Downloads, result: (UiState<String>) -> Unit)
}