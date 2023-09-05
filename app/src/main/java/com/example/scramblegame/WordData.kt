package com.example.scramblegame

const val MAX_NO_OF_WORD = 10
const val SCORE_INCREASE = 20

val allWord: Set<String> = setOf(
    "animal",
    "auto",
    "anecdote",
    "alphabet",
    "all",
    "awesome",
    "arise",
    "balloon",
    "basket",
    "bench",
    "zoology",
    "zone",
    "zeal"
)


//  Maps words to their lengths. Each word in allWords has a unique length. This is required since
// the words are randomly picked inside GameViewModel and the selection is unpredictable.

private val wordLengthMap: Map<Int, String> = allWord.associateBy({ it.length }, { it })
internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""