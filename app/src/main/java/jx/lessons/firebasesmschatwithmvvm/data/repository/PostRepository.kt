package jx.lessons.firebasesmschatwithmvvm.data.repository

import android.net.Uri
import jx.lessons.firebasesmschatwithmvvm.data.model.Comments
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface PostRepository {
    fun upLoadPost(post: Post,imageUri: Uri, result: (UiState<String>) -> Unit)

}