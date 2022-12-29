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
import com.dk.nasa.model.PictureOfTheDayData
import com.google.android.material.bottomsheet.BottomSheetBehavior

@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        viewModel.getLiveData().observe(viewLifecycleOwner) { appState ->
            when (appState) {
                is AppState.Error -> {

                }
                AppState.Loading -> {

                }
                is AppState.Success -> {
                    renderData(appState.pictureOfTheDayData)
                }
            }
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputET.text.toString()}")
            })
        }

        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)
    }

    private fun renderData(pictureOfTheDayData: PictureOfTheDayData) {
        with(binding) {
            pictureOfTheDay.load(pictureOfTheDayData.url)
            bottomSheet.bottomSheetDescriptionHeader.text = pictureOfTheDayData.title
            bottomSheet.bottomSheetDescription.text = pictureOfTheDayData.explanation

        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}