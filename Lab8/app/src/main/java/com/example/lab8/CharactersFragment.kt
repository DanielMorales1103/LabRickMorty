package com.example.lab8

import android.content.Context
import android.opengl.Visibility
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
import androidx.room.Room
import coil.size.Dimension
import com.example.lab8.adapters.CharacterAdapter
import com.example.lab8.data.local_source.Database
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.AllApiResponse
import com.example.lab8.datasource.model.Character
import com.example.lab8.datasource.model.CharacterTable
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
    private val characterListtable : MutableList<CharacterTable> = mutableListOf()
    private lateinit var toolbar : MaterialToolbar
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var database: Database


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerCharactersList)
        toolbar =requireActivity().findViewById(R.id.toolbar_inicial)

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dbname"
        ).build()
        getCharacters()
        setuplisteners()
    }
    private fun setuplisteners() {

        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.opcionOrdenarAscendente -> {
                    characterListtable.sortBy { character -> character.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    //characterAdapter.notifyDataSetChanged()
                }
                R.id.opcionOrdenarDescendente ->{
                    characterListtable.sortByDescending { character -> character.name }
                    recyclerView.adapter!!.notifyDataSetChanged()
                    //characterAdapter.notifyDataSetChanged()
                }
                R.id.sincronizar->{
                    apiconection()
                }
                R.id.cerrarSesion->{
                    CoroutineScope(Dispatchers.IO).launch {
                        saveKeyValue()
                        database.characterDao().deleteAll()
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
    private fun apiconection(){
        characterListtable.clear()
        RetrofitInstance.api.getCharacters().enqueue(object : Callback<AllApiResponse> {
            override fun onResponse(
                call: Call<AllApiResponse>,
                response: Response<AllApiResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    characterList = response.body()!!.results
                    agregarApi()
                    println(response.body())
                    setUpRecyclerView()
                }
            }

            override fun onFailure(call: Call<AllApiResponse>, t: Throwable) {
                println("Connection failed")
                Toast.makeText(activity, "No fue posible obtener la lista de personajes. Revisa tu conexión a internet.", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun agregarApi(){
        for (item in characterList){
            characterListtable.add(
                CharacterTable(
                    id = item.id,
                    image = item.image,
                    name = item.name,
                    status = item.status,
                    origin = item.origin.name,
                    episode = item.episode.size,
                    gender = item.gender,
                    species = item.species
                )
            )
        }
        for (item2 in characterListtable){
            CoroutineScope(Dispatchers.IO).launch {
                database.characterDao().insert(item2)
            }
        }
    }

    private fun getCharacters(){
        CoroutineScope(Dispatchers.IO).launch {
            val characters = database.characterDao().getAllCharacters()
            if(characters.isEmpty()){
                apiconection()
            }else{
                characterListtable.clear()
                characterListtable.addAll(characters)
                CoroutineScope(Dispatchers.Main).launch {
                    setUpRecyclerView()
                }
            }
        }
    }

    private fun setUpRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(characterListtable,this)
    }

    override fun onCharacterClick(character: CharacterTable) {
        requireView().findNavController().navigate(
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
                character.id
            )
        )
    }
}


