package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.AllApiResponse

import org.w3c.dom.Text
import com.example.lab8.datasource.model.Character
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CharakterDetailFragment : Fragment(R.layout.fragment_charakter_detail) {

    lateinit var namecharacter: TextView
    lateinit var speciescharacter : TextView
    lateinit var statuscharacter : TextView
    lateinit var gendercharacter: TextView
    lateinit var imagecharacter : ImageView
    lateinit var origencharacter : TextView
    lateinit var episodioscharacter : TextView
    private lateinit var character: Character
    private val args: CharakterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagecharacter = view.findViewById(R.id.imagenCharacterDetails)
        namecharacter = view.findViewById(R.id.nombreCharacterDetails)
        speciescharacter = view.findViewById(R.id.speciestext)
        statuscharacter = view.findViewById(R.id.statustext)
        gendercharacter = view.findViewById(R.id.gendertext)
        origencharacter = view.findViewById(R.id.textorigen)
        episodioscharacter = view.findViewById(R.id.textepisodios)

        RetrofitInstance.api.getCharacter(args.id).enqueue(object : Callback<Character>  {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful){
                    println("Character Details obtained successfully")
                    character = response.body()!!
                    setUpCharacter()
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                println("Connection failed")
                Toast.makeText(activity, "No fue posible obtener los detalles del personaje. Revisa tu conexión a internet.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setUpCharacter(){

        imagecharacter.load(character.image){
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
        namecharacter.text = character.name
        speciescharacter.text = character.species
        statuscharacter.text = character.status
        gendercharacter.text = character.gender
        origencharacter.text = character.origin.name
        episodioscharacter.text = character.episode.size.toString()
    }
}