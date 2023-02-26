package com.dk.nasa.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.dk.nasa.R
import com.dk.nasa.databinding.FragmentMainBinding
import com.dk.nasa.model.pictureOfTheDay.PictureOfTheDayData
import com.google.android.material.bottomsheet.BottomSheetBehavior

@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment() {


    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendRequest()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            when (group.checkedChipId) {
                R.id.chipToday -> {
                    viewModel.setDate(0)
                    viewModel.sendRequest()
                }
                R.id.chipYesterday -> {
                    viewModel.setDate(1)
                    viewModel.sendRequest()
                }
                R.id.chipAfterYesterday -> {
                    viewModel.setDate(2)
                    viewModel.sendRequest()
                }
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.pictureOfTheDay.transitionAlpha = 1 - slideOffset
            }

        })

        viewModel.getLiveData().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Error -> {
                    hideProgressbar()
                }
                AppState.Loading -> {
                    showProgressbar()
                }
                is AppState.Success -> {
                    hideProgressbar()
                    renderData(appState.pictureOfTheDayData)
                }
            }
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputET.text.toString()}")
            })
        }
    }

    private fun showProgressbar() {
        with(binding) {
            progressbar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressbar() {
        with(binding) {
            progressbar.visibility = View.GONE
        }
    }

    private fun renderData(pictureOfTheDayData: PictureOfTheDayData) {
        with(binding) {
            chipHD.setOnCheckedChangeListener { _, b ->
                setHD(b, pictureOfTheDayData)
            }
            setHD(chipHD.isChecked, pictureOfTheDayData)
            bottomSheet.bottomSheetDescriptionHeader.text = pictureOfTheDayData.title
            bottomSheet.bottomSheetDescription.text = pictureOfTheDayData.explanation

        }
    }

    private fun setHD(isHD: Boolean, pictureOfTheDayData: PictureOfTheDayData) {
        if (isHD) {
            with(binding) {
                chipHD.text = getString(R.string.hd)
                pictureOfTheDay.load(pictureOfTheDayData.hdurl)
            }

        } else {
            with(binding) {
                chipHD.text = getString(R.string.sd)
                pictureOfTheDay.load(pictureOfTheDayData.url)
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}