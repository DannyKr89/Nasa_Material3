package com.dk.nasa.ui.solar.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dk.nasa.databinding.FragmentEarthBinding
import com.dk.nasa.model.epic.EpicData
import com.dk.nasa.model.photos.Photos
import com.dk.nasa.ui.solar.fragments.viewModels.EarthViewModel

@RequiresApi(Build.VERSION_CODES.O)
class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!
    private val adapter = SolarAdapter()

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

    private fun renderData(epicData: EpicData) {
        val list = convertToPhotos(epicData)
        adapter.submitList(list)
        binding.earthRV.adapter = adapter
        }

        private fun convertToPhotos(epicData: EpicData): MutableList<Photos> {
            val year = epicData.first().date.substring(0, 4)
            val month = epicData.first().date.substring(5, 7)
            val day = epicData.first().date.substring(8, 10)
            val mutableList = mutableListOf<Photos>()
            epicData.forEach {
                mutableList.add(
                    Photos(
                        image = "https://epic.gsfc.nasa.gov/archive/natural/$year/$month/$day/png/${it.image}.png",
                        description = it.caption,
                        date = it.date
                    )
                )
            }
            return mutableList
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    }