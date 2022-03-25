package com.project.template.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.project.template.CharactersListQuery
import com.project.template.databinding.FragmentCharacterListRowBinding
import com.project.template.ui.main.draft.UserCharacterListFragmentDirections

class UserCharacterListAdapter(private var charactersList: List<CharactersListQuery.Result?>?) :
    RecyclerView.Adapter<UserCharacterListAdapter.UserListViewHolder>() {

    inner class UserListViewHolder(val binding: FragmentCharacterListRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val binding = FragmentCharacterListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        charactersList?.get(position).let {
            holder.binding.tvUserId.text = it?.id
            holder.binding.tvUserName.text = it?.name
            holder.binding.tvSpecies.text = it?.species
        }
        holder.binding.root.setOnClickListener {
            val userIdArgs = charactersList?.get(position)?.id
            val action = UserCharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(userIdArgs!!)
            launchScreen(it, action)
        }
    }

    override fun getItemCount(): Int = charactersList?.size!!

    private fun launchScreen(view: View, action: NavDirections) {
        view.findNavController().navigate(action)
    }
}