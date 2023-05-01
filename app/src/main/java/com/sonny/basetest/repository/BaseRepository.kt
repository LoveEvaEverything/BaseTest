package com.sonny.basetest.repository


import com.sonny.basetest.base.ResultFlow
import com.sonny.basetest.base.network.BaseRemoteApi
import com.sonny.basetest.base.network.BaseResponse
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import com.sonny.basetest.base.Result
interface BaseRepository {
    suspend fun getData(request : String): ResultFlow<BaseResponse>
}

class RepositoryImplement(
    private val apiOtt: BaseRemoteApi
) : BaseRepository {


    override suspend fun getData(request: String): ResultFlow<BaseResponse> = flow{
        val result = getResponse(
                request = { apiOtt.getData(request) },
                defaultErrorMessage = "Error fetching page data"
            )

        emit(result)
    }


    protected suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            val result = request()
            if (result.isSuccessful) {
                return Result.success(result.body()!!)
            } else {
                Result.error<T>(BaseResponse())
            }
        } catch (e: Throwable) {
            Result.error<T>(BaseResponse(e.message.toString()))
        }
    }

}