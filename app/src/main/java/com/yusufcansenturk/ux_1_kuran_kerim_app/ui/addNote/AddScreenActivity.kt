package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_screen)
    }
}