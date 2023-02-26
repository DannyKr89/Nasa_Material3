package com.dk.nasa.ui.solar.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dk.nasa.databinding.FragmentMarsBinding
import com.dk.nasa.model.marsRover.MarsData
import com.dk.nasa.model.photos.Photos
import com.dk.nasa.ui.solar.fragments.viewModels.MarsViewModel

@RequiresApi(Build.VERSION_CODES.O)
class MarsFragment : Fragment() {

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!
    private val adapter = SolarAdapter()

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
        var i = 0
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            if (it.photos.isEmpty()){
                viewModel.setDate(i.toLong())
                i++
                viewModel.sendRequest()
            } else{
                renderData(it)
            }
        }
    }

    private fun renderData(marsData: MarsData) {

        val list = convertToPhotos(marsData)
        adapter.submitList(list)
        println(list)
        binding.marsRV.adapter = adapter
    }

    private fun convertToPhotos(marsData: MarsData): MutableList<Photos> {
        val mutableList = mutableListOf<Photos>()
        marsData.photos.forEach {
            mutableList.add(
                Photos(
                    image = it.img_src,
                    description = it.camera.full_name,
                    date = it.earth_date
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