package com.dk.nasa.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dk.nasa.R
import com.dk.nasa.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var sprefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTheme()

        binding.apply.setOnClickListener {
            activity?.recreate()
        }
    }

    private fun getTheme() {

        sprefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val theme = sprefs.getInt("themes", R.style.Theme_Nasa)

        binding.radioGroup.clearCheck()
        when (theme) {
            R.style.Theme_Nasa -> {
                binding.rbDefault.isChecked = true
            }
            R.style.Red -> {
                binding.rbRed.isChecked = true
            }
            R.style.Yellow -> {
                binding.rbYellow.isChecked = true
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbDefault -> {
                    saveTheme(R.style.Theme_Nasa)
                }
                R.id.rbRed -> {
                    saveTheme(R.style.Red)
                }
                R.id.rbYellow -> {
                    saveTheme(R.style.Yellow)
                }
            }
        }
    }

    private fun saveTheme(theme: Int) {
        val editor = sprefs.edit()
        editor.putInt("themes", theme).apply()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
