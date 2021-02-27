package org.vimteam.weatherstatistic

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.vimteam.weatherstatistic.data.api.VisualcrossingApi
import org.vimteam.weatherstatistic.data.database.LocalDatabase
import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.data.repositories.WeatherStatRepository
import org.vimteam.weatherstatistic.domain.contracts.*
import org.vimteam.weatherstatistic.domain.viewmodels.StatQueryViewModel
import org.vimteam.weatherstatistic.domain.viewmodels.WeatherDetailsViewModel
import org.vimteam.weatherstatistic.domain.viewmodels.WeatherStatViewModel
import org.vimteam.weatherstatistic.ui.providers.ResourcesProvider

object MainModule {
    fun get() = module {

        singleBy<ResourcesProviderContract, ResourcesProvider>()
        singleBy <DatabaseContract, LocalDatabase>()
        singleBy <ApiContract, VisualcrossingApi>()

        factory<WeatherStatRepositoryContract> { WeatherStatRepository(get(), get()) }
        viewModel<StatQueryContract.ViewModel> { StatQueryViewModel(get(), get()) }
        viewModel<WeatherStatContract.ViewModel> { WeatherStatViewModel(get()) }
        viewModel<WeatherDetailsContract.ViewModel> { WeatherDetailsViewModel(get()) }
    }
}