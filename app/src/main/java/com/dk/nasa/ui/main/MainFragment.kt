package com.dk.nasa.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
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
    private var flag = false
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

        zoomImage()

        changeDay()

        initBehavior()

        initViewModel()

        searchInWiki()
    }

    private fun searchInWiki() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputET.text.toString()}")
            })
        }
    }

    private fun initViewModel() {
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
    }

    private fun initBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.pictureOfTheDay.alpha = 1 - slideOffset / 2
            }

        })
    }

    private fun changeDay() {
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
    }

    private fun zoomImage() {
        binding.pictureOfTheDay.setOnClickListener {

            val changeImageTransform = ChangeImageTransform()
            val transitionSet = TransitionSet().apply {
                addTransition(changeImageTransform)
            }
            TransitionManager.beginDelayedTransition(binding.root, transitionSet)
            if (flag) {
                (it as ImageView).scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                (it as ImageView).scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            flag = !flag
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
                val changeBounds = ChangeBounds().apply {
                    duration = 1000
                    interpolator = AnticipateOvershootInterpolator(1f)
                    pathMotion = ArcMotion().apply {
                        minimumHorizontalAngle = 90f
                        maximumAngle = 90f
                    }
                }
                TransitionManager.beginDelayedTransition(root, changeBounds)
                setHD(b, pictureOfTheDayData)
            }
            loadImage(pictureOfTheDayData.url)
            bottomSheet.bottomSheetDescriptionHeader.text = pictureOfTheDayData.title
            var spannableString = SpannableString(pictureOfTheDayData.explanation)
            val colors = arrayOf(
                R.color.red,
                R.color.orange,
                R.color.yellow,
                R.color.green,
                R.color.lightblue,
                R.color.blue,
                R.color.violet,
            )
            bottomSheet.bottomSheetDescription.setText(
                spannableString,
                TextView.BufferType.SPANNABLE
            )
            spannableString = bottomSheet.bottomSheetDescription.text as SpannableString
            var firstSpace = 0
            var lastSpace: Int
            for (i in spannableString.indices) {
                if (spannableString[i] == ' ') {
                    lastSpace = i
                    spannableString.setSpan(
                        ForegroundColorSpan(requireContext().getColor(colors.random())),
                        firstSpace,
                        lastSpace,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    firstSpace = i
                }

            }

        }
    }

    private fun loadImage(image: String) {
        binding.pictureOfTheDay.load(image) {
            crossfade(true)
        }
    }

    private fun setHD(isHD: Boolean, pictureOfTheDayData: PictureOfTheDayData) {

        val params = binding.chipHD.layoutParams as ConstraintLayout.LayoutParams
        if (isHD) {
            with(binding) {
                chipHD.text = getString(R.string.hd)
                params.horizontalBias = 0.95f
                ObjectAnimator.ofFloat(chipHD, View.ROTATION, 0f, 360f).apply {
                    duration = 500
                    startDelay = 200
                    start()
                }
                loadImage(pictureOfTheDayData.hdurl)
            }

        } else {
            with(binding) {
                chipHD.text = getString(R.string.sd)
                params.horizontalBias = 0.05f
                ObjectAnimator.ofFloat(chipHD, View.ROTATION, 360f, 0f).apply {
                    duration = 500
                    startDelay = 200
                    start()
                }
                loadImage(pictureOfTheDayData.url)
            }
        }
        binding.chipHD.layoutParams = params
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}