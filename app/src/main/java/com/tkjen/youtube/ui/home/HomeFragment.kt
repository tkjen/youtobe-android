package com.tkjen.youtube.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.tkjen.youtube.R
import com.tkjen.youtube.databinding.FragmentHomeBinding
import com.tkjen.youtube.ui.home.adapter.CategoryViewPagerAdapter
import com.tkjen.youtube.ui.home.adapter.VideoAdapter
import com.tkjen.youtube.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding


    private lateinit var viewPagerAdapter: CategoryViewPagerAdapter

    private val categories = listOf(
    "All" ,
        "Music" ,
    "Gaming" ,
    "Sports" ,
    "News" ,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventClick()
        setupViewPager()
        setupTabLayout()
    }

    private fun eventClick() {
        binding.icProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

    }

    private fun setupViewPager() {
        viewPagerAdapter = CategoryViewPagerAdapter(requireActivity(), categories)
        binding.viewPager.adapter = viewPagerAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null
    }



}
