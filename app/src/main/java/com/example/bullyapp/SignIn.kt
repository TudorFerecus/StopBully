package com.example.bullyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.bullyapp.databinding.ActivityMainBinding
import com.example.bullyapp.databinding.SignInBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

const val SIGNIN = "SignIn"

class SignIn : UtilitiesClass() {
    private lateinit var responseGlobal : Response<Token>
    private lateinit var binding : SignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.isVisible = false;
        ShowButtons(true)


         binding.logBtn.setOnClickListener {
             //rawJSON()
            signIn(binding.username.text.toString(), binding.password.text.toString())



         }

    }
//54/62
    fun ShowButtons(canShow : Boolean)
    {
        binding.username.isVisible = canShow
        binding.password.isVisible = canShow
        binding.logBtn.isVisible = canShow
        binding.forgotPassword.isVisible = canShow
        binding.progressBar.isVisible = !canShow
    }

    fun signIn(email : String, password : String) {
        lifecycleScope.launchWhenCreated {
            var response = try {

                ShowButtons(false)

                val jsonObject = JSONObject()
                jsonObject.put("username", email)
                jsonObject.put("password", password)

                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                RetrofitInstance.api.getUsers(requestBody)
            } catch (e: HttpException) {
                Log.e(SIGNIN, "HttpException, unexpected response")
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

                ShowButtons(true)

                val token = Gson().fromJson(prettyJson, Token::class.java)

                UserValues.token = token.token
                switchActivities(MainApp::class.java)


            } else {

                try {
                    ShowButtons(true)
                    showInvalidSignIn()
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

    fun showInvalidSignIn()
    {
        Toast.makeText(this, "Invalid email/password", Toast.LENGTH_LONG).show()

    }

}
