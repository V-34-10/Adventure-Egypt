package com.asiaegypt.advente.ui.offerwall.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.asiaegypt.advente.R
import com.bumptech.glide.Glide
import com.asiaegypt.advente.ui.offerwall.model.Game

class GameAdapter(private val onGameClick: (Game) -> Unit) :
    RecyclerView.Adapter<GameViewHolder>() {
    private var games: List<Game> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position], onGameClick)
    }

    override fun getItemCount(): Int = games.size
}

class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val backImage: ImageView = itemView.findViewById(R.id.background_game)
    private val foreImage: ImageView = itemView.findViewById(R.id.image_game)
    private val button: Button = itemView.findViewById(R.id.start_game)

    fun bind(game: Game, onGameClick: (Game) -> Unit) {
        Glide.with(itemView.context).load(game.visualFile).into(backImage)
        Glide.with(itemView.context).load(game.auxImage).into(foreImage)
        button.text = game.activateText
        button.setOnClickListener { onGameClick(game) }
        backImage.setOnClickListener { onGameClick(game) }
        foreImage.setOnClickListener { onGameClick(game) }
    }
}