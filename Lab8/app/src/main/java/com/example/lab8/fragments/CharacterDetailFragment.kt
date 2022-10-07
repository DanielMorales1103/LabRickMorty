package com.example.lab8.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.lab8.MainActivity
import com.example.lab8.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.example.lab8.data.local_source.Database
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.Character
import com.example.lab8.datasource.model.CharacterTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    lateinit var imagencharacter : ImageView
    lateinit var inputNameCharacter : TextInputLayout
    lateinit var inputSpeciesCharacter : TextInputLayout
    lateinit var inputStatusCharacter : TextInputLayout
    lateinit var inputGenderCharacter : TextInputLayout
    lateinit var inputOrigenCharacter : TextInputLayout
    lateinit var inputEpisodesCharacter : TextInputLayout
    lateinit var botonSave : Button
    lateinit var toolbar: MaterialToolbar
    private lateinit var database: Database
    private lateinit var currentCharacter : CharacterTable
    private val args: CharacterDetailFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagencharacter = view.findViewById(R.id.imagenCharacterDetails)
        inputNameCharacter = view.findViewById(R.id.inputNameCharacter)
        inputSpeciesCharacter = view.findViewById(R.id.inputSpeciesCharacter)
        inputStatusCharacter = view.findViewById(R.id.inputStatusCharacter)
        inputGenderCharacter = view.findViewById(R.id.inputGenderCharacter)
        inputOrigenCharacter = view.findViewById(R.id.inputOriginCharacter)
        inputEpisodesCharacter = view.findViewById(R.id.inputEpisodesCharacter)
        botonSave = view.findViewById(R.id.botonSave)
        toolbar = requireActivity().findViewById(R.id.toolbar_inicial)
        
        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dbname"
        ).build()

        CoroutineScope(Dispatchers.IO).launch {
            currentCharacter = database.characterDao().getCharacter(args.id)
            setDetails()
        }
        setListeners()
    }
    private fun setListeners(){
        botonSave.setOnClickListener {
            updateCharacter()
        }
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.sincronizar->{
                    llamadaApi()
                }
                R.id.eliminar->{
                    deleteDialog()
                }
            }
            true
        }
    }
    private fun deleteDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Advertencia")
            setMessage("¿Desea eliminar este caracter?")
            setPositiveButton("Eliminar"
            ) { _, _ ->
                eliminarCharacter()
            }
            setNegativeButton("Cancelar") { _, _ -> }
            show()
        }
    }
    private fun eliminarCharacter(){
        CoroutineScope(Dispatchers.IO).launch {
            val numdeleted = database.characterDao().deleteCharacter(currentCharacter)
            CoroutineScope(Dispatchers.Main).launch {
                if(numdeleted > 0){
                    Toast.makeText(requireContext(),"Personaje eliminado exitosamente",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),
                        "No se elimino personaje con ID ${currentCharacter.id}",Toast.LENGTH_LONG
                    ).show()
                }
                requireActivity().onBackPressed()
            }

        }
    }
    private fun llamadaApi(){
        RetrofitInstance.api.getCharacter(args.id).enqueue(object :Callback<Character>{
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                response.body()!!.apply {
                    currentCharacter = CharacterTable(
                        name = name,
                        gender = gender,
                        origin = origin.name,
                        species = species,
                        status = status,
                        episode = episode.size,
                        id = id,
                        image = image
                    )
                }
                println(response.body())
                setDetails()
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                println("Error")
            }

        })
    }
    private fun updateCharacter(){
        val updateCharacter = currentCharacter.copy(
            name = inputNameCharacter.editText!!.text.toString(),
            gender = inputGenderCharacter.editText!!.text.toString(),
            origin = inputOrigenCharacter.editText!!.text.toString(),
            species = inputSpeciesCharacter.editText!!.text.toString(),
            status = inputStatusCharacter.editText!!.text.toString(),
            episode = inputEpisodesCharacter.editText!!.text.toString().toInt()
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().updateCharacter(updateCharacter)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    requireContext(),
                    "Personaje actualizado exitosamente",
                    Toast.LENGTH_LONG
                ).show()
                requireActivity().onBackPressed()
            }
        }
    }
    private fun setDetails() {
        CoroutineScope(Dispatchers.Main).launch {
            imagencharacter.load(currentCharacter.image){
                transformations(CircleCropTransformation())
                memoryCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
            }
            inputNameCharacter.editText!!.setText(currentCharacter.name)
            inputOrigenCharacter.editText!!.setText(currentCharacter.origin)
            inputEpisodesCharacter.editText!!.setText(currentCharacter.episode.toString())
            inputGenderCharacter.editText!!.setText(currentCharacter.gender)
            inputSpeciesCharacter.editText!!.setText(currentCharacter.species)
            inputStatusCharacter.editText!!.setText(currentCharacter.status)
        }
    }
}