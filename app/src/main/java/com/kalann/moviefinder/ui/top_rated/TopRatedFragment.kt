package com.kalann.moviefinder.ui.top_rated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kalann.moviefinder.R
import com.kalann.moviefinder.databinding.FragmentNowPlayingBinding
import com.kalann.moviefinder.databinding.FragmentTopRatedBinding

class TopRatedFragment: Fragment() {
    lateinit var fragmentTopRatedBinding: FragmentTopRatedBinding
    lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTopRatedBinding = FragmentTopRatedBinding.inflate(inflater, container, false)
        rootView = fragmentTopRatedBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }
}