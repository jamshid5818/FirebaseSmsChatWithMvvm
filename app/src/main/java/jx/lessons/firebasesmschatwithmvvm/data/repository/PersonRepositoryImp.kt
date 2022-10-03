package jx.lessons.firebasesmschatwithmvvm.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import jx.lessons.firebasesmschatwithmvvm.data.model.Post
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonRepositoryImp @Inject constructor(
    val myRef: FirebaseDatabase,
) : PersonRepostiroy{
    override fun getPosts(emailAddress: String, result: (UiState<ArrayList<Post>>) -> Unit) {
        val list = ArrayList<Post>()
        CoroutineScope(Dispatchers.Default).launch{
            myRef.getReference("posts")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            val allPosts:Post = it.getValue(Post::class.java)!!
                            if (allPosts.userEmail ==emailAddress){
                                list.add(allPosts)
                            }
                        }
                        result.invoke(UiState.Success(list))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure("FAILED /// $error"))
                    }

                })
        }
    }

    override fun getProfileData(emailAddress:String, result: (UiState<UserInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users).child(emailAddress).child("userInfo").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfo:UserInfo = snapshot.getValue(UserInfo::class.java)!!
                    result.invoke(UiState.Success(userInfo))
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("Failed////$error"))
                }

            })
        }
    }
}