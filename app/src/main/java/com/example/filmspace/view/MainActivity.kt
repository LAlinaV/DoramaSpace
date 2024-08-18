package com.example.filmspace.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.filmspace.R
import com.example.filmspace.data.DoramaAdapter
import com.example.filmspace.data.DoramaRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var doramaRecyclerView: RecyclerView
    private val doramaRepository=DoramaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var isFavorite = false
        val popular = findViewById<Button>(R.id.buttonPopular)
        val favorite = findViewById<Button>(R.id.buttonFavorite)
        val titlePage = findViewById<TextView>(R.id.title)
        val buttonLogout = findViewById<ImageButton>(R.id.buttonLogout)


        doramaRecyclerView = findViewById(R.id.chats)
        val doramaAdapter = DoramaAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        doramaRecyclerView.adapter = doramaAdapter
        doramaRecyclerView.layoutManager = layoutManager
        doramaAdapter.doramas = doramaRepository.getPopular(this)
        doramaRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        /*chatsRecyclerView.addItemDecoration(ChatPreviewOffsetItemDecoration(
            bottomOffset = 16f.toInt(),
            topOffset = 16f.toInt()
        ))*/
        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            if(isFavorite){
                doramaAdapter.doramas= doramaRepository.getFavorite(this)
                pullToRefresh.isRefreshing = false
            } else {
                doramaAdapter.doramas= doramaRepository.getPopular(this)
                pullToRefresh.isRefreshing = false
            }
        }

        popular.setOnClickListener {
            doramaAdapter.doramas = doramaRepository.getPopular(this)
            titlePage.setText("Popular Doramas")
            isFavorite = false
        }

        favorite.setOnClickListener {
            doramaAdapter.doramas= doramaRepository.getFavorite(this)
            titlePage.setText("Favorite Doramas")
            isFavorite = true
        }

        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

    }
}