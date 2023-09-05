package com.example.scramblegame.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scramblegame.MAX_NO_OF_WORD
import com.example.scramblegame.SCORE_INCREASE
import com.example.scramblegame.allWord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

// Game UI state

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of Word used in the game
    private var useWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    init {
        resetGame()
    }

// Re-initialized game data to restart the game

    fun resetGame() {
        useWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

// Update the userGuess

    fun updateUserGuess(guessWords: String) {
        userGuess = guessWords
    }

//    check the user guess is correct
//    that's increase a score accordingly

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round
            val updateScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState -> currentState.copy(isGuessWordWrong = true) }
        }
        // reset your guess
        updateUserGuess("")
    }

    //    skip next word
    fun skipWord() {
        updateGameState(_uiState.value.score)
        // reset your guess
        updateUserGuess("")
    }

    // Picks a new currentWord and currentScrambledWord and updates UiState according to
    // current game state.

    private fun updateGameState(updatedScore: Int) {
        if (useWords.size == MAX_NO_OF_WORD) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWord.random()
        return if (useWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            useWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }

}
