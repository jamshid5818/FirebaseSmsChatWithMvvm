package jx.lessons.firebasesmschatwithmvvm.domain.chatAc.global

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jx.lessons.firebasesmschatwithmvvm.data.model.Sms
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

class GlobalRepositoryImp @Inject constructor(
     private val myRef: FirebaseDatabase
) : GlobalRepository {
    override fun sendSms(email: String, smsText: String,gender:String, unixTime:Long, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            myRef.getReference("global").child(unixTime.toString())
                .setValue(Sms(smsText = smsText, emailAddress = email, gender = gender, unixTime = unixTime))
                .addOnSuccessListener {
                    result.invoke(UiState.Success("true"))
                }
                .addOnFailureListener {
                    result.invoke(UiState.Failure(it.message))
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