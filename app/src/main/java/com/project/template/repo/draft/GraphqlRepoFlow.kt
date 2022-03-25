package com.project.template.repo.draft

class GraphqlRepoFlow(var graphqlRdsFlow: GraphqlRdsFlow) {

    fun fetchCharacterListRDSCall() = graphqlRdsFlow.fetchCharacterListRDSCall()

    fun fetchCharacterDetailRDSCall(userId: String) = graphqlRdsFlow.fetchCharacterDetailRDSCall(userId)
}
