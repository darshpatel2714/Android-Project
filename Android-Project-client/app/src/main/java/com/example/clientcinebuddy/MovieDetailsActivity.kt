package com.example.clientcinebuddy


import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clientcinebuddy.Adapter.MovieShowAdapter
import com.example.clientcinebuddy.Model.GetVideoDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*


class MovieDetailsActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
    }
}
//class MovieDetailsActivity : AppCompatActivity(), MovieShowAdapter.MovieClickListener {
//    private lateinit var MoviesThumbNail: ImageView
//    private lateinit var MoviesCoverImg: ImageView
//    private lateinit var tv_title: TextView
//    private lateinit var tv_description: TextView
//    private lateinit var play_fab: FloatingActionButton
//    private lateinit var RvCase: RecyclerView
//    private lateinit var recyclerView_similarMovies: RecyclerView
//    private lateinit var movieShowAdapter: MovieShowAdapter
//    private lateinit var mDatabasereferance: DatabaseReference
//    private lateinit var uploads: MutableList<GetVideoDetails>
//    private lateinit var actionsmovies: MutableList<GetVideoDetails>
//    private lateinit var sportMovies: MutableList<GetVideoDetails>
//    private lateinit var comedymovies: MutableList<GetVideoDetails>
//    private lateinit var romanticmovies: MutableList<GetVideoDetails>
//    private lateinit var advanturemovies: MutableList<GetVideoDetails>
//    private lateinit var current_video_url: String
//    private lateinit var current_video_category: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_movie_details)
//        inView()
//        similarmoviesRecycler()
//        similarMovies()
//        play_fab.setOnClickListener {
//            val intent = Intent(this@MovieDetailsActivity, MoviePlayerActivity::class.java)
//            intent.putExtra("videoUri", current_video_url)
//            startActivity(intent)
//        }
//    }
//
//    private fun similarMovies() {
//        when (current_video_category) {
//            "Action" -> {
//                movieShowAdapter = MovieShowAdapter(this, actionsmovies, this)
//                recyclerView_similarMovies.adapter = movieShowAdapter
//                recyclerView_similarMovies.layoutManager =
//                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
//                movieShowAdapter.notifyDataSetChanged()
//            }
//            "Sports" -> {
//                movieShowAdapter = MovieShowAdapter(this, sportMovies, this)
//                recyclerView_similarMovies.adapter = movieShowAdapter
//                recyclerView_similarMovies.layoutManager =
//                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
//                movieShowAdapter.notifyDataSetChanged()
//            }
//            "Adventure" -> {
//                movieShowAdapter = MovieShowAdapter(this, advanturemovies, this)
//                recyclerView_similarMovies.adapter = movieShowAdapter
//                recyclerView_similarMovies.layoutManager =
//                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
//                movieShowAdapter.notifyDataSetChanged()
//            }
//            "Comedy" -> {
//                movieShowAdapter = MovieShowAdapter(this, comedymovies, this)
//                recyclerView_similarMovies.adapter = movieShowAdapter
//                recyclerView_similarMovies.layoutManager =
//                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
//                movieShowAdapter.notifyDataSetChanged()
//            }
//            "Romantic" -> {
//                movieShowAdapter = MovieShowAdapter(this, romanticmovies, this)
//                recyclerView_similarMovies.adapter = movieShowAdapter
//                recyclerView_similarMovies.layoutManager =
//                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
//                movieShowAdapter.notifyDataSetChanged()
//            }
//        }
//    }
//
//    private fun similarmoviesRecycler() {
//        uploads = ArrayList()
//        sportMovies = ArrayList()
//        comedymovies = ArrayList()
//        romanticmovies = ArrayList()
//        advanturemovies = ArrayList()
//        actionsmovies = ArrayList()
//        mDatabasereferance = FirebaseDatabase.getInstance().getReference("video")
//        mDatabasereferance.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (postsnapshot in dataSnapshot.children) {
//                    val upload = postsnapshot.getValue(GetVideoDetails::class.java)
//                    when (upload?.video_category) {
//                        "Action" -> actionsmovies.add(upload)
//                        "Sports" -> sportMovies.add(upload)
//                        "Adventure" -> advanturemovies.add(upload)
//                        "Comedy" -> comedymovies.add(upload)
//                        "Romantic" -> romanticmovies.add(upload)
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//    }
//
//    private fun inView() {
//        play_fab = findViewById(R.id.play_fab)
//        tv_title = findViewById(R.id.detail_movie_title)
//        tv_description = findViewById(R.id.detail_movie_desc)
//        MoviesThumbNail = findViewById(R.id.details_movies_img)
//        MoviesCoverImg = findViewById(R.id.detail_movies_cover)
//        recyclerView_similarMovies = findViewById(R.id.recyler_similar_movies)
//        val movieTitle = intent.extras?.getString("title")
//        val imgRecoresId = intent.extras?.getString("imgURL")
//        val imageCover = intent.extras?.getString("imgCover")
//        val moviesDetailstext = intent.extras?.getString("movieDetails")
//        val movieUrl = intent.extras?.getString("movieUrl")
//        val movieCategory = intent.extras?.getString("movieCategory")
//        current_video_url = movieUrl!!
//        current_video_category = movieCategory!!
//        Glide.with(this).load(imgRecoresId).into(MoviesThumbNail)
//        Glide.with(this).load(imageCover).into(MoviesCoverImg)
//        tv_title.text = movieTitle
//        tv_description.text = moviesDetailstext
//        supportActionBar?.title = movieTitle
//    }
//
//    override fun onMovieClick(movie: GetVideoDetails, imageView: ImageView) {
//        tv_title.text = movie.video_name
//        supportActionBar?.title = movie.video_name
//        Glide.with(this).load(movie.video_thumb).into(MoviesThumbNail)
//        Glide.with(this).load(movie.video_thumb).into(MoviesCoverImg)
//        tv_description.text = movie.video_description
//        current_video_url = movie.video_url
//        current_video_category = movie.video_category
//        val options = ActivityOptions.makeSceneTransitionAnimation(
//            this@MovieDetailsActivity,
//            imageView,
//            "sharedName"
//        )
//        options.toBundle()
//    }
//}


