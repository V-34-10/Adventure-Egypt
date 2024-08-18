package com.asiaegypt.adventu.ui.game

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivitySceneBinding
import com.asiaegypt.adventu.ui.game.fragments.ActecFragment
import com.asiaegypt.adventu.ui.game.fragments.AsianFragment
import com.asiaegypt.adventu.ui.game.fragments.EgyptFragment
import com.asiaegypt.adventu.ui.menu.MenuActivity

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
        if (!handleBackStack()) {
            navigateToMenuActivity()
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

    private fun navigateToMenuActivity() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}