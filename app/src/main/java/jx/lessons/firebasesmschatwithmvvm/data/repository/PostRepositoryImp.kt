package jx.lessons.firebasesmschatwithmvvm.data.repository

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import jx.lessons.firebasesmschatwithmvvm.data.model.Comments
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImp @Inject constructor(
    val myRef: FirebaseDatabase,
    var storageReference: StorageReference
) : PostRepository{

    override fun upLoadPost(post: Post, imageUri: Uri, result: (UiState<String>) -> Unit) {
        upLoad(post, imageUri, result)
    }


    fun upLoad(post: Post, imageUri: Uri, result: (UiState<String>) -> Unit){
        val randomKey =UUID.randomUUID().toString()
        val riversRef =storageReference.child("posts/$randomKey.png")
        CoroutineScope(Dispatchers.IO).launch {
            riversRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.child("posts/$randomKey.png").downloadUrl
                        .addOnSuccessListener {uri->
                            myRef.getReference("posts").child(randomKey).setValue(Post(post.comments,post.likeS,post.postDescription,post.postTitle,post.userEmail, post.tagsList, randomKey))
                                .addOnSuccessListener {
                                    result.invoke(UiState.Success("Successfull"))
                                }
                                .addOnFailureListener {e->
                                    result.invoke(UiState.Failure("ERROR: ${e.message}"))
                                }
                            myRef.getReference("posts").child(randomKey).child("imageUri").setValue(uri.toString())
                                .addOnSuccessListener {
                                    result.invoke(UiState.Success("Successfull"))
                                }
                                .addOnFailureListener {e->
                                    result.invoke(UiState.Failure("ERROR: ${e.message}"))
                                }
                        }
                        .addOnFailureListener {e->
                            result.invoke(UiState.Failure("ERROR: ${e.message}"))
                        }
                }
                .addOnFailureListener{e->
                    result.invoke(UiState.Failure("ERROR: ${e.message}"))
                }
        }
    }
}