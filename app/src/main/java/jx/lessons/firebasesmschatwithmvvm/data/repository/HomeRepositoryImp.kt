package jx.lessons.firebasesmschatwithmvvm.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jx.lessons.firebasesmschatwithmvvm.data.model.Likes
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val myRef: FirebaseDatabase,
):HomeRepository{
    override fun getAllPost(result: (UiState<ArrayList<Post>>) -> Unit) {
        val list = ArrayList<Post>()
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
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
    override fun getClickedUsers(randomKey:String,result: (UiState<ArrayList<Likes>>) -> Unit) {
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

                    }

                })
        }
    }

    override fun PlusLike(list:ArrayList<Likes>,randomKey:String,emailAddress: String, result: (UiState<Boolean>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(randomKey).child("likeS").child((list.size+1).toString()).setValue(Likes(emailAddress))

        }
    }

    override fun MinusLike(emailAddress: String,randomKey: String, result: (UiState<Boolean>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(randomKey).child("likeS")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in 0 until snapshot.childrenCount){
                            val likes:Likes = snapshot.child(i.toString()).getValue(Likes::class.java)!!
                            if (likes.email==emailAddress){
                                myRef.getReference(FirebaseRealtimeDatabaseConstants.path_posts).child(randomKey).child("likeS").child(i.toString())
                                    .ref.removeValue()
                                    .addOnCompleteListener {
                                        result.invoke(UiState.Success(true))
                                    }
                                    .addOnFailureListener {
                                        result.invoke(UiState.Failure("$it"))
                                    }
                                break
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure(error.message))
                    }

                })
        }
    }

}