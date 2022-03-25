package com.project.template.repo.draft

class GraphqlRepoFlow(var graphqlRdsFlow: GraphqlRdsFlow) {

    fun fetchUserCharacterListRDSCall() = graphqlRdsFlow.fetchUserCharacterListRDSCall()

    fun fetchUserCharacterDetailRDSCall(userId: String) = graphqlRdsFlow.fetchUserCharacterDetailRDSCall(userId)
}
