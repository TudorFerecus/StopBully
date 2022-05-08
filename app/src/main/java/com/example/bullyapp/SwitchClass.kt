package com.example.bullyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

object SwitchClass : AppCompatActivity(){

    fun <T>switchActivities(newActivity: Class<T>) {

        val intent = Intent(applicationContext, newActivity)
        startActivity(intent)

    }

}