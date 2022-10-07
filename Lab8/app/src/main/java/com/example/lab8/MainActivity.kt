package com.example.lab8

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
    private val dataStoreName ="ds-sttings"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configuracion
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.containerViewprincipal
        ) as NavHostFragment
        navController = navHostFragment.navController


        val appbarConfig = AppBarConfiguration(setOf(R.id.loginFragment, R.id.charactersFragment))
        toolbar = findViewById(R.id.toolbar_inicial)
        toolbar.setupWithNavController(navController, appbarConfig)

        setToolbarListeners()
    }

    private fun setToolbarListeners() {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.characterDetailFragment -> {
                    toolbar.visibility = View.VISIBLE
                    toolbar.menu.clear()
                    toolbar.inflateMenu(R.menu.app_bar_menu)
                    toolbar.menu.findItem(R.id.opcionOrdenarDescendente).isVisible = false
                    toolbar.menu.findItem(R.id.opcionOrdenarAscendente).isVisible = false
                    toolbar.menu.findItem(R.id.cerrarSesion).isVisible = false
                }
                R.id.loginFragment -> {
                    toolbar.visibility = View.GONE
                    toolbar.menu.clear()
                }
                R.id.charactersFragment->{
                    toolbar.visibility = View.VISIBLE
                    toolbar.menu.clear()
                    toolbar.inflateMenu(R.menu.app_bar_menu)
                    toolbar.menu.findItem(R.id.eliminar).isVisible = false
                }
            }
        }
    }



}