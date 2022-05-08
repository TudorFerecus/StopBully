package com.example.bullyapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.bullyapp.databinding.ActivityTestBinding
import com.example.bullyapp.databinding.TestBinding

class Test() : UtilitiesClass() {

    lateinit var binding: TestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToSignIn.setOnClickListener{
            switchActivities(SignIn::class.java)
        }

    }

}