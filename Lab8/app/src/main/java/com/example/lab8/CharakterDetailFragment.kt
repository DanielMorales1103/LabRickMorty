package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import org.w3c.dom.Text


class CharakterDetailFragment : Fragment(R.layout.fragment_charakter_detail) {

    lateinit var namecharacter: TextView
    lateinit var speciescharacter : TextView
    lateinit var statuscharacter : TextView
    lateinit var gendercharacter: TextView
    lateinit var imagecharacter : ImageView
    private val args: CharakterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagecharacter = view.findViewById(R.id.imagenCharacterDetails)
        namecharacter = view.findViewById(R.id.nombreCharacterDetails)
        speciescharacter = view.findViewById(R.id.speciestext)
        statuscharacter = view.findViewById(R.id.statustext)
        gendercharacter = view.findViewById(R.id.gendertext)

        imagecharacter.load(args.character.image){
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
        }
        namecharacter.text = args.character.name
        speciescharacter.text = args.character.species
        statuscharacter.text = args.character.status
        gendercharacter.text = args.character.gender
    }
}