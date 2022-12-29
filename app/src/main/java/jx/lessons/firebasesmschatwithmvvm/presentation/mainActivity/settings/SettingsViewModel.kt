package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

}