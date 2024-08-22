package com.example.filmspace.viewModel

import Dorama
import DoramaRepository
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmspace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DoramaAdapter(
    private var doramas: List<Dorama>,
    db: FirebaseDatabase,
    repository: DoramaRepository
) : RecyclerView.Adapter<ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    var dbAdapter: FirebaseDatabase = db
    val doramaRepository = repository

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }


    interface OnClickListener {
        fun onClick(position: Int, model: Dorama)
    }

    fun updateDoramas(newDoramas: List<Dorama>) {
        val callback = CallbackImpl(
            oldItems = doramas,
            newItems = newDoramas,
            { oldItem: Dorama, newItem -> oldItem.id == newItem.id }
        )
        doramas = newDoramas
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dorama_preview, parent, false)
        val viewHolder = DoramaPreviewViewHolder(view, onClickListener, doramaRepository)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return doramas.size
    }

    fun deleteFavoriteDorama(doramaId: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val favoritesRef = dbAdapter.getReference("/users/$userId/favorites")
        favoritesRef.child(doramaId.toString()).removeValue()

    }

    fun addFavoriteDorama(doramaId: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val favoritesRef = dbAdapter.getReference("/users/$userId/favorites")
        favoritesRef.child(doramaId.toString()).setValue(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is DoramaPreviewViewHolder) holder.onBind(doramas[position])
        val favoriteButton: ImageButton = holder.itemView.findViewById(R.id.heartButton)
        favoriteButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (favoriteButton.drawable.constantState == ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.heard_filled
                    )?.constantState
                ) {

                        favoriteButton.setImageResource(R.drawable.heart)

                        deleteFavoriteDorama(doramas[position].id)

                        doramaRepository.deleteFavorite(doramas[position])
                    Log.d("MyTag", "delete4")
                } else {
                    favoriteButton.setImageResource(R.drawable.heard_filled)
                    addFavoriteDorama(doramas[position].id)
                    doramaRepository.addFavorite(doramas[position])
                }

            }
        }
    }

    class DoramaPreviewViewHolder(itemView: View, private val onClickListener: OnClickListener?, doramaRepository: DoramaRepository) :
        ViewHolder(itemView) {
        private val repository = doramaRepository
        private val poster: ImageView = itemView.findViewById(R.id.poster)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val heartButton: ImageButton = itemView.findViewById(R.id.heartButton)


        fun onBind(dorama: Dorama) {
            //poster.setImageDrawable(doramaPreview.poster)
            if (repository.isFavorite(dorama)) {
                heartButton.setImageResource(R.drawable.heard_filled)
            } else {
                heartButton.setImageResource(R.drawable.heart)
            }
            title.text = dorama.title
            Glide.with(itemView.context)
                .load(dorama.posterUrl)
                .into(poster)
            itemView.setOnClickListener {
                onClickListener!!.onClick(adapterPosition, dorama)
            }
        }

    }
}