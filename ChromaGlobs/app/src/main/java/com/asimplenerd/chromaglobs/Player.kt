package com.asimplenerd.chromaglobs

import java.io.Serializable

class Player(){
    lateinit var username : String
    lateinit var id : String
    var OwnedCards = ArrayList<Card>()

    constructor(Name : String, id : String, vararg OwnedCards : ArrayList<Card>) : this(){
        this.username = Name
        this.id = id
        OwnedCards.forEach { this.OwnedCards.addAll(it) }
    }

    fun addCard(card : Card){
        OwnedCards.add(card)
    }
}