package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.like

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.domain.mainAc.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

}