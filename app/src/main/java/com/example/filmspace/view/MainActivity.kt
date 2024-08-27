package com.example.filmspace.view

import com.example.filmspace.model.Dorama
import com.example.filmspace.model.DoramaRepository
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.filmspace.R
import com.example.filmspace.viewModel.DoramaAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var doramaRecyclerView: RecyclerView
    private val doramaRepository= DoramaRepository()
    lateinit var db: FirebaseDatabase
    lateinit var doramaAdapter: DoramaAdapter
    var isFavorite = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state", isFavorite.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            isFavorite = if(savedInstanceState.getString("state") == "true") true else false
        }
        db = FirebaseDatabase.getInstance()
        doramaRepository.readFileDoramas(this)
        doramaAdapter = DoramaAdapter(emptyList(), db, doramaRepository)
        doramaRepository.readFavoriteDoramas(db) {
            if(isFavorite){
                doramaAdapter.updateDoramas(doramaRepository.getFavorite())
            } else {
                doramaAdapter.updateDoramas(doramaRepository.getPopular())
            }
        }

        setContentView(R.layout.activity_main)


        val popular = findViewById<Button>(R.id.buttonPopular)
        val favorite = findViewById<Button>(R.id.buttonFavorite)
        val titlePage = findViewById<TextView>(R.id.title)
        val buttonLogout = findViewById<ImageButton>(R.id.buttonLogout)
        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pull_to_refresh)
        doramaRecyclerView = findViewById(R.id.chats)

        if(isFavorite){
            titlePage.setText(R.string.popular_doramas)
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        doramaRecyclerView.adapter = doramaAdapter
        doramaRecyclerView.layoutManager = layoutManager
        doramaRecyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        /*chatsRecyclerView.addItemDecoration(ChatPreviewOffsetItemDecoration(
            bottomOffset = 16f.toInt(),
            topOffset = 16f.toInt()
        ))*/



        pullToRefresh.setOnRefreshListener {
            if(isFavorite){
                doramaAdapter.updateDoramas( doramaRepository.getFavorite())
                pullToRefresh.isRefreshing = false
            } else {

                doramaAdapter.updateDoramas(doramaRepository.getPopular())
                pullToRefresh.isRefreshing = false

            }
        }

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

        popular.setOnClickListener {
            doramaAdapter.updateDoramas(doramaRepository.getPopular())
            titlePage.setText(R.string.popular_doramas)
            isFavorite = false
        }

        favorite.setOnClickListener {
            doramaAdapter.updateDoramas(doramaRepository.getFavorite())
            titlePage.setText(R.string.favorite_doramas)
            isFavorite = true
        }

        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

    }
}