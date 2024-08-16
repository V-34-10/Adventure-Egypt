package com.asiaegypt.adventu.ui.game.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.FragmentAsianBinding
import com.asiaegypt.adventu.ui.game.managers.GameSettings
import com.asiaegypt.adventu.ui.game.managers.ManagerFindPair
import com.asiaegypt.adventu.ui.score.HighScoreActivity
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

class AsianFragment : Fragment() {
    private lateinit var binding: FragmentAsianBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    private var choiceLevelPairGame: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAsianBinding.inflate(layoutInflater, container, false)

        managerMusic = context?.let { MusicManager(it) }!!
        preferences =
            requireActivity().getSharedPreferences(
                "AsianEgyptAdventurePref",
                AppCompatActivity.MODE_PRIVATE
            )

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MusicStart.musicStartMode(R.raw.music_asian, managerMusic, preferences)

        choiceLevelPairGame = preferences.getString("levelFindPair", "").toString()
        binding.textLevel.text = "Level\n$choiceLevelPairGame"
        setBackground()

        GameSettings.init(requireContext())
        context?.let { ManagerFindPair.initFindPairGame(binding, it) }
        initControlBarGame()
    }

    private fun setBackground() {
        when (choiceLevelPairGame) {
            "Medium" -> binding.backgroundAsian.setBackgroundResource(R.drawable.background_asian_medium)
            "Hard" -> binding.backgroundAsian.setBackgroundResource(R.drawable.background_asian_hard)
        }
    }

    private fun initControlBarGame() {
        var animationButton = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
        binding.btnPlay.setOnClickListener {
            it.startAnimation(animationButton)
            ManagerFindPair.resetFindPairGame(binding)
        }
        binding.btnHighScore.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(context, R.anim.scale_animation)
            it.startAnimation(animationButton)
            ManagerFindPair.resetFindPairGame(binding)
            startActivity(Intent(context, HighScoreActivity::class.java))
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        managerMusic.resume()
    }

    override fun onPause() {
        super.onPause()
        managerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        managerMusic.release()
    }
}