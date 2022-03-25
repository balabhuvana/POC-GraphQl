package com.project.template.repo.draft

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.project.template.CharacterQuery
import com.project.template.CharactersListQuery
import kotlinx.coroutines.flow.flow

class GraphqlRdsFlow(var apolloClient: ApolloClient) {

    fun fetchCharacterListRDSCall() =
        flow {
            emit(apolloClient.query(CharactersListQuery()).await())
        }

    fun fetchCharacterDetailRDSCall(userId: String) =
        flow {
            emit(apolloClient.query(CharacterQuery(userId)).await())
        }
}
