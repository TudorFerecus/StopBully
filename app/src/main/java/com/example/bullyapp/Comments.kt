package com.example.bullyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bullyapp.databinding.ActivityCommentsBinding
import com.example.bullyapp.databinding.ActivityMainAppBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class Comments : UtilitiesClass() {


    private lateinit var binding : ActivityCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentsBinding.inflate(layoutInflater)

        binding.btnSendComm.setOnClickListener{
            sendComment()
            binding.etSendComment.setText("")
        }

        setContentView(binding.root)
        getComments()
    }



    fun getComments()
    {
        lifecycleScope.launchWhenCreated {
            var response = try {

                val tokenHeader = "Token ${UserValues.token}"

                var id = intent.getIntExtra("id", 0)
                var content = intent.getStringExtra("content")
                var caption = intent.getStringExtra("caption")

                binding.etCaption.text = caption
                binding.etContent.text = content

                var url = "https://stopbully.herokuapp.com/api/comments/$id/"

                RetrofitInstance.api.getComments(url, tokenHeader)

            } catch (e: HttpException) {
                Log.e("COMMENTS", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                var json = "{" + '"' + "comments" + '"' +':'+ response.body()!!.string() + "}"

                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        json
                    )
                )
                var commentDB = Gson().fromJson(prettyJson, CommentList::class.java)
                Log.e("MainApp", commentDB.toString())
                showComments(commentDB)

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                try {

                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Log.e("Error ", "response : $jObjError")
                    showError(jObjError)

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun sendComment()
    {
        lifecycleScope.launchWhenCreated {
            var response = try {

                val tokenHeader = "Token ${UserValues.token}"

                var id = intent.getIntExtra("id", 0)

                var requestJson = JSONObject()
                requestJson.put("post_id", id)
                requestJson.put("content", binding.etSendComment.text.toString())

                val requestBody = requestJson.toString().toRequestBody("application/json".toMediaTypeOrNull())

                RetrofitInstance.api.sendComment(tokenHeader, requestBody)

            } catch (e: HttpException) {
                Log.e("COMMENTS", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                getComments()

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                try {

                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Log.e("Error ", "response : $jObjError")
                    showError(jObjError)

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }



    fun showComments(commsDP : CommentList)
    {
        var comms = mutableListOf<CommentData>()
        for(comm in commsDP.comments!!)
        {
            //getPostAge(post.created)
            comms.add(comm)
        }
        val adapter = CommentAdapter(comms)

        binding.rvComments.adapter = adapter
        binding.rvComments.layoutManager = LinearLayoutManager(this)

    }


}