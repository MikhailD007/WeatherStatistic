package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.formatShort
import org.vimteam.weatherstatistic.base.toLocalDate
import org.vimteam.weatherstatistic.databinding.FragmentStatResultBinding
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatContract
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.WeatherStatState
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class WeatherStatFragment : Fragment() {

    private var _binding: FragmentStatResultBinding? = null
    private val binding get() = _binding!!

    private val args: WeatherStatFragmentArgs by navArgs()

    private val weatherStatViewModel by viewModel<WeatherStatContract.ViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatResultBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(args.requestHistory)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(requestHistory: RequestHistory) {
        weatherStatViewModel.weatherStatState.observe(viewLifecycleOwner) {
            renderView(it)
        }
        if (requestHistory.weatherDataList.size > 0) {
            binding.placeNameTextView.text = requestHistory.city.name
            binding.periodTextView.text =
                "${requestHistory.dateFrom.formatShort()} - ${requestHistory.dateTo.formatShort()}"
        } else {
            binding.placeNameTextView.text = "New request to API:\n${requestHistory.city.name}"
            binding.periodTextView.text =
                "${requestHistory.dateFrom.formatShort()} - ${requestHistory.dateTo.formatShort()}"
            weatherStatViewModel.getWeatherData(
                requestHistory.city.name,
                requestHistory.dateFrom.toLocalDate(),
                requestHistory.dateTo.toLocalDate()
            )
        }
    }

    private fun renderView(state: WeatherStatState) {
        when (state) {
            is WeatherStatState.Success -> {
                val minTemperatureValues: ArrayList<BarEntry> = ArrayList()
                val maxTemperatureValues: ArrayList<BarEntry> = ArrayList()
                for (weatherStat in state.requestData) {
                    binding.placeNameTextView.text = weatherStat.city.name
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
                    xAxis.position = XAxisPosition.BOTTOM
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
            is WeatherStatState.Error -> {
                (activity as LoadState).setLoadState(false)
                Snackbar
                    .make(requireView(), state.error.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
            is WeatherStatState.Loading -> {
                (activity as LoadState).setLoadState(true)
            }
        }
    }

    inner class XAxisFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return value.toInt().toString()
        }
    }

}