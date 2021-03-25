package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.joda.time.LocalDate
import org.joda.time.ReadablePeriod
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.App
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.base.toSQLDate
import org.vimteam.weatherstatistic.databinding.FragmentStatQueryBinding
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.models.City
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.StatQueryState
import org.vimteam.weatherstatistic.ui.adapters.RequestsHistoryAdapter
import org.vimteam.weatherstatistic.ui.interfaces.LoadState
import java.io.Serializable
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class StatQueryFragment : Fragment(), RequestsHistoryAdapter.OnItemClickListener {

    private var _binding: FragmentStatQueryBinding? = null
    private val binding get() = _binding!!

    private val statQueryViewModel by viewModel<StatQueryContract.ViewModel>()

    private val requestsHistoryAdapter: RequestsHistoryAdapter =
        RequestsHistoryAdapter(ArrayList(), this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatQueryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(requestHistory: RequestHistory) {
        val action =
            StatQueryFragmentDirections.actionStatQueryFragmentToWeatherStatFragmentFromHistory(
                requestHistory
            )
        requireView().findNavController().navigate(action)
    }

    private fun initView() {
        initDateTimePicker(
            getString(R.string.dateFrom),
            binding.dateFromEditText,
            statQueryViewModel.dateFrom.value
        ) {
            statQueryViewModel.setDateFrom(it)
            if (statQueryViewModel.dateTo.value == null) {
                initDateTimePicker(
                    getString(R.string.dateTo),
                    binding.dateToEditText,
                    statQueryViewModel.dateFrom.value
                ) { dateTo -> statQueryViewModel.setDateTo(dateTo) }
                binding.dateToEditText.requestFocus()
            }
        }
        initDateTimePicker(
            getString(R.string.dateTo),
            binding.dateToEditText,
            statQueryViewModel.dateTo.value
        ) { statQueryViewModel.setDateTo(it) }
        binding.requestsHistoryRecyclerView.adapter = requestsHistoryAdapter
        statQueryViewModel.statQueryState.observe(viewLifecycleOwner) {
            renderView(it)
        }
        statQueryViewModel.getRequestsHistoryList()
        binding.searchPlaceEditText.setText("Saratov")
        binding.requestStatisticMaterialButton.setOnClickListener { requestWeatherStatistic() }
    }

    private fun initDateTimePicker(
        title: String,
        editText: TextInputEditText,
        initialDate: LocalDate?,
        func: (LocalDate) -> Unit
    ) {
        var initialYear = Calendar.getInstance().get(Calendar.YEAR)
        var initialMonth = Calendar.getInstance().get(Calendar.MONTH)
        var initialDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        initialDate?.let {
            val selectedDate = it
            initialYear = selectedDate.year
            initialMonth = selectedDate.monthOfYear-1
            initialDay = selectedDate.dayOfMonth
        }
        val datePickerDialog: DatePickerDialog = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val date = LocalDate(year, monthOfYear + 1, dayOfMonth)
                editText.setText(
                    java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT).format(date.toDate())
                )
                (editText.parent.parent as? TextInputLayout)?.error = null
                func.invoke(date)
            },
            initialYear, initialMonth, initialDay
        )
        datePickerDialog.showYearPickerFirst(false)
        datePickerDialog.setTitle(title)
        editText.setOnFocusChangeListener { _, b ->
            if (b) datePickerDialog.show(childFragmentManager, "DatePickerDialog")
        }
        editText.setOnClickListener { datePickerDialog.show(childFragmentManager, "DatePickerDialog") }
    }

    private fun renderView(state: StatQueryState) {
        when (state) {
            is StatQueryState.Success -> {
                requestsHistoryAdapter.requestsHistory.clear()
                requestsHistoryAdapter.requestsHistory.addAll(state.requestsHistory)
                requestsHistoryAdapter.notifyDataSetChanged()
                (activity as LoadState).setLoadState(false)
            }
            is StatQueryState.Loading -> {
                (activity as LoadState).setLoadState(true)
            }
            is StatQueryState.Error -> {
                (activity as LoadState).setLoadState(false)
                Snackbar
                    .make(requireView(), state.error.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun requestWeatherStatistic() {
        var flError = false
        val dateFrom = statQueryViewModel.dateFrom.value
        val dateTo = statQueryViewModel.dateTo.value
        if (dateFrom == null) {
            binding.dateFromTextInputLayout.error = getString(R.string.obligatory_field)
            flError = true
        }
        if (dateTo == null) {
            binding.dateToTextInputLayout.error = getString(R.string.obligatory_field)
            flError = true
        }
        if (dateFrom != null && dateTo != null) {
            if (dateFrom > LocalDate() || dateTo > LocalDate()) {
                Snackbar
                    .make(requireView(), getString(R.string.future_date), Snackbar.LENGTH_LONG)
                    .show()
                binding.dateFromTextInputLayout.error = getString(R.string.wrong_date)
                binding.dateToTextInputLayout.error = getString(R.string.wrong_date)
                flError = true
            }
            if (dateFrom > dateTo) {
                Snackbar
                    .make(requireView(), getString(R.string.dateFrom_later_dateTo), Snackbar.LENGTH_LONG)
                    .show()
                binding.dateToTextInputLayout.error = getString(R.string.wrong_date)
                flError = true
            } else if (dateTo.minusDays(App.MAX_DAYS-1) > dateFrom) {
                Snackbar
                    .make(requireView(), getString(R.string.period_restraints, App.MAX_DAYS), Snackbar.LENGTH_LONG)
                    .show()
                binding.dateToTextInputLayout.error = getString(R.string.wrong_date)
                flError = true
            }
        }
        if (flError) return
        val action =
            StatQueryFragmentDirections.actionStatQueryFragmentToWeatherStatFragmentFromApi(
                RequestHistory(
                    City(
                        "",
                        binding.searchPlaceEditText.text.toString().trim(),
                        0.0,
                        0.0
                    ),
                    (statQueryViewModel.dateFrom.value as LocalDate).toSQLDate(),
                    (statQueryViewModel.dateTo.value as LocalDate).toSQLDate(),
                    ArrayList()
                )
            )
        requireView().findNavController().navigate(action)
    }
}