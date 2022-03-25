package com.project.template.ui.main.draft

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.template.CharactersListQuery
import com.project.template.databinding.FragmentCharacterListBinding
import com.project.template.model.UserCharacterListUIState
import com.project.template.network.MyOkhttpClient
import com.project.template.repo.draft.GraphqlRdsFlow
import com.project.template.repo.draft.GraphqlRepoFlow
import com.project.template.repo.draft.GraphqlViewModel
import com.project.template.ui.main.adapter.UserCharacterListAdapter
import kotlinx.coroutines.launch

class UserCharacterListFragment : Fragment() {

    private val graphqlViewModel: GraphqlViewModel by activityViewModels()
    private lateinit var binding: FragmentCharacterListBinding
    private lateinit var userListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apolloClient = MyOkhttpClient().getApolloClient()
        val graphqlRdsFlow = GraphqlRdsFlow(apolloClient)
        val graphqlRepoFlow = GraphqlRepoFlow(graphqlRdsFlow)

        binding.btnClick.setOnClickListener {
            launchGraphQl(it, graphqlRepoFlow)
        }

    }

    private fun launchGraphQl(view: View, graphqlRepoFlow: GraphqlRepoFlow) {
        graphqlViewModel.fetchUserCharacterList(graphqlRepoFlow)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                graphqlViewModel.uiState.collect {
                    when (it) {
                        is UserCharacterListUIState.Success -> {
                            Log.i("-----> ", "${it.userResultList}")
                            updateUserListAdapter(it.userResultList)
                        }
                        is UserCharacterListUIState.Failure -> {
                            showSnackBar(view, it.exception.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar(view: View, displayText: String) {
        Snackbar.make(view, displayText, Snackbar.LENGTH_LONG).show()
    }

    private fun updateUserListAdapter(userList: List<CharactersListQuery.Result?>?) {
        val userCharacterListAdapter = UserCharacterListAdapter(userList)
        userListRecyclerView = binding.characterRecyclerView
        userListRecyclerView.adapter = userCharacterListAdapter
        userListRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

}