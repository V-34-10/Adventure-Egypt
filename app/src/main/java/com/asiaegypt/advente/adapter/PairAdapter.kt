package com.asiaegypt.advente.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.asiaegypt.advente.R
import com.asiaegypt.advente.model.Pairs

class PairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pairImage: ImageView = itemView.findViewById(R.id.pairResImage)
}

class PairAdapter (
    private val pairList: List<Pairs>,
    private val level: String,
    private val theme: String
) :
    RecyclerView.Adapter<PairViewHolder>() {

    var onPairClick: ((Pairs, Int) -> Unit)? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.pair_image, parent, false)
        val cardImage: ImageView = itemView.findViewById(R.id.pairResImage)

        val cardSize = when (level) {
            "Medium" -> {
                60.dpConvertToPx(parent.context)
            }
            "Hard" -> {
                50.dpConvertToPx(parent.context)
            }
            else -> {
                70.dpConvertToPx(parent.context)
            }
        }
        cardImage.layoutParams.width = cardSize
        cardImage.layoutParams.height = cardSize

        return PairViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        val pairItem = pairList[position]
        holder.pairImage.setImageResource(
            if (pairItem.flipped) {
                pairItem.image
            } else {
                getClosedCardResource(theme)
            }
        )

        holder.itemView.setOnClickListener {
            onPairClick?.invoke(pairItem, position)
        }
    }

    private fun getClosedCardResource(theme: String): Int {
        return when (theme) {
            "Actec" -> R.drawable.pair_actec_easy_0
            "Egypt" -> R.drawable.pair_egypt_easy_0
            "Asian" -> R.drawable.pair_asian_easy_0
            else -> R.drawable.pair_actec_easy_0
        }
    }

    override fun getItemCount(): Int = pairList.size

    private fun Int.dpConvertToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
}