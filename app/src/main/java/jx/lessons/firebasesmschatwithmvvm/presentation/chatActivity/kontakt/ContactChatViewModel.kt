package jx.lessons.firebaseSmsChatWithMvvm.presentation.chatActivity.kontakt

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc.HomeRepository
import javax.inject.Inject

@HiltViewModel
class ContactChatViewModel @Inject constructor(
    val repository: HomeRepository
): ViewModel() {
}