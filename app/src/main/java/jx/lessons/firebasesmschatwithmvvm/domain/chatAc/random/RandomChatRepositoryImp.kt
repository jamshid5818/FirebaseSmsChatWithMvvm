package jx.lessons.firebasesmschatwithmvvm.domain.chatAc.random

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import jx.lessons.firebasesmschatwithmvvm.data.utils.firebasePathgmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RandomChatRepositoryImp @Inject constructor(
    private val myRef: FirebaseDatabase
) : RandomChatRepository {
    override fun addNewChat(findGender:String, unixTime: Long, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<UserInfo>()
                        snapshot.children.forEach {
                            val userInfo = it.child("userInfo").getValue(UserInfo::class.java)
                            if (userInfo?.gender==findGender){
                                list.add(userInfo)
                            }
                        }
                        val randomNumber = (0 until list.count()).random()
                        result.invoke(UiState.Success(firebasePathgmail(list[randomNumber].email)))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure(error.message))
                    }

                })
        }
    }
}