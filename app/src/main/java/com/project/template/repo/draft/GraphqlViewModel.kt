package com.project.template.repo.draft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.template.model.UserCharacterDetailUIState
import com.project.template.model.UserCharacterListUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class GraphqlViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow<UserCharacterListUIState>(UserCharacterListUIState.Success(emptyList()))

    val uiState: StateFlow<UserCharacterListUIState> = _uiState

    private val _characterUiState =
        MutableStateFlow<UserCharacterDetailUIState>(UserCharacterDetailUIState.Success(null))

    val characterUiState: StateFlow<UserCharacterDetailUIState> = _characterUiState

    fun fetchUserCharacterList(graphqlRepoFlow: GraphqlRepoFlow) {
        viewModelScope.launch {
            graphqlRepoFlow.fetchUserCharacterListRDSCall()
                .catch { exception ->
                    _uiState.value = UserCharacterListUIState.Failure(exception)
                }.collect {
                    _uiState.value = UserCharacterListUIState.Success(it.data?.characters?.results)
                }
        }
    }

    fun fetchUserCharacter(userId: String, graphqlRepoFlow: GraphqlRepoFlow) {
        viewModelScope.launch {
            graphqlRepoFlow.fetchUserCharacterDetailRDSCall(userId)
                .catch { exception ->
                    _characterUiState.value = UserCharacterDetailUIState.Failure(exception)
                }
                .collect {
                    _characterUiState.value = UserCharacterDetailUIState.Success(it.data?.character)
                }
        }
    }

}