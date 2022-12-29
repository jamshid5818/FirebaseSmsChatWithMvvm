package jx.lessons.firebaseSmsChatWithMvvm.data.utils

sealed class UiState<T>(
    val data: T? = null,
    val message: String? = null,
    val isLoading: Boolean? = null
) {
    class Success<T>(data: T) : UiState<T>(data = data)
    class Failure<T>(message: String?) : UiState<T>(message = message)
    class Loading<T>(isLoading: Boolean?) : UiState<T>(isLoading = isLoading)
}