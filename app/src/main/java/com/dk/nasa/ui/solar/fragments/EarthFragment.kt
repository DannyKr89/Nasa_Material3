package com.dk.nasa.ui.solar.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.dk.nasa.databinding.FragmentEarthBinding
import com.dk.nasa.model.epic.EpicData
import com.dk.nasa.ui.solar.fragments.viewModels.EarthViewModel

@RequiresApi(Build.VERSION_CODES.O)
class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[EarthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendRequest()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(it: EpicData) {
        with(binding){
            val year = it.first().date.substring(0,4)
            val month = it.first().date.substring(5,7)
            val day = it.first().date.substring(8,10)
            imEarth.load("https://epic.gsfc.nasa.gov/archive/natural/$year/$month/$day/png/${it.first().image}.png")
            tvDescription.text = it.first().caption
            tvDate.text = it.first().date
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}