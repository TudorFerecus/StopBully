package com.example.bullyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

open class UtilitiesClass : AppCompatActivity() {

    var boolean : Boolean = false

    fun <T>switchActivities(newActivity: Class<T>) {

        val intent = Intent(applicationContext, newActivity)
        startActivity(intent)

    }

    fun showError(jsonObject: JSONObject)
    {
        val password = jsonObject.getString("password").substringBefore(',')
        val email = jsonObject.getString("email").substringBefore(',')
        val non_field = jsonObject.getString("non_field_errors").substringBefore(',')

        if(!jsonObject.getString("password").isEmpty())
            Toast.makeText(this, password.takeLast(password.length - 2).take(password.length-3), Toast.LENGTH_LONG).show()
        if(!jsonObject.getString("email").isEmpty())
            Toast.makeText(this, email.takeLast(email.length - 2).take(email.length-3), Toast.LENGTH_LONG).show()



    }

    fun getPostAge(time : String) : String
    {
        var string = "1 minute ago"
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val currentYear = currentDate.take(10).takeLast(4)
        val currentMonth = currentDate.take(5).takeLast(1)
        val currentDay = currentDate.take(2)

        val currentMin = currentDate.take(15).takeLast(2)
        val currentHour = currentDate.take(12).takeLast(2)

        val year = time.take(4)
        val month = time.take(7).takeLast(2)
        val day = time.take(10).takeLast(2)
        val daysAgo = currentYear.toInt() * 356 + currentMonth.toInt() * 30 + currentDay.toInt() - year.toInt() * 356 - month.toInt() * 30 - day.toInt()

        if(daysAgo == 0)
        {
            //TODO
        }

        if(daysAgo in 1..6)
        {
            if(daysAgo == 1)
                string = "one day ago"
            else string = "$daysAgo days ago"
        }
        else if(daysAgo in 7..355)
        {
            val weeksAgo = daysAgo/7
            string ="$weeksAgo weeks ago"
        }
        else if(daysAgo >= 355)
        {
            val yearsAgo = daysAgo/356
            string = "$yearsAgo weeks ago"
        }
        return string
    }


}