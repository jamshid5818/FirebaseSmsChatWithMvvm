package jx.lessons.firebaseSmsChatWithMvvm.domain.chatAc.global

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import jx.lessons.firebaseSmsChatWithMvvm.data.model.Sms
import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

class GlobalRepositoryImp @Inject constructor(
     private val myRef: FirebaseDatabase,
     private var storageReference: StorageReference
) : GlobalRepository {
    override fun sendSms(email:String,smsText:String,gender:String,unixTime:Long,imageUri: Uri,result: (UiState<ArrayList<String>>) -> Unit) {
        if (imageUri.toString()==""){
            CoroutineScope(Dispatchers.IO).launch {
                myRef.getReference(FirebaseRealtimeDatabaseConstants.path_global).child(unixTime.toString())
                    .setValue(Sms(smsText = smsText, emailAddress = email, gender = gender, unixTime = unixTime, imageUri=imageUri.toString()))
                    .addOnSuccessListener {
                        myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users).addValueEventListener(object :ValueEventListener{
                            val list = ArrayList<String>()
                            override fun onDataChange(snapshot: DataSnapshot) {
                                repeat(snapshot.children.count()) {
                                    val data: UserInfo =
                                        snapshot.child("userInfo").getValue(UserInfo::class.java)!!
                                    if (data.email!=email){
                                        data.tokens?.forEach {
                                            list.add(it)
                                        }
                                    }
                                }
                                result.invoke(UiState.Success(list))
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }
                    .addOnFailureListener {
                        result.invoke(UiState.Failure(it.message))
                    }
            }
        }else{
            val riversRef =storageReference.child("posts/$unixTime.png")
            CoroutineScope(Dispatchers.IO).launch {
                riversRef.putFile(imageUri)
                    .addOnSuccessListener {
                        storageReference.child("posts/$unixTime.png").downloadUrl
                            .addOnSuccessListener { uri->
                                myRef.getReference(FirebaseRealtimeDatabaseConstants.path_global).child(unixTime.toString())
                                    .setValue(Sms(
                                        imageUri = uri.toString(),
                                        emailAddress = email,
                                        gender = gender,
                                        unixTime = unixTime,
                                        smsText = smsText
                                    ))
                                    .addOnSuccessListener {
                                        myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users).addValueEventListener(object :ValueEventListener{
                                            val list = ArrayList<String>()
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                repeat(snapshot.children.count()) {
                                                    val data: UserInfo =
                                                        snapshot.child("userInfo").getValue(UserInfo::class.java)!!
                                                    if (data.email!=email){
                                                        data.tokens?.forEach {
                                                            list.add(it)
                                                        }
                                                    }
                                                }
                                                result.invoke(UiState.Success(list))
                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })
                                    }
                                    .addOnFailureListener {
                                        result.invoke(UiState.Failure(it.message))
                                    }
                            }
                            .addOnFailureListener {
                                result.invoke(UiState.Failure(it.message))
                            }
                    }
                    .addOnFailureListener{
                        result.invoke(UiState.Failure(it.message))
                    }
            }
        }
    }

    override fun getAllSms(result: (UiState<ArrayList<Sms>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Sms>()
            myRef.getReference("global")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        snapshot.children.forEach {
                            val sms:Sms=it.getValue(Sms::class.java)!!
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