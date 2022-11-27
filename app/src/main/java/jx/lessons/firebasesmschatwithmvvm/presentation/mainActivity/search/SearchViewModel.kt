package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jx.lessons.firebasesmschatwithmvvm.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

}