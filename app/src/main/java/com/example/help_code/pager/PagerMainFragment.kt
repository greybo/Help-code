package com.example.help_code.pager

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.PagerMainFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class PagerMainFragment :
    BaseBindingFragment<PagerMainFragmentBinding>(PagerMainFragmentBinding::inflate) {

    private val args by lazy { arguments?.let { PagerMainFragmentArgs.fromBundle(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pagerActionBarCustom.getBuilder()
            .title("Pager")
            .homeCallback {
                findNavController().popBackStack()
            }
            .create()

        val adapter = PageAdapter(this)
        binding.pagerViewPager2.adapter = adapter
        TabLayoutMediator(binding.pagerTabLayout, binding.pagerViewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Tab 1"
                1 -> "Tab 2"
                else -> "Tab 3"
            }
        }.attach()
        args?.tabNumber?.let {
            binding.pagerViewPager2.currentItem = it.toInt()
        }
    }

    inner class PageAdapter(frg: Fragment) : FragmentStateAdapter(frg) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ItemPageFragment().apply {
                    arguments = bundleOf("data" to "11111")
                }

                1 -> ItemPageFragment().apply {
                    arguments = bundleOf("data" to "22222")
                }
                else -> ItemPageFragment().apply {
                    arguments = bundleOf("data" to "33333")
                }
            }
        }

    }

//    private fun navigateTo(resId: Int, bundle: Bundle) {
//        val finalHost = NavHostFragment.create(R.navigation.nav_page)
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.navHostPager, finalHost)
//            .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
//            .commit()
//    }
}