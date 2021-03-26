package org.vimteam.weatherstatistic

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDate
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.vimteam.weatherstatistic.base.ConnectivityListener
import org.vimteam.weatherstatistic.data.api.interceptors.VisualcrossingInterceptor
import org.vimteam.weatherstatistic.data.converters.LocalDateConverter
import org.vimteam.weatherstatistic.data.database.LocalDatabase
import org.vimteam.weatherstatistic.data.database.WeatherDB
import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.data.repositories.DatabaseRepository
import org.vimteam.weatherstatistic.data.repositories.SharedPreferencesRepository
import org.vimteam.weatherstatistic.data.repositories.WeatherStatRepository
import org.vimteam.weatherstatistic.domain.contracts.*
import org.vimteam.weatherstatistic.domain.viewmodels.StatQueryViewModel
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
            okHttpClientBuilder.addInterceptor(VisualcrossingInterceptor())
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

        fun provideDatabase(application: Application): WeatherDB {
            return Room.databaseBuilder(application, WeatherDB::class.java, "WeatherStatDatabaseVimteamOrg")
                .fallbackToDestructiveMigration()
                .build()
        }
        
        singleBy<ResourcesProviderContract, ResourcesProvider>()
        singleBy<SharedPreferencesContract, SharedPreferencesRepository>()
        singleBy <DatabaseContract, LocalDatabase>()
        single { provideRetrofit() }
        single { provideWeatherApi(get()) }
        single { ConnectivityListener(get()) }
        single { provideDatabase(androidApplication()) }
        factory<DatabaseRepositoryContract> { DatabaseRepository(get()) }

        factory<WeatherStatRepositoryContract> { WeatherStatRepository(get(), get()) }
        viewModel<StatQueryContract.ViewModel> { StatQueryViewModel(get(), get()) }
        viewModel<WeatherStatContract.ViewModel> { WeatherStatViewModel(get(), get()) }

    }
}