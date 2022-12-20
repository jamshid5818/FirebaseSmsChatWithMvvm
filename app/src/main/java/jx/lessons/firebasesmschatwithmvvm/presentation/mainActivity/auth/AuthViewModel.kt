package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.model.UserInfo
import jx.lessons.firebasesmschatwithmvvm.domain.mainAc.AuthRepository
import jx.lessons.firebasesmschatwithmvvm.data.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword


    fun register(
        userInfo: UserInfo,
        emailFireKey:String
    ) {
        _register.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.registerUser(
                userInfo = userInfo,
                emailFireKey
            ) {
                _register.value = it
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.loginUser(
                email,
                password
            ){
                _login.value = it
            }
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading(true)
        viewModelScope.launch(Dispatchers.Main){
            repository.forgotPassword(email){
                _forgotPassword.value = it
            }
        }
    }


}