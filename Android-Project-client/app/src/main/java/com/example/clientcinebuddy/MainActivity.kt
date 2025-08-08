package com.example.clientcinebuddy

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.clientcinebuddy.Adapter.MovieShowAdapter
import com.example.clientcinebuddy.Adapter.SliderPagerAdapterNew
import com.example.clientcinebuddy.Model.GetVideoDetails
import com.example.clientcinebuddy.Model.MovieItemClickListenerNew
import com.example.clientcinebuddy.Model.SliderSide
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), MovieItemClickListenerNew {
    private lateinit var movieShowAdapter: MovieShowAdapter
    private lateinit var mDatabaserefence: DatabaseReference
    private lateinit var uploads: MutableList<GetVideoDetails>
    private lateinit var uploadslistLatests: MutableList<GetVideoDetails>
    private lateinit var uploadsListPopular: MutableList<GetVideoDetails>
    private lateinit var actionsmovies: MutableList<GetVideoDetails>
    private lateinit var sportsmovies: MutableList<GetVideoDetails>
    private lateinit var comedymovies: MutableList<GetVideoDetails>
    private lateinit var romanticmovies: MutableList<GetVideoDetails>
    private lateinit var advanturemovies: MutableList<GetVideoDetails>
    private lateinit var uploadsSlider: MutableList<SliderSide>
    private lateinit var sliderPager: ViewPager
    private lateinit var indicator: TabLayout
    private lateinit var MoviesRv: RecyclerView
    private lateinit var moviesRvWeek: RecyclerView
    private lateinit var tab: RecyclerView

    //private lateinit var progressDialog: ProgressBar
    private lateinit var tabmoviesactions: TabLayout
    private lateinit var progressDialog: ProgressDialog

    //edit profile
    private lateinit var textViewWelcomeMessage: TextView
    private lateinit var editProfileButton: ImageView
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.actionbar)
            setDisplayShowCustomEnabled(true)
        }
        //progressDialog = ProgressBar(this)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        inViews()
        addAllMovies()
        iniPopularMovies()
        iniWeekMovies()
        moviesViewTab()


        //edit profile
        textViewWelcomeMessage = findViewById(R.id.textViewWelcomeMessage)
        editProfileButton = findViewById(R.id.editProfile)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val displayName = currentUser?.displayName

        if (displayName != null && displayName.isNotEmpty()) {
            textViewWelcomeMessage.text = "Welcome, $displayName!"
        } else {
            textViewWelcomeMessage.text = "Welcome!"
        }

        editProfileButton.setOnClickListener { view -> onEditProfileButtonClick(view) }

    }


    //edit profile fun
    private fun onEditProfileButtonClick(view: View) {
        // Check if the user is signed in before navigating to the EditProfileActivity
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, open the EditProfileActivity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        } else {
            // User is not signed in, handle accordingly (e.g., show a login screen)
            // For simplicity, this example assumes the user is always signed in
        }

    }

    private fun addAllMovies() {
        uploads = ArrayList()
        uploadslistLatests = ArrayList()
        uploadsListPopular = ArrayList()
        actionsmovies = ArrayList()
        advanturemovies = ArrayList()
        comedymovies = ArrayList()
        sportsmovies = ArrayList()
        romanticmovies = ArrayList()
        uploadsSlider = ArrayList()
        mDatabaserefence = FirebaseDatabase.getInstance().getReference("videos")
        progressDialog.setMessage("loading...")
        progressDialog.show()
        //progressDialog.visibility = View.VISIBLE
        mDatabaserefence.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(GetVideoDetails::class.java)
                    val slide = postSnapshot.getValue(SliderSide::class.java)
                    if (upload?.video_type == "latest movies") {
                        uploadslistLatests.add(upload)
                    }
                    if (upload?.video_type == "Best popular movies") {
                        uploadsListPopular.add(upload)
                    }
                    if (upload?.video_category == "Action") {
                        actionsmovies.add(upload)
                    }
                    if (upload?.video_category == "Adventure") {
                        advanturemovies.add(upload)
                    }
                    if (upload?.video_category == "Comedy") {
                        comedymovies.add(upload)
                    }
                    if (upload?.video_category == "Romantic") {
                        romanticmovies.add(upload)
                    }
                    if (upload?.video_category == "Sports") {
                        sportsmovies.add(upload)
                    }
                    if (upload?.video_slide == "Slide movies" && slide != null) {
                        uploadsSlider.add(slide)
                    }
                    upload?.let { uploads.add(it) }
                }
                iniSlider()
                //progressDialog.visibility = View.GONE
                progressDialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressDialog.dismiss()
            }
        })
    }

    private fun iniSlider() {
        val adapterNew = SliderPagerAdapterNew(this, uploadsSlider)
        sliderPager.adapter = adapterNew
        adapterNew.notifyDataSetChanged()

        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
        indicator.setupWithViewPager(sliderPager, true)
    }


    private fun iniWeekMovies() {
        movieShowAdapter = MovieShowAdapter(this, uploadslistLatests, this)
        moviesRvWeek.adapter = movieShowAdapter
        moviesRvWeek.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun iniPopularMovies() {
        movieShowAdapter = MovieShowAdapter(this, uploadsListPopular, this)
        MoviesRv.adapter = movieShowAdapter
        MoviesRv.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun moviesViewTab() {
        getActionMovies()
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Action"))
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Adventure"))
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Comedy"))
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Romantic"))
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Sports"))
        tabmoviesactions.tabGravity = TabLayout.GRAVITY_FILL
        tabmoviesactions.setTabTextColors(ColorStateList.valueOf(Color.WHITE))
        tabmoviesactions.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> getActionMovies()
                    1 -> getAdvantureMovies()
                    2 -> getComedyMovies()
                    3 -> getRomanticMovies()
                    4 -> getSportMovies()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun inViews() {
        tabmoviesactions = findViewById(R.id.tabActionMovies)
        sliderPager = findViewById(R.id.slider_pager)
        indicator = findViewById(R.id.indicator)
        moviesRvWeek = findViewById(R.id.rv_movies_week)
        MoviesRv = findViewById(R.id.Rv_movies)
        tab = findViewById(R.id.tabrecyler)
    }

    override fun onMovieClick(getVideoDetails: GetVideoDetails, imageView: ImageView) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("title", getVideoDetails.video_name)
        intent.putExtra("imgURL", getVideoDetails.video_thumb)
        intent.putExtra("imgCover", getVideoDetails.video_thumb)
        intent.putExtra("movieDetails", getVideoDetails.video_description)
        intent.putExtra("movieUrl", getVideoDetails.video_url)
        intent.putExtra("movieCategory", getVideoDetails.video_category)
        val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, imageView, "sharedName")
        startActivity(intent, options.toBundle())
    }

    inner class SliderTimer : TimerTask() {
        override fun run() {
            runOnUiThread {
                if (sliderPager.currentItem < uploadsSlider.size - 1) {
                    sliderPager.currentItem = sliderPager.currentItem + 1
                } else {
                    sliderPager.currentItem = 0
                }
            }
        }
    }

    private fun getActionMovies() {
        movieShowAdapter = MovieShowAdapter(this, actionsmovies, this)
        tab.adapter = movieShowAdapter
        tab.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun getSportMovies() {
        movieShowAdapter = MovieShowAdapter(this, sportsmovies, this)
        tab.adapter = movieShowAdapter
        tab.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun getRomanticMovies() {
        movieShowAdapter = MovieShowAdapter(this, romanticmovies, this)
        tab.adapter = movieShowAdapter
        tab.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun getComedyMovies() {
        movieShowAdapter = MovieShowAdapter(this, comedymovies, this)
        tab.adapter = movieShowAdapter
        tab.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }

    private fun getAdvantureMovies() {
        movieShowAdapter = MovieShowAdapter(this, advanturemovies, this)
        tab.adapter = movieShowAdapter
        tab.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        movieShowAdapter.notifyDataSetChanged()
    }



}