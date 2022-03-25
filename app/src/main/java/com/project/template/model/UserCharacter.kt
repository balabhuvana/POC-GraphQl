package com.project.template.model

import com.project.template.CharacterQuery
import com.project.template.CharactersListQuery

sealed class UserCharacterListUIState {
    data class Success(var userResultList: List<CharactersListQuery.Result?>?) : UserCharacterListUIState()
    data class Failure(var exception: Throwable) : UserCharacterListUIState()
}

sealed class UserCharacterDetailUIState {
    data class Success(var user: CharacterQuery.Character?) : UserCharacterDetailUIState()
    data class Failure(var exception: Throwable) : UserCharacterDetailUIState()
}