package com.rahul.imagegame.models

enum class BoardSize(val numCards:Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getWidth():Int{
        return when(this){
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getHeight():Int{
        return numCards/2
    }

    fun getNumPairs():Int{
        return numCards/2
    }
}