package com.example.lab8

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.size.Dimension
import com.example.lab8.adapters.CharacterAdapter
import com.example.lab8.database.RickAndMortyDB
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.AllApiResponse
import com.example.lab8.datasource.model.Character
import com.example.lab8.fragments.LoginFragment.Companion.dataStore
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerViewCharacterClickHandler {
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList : MutableList<Character>
    private lateinit var toolbar : MaterialToolbar
    private lateinit var adapter: CharacterAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerCharactersList)
        toolbar =requireActivity().findViewById(R.id.toolbar_inicial)
        apiconection()
        setuplisteners()
    }
    private fun setuplisteners() {

        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.opcionOrdenarAscendente -> {
                    characterList.sortBy { character -> character.name }
                    adapter.notifyDataSetChanged()
                }
                R.id.opcionOrdenarDescendente ->{
                    characterList.sortByDescending { character -> character.name }
                    adapter.notifyDataSetChanged()
                }
                R.id.cerrarSesion->{
                    CoroutineScope(Dispatchers.IO).launch {
                        saveKeyValue()
                    }
                    val action = CharactersFragmentDirections.actionCharactersFragmentToLoginFragment()
                    requireView().findNavController().navigate(action)

                }
            }

            true

        }
    }
    private suspend fun saveKeyValue(){
        val dataSotoreKey = stringPreferencesKey("mor21785@uvg.edu.gt")
        requireContext().dataStore.edit { settings ->
            settings[dataSotoreKey] = ""
        }
    }
    private fun apiconection() {
        RetrofitInstance.api.getCharacters().enqueue(object : Callback<AllApiResponse> {
            override fun onResponse(
                call: Call<AllApiResponse>,
                response: Response<AllApiResponse>
            ) {
                if (response.isSuccessful){
                    println("Successfully obtained characters list")
                    characterList = response.body()!!.results
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<AllApiResponse>, t: Throwable) {
                println("Connection failed")
                Toast.makeText(activity, "No fue posible obtener la lista de personajes. Revisa tu conexión a internet.", Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun setupRecycler() {
        adapter = CharacterAdapter(characterList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCharacterClick(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharakterDetailFragment(
            character.id
        )
        requireView().findNavController().navigate(action)
    }
}


