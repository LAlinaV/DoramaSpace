package com.example.filmspace.model

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject

class DoramaRepository {
    private val doramas = mutableListOf<Dorama>()

    fun readFavoriteDoramas(db: FirebaseDatabase, callback: () -> Unit) {
        val favoriteDoramaIds = mutableListOf<String>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val favoritesRef = db.getReference("/users/$userId/favorites")

        favoritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    favoriteDoramaIds.add(snapshot.key!!)
                }
                for (dorama in doramas) {
                    if (dorama.id.toString() in favoriteDoramaIds) {
                        dorama.isFavorite = true
                    }
                }
                callback()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })


    }


    fun readFileDoramas(context: Context) {
        val inputStream = context.assets.open("info.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonString)
        val moviesArray = jsonObject.getJSONArray("doramas")

        for (i in 0 until moviesArray.length()) {
            val movieObject = moviesArray.getJSONObject(i)
            val id = i + 1
            val title = movieObject.getString("title")
            val year = movieObject.getInt("year")
            val posterUrl = movieObject.getString("poster")
            val description = movieObject.getString("description")
            val episodesObject = movieObject.getJSONObject("episodes")
            val episodes = mutableListOf<String>()
            val episodesArray = episodesObject.getJSONArray("links")
            for (i in 0 until episodesArray.length()) {
                episodes.add(episodesArray.getString(i))
            }
            val soundtracksObject = movieObject.getJSONObject("soundtracks")
            val soundtracks = mutableListOf<String>()
            val soundtracksArray = soundtracksObject.getJSONArray("links")
            for (i in 0 until soundtracksArray.length()) {
                soundtracks.add(soundtracksArray.getString(i))
            }

            val dorama = Dorama(
                id,
                false,
                title,
                posterUrl,
                description,
                year,
                episodes.count(),
                soundtracks.count(),
                episodes,
                soundtracks
            )
            doramas.add(dorama)
        }
    }

    fun addFavorite(dorama: Dorama) {
        val index = doramas.indexOfFirst { it.id == dorama.id }
        if (index != -1) {
            doramas[index].isFavorite = true
        }
    }

    fun deleteFavorite(dorama: Dorama){
        val index = doramas.indexOfFirst { it.id == dorama.id }
        if (index != -1) {
            doramas[index].isFavorite = false
        }
    }
    fun getPopular(): List<Dorama> {
        return doramas
    }

    fun getFavorite(): List<Dorama> {
        return doramas.filter { it.isFavorite }
    }

    fun isFavorite(dorama: Dorama): Boolean{
        val index = doramas.indexOfFirst { it.id == dorama.id }
        if (index != -1) {
            return (doramas[index].isFavorite)
        }
        return false
    }
}
