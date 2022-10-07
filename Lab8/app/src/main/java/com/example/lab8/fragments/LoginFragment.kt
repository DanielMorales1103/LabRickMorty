package com.example.lab8.fragments

import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import com.example.lab8.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var iniciarSesion : Button
    private lateinit var usuario :TextInputLayout
    private lateinit var contra : TextInputLayout

    private lateinit var value : String
    val correo = "mor21785@uvg.edu.gt"
    companion object{
        public val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ds-sttings")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarSesion = view.findViewById(R.id.botonIniciarSesion)
        usuario = view.findViewById(R.id.ingreso_correo_profile)
        contra = view.findViewById(R.id.ingreso_contra_profile)


        CoroutineScope(Dispatchers.Main).launch {
            if (getValueFromkey(correo).compareTo(correo)==0){
                requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCharactersFragment())
            }
        }

        setlisteners()
    }

    private fun setlisteners() {
        iniciarSesion.setOnClickListener {
            if ((correo.compareTo(usuario.editText!!.text.toString())) == 0 && correo.compareTo(contra.editText!!.text.toString())==0){
                CoroutineScope(Dispatchers.IO).launch {
                    saveKeyValue(usuario.editText!!.text.toString(),
                        contra.editText!!.text.toString())
                }
                requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCharactersFragment())
            }else{
                Toast.makeText(activity, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }

    }

    private suspend fun saveKeyValue(key: String, value: String){
        val dataSotoreKey = stringPreferencesKey(key)
        requireContext().dataStore.edit { settings ->
            settings[dataSotoreKey] = value
        }
    }
    private  suspend fun  getValueFromkey(key : String) : String{
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = requireContext().dataStore.data.first()
        return preferences[dataStoreKey] ?: "null"
    }
}