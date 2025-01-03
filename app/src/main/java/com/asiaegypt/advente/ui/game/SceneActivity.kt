package com.asiaegypt.advente.ui.game

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.asiaegypt.advente.NavigationManager
import com.asiaegypt.advente.R
import com.asiaegypt.advente.databinding.ActivitySceneBinding
import com.asiaegypt.advente.ui.game.fragments.ActecFragment
import com.asiaegypt.advente.ui.game.fragments.AsianFragment
import com.asiaegypt.advente.ui.game.fragments.EgyptFragment
import com.asiaegypt.advente.ui.game.managers.ManagerFindPair
import com.asiaegypt.advente.ui.levels.LevelActivity

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    private val preferences by lazy {
        getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)
        initGameFindPairFragment()
    }

    private fun initGameFindPairFragment() = replaceFragment(createFragmentForTheme(preferences.getString("themeFindPair", getString(R.string.button_theme_first))), R.id.container_game_find_pair)

    private fun createFragmentForTheme(theme: String?): Fragment {
        return when (theme) {
            getString(R.string.button_theme_second) -> EgyptFragment()
            getString(R.string.button_theme_three) -> AsianFragment()
            else -> ActecFragment()
        }
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int) {
        supportFragmentManager.commit {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        ManagerFindPair.resetFindPairGame(binding)
        if (!handleBackStack()) {
            navigateToLevelActivity()
        }
    }

    private fun handleBackStack(): Boolean {
        return if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }

    private fun navigateToLevelActivity() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        startActivity(Intent(this, LevelActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        finish()
    }
}