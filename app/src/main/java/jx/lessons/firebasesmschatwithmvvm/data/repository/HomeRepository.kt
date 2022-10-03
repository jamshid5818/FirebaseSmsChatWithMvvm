package jx.lessons.firebasesmschatwithmvvm.data.repository

import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import java.util.concurrent.Flow

interface HomeRepository {
    fun getAllPost(result: (UiState<ArrayList<Post>>) -> Unit)
    fun getClickedUsers(randomKey:String,result: (UiState<ArrayList<Likes>>) -> Unit)
    fun PlusLike(list:ArrayList<Likes>,randomKey:String,emailAddress:String,result: (UiState<Boolean>) -> Unit)
    fun MinusLike(emailAddress: String,randomKey: String,result: (UiState<Boolean>) -> Unit)
}