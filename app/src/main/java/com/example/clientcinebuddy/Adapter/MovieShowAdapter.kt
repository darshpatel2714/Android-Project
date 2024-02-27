package com.example.clientcinebuddy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientcinebuddy.Model.GetVideoDetails
import com.example.clientcinebuddy.Model.MovieItemClickListenerNew
import com.example.clientcinebuddy.R

class MovieShowAdapter(private val mContext: Context,
                        private val uploads: List<GetVideoDetails>,
                        private val movieItemClickListenerNew: MovieItemClickListenerNew
) : RecyclerView.Adapter<MovieShowAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.movie_item_new, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val getVideoDetails = uploads[position]
        holder.tvTitle.text = getVideoDetails.video_name
        Glide.with(mContext).load(getVideoDetails.video_thumb).into(holder.ImgMovie)
    }

    override fun getItemCount(): Int {
        return uploads.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.item_movie_title)
        var ImgMovie: ImageView = itemView.findViewById(R.id.item_movies_img)
        var container: ConstraintLayout = itemView.findViewById(R.id.container)

        init {
            itemView.setOnClickListener {
                movieItemClickListenerNew.onMovieClick(uploads[adapterPosition], ImgMovie)
            }
        }
    }
}

