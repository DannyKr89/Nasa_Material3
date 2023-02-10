package com.dk.nasa.ui.solar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dk.nasa.ui.solar.fragments.EarthFragment
import com.dk.nasa.ui.solar.fragments.MarsFragment
import com.dk.nasa.ui.solar.fragments.SolarWeatherFragment

@RequiresApi(Build.VERSION_CODES.O)
class VPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOf(EarthFragment(), MarsFragment(), SolarWeatherFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}