package com.example.lab8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.AllApiResponse
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configuracion
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.containerViewprincipal
        ) as NavHostFragment
        navController = navHostFragment.navController

        val appbarConfig = AppBarConfiguration(navController.graph)
        toolbar = findViewById(R.id.toolbar_inicial)
        toolbar.setupWithNavController(navController, appbarConfig)



    }

}