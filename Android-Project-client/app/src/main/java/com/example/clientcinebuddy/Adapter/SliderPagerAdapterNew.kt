package com.example.clientcinebuddy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.clientcinebuddy.Model.SliderSide
import com.example.clientcinebuddy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SliderPagerAdapterNew(private val mContext: Context, private val mList: List<SliderSide>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.slide_item, null)
        val slideimage: ImageView = slideLayout.findViewById(R.id.slide_img)
        val slidetitle: TextView = slideLayout.findViewById(R.id.slide_title)
        val floatingActionButton: FloatingActionButton = slideLayout.findViewById(R.id.floatingActionButton)

        Glide.with(mContext).load(mList[position].video_thumb).into(slideimage)
        slidetitle.text = "${mList[position].video_name}\n${mList[position].video_description}"

        floatingActionButton.setOnClickListener { view ->
            // Handle click
        }

        container.addView(slideLayout)
        return slideLayout
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}