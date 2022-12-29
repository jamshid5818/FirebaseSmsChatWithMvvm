package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Post
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostRepositoryImp @Inject constructor(
    val myRef: FirebaseDatabase,
    var storageReference: StorageReference
) : PostRepository {

    override fun upLoadPost(post: Post, imageUri: Uri, result: (UiState<String>) -> Unit) {
        upLoad(post, imageUri, result)
    }


    fun upLoad(post: Post, imageUri: Uri, result: (UiState<String>) -> Unit){
        val randomKey =post.unixTime.toString()
        val riversRef =storageReference.child("posts/$randomKey.png")
        CoroutineScope(Dispatchers.IO).launch {
            riversRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.child("posts/$randomKey.png").downloadUrl
                        .addOnSuccessListener {uri->
                            myRef.getReference("posts").child(randomKey).setValue(post.comments?.let { comments ->
                                post.likeS?.let { likeS ->
                                    post.tagsList?.let { tagList ->
                                        post.downloads?.let { downloads ->
                                            Post(
                                                comments = comments,
                                                likeS = likeS,
                                                postDescription = post.postDescription,
                                                postTitle = post.postTitle,
                                                userEmail = post.userEmail,
                                                tagsList = tagList,
                                                randomKey.toLong(),
                                                downloads = downloads

                                            )
                                        }
                                    }
                                }
                            })
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