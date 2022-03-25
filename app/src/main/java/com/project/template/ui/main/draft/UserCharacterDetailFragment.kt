package com.project.template.ui.main.draft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.project.template.CharacterQuery
import com.project.template.R
import com.project.template.databinding.FragmentCharacterDetailBinding
import com.project.template.model.UserCharacterDetailUIState
import com.project.template.network.MyOkhttpClient
import com.project.template.repo.draft.GraphqlRdsFlow
import com.project.template.repo.draft.GraphqlRepoFlow
import com.project.template.repo.draft.GraphqlViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class UserCharacterDetailFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailBinding
    private val graphQlViewModel: GraphqlViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apolloClient = MyOkhttpClient().getApolloClient()
        val graphqlRdsFlow = GraphqlRdsFlow(apolloClient)
        val graphqlRepoFlow = GraphqlRepoFlow(graphqlRdsFlow)

        val safeArgs: UserCharacterDetailFragmentArgs by navArgs()
        val userId = safeArgs.userIdArgs

        fetchUserDetail(view, userId, graphqlRepoFlow)
    }

    private fun fetchUserDetail(view: View, userId: String, graphqlRepoFlow: GraphqlRepoFlow) {

        graphQlViewModel.fetchUserCharacter(userId, graphqlRepoFlow)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                graphQlViewModel.characterUiState.collect() {
                    when (it) {
                        is UserCharacterDetailUIState.Success -> {
                            setUserData(it.user)
                        }
                        is UserCharacterDetailUIState.Failure -> {
                            showSnackBar(view, it.exception.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setUserData(character: CharacterQuery.Character?) {
        binding.tvFirstName.text = character?.id
        binding.tvLastName.text = character?.name
        binding.tvEmail.text = character?.image

        Picasso.get()
            .load(character?.image).fit().centerCrop()
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(binding.ivUserImage)
    }

    private fun showSnackBar(view: View, displayText: String) {
        Snackbar.make(view, displayText, Snackbar.LENGTH_LONG).show()
    }
}