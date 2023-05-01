package com.sonny.basetest.base

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.sonny.basetest.MainActivityViewModel
import com.sonny.basetest.base.MyApplication.Companion.TIMEOUT
import com.sonny.basetest.base.network.BaseRemoteApi
import com.sonny.basetest.base.network.EnvSpecificLogic
import com.sonny.basetest.repository.BaseRepository
import com.sonny.basetest.repository.RepositoryImplement
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun start(application: Application) {
	startKoin {
		androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
		androidContext(application)
		modules(
			listOf(
				remoteDataModule
			)
		)
	}
}
val remoteDataModule = module {
	single(named("HttpClient")) {
		EnvSpecificLogic.okHttpClient
				.addInterceptor(ChuckInterceptor(MyApplication.context))
				.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS)
				.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.build()
	}
	single {
		Retrofit.Builder()
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.client(get(named("HttpClient")))
				.baseUrl("http://test")// TODO add URL
				.build()
				.create(BaseRemoteApi::class.java)
	}
	single<BaseRepository> {
		RepositoryImplement(get())
	}
	viewModel {(data: String) ->
		MainActivityViewModel(get(),data)
	}
}

