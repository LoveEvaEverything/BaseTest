package com.sonny.basetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonny.basetest.base.collectResult
import com.sonny.basetest.base.network.BaseResponse
import com.sonny.basetest.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface BaseEvent
sealed class MainEvent : BaseEvent {

    object Update:MainEvent()
}
class MainActivityViewModel( val baseRepository: BaseRepository, data : String) : ViewModel() {
    private val _pageData: MutableStateFlow<BaseResponse?> = MutableStateFlow(null)
    val pageData: StateFlow<BaseResponse?> = _pageData
    init {
        refreshPageData(data)
    }

    private fun setPageData(response: BaseResponse) {
        _pageData.value = response
    }


    private fun refreshPageData(requestId: String) {
        viewModelScope.launch {
            baseRepository.getData(requestId).collectResult(
                onSuccess = {
                    setPageData(it)
                },
                onError = {

                }
            )
        }
    }
}