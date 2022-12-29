package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import android.net.Uri
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState

interface PostRepository {
    fun upLoadPost(post: Post,imageUri: Uri, result: (UiState<String>) -> Unit)

}