package com.asimplenerd.chromaglobs.Classes

/**
 * This class is used to host the current available card ids. Cards need to be fetched from the db?
 */
class CardList {
    companion object {
        val availableCards = ArrayList<Int>()
    }

    fun CardList(vararg ids : Int){
        availableCards.addAll(ids.asList())
    }
}