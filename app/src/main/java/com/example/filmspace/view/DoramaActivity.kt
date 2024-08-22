package com.example.filmspace.view

import Dorama
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.filmspace.R
import com.example.filmspace.data.DoramaSeria
import com.example.filmspace.databinding.ActivityDoramaBinding

class DoramaActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDoramaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDoramaBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        val dorama = intent.getSerializableExtra("dorama") as Dorama
        mBinding.movieTitlePreview.text = dorama.title
        mBinding.movieYearPreview.text = dorama.year.toString()
        mBinding.movieDescriptionPreview.text = dorama.description

        Glide.with(this)
            .load(dorama.posterUrl)
            .into(mBinding.movieImagePreview)

        val items = mutableListOf<String>()
        items.add(getString(R.string.seria))
        for(i in 1..dorama.countSeries){
            items.add(getString(R.string.seria) +" $i")
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.series.adapter = adapter

        mBinding.series.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val selectedSeria = mBinding.series.selectedItemPosition
                    val doramaSeria = DoramaSeria(dorama, selectedSeria)
                    val intent = Intent(this@DoramaActivity, SeriaActivity::class.java)
                    intent.putExtra("doramaSeria", doramaSeria)
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }


        mBinding.backButton.setOnClickListener {
            finish()
        }
    }
}