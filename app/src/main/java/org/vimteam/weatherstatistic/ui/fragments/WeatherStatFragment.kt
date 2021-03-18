package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.vimteam.weatherstatistic.databinding.FragmentStatResultBinding
import org.vimteam.weatherstatistic.domain.contracts.WeatherStatContract
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
        initView()
        val requestHistory = args.requestHistory
        if (requestHistory != null) {
            binding.placeNameTextView.text = requestHistory.city.name
            binding.periodTextView.text = "${requestHistory.dateFrom} - ${requestHistory.dateTo}"
        } else {
            binding.placeNameTextView.text = "New request to API"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        weatherStatViewModel.weatherStatState.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }

    private fun renderView(state: WeatherStatState) {
        when (state) {
            is WeatherStatState.Success -> {
                //TODO fill view with data
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

}