package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.firebasePathgmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    val auth: FirebaseAuth,
    private val myRef: FirebaseDatabase,
) : AuthRepository {
    override fun registerUser(
        userInfo: UserInfo,
        emailFireKey:String,
        result: (UiState<String>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch{
            auth.createUserWithEmailAndPassword(userInfo.email,userInfo.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("USER IID", task.result.user.toString())
                        myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users).child(emailFireKey).child("userInfo").setValue(userInfo).addOnCompleteListener {
                            if (it.isSuccessful){
                                result.invoke(UiState.Success("Email address has been successfully registered"))
                            }else {
                                result.invoke(UiState.Success("Error: ${it.result}"))
                            }
                        }
                            .addOnFailureListener {
                                result.invoke(UiState.Failure(it.message))
                            }
                    }else{
                        try {
                            throw task.exception ?: java.lang.Exception("Invalid authentication")
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                        }catch (e: FirebaseAuthInvalidCredentialsException) {
                            result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                        }catch (e: FirebaseAuthUserCollisionException) {
                            result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                        } catch (e: Exception) {
                            result.invoke(UiState.Failure(e.message))
                        }
                    }
                }
                .addOnFailureListener {
                    result.invoke(
                        UiState.Failure(
                            it.localizedMessage
                        )
                    )
                }
        }

    }


    override fun loginUser(
        email: String,
        password: String,
        result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("USER IID", task.result.user?.uid.toString())
                        myRef.getReference(FirebaseRealtimeDatabaseConstants.path_users)
                            .child(firebasePathgmail(email)).child("userInfo").addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userInfo: UserInfo = snapshot.getValue(UserInfo::class.java)!!
                                    result.invoke(UiState.Success(userInfo.gender))
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    result.invoke(UiState.Failure(error.message))
                                }

                            })
                    }
                }.addOnFailureListener {
                    result.invoke(UiState.Failure("Authentication failed, Check email and password"))
                }
        }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.invoke(UiState.Success("Email has been sent"))
                    }else {
                        result.invoke(UiState.Failure(task.exception?.message))
                    }
                }.addOnFailureListener {
                    result.invoke(UiState.Failure("Authentication failed, Check email"))
                }
        }
    }
}