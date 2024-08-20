package com.example.filmspace.viewModel

import Dorama
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
import com.example.filmspace.R

class DoramaAdapter(private var doramas: List<Dorama>): RecyclerView.Adapter<ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: Dorama)
    }
    //var doramas = listOf<Dorama>()
        fun updateDoramas(newDoramas: List<Dorama>){
            /*val callback = CallbackImpl(
                oldItems = field,
                newItems = value,
                { oldItem: Dorama, newItem -> oldItem.id == newItem.id }
            )
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)*/
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

    val view = LayoutInflater.from(parent.context).inflate(R.layout.dorama_preview, parent, false)
    val viewHolder = DoramaPreviewViewHolder(view, onClickListener)

    return viewHolder
    }

    override fun getItemCount(): Int {
        return doramas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       if(holder is DoramaPreviewViewHolder) holder.onBind(doramas[position] as Dorama)
        val favoriteButton: ImageButton = holder.itemView.findViewById(R.id.heartButton)
        favoriteButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (favoriteButton.drawable.constantState == ContextCompat.getDrawable(holder.itemView.context, R.drawable.heard_filled)?.constantState) {
                    favoriteButton.setImageResource(R.drawable.heart)
                } else {
                    favoriteButton.setImageResource(R.drawable.heard_filled)
                }

            }
        }
    }

    class DoramaPreviewViewHolder(itemView: View, private val onClickListener: OnClickListener?): RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.poster)
        private val title: TextView = itemView.findViewById(R.id.title)


        fun onBind(dorama: Dorama) {
            //poster.setImageDrawable(doramaPreview.poster)
            title.text = dorama.title
            itemView.setOnClickListener {
                onClickListener!!.onClick(adapterPosition, dorama)
            }
        }

    }
}