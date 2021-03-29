package org.vimteam.weatherstatistic.ui.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.formatShort
import org.vimteam.weatherstatistic.databinding.FragmentResultWeatherStatisticBinding
import org.vimteam.weatherstatistic.domain.contracts.ResultWeatherStatisticContract
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatistic
import org.vimteam.weatherstatistic.domain.models.state.ResultWeatherStatisticState
import org.vimteam.weatherstatistic.ui.NetworkService
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class ResultWeatherStatisticFragment : Fragment() {

    private var _binding: FragmentResultWeatherStatisticBinding? = null
    private val binding get() = _binding!!

    private val args: ResultWeatherStatisticFragmentArgs by navArgs()

    private val resultWeatherStatisticViewModel by viewModel<ResultWeatherStatisticContract.ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultWeatherStatisticBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(args.requestHistory)
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(receiverResult, IntentFilter(NetworkService.ACTION_INTENT))
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiverResult)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initView(requestHistory: RequestHistory) {
        resultWeatherStatisticViewModel.resultWeatherStatisticState.observe(viewLifecycleOwner) {
            renderView(it)
        }
        if (requestHistory.weatherDataList.size > 0) {
            resultWeatherStatisticViewModel.proceedWeatherData(requestHistory.weatherDataList)
        } else {
            binding.placeNameTextView.text = getString(R.string.request_to_server_in_progress)
            binding.periodTextView.text = getString(
                R.string.request_params,
                requestHistory.place.name,
                requestHistory.dateFrom.formatShort(),
                requestHistory.dateTo.formatShort()
            )
            val intentExchange = Intent(requireContext(), NetworkService::class.java).apply {
                this.putExtra(NetworkService.PLACE, requestHistory.place.name)
                this.putExtra(NetworkService.DATE_FROM, requestHistory.dateFrom)
                this.putExtra(NetworkService.DATE_TO, requestHistory.dateTo)
            }
            requireActivity().startService(intentExchange)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderView(state: ResultWeatherStatisticState) {
        when (state) {
            is ResultWeatherStatisticState.Success -> {
                binding.headerImageView.load(getString(R.string.city_picture))
                val minTemperatureValues: ArrayList<BarEntry> = ArrayList()
                val maxTemperatureValues: ArrayList<BarEntry> = ArrayList()
                for (weatherStat in state.requestData) {
                    binding.placeNameTextView.text = weatherStat.place.name
                    binding.periodTextView.text =
                        "${args.requestHistory.dateFrom.formatShort()} - ${args.requestHistory.dateTo.formatShort()}"
                    minTemperatureValues.add(
                        BarEntry(
                            ("" + weatherStat.date.dayOfMonth + "." + if (weatherStat.date.monthOfYear > 9) weatherStat.date.monthOfYear else "0" + weatherStat.date.monthOfYear).toFloat(),
                            weatherStat.minTemperature
                        )
                    )
                    maxTemperatureValues.add(
                        BarEntry(
                            ("" + weatherStat.date.dayOfMonth + "." + if (weatherStat.date.monthOfYear > 9) weatherStat.date.monthOfYear else "0" + weatherStat.date.monthOfYear).toFloat(),
                            weatherStat.maxTemperature
                        )
                    )
                }
                val minTemperatureDataSet: BarDataSet = BarDataSet(
                    minTemperatureValues,
                    getString(R.string.min_temperature)
                ).also {
                    it.color = ContextCompat.getColor(requireContext(), R.color.blue_light)
                    it.setDrawValues(true)
                }
                val maxTemperatureDataSet: BarDataSet = BarDataSet(
                    maxTemperatureValues,
                    getString(R.string.max_temperature)
                ).also {
                    it.color = ContextCompat.getColor(requireContext(), R.color.red_light)
                    it.setDrawValues(true)
                }
                val temperatureDataSets: ArrayList<IBarDataSet> = ArrayList()
                temperatureDataSets.add(minTemperatureDataSet)
                temperatureDataSets.add(maxTemperatureDataSet)
                with(binding.temperatureBarChart) {
                    data = BarData(temperatureDataSets)
                    setMaxVisibleValueCount(60)
                    setPinchZoom(false)
                    setDrawBarShadow(false)
                    setDrawGridBackground(false)
                    setFitBars(true)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 1f
                    xAxis.valueFormatter = XAxisFormatter()
                    axisLeft.setDrawGridLines(false)
                    animateY(500)
                    legend.isEnabled = true
                    invalidate()
                }

                (activity as LoadState).setLoadState(false)
            }
            is ResultWeatherStatisticState.Error -> {
                binding.headerImageView.load(getString(R.string.failed_picture))

                (activity as LoadState).setLoadState(false)
                Snackbar
                    .make(requireView(), state.error.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
            is ResultWeatherStatisticState.Loading -> {
                binding.headerImageView.load(getString(R.string.loading_picture))
                (activity as LoadState).setLoadState(true)
            }
        }
    }

    var receiverResult: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(arg0: Context, intent: Intent) {
            if (intent.action?.contentEquals(NetworkService.ACTION_INTENT) == true) {
                val weatherStatistic =
                    intent.getParcelableArrayListExtra<WeatherStatistic>(NetworkService.RESULT_DATA_TAG) as ArrayList<WeatherStatistic>
                resultWeatherStatisticViewModel.proceedWeatherData(weatherStatistic)
            }
        }
    }

    inner class XAxisFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return value.toInt().toString()
        }
    }

}