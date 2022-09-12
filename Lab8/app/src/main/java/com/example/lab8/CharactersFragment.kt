package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.adapters.CharacterAdapter
import com.example.lab8.database.Character
import com.example.lab8.database.RickAndMortyDB


class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerViewCharacterClickHandler {
    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList : MutableList<Character>
    private lateinit var ordenarasc : Button
    private lateinit var ordenardes : Button
    private lateinit var adapter: CharacterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerCharactersList)
        ordenarasc = view.findViewById(R.id.boton_ordenarAscendente)
        ordenardes = view.findViewById(R.id.boton_ordenarDescendente)
        setupRecycler()
        setuplisteners()
    }

    private fun setuplisteners() {
        ordenarasc.setOnClickListener{
            characterList.sortBy { character -> character.name  }
            adapter.notifyDataSetChanged()
        }
        ordenardes.setOnClickListener {
            characterList.sortByDescending { character -> character.name }
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecycler() {
        characterList = RickAndMortyDB.getCharacters()
        adapter = CharacterAdapter(characterList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCharacterClick(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharakterDetailFragment(
            character
        )
        requireView().findNavController().navigate(action)
    }
}