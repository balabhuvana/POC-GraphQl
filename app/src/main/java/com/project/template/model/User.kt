package com.project.template.model

import com.project.template.CharacterQuery
import com.project.template.CharactersListQuery

sealed class CharacterListUIState {
    data class Success(var userResultList: List<CharactersListQuery.Result?>?) : CharacterListUIState()
    data class Failure(var exception: Throwable) : CharacterListUIState()
}

sealed class CharacterDetailUIState {
    data class Success(var user: CharacterQuery.Character?) : CharacterDetailUIState()
    data class Failure(var exception: Throwable) : CharacterDetailUIState()
}