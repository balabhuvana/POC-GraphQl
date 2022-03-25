package com.project.template.repo.draft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.template.CharacterQuery
import com.project.template.model.CharacterDetailUIState
import com.project.template.model.CharacterListUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GraphqlViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow<CharacterListUIState>(CharacterListUIState.Success(emptyList()))

    val uiState: StateFlow<CharacterListUIState> = _uiState

    private val _characterUiState =
        MutableStateFlow<CharacterDetailUIState>(CharacterDetailUIState.Success(null))

    val characterUiState: StateFlow<CharacterDetailUIState> = _characterUiState

    fun fetchUserList(graphqlRepoFlow: GraphqlRepoFlow) {
        viewModelScope.launch {
            graphqlRepoFlow.fetchCharacterListRDSCall()
                .catch { exception ->
                    _uiState.value = CharacterListUIState.Failure(exception)
                }.collect {
                    _uiState.value = CharacterListUIState.Success(it.data?.characters?.results)
                }
        }
    }

    fun fetchCharacter(userId: String, graphqlRepoFlow: GraphqlRepoFlow) {
        viewModelScope.launch {
            graphqlRepoFlow.fetchCharacterDetailRDSCall(userId)
                .catch { exception ->
                    _characterUiState.value = CharacterDetailUIState.Failure(exception)
                }
                .collect {
                    _characterUiState.value = CharacterDetailUIState.Success(it.data?.character)
                }
        }
    }

}