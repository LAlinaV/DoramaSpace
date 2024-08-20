package com.example.filmspace.viewModel

import Dorama
import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.example.filmspace.R

class DoramaRepository {
    fun getPopular(context: Context):List<Dorama>{
        return buildList {
            val poster = AppCompatResources.getDrawable(context, R.drawable.welcome)
            val title = "Some Dorama"
            val description = "bvekvbqhkvehjqvfjvejvqjfvgvfgrv"
            val year = 2024
            val countSeries = 16
            val countTracks = 3
            val listSeries= mutableListOf<String>()
            val listTracks= mutableListOf<String>()

            val numberOfDoramas=(1..10).random()
            for(i in 0..numberOfDoramas)
                add(Dorama(i,/*poster,*/ title, description, year, countSeries, countTracks, listSeries, listTracks))

        }

    }

    fun getFavorite(context: Context):List<Dorama>{
        return buildList {
            val poster = AppCompatResources.getDrawable(context, R.drawable.welcome)
            val title = "Some Another Dorama"
            val description = "bvekvbqhkvehjqvfjvejvqjfvgvfgrv"
            val year = 2024
            val countSeries = 16
            val countTracks = 3
            val listSeries= mutableListOf<String>()
            val listTracks= mutableListOf<String>()

            val numberOfDoramas=(1..10).random()
            for(i in 0..numberOfDoramas)
                add(Dorama(i,/*poster,*/ title, description, year, countSeries, countTracks, listSeries, listTracks))

        }

    }
}