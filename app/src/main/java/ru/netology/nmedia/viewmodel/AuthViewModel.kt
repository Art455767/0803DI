package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application
) : ViewModel() {
    private val postViewModel: PostViewModel = PostViewModel(application)

    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)

    fun login(id: Long, token: String) {
        AppAuth.getInstance().setAuth(id, token)
        postViewModel.refreshPosts()
    }

    fun logout() {
        AppAuth.getInstance().removeAuth()
        postViewModel.refreshPosts()
    }

    fun signup() {
        TODO("Not yet implemented")
    }

    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L
}