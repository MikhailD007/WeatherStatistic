package org.vimteam.weatherstatistic.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.vimteam.weatherstatistic.databinding.FragmentStatResultBinding
import org.vimteam.weatherstatistic.domain.models.RequestHistory

class WeatherStatFragment : Fragment() {

    private var _binding: FragmentStatResultBinding? = null
    private val binding get() = _binding!!

    private val args: WeatherStatFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val requestHistory = args.requestHistory.also {

        }
        if (requestHistory != null) {
            //binding.dumbTextView.text = requestHistory.city.name + "\n" + requestHistory.dateFrom + " - " + requestHistory.dateTo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}