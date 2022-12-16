package jx.lessons.firebasesmschatwithmvvm.presentation.chatActivity.random

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.repository.mainAc.HomeRepository
import javax.inject.Inject

@HiltViewModel
class RandomChatViewModel @Inject constructor(
    val repository: HomeRepository
): ViewModel() {
}