package com.sonny.basetest.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sonny.basetest.base.network.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

typealias ResultFlow<T> = Flow<Result<T>>

fun <T> Flow<T>.stateIn(scope: CoroutineScope, initialValue: T) = stateIn(scope, SharingStarted.Lazily, initialValue)

inline fun Fragment.launchRepeatOnStarted(crossinline block: CoroutineScope.() -> Unit) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        block(this)
    }
}

sealed interface Result<out T> {
    @JvmInline
    value class Success<T>(private val _data: Any) : Result<T> {
        @Suppress("UNCHECKED_CAST")
        val data get() = _data as T
    }
    @JvmInline
    value class Error(val error: BaseResponse) : Result<Nothing>
    class Loading<T> : Result<T>

    companion object {
        fun <T> loading(): Loading<T> = Loading()
        fun <T> success(data: T) = Success<T>(data as Any)
        fun <T> error(message: String) = Error(BaseResponse(message))
        fun <T> error(baseResponse: BaseResponse) = Error(baseResponse)
    }
}

suspend fun <T> ResultFlow<T>.collectResult(
    onSuccess: (T) -> Unit,
    onError: (Result.Error) -> Unit,
    onLoading: (() -> Unit)? = null
) = collect {
    when (it) {
        is Result.Success -> {
            onSuccess(it.data)
        }
        is Result.Error -> {
            onError(it)
        }
        is Result.Loading -> {
            onLoading?.invoke()
        }
    }
}

