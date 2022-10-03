package jx.lessons.firebasesmschatwithmvvm.presentation.like

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

}