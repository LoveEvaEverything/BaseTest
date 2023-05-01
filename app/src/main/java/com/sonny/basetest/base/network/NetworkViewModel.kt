package com.sonny.basetest.base.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnlineHandler(
    private val onlineState: StateFlow<Boolean>,
    private val coroutineScope: CoroutineScope
) {
    private var jobs: Channel<() -> Unit> = Channel()

    init {
        coroutineScope.launch {
            onlineState.collect {
                if (it) {
                    jobs.consumeEach {
                        it()
                    }
                }
            }
        }
    }

    operator fun plusAssign(job: () -> Unit) {
        if (onlineState.value) {
            job()
        } else {
            coroutineScope.launch {
                jobs.send(job)
            }
        }
    }
}

interface NetworkViewModel {
    val isOnline: MutableStateFlow<Boolean>

    val onlineHandler: OnlineHandler
}

class NetworkViewModelImpl : ViewModel(), NetworkViewModel {
    override val isOnline: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val onlineHandler: OnlineHandler = OnlineHandler(isOnline, viewModelScope)
}

