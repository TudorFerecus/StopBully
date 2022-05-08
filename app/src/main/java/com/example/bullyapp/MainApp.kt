package com.example.bullyapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bullyapp.databinding.ActivityMainAppBinding
import com.example.bullyapp.databinding.PostPopUpBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import kotlin.concurrent.thread

//e1db6a4365a7bae1c5d43faf90e744e38bcd8ed3
class MainApp() : UtilitiesClass() {
    private lateinit var binding : ActivityMainAppBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPosts()

        binding.btnPost.setOnClickListener{
            val view = View.inflate(this@MainApp,R.layout.post_pop_up,null)

            val builder = AlertDialog.Builder(this@MainApp)
            builder.setView(view)
            val etContent = view.findViewById<EditText>(R.id.etContent1)
            val etCaption = view.findViewById<EditText>(R.id.etCaption1)
            builder.setPositiveButton("Post"){
                dialog, id -> post(etCaption.text.toString(), etContent.text.toString())
                dialog.cancel()
            }
            builder.setNegativeButton("Cancel"){
                    dialog, id -> dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }

        binding.btnGoToProfile.setOnClickListener{
            switchActivities(ProfilePage::class.java)
        }
        val recycler = findViewById<RecyclerView>(R.id.rvTodos)


        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    fun getPosts() {
        lifecycleScope.launchWhenCreated {
            var response = try {

                val tokenHeader = "Token ${UserValues.token}"

                RetrofitInstance.api.getPosts(tokenHeader)
            } catch (e: HttpException) {
                Log.e(SIGNIN, "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                    var json = "{" + '"' + "post" + '"' +':'+ response.body()!!.string() + "}"
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        json
                    )
                )
                var postsDB = Gson().fromJson(prettyJson, PostList::class.java)
                    Log.e("MainApp", json)
                showPosts(postsDB)


//                Log.e("MainApp", posts.toString())

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

    fun post(caption : String, content : String)
    {

        lifecycleScope.launchWhenCreated {
            var response = try {

                val tokenHeader = "Token ${UserValues.token}"

                val jsonBody = JSONObject()
                jsonBody.put("caption", caption )
                jsonBody.put("content", content)

                val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

                RetrofitInstance.api.post(tokenHeader, requestBody)
            } catch (e: HttpException) {
                Log.e("MainApp", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                getPosts()
                Log.e("Post", response.body()?.string()!!)

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                try {

                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Log.e("Error ", "response : $jObjError")

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun showPosts(postsDB: PostList)
    {
        var posts = mutableListOf<Post>()
        for(post in postsDB.post!!)
        {
            getPostAge(post.created)
            posts.add(post)
        }
        val adapter = PostAdapter(posts, object : OnSwitchActivities{
            override fun onLoaded(id : Int, content: String, caption: String)
            {

                val intent = Intent(applicationContext, Comments::class.java)
                intent.putExtra("id", id)
                intent.putExtra("content", content)
                intent.putExtra("caption", caption)
                Log.e("id",id.toString())
                startActivity(intent)
            }
        }
        )
        binding.rvTodos.adapter = adapter
        binding.rvTodos.layoutManager = LinearLayoutManager(this)

    }

}