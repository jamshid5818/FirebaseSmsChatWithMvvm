package jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc

import jx.lessons.firebaseSmsChatWithMvvm.data.model.UserInfo
import jx.lessons.firebaseSmsChatWithMvvm.data.utils.UiState

interface AuthRepository {
    fun registerUser(userInfo: UserInfo,emailFireKey:String, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)

}