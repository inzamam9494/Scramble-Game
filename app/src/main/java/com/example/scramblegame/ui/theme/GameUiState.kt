package com.example.scramblegame.ui.theme

// data class represent the UI state

data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)