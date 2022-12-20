package com.yusufcansenturk.ux_1_kuran_kerim_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.ActivityMainBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote.AddNoteFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite.FavoriteFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.HomeFragment
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Navigation bar sabitleme işlemi kaldı ve diğer tasarımlar yapılacak!!!

        replaceFragment(HomeFragment())
        navigationBarFunction()


    }

    private fun navigationBarFunction() {
        binding.NavigationBar.setItemSelected(R.id.menu_Anasayfa)
        binding.NavigationBar.setOnItemSelectedListener {
            when(it) {
                R.id.menu_Anasayfa -> { replaceFragment(HomeFragment())}
                R.id.menu_Favorite -> { replaceFragment(FavoriteFragment())}
                R.id.menu_AddNote -> { replaceFragment(AddNoteFragment())}
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


}