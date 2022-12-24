package jx.lessons.firebasesmschatwithmvvm.domain.chatAc.random

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
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
    override fun findFriend(findGender:String,sharedGender:String,result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<UserInfo>()
                        snapshot.children.forEach {
                            val userInfo = it.child("userInfo").getValue(UserInfo::class.java)
                            if (userInfo?.gender==findGender && userInfo.gender !=sharedGender){
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

    override fun addNewChat(fireBaseKey: String, sms: Sms, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_random).child(fireBaseKey)
                .child(sms.unixTime.toString()).setValue(sms)
                .addOnSuccessListener {
                    result.invoke(UiState.Success("SUCCESSFULLY"))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(it.message))
                }
        }
    }

    override fun getFindUserInfo(findUserkey: String, result: (UiState<UserInfo>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users).child(findUserkey)
                .child("userInfo").addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val data:UserInfo = snapshot.getValue(UserInfo::class.java)!!
                        result.invoke(UiState.Success(data))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure(error.message))
                    }

                })
        }
    }

    override fun getRandomChatAllSms(key: String, result: (UiState<ArrayList<Sms>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Sms>()
            myRef.getReference(FirebaseRealtimeDatabaseConstants.path_random).child(key)
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        snapshot.children.forEach {
                            val sms:Sms = it.getValue(Sms::class.java)!!
                            list.add(sms)
                        }
                        result.invoke(UiState.Success(list))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure(error.message))
                    }
                })
        }
    }
}