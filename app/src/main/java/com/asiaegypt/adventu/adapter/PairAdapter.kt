package com.asiaegypt.adventu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.model.Pairs

class PairAdapter (
    private val pairList: List<Pairs>,
    private val level: String,
    private val theme: String
) :
    RecyclerView.Adapter<PairAdapter.PairViewHolder>() {

    var onPairClick: ((Pairs, Int) -> Unit)? = null

    class PairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pairImage: ImageView = itemView.findViewById(R.id.pairResImage)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.pair_image, parent, false)
        val cardImage: ImageView = itemView.findViewById(R.id.pairResImage)

        val cardSize = if (level == "Medium" || level == "Hard") {
            80.dpToPx(parent.context)
        } else {
            100.dpToPx(parent.context)
        }
        cardImage.layoutParams.width = cardSize
        cardImage.layoutParams.height = cardSize

        return PairViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        val pairItem = pairList[position]
        if (pairItem.flipped) {
            holder.pairImage.setImageResource(pairItem.image)
        } else {
            when (theme) {
                "Actec" -> {
                    holder.pairImage.setImageResource(R.drawable.pair_actec_easy_0)
                }

                "Egypt" -> {
                    holder.pairImage.setImageResource(R.drawable.pair_egypt_easy_0)
                }

                "Asian" -> {
                    holder.pairImage.setImageResource(R.drawable.pair_asian_easy_0)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onPairClick?.invoke(pairItem, position)
        }
    }

    override fun getItemCount(): Int {
        return pairList.size
    }

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density).toInt()
    }
}