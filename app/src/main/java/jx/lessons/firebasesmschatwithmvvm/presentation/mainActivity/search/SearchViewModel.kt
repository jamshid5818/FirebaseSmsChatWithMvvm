package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebaseSmsChatWithMvvm.domain.mainAc.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

}