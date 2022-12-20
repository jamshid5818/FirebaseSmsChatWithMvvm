package jx.lessons.firebasesmschatwithmvvm.domain.mainAc

import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface HomeRepository {
    fun getAllPost(result: (UiState<ArrayList<Post>>) -> Unit)
    fun getClickedLikeUsers(randomKey:String, result: (UiState<ArrayList<Likes>>) -> Unit)
    fun PlusLike(list:ArrayList<Likes>,randomKey:String,emailAddress:String,result: (UiState<Boolean>) -> Unit)
    fun MinusLike(emailAddress: String,randomKey: String,result: (UiState<Boolean>) -> Unit)
}