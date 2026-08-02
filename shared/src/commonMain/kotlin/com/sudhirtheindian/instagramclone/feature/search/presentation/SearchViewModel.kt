package com.sudhirtheindian.instagramclone.feature.search.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SearchUiState(
    val searchQuery: String = "",
    val categories: List<String> = listOf("Shop", "Style", "Travel", "Architecture", "Decor", "Art", "Food", "Style", "TV & Movies", "Audio"),
    val trendingPosts: List<String> = List(30) { index ->
        val height = if (index % 3 == 0) 600 else if (index % 2 == 0) 400 else 500
        "https://picsum.photos/400/$height?random=$index"
    },
    val isSearching: Boolean = false
)

class SearchViewModel : ScreenModel {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, isSearching = query.isNotEmpty())
    }
}
