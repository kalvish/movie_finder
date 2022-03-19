package com.kalann.moviefinder.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kalann.moviefinder.databinding.FragmentPopularBinding

class PopularFragment: Fragment() {
    lateinit var fragmentPopularBinding: FragmentPopularBinding
    lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPopularBinding = FragmentPopularBinding.inflate(inflater, container, false)
        rootView = fragmentPopularBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_popular, container, false)
    }
}