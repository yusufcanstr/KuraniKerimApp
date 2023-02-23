package com.yusufcansenturk.ux_1_kuran_kerim_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.ActivityMainBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote.AddNoteFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote.AddScreenFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite.FavoriteFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.HomeFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        replaceFragment(HomeFragment())
        navigationBarFunction()



    }

    private fun navigationBarFunction() {
        binding.navigationBar.setItemSelected(R.id.menu_Anasayfa)
        binding.navigationBar.setOnItemSelectedListener {
            when(it) {
                R.id.menu_Anasayfa -> { replaceFragment(HomeFragment())}
                R.id.menu_Favorite -> { replaceFragment(FavoriteFragment())}
                R.id.menu_AddNote -> { replaceFragment(AddNoteFragment()) }
                R.id.menu_Ayarlar -> { replaceFragment(SettingsFragment())}
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_Layout, fragment)
        fragmentTransaction.commit()

    }

    fun hideNavigationBar() {
        binding.navigationBar.visibility = View.GONE
    }

    fun showNavigationBar() {
        binding.navigationBar.visibility = View.VISIBLE
    }


}