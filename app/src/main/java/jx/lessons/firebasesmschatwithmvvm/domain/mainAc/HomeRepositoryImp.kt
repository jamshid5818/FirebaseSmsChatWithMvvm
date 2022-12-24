package jx.lessons.firebasesmschatwithmvvm.domain.mainAc

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jx.lessons.firebasesmschatwithmvvm.data.model.Downloads
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val myRef: FirebaseDatabase,
): HomeRepository {
    override fun getAllPost(result: (UiState<ArrayList<Post>>) -> Unit) {
        val list = ArrayList<Post>()
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        snapshot.children.forEach {
                            val post:Post = it.getValue(Post::class.java)!!
                            list.add(post)
                        }
                        result.invoke(UiState.Success(list))
                    }
                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure("FAILED /// $error"))
                    }

                })
        }
    }
    override fun getClickedLikeUsers(randomKey:String, result: (UiState<ArrayList<Likes>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Likes>()
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(randomKey).child("likeS")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val value:Likes = it.getValue(Likes::class.java)!!
                            list.add(value)
                        }
                            result.invoke(UiState.Success(list))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure(error.message))
                    }
                })
        }
    }

    override fun plusLike(key:String,likes: Likes, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var size=0
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(key).child("likeS").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    size = snapshot.children.count()
                    var alreadyClicked = false
                    snapshot.children.forEach {
                        val data:Likes = it.getValue(Likes::class.java)!!
                        if (data.email==likes.email){
                            result.invoke(UiState.Success("You have already clicked like"))
                            alreadyClicked = true
                        }
                    }
                    if (!alreadyClicked){
                        myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(key).child("likeS").child(size.toString()).setValue(
                            likes
                        )
                            .addOnSuccessListener {
                                result.invoke(UiState.Success("Successfully"))
                                Log.d("LikeCount", size.toString())
                            }
                            .addOnFailureListener {
                                result.invoke(UiState.Failure(it.message))
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure(error.message))
                }

            })

        }
    }

    override fun plusDown(key: String, downloads: Downloads, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var size=0
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(key).child("downloads").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    size = snapshot.children.count()
                    myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(key).child("downloads").child(size.toString()).setValue(
                        downloads
                    )
                        .addOnSuccessListener {
                            result.invoke(UiState.Success("SUCCES"))
                            Log.d("DOWNLOADCOUNT", size.toString())
                        }
                        .addOnFailureListener {
                            result.invoke(UiState.Failure(it.message))
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure(error.message))
                }
            })
        }
    }
}