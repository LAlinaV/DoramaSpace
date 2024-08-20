package com.example.filmspace.view

import Dorama
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.filmspace.R
import com.example.filmspace.viewModel.DoramaAdapter
import com.example.filmspace.viewModel.DoramaRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var doramaRecyclerView: RecyclerView
    private val doramaRepository= DoramaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var isFavorite = false
        val popular = findViewById<Button>(R.id.buttonPopular)
        val favorite = findViewById<Button>(R.id.buttonFavorite)
        val titlePage = findViewById<TextView>(R.id.title)
        val buttonLogout = findViewById<ImageButton>(R.id.buttonLogout)


        val doramas = listOf(
            Dorama(1,  /*ContextCompat.getDrawable(this, R.drawable.welcome), */"Title 1", "Description 1", 2021, 12, 3, mutableListOf("Series 1", "Series 2"), mutableListOf("Track 1", "Track 2")),
            Dorama(2, /*ContextCompat.getDrawable(this, R.drawable.baseline_camera_24),*/ "Title 2", "Description 2", 2020, 10, 2, mutableListOf("Series 1", "Series 2"), mutableListOf("Track 1", "Track 2"))
        )


        doramaRecyclerView = findViewById(R.id.chats)
        val doramaAdapter = DoramaAdapter(doramaRepository.getPopular(this))

        doramaAdapter.setOnClickListener(object : DoramaAdapter.OnClickListener {
            override fun onClick(position: Int, model: Dorama) {
                val intent = Intent(
                    this@MainActivity,
                    DoramaActivity::class.java
                )
                intent.putExtra("dorama", model)
                startActivity(intent)
            }
        })

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        doramaRecyclerView.adapter = doramaAdapter
        doramaRecyclerView.layoutManager = layoutManager

        doramaRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        /*chatsRecyclerView.addItemDecoration(ChatPreviewOffsetItemDecoration(
            bottomOffset = 16f.toInt(),
            topOffset = 16f.toInt()
        ))*/
        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        pullToRefresh.setOnRefreshListener {
            if(isFavorite){
                doramaAdapter.updateDoramas( doramaRepository.getFavorite(this))
                //doramaAdapter.updateDoramas(doramas)
                pullToRefresh.isRefreshing = false
            } else {
                doramaAdapter.updateDoramas(doramaRepository.getPopular(this))
                //doramaAdapter.updateDoramas(doramas)
                pullToRefresh.isRefreshing = false
            }
        }

        popular.setOnClickListener {
            doramaAdapter.updateDoramas(doramaRepository.getPopular(this))
            //doramaAdapter.updateDoramas(doramas)
            titlePage.setText("Popular Doramas")
            isFavorite = false
        }

        favorite.setOnClickListener {
            doramaAdapter.updateDoramas(doramaRepository.getFavorite(this))
            //doramaAdapter.updateDoramas(doramas)
            titlePage.setText("Favorite Doramas")
            isFavorite = true
        }

        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

    }
}