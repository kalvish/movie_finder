package com.kalann.moviefinder.ui.movie_details

import android.content.Context


import android.view.LayoutInflater


import android.view.View


import android.view.ViewGroup


import android.widget.ImageView


import androidx.viewpager.widget.PagerAdapter
import coil.load
import com.kalann.moviefinder.R


public class MoviePagerAdapter(var list: List<String>, var ctx: Context) : PagerAdapter() {
    lateinit var layoutInflater:LayoutInflater
    lateinit var context:Context

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(ctx)
        val view = layoutInflater.inflate(R.layout.view_pager_images_item,container,false)
        val img = view.findViewById<ImageView>(R.id.simpleimg)
        img.load(list[position]) {
            crossfade(true)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
//        img.setImageResource(list[position])
        container.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
