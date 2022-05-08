package com.example.bullyapp


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bullyapp.databinding.ActivityProfilePageBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ProfilePage : AppCompatActivity() {
    private lateinit var binding: ActivityProfilePageBinding
    lateinit var view: View
    private var fileUri : Uri? = null

    var path : String = "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F29/ORIGINAL/NONE/1326446723"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        images.add(binding.img1)
//        images.add(binding.img2)
//        images.add(binding.img3)

        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        view = binding.root

        binding.btnImg2.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // show photo
        showPhoto()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            var imageUri = data?.data
            binding.img.setImageURI(imageUri)
        }
    }

    fun showPhoto()
    {
        lifecycleScope.launchWhenCreated {
            var response = try {

                val tokenHeader = "Token ${UserValues.token}"

                val url ="https://stopbully.herokuapp.com//"
                RetrofitInstance.api.getPhoto(tokenHeader)
            } catch (e: HttpException) {
                Log.e("MainApp", "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string()
                    )
                )
                val url_response = Gson().fromJson(prettyJson, PhotoResponse::class.java).image_url
                Picasso.get().load(url_response).resize(1280,720).into(binding.imageView2)
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

}