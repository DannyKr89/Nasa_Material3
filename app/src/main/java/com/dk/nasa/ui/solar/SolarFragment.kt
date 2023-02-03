package com.dk.nasa.ui.solar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dk.nasa.R
import com.dk.nasa.databinding.FragmentSolarBinding
import com.google.android.material.tabs.TabLayoutMediator

class SolarFragment : Fragment() {

    private var _binding: FragmentSolarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        with(binding){
            vpSolar.adapter = VPAdapter(this@SolarFragment.requireActivity())
            TabLayoutMediator(tlSolar,vpSolar) { tab, position ->
                vpSolar.setCurrentItem(tab.position, true)
                tab.text = when(position){
                    0 ->{
                         getString(R.string.earth)
                    }
                    1 ->{
                        getString(R.string.mars)
                    }
                    2 ->{
                        getString(R.string.weather)
                    }
                    else -> {""}
                }
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}