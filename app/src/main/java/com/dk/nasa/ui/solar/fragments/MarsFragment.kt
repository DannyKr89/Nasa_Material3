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
import com.dk.nasa.databinding.FragmentMarsBinding
import com.dk.nasa.model.marsRover.MarsData
import com.dk.nasa.ui.solar.fragments.viewModels.MarsViewModel

@RequiresApi(Build.VERSION_CODES.O)
class MarsFragment : Fragment() {

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[MarsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendRequest()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(marsData: MarsData) {
        with(binding){
            imMars.load(marsData.photos.first().img_src)
            tvDescription.text = marsData.photos.first().camera.full_name
            tvDate.text = marsData.photos.first().earth_date
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}