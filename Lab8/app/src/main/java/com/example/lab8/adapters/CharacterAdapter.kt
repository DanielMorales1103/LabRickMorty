package com.example.lab8.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.example.lab8.datasource.model.Character
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.lab8.CharactersFragmentDirections
import com.example.lab8.datasource.api.RetrofitInstance
import com.example.lab8.datasource.model.AllApiResponse
import com.example.lab8.datasource.model.CharacterTable
import javax.security.auth.callback.Callback

class CharacterAdapter(private val dataSet: MutableList<CharacterTable>,
                       private val listener: RecyclerViewCharacterClickHandler
                       ) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>(){

    class ViewHolder(view : View, private val listener: RecyclerViewCharacterClickHandler) : RecyclerView.ViewHolder(view){

        private val imageCharacter : ImageView = view.findViewById(R.id.imageCharacter)
        private val nombreCharacter : TextView = view.findViewById(R.id.nombreCharacter)
        private val infoCharacter : TextView = view.findViewById(R.id.infoCharacter)
        private val layoutCharacter: ConstraintLayout = view.findViewById(R.id.layout_itemCharacter)

        fun setData(character: CharacterTable ){
            nombreCharacter.text = character.name
            infoCharacter.text = character.species+" - "+character.status

            imageCharacter.load(character.image){
                transformations(CircleCropTransformation())
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
            }

            layoutCharacter.setOnClickListener{
                listener.onCharacterClick(character)
            }

        }
    }

    interface RecyclerViewCharacterClickHandler{
        fun onCharacterClick(character: CharacterTable)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_characters,parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}
