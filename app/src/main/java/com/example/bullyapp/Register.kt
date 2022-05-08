package com.example.bullyapp

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bullyapp.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response


class Register : UtilitiesClass() {

    lateinit var responseGlobal : Response<ResponseBody>
    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            createUser(binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.text.toString())
        }

    }


    fun createUser(email : String, password1 : String, password2: String) {
        lifecycleScope.launchWhenCreated {
            var response = try {

                val jsonObject = JSONObject()
                jsonObject.put("email", email)
                jsonObject.put("password", password1)
                jsonObject.put("password2", password2)

                Log.e("Register", jsonObject.toString())

                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                RetrofitInstance.api.postUser(requestBody)
            } catch (e: HttpException) {
                Log.e(SIGNIN, "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful) {

                switchActivities(Test::class.java)

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

    fun showErrorSignIn()
    {
        Toast.makeText(this,"Incorrect password/email", Toast.LENGTH_LONG).show()
    }

}