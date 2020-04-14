package com.arthur.eventsapp.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arthur.eventsapp.R
import com.arthur.eventsapp.view.fragments.MainFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.content_frame,
                MainFragment()
            ).commit()
    }

}