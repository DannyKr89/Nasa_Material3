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
    var theme: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sprefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        theme = sprefs.getInt("themes", R.style.Theme_Nasa)
        changeRB()

        binding.apply.setOnClickListener {
            activity?.recreate()
        }
    }

    private fun changeRB() {
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
        val editor = sprefs.edit()
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbDefault -> {
                    editor.putInt("themes", R.style.Theme_Nasa).apply()
                }
                R.id.rbRed -> {
                    editor.putInt("themes", R.style.Red).apply()
                }
                R.id.rbYellow -> {
                    editor.putInt("themes", R.style.Yellow).apply()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
