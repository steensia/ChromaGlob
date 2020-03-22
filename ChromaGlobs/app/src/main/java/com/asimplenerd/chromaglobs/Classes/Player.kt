package com.asimplenerd.chromaglobs.Classes

class Player{
    lateinit var username : String
    lateinit var id : String
    var OwnedCards = arrayListOf<Card>()

    constructor(Name : String, id : String, vararg OwnedCards : ArrayList<Card>){
        this.username = Name
        this.id = id
        OwnedCards.forEach { this.OwnedCards.addAll(it) }
    }
}