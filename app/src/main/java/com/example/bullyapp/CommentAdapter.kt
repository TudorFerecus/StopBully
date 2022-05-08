package com.example.bullyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.bullyapp.databinding.ItemCommentBinding
import com.example.bullyapp.databinding.ItemPostareBinding
import java.text.SimpleDateFormat
import java.util.*


class CommentAdapter(var comms: List<CommentData>): RecyclerView.Adapter<CommentAdapter.CommentAdapter>() {

    inner class CommentAdapter(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)

        return CommentAdapter(binding)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentAdapter, position: Int) {
        holder.binding.apply {

              tvComm.text = comms[comms.size - position - 1].content

        }
    }

    override fun getItemCount(): Int {
        return  comms.size
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

        val hour = time.take(13).takeLast(2)
        val min = time.take(16).takeLast(2)

        val year = time.take(4)
        val month = time.take(7).takeLast(2)
        val day = time.take(10).takeLast(2)
        val daysAgo = currentYear.toInt() * 356 + currentMonth.toInt() * 30 + currentDay.toInt() - year.toInt() * 356 - month.toInt() * 30 - day.toInt()

        //val hoursAgo = currentHour.toInt() * 60 + currentMin.toInt()  - hour.toInt() * 60 -

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