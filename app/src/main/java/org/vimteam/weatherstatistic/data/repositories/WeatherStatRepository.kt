package org.vimteam.weatherstatistic.data.repositories

import org.vimteam.weatherstatistic.data.interfaces.ApiContract
import org.vimteam.weatherstatistic.data.interfaces.DatabaseContract
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatRepositoryContract

class WeatherStatRepository(
    api: ApiContract,
    db: DatabaseContract
) : WeatherStatRepositoryContract {

}