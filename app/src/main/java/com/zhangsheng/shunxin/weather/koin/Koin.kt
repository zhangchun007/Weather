package com.zhangsheng.shunxin.weather.koin

import com.maiya.thirdlibrary.base.BaseViewModel
import com.zhangsheng.shunxin.calendar.model.AlmanacModel
import com.zhangsheng.shunxin.calendar.model.CalendarModel
import com.zhangsheng.shunxin.weather.model.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val viewModelModule: Module = module {
    viewModel { MainViewModel() }
    viewModel { WeatherViewModel() }
    viewModel { CityManageViewModel() }
    viewModel { SplashViewModel() }
    viewModel { CalendarModel() }
    viewModel { AlmanacModel() }
    viewModel { WeatherPageViewModel() }
    viewModel { WeatherMapModel() }
    viewModel { AirModel() }
    viewModel { SettingViewModel() }
    viewModel { CitySelectViewModel() }
    viewModel { BaseViewModel() }
    viewModel { TyphoonViewModel() }
    viewModel { AirMapViewModel() }
    viewModel { ForthWeatherModel() }
}