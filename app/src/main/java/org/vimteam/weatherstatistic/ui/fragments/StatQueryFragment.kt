package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import org.joda.time.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.R
import org.vimteam.weatherstatistic.databinding.FragmentStatQueryBinding
import org.vimteam.weatherstatistic.domain.contracts.StatQueryContract
import org.vimteam.weatherstatistic.domain.models.RequestHistory
import org.vimteam.weatherstatistic.domain.models.StatQueryState
import org.vimteam.weatherstatistic.ui.adapters.RequestsHistoryAdapter
import org.vimteam.weatherstatistic.ui.interfaces.LoadState

class StatQueryFragment : Fragment(), RequestsHistoryAdapter.OnItemClickListener {

    private var _binding: FragmentStatQueryBinding? = null
    private val binding get() = _binding!!

    private val statQueryViewModel by viewModel<StatQueryContract.ViewModel>()

    private val requestsHistoryAdapter: RequestsHistoryAdapter = RequestsHistoryAdapter(ArrayList(), this)

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
        val action = StatQueryFragmentDirections.actionStatQueryFragmentToWeatherStatFragment(requestHistory)
        requireView().findNavController().navigate(action)
    }

    private fun initView() {
        initDateTimePicker(
            getString(R.string.dateFrom),
            binding.dateFromTextInputLayout,
            binding.dateFromEditText
        )
        initDateTimePicker(
            getString(R.string.dateTo),
            binding.dateToTextInputLayout,
            binding.dateToEditText
        )
        binding.requestsHistoryRecyclerView.adapter = requestsHistoryAdapter
        statQueryViewModel.statQueryState.observe(viewLifecycleOwner) {
            renderView(it)
        }
        statQueryViewModel.getRequestsHistoryList()
    }

    private fun initDateTimePicker(
        title: String,
        layout: TextInputLayout,
        editText: TextInputEditText
    ) {
        val datePickerDialog: DatePickerDialog =
            DatePickerDialog.newInstance { _, year, monthOfYear, dayOfMonth ->
                val date = LocalDate(year, monthOfYear + 1, dayOfMonth)
                editText.setText(date.toString())
                editText.tag = date.toDateTimeAtStartOfDay().millis
                layout.error = null
            }
        datePickerDialog.showYearPickerFirst(false)
        datePickerDialog.setTitle(title)
        editText.setOnFocusChangeListener { _, b ->
            if (b) datePickerDialog.show(childFragmentManager, "DatePickerDialog")
        }
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
                    .make(requireView(), state.error.localizedMessage, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

}