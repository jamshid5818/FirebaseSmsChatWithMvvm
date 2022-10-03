package jx.lessons.firebasesmschatwithmvvm.data.repository

import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState

interface AuthRepository {
    fun registerUser(userInfo: UserInfo,emailFireKey:String, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
}