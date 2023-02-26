package com.dk.nasa.ui.solar.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dk.nasa.databinding.FragmentMarsBinding
import com.dk.nasa.model.photos.Photos
import com.dk.nasa.ui.solar.fragments.viewModels.MarsViewModel

@RequiresApi(Build.VERSION_CODES.O)
class MarsFragment : Fragment() {

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!
    private val adapter = SolarAdapter()
    private val helper = ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            viewModel.swapItems(viewHolder.adapterPosition, target.adapterPosition)
            recyclerView.adapter?.notifyItemMoved(
                viewHolder.adapterPosition,
                target.adapterPosition
            )
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    })
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

    private fun renderData(marsData: MutableList<Photos>) {

        adapter.submitList(marsData)

        binding.marsRV.adapter = adapter
        helper.attachToRecyclerView(binding.marsRV)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}