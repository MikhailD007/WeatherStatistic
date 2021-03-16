package org.vimteam.weatherstatistic

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDate
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.vimteam.weatherstatistic.data.converters.LocalDateConverter
import org.vimteam.weatherstatistic.data.database.LocalDatabase
import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.data.repositories.WeatherStatRepository
import org.vimteam.weatherstatistic.domain.contracts.*
import org.vimteam.weatherstatistic.domain.viewmodels.StatQueryViewModel
import org.vimteam.weatherstatistic.domain.viewmodels.WeatherDetailsViewModel
import org.vimteam.weatherstatistic.domain.viewmodels.WeatherStatViewModel
import org.vimteam.weatherstatistic.ui.providers.ResourcesProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModule {
    fun get() = module {

        fun provideRetrofit(): Retrofit {
            val okHttpClientBuilder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            val loggingInterceptor =
                httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
            val client = okHttpClientBuilder.build()

            return Retrofit.Builder()
                .baseUrl("https://weather.visualcrossing.com")
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .registerTypeAdapter(
                                LocalDate::class.java,
                                LocalDateConverter()
                            )
                            .create()
                    )
                )
                .client(client)
                .build()
        }

        fun provideWeatherApi(retrofit: Retrofit): ApiContract {
            return retrofit.create(ApiContract::class.java)
        }
        
        singleBy<ResourcesProviderContract, ResourcesProvider>()
        singleBy <DatabaseContract, LocalDatabase>()
        single { provideRetrofit() }
        single { provideWeatherApi(get()) }
        
        factory<WeatherStatRepositoryContract> { WeatherStatRepository(get(), get()) }
        viewModel<StatQueryContract.ViewModel> { StatQueryViewModel(get(), get()) }
        viewModel<WeatherStatContract.ViewModel> { WeatherStatViewModel(get()) }
        viewModel<WeatherDetailsContract.ViewModel> { WeatherDetailsViewModel(get()) }

    }
}