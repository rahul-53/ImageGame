package com.rahul.imagegame

import com.rahul.imagegame.models.BoardSize
import com.rahul.imagegame.models.MemoryCard
import com.rahul.imagegame.utils.Constants

class ImageGame(private val boardSize: BoardSize) {


    val cards: List<MemoryCard>
    var numberOfPairsFound:Int = 0
    var numCardFlips:Int =0

    private var indexOfSingleSelectedCard:Int? =null
    init {
        val chooseImages = Constants.DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomImages = (chooseImages+chooseImages).shuffled()
        cards = randomImages.map { MemoryCard(it) }

    }

    fun flipCard(position: Int):Boolean {
         numCardFlips++
        val card:MemoryCard = cards[position]
        var matchFound = false
        if(indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else{
            matchFound = checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp =! card.isFaceUp
        return matchFound
    }

    private  fun restoreCards(){
        for (card:MemoryCard in cards){
            if (!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(positionOne:Int, positionTwo:Int):Boolean{
        if (cards[positionOne].identifier != cards[positionTwo].identifier)
            return false
        cards[positionOne].isMatched = true
        cards[positionTwo].isMatched = true
        numberOfPairsFound++
        return true
    }

    fun haveWonTheGame(): Boolean {
        return numberOfPairsFound == boardSize.getNumPairs()

    }

    fun isCardFacedUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumberOfMoves(): Int {
        return numCardFlips / 2
    }

}