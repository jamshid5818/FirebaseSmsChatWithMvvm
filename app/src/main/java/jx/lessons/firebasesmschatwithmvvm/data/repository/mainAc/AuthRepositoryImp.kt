package jx.lessons.firebasesmschatwithmvvm.data.repository.mainAc

import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.FirebaseRealtimeDatabaseConstants
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
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
        var unixTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch{
            auth.createUserWithEmailAndPassword(userInfo.email,userInfo.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
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
                        result.invoke(UiState.Success("Succesfully"))
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

    override fun logout(result: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            auth.signOut()
                result.invoke()
        }
    }
}