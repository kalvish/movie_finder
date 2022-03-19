package com.kalann.moviefinder.ui.now_playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kalann.moviefinder.R
import com.kalann.moviefinder.databinding.FragmentNowPlayingBinding

class NowPlayingFragment: Fragment() {
    lateinit var fragmentNowPlayingBinding: FragmentNowPlayingBinding
    lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNowPlayingBinding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        rootView = fragmentNowPlayingBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }
}