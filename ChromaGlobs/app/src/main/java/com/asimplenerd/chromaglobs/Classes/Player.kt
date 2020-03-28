package com.asimplenerd.chromaglobs.Classes

import android.util.Log
import com.asimplenerd.chromaglobs.Missions

class Player(){
    lateinit var username : String
    lateinit var id : String
    var OwnedCards = ArrayList<Card>()
    var missions = arrayOf(-1, -1, -1)
    var nextMissionID = 0

    constructor(Name : String, id : String, vararg OwnedCards : ArrayList<Card>) : this(){
        this.username = Name
        this.id = id
        OwnedCards.forEach { this.OwnedCards.addAll(it) }
    }

    fun addCard(card : Card){
        OwnedCards.add(card)
    }

    fun nextMissionID() : Int{
        val id = nextMissionID
        nextMissionID++
        nextMissionID %= 3
        Log.d("nextmissionid", nextMissionID.toString())
        return missions[id]
    }

    fun setMissions(vararg m : Int) {
        missions[0] = m[0]
        missions[1] = m[1]
        missions[2] = m[2]
    }

}