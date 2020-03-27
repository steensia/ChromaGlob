package com.asimplenerd.chromaglobs.Classes

import android.os.AsyncTask
import android.renderscript.Sampler
import android.util.Log
import com.asimplenerd.chromaglobs.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.util.concurrent.CountDownLatch

var playerGold = -1L
var upToDate = false
var currentCardId = -1
var currentCardCount = 0
var playerOwnsCurrentCard = false
lateinit var mainActivity : MainActivity
var playerName = ""

fun initialize(main : MainActivity){
    mainActivity = main
}

fun addCardToPlayer(player : Player, card : Card){
    val db = FirebaseDatabase.getInstance()
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            var cardCount = 0L
            if(p0.exists()){
                cardCount = p0.value as Long
            }
            Log.d("PlayerCard", "Player has: " + cardCount + " copies of " + card.getCardName())
            cardCount++
            db.getReference("Users").child(player.id).child("OwnedCards").child(card.getId().toString()).setValue(cardCount).addOnSuccessListener {
                Log.d("PlayerCard", "Player has received a copy of " + card.getCardName())
            }.addOnFailureListener{
                Log.w("PlayerCard", "Player was unable to receive a copy of " + card.getCardName())
            }
        }

        override fun onCancelled(p0: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

}

fun readPlayerGold(player : Player){
    val db = FirebaseDatabase.getInstance()
    var goldAmount = -1
    upToDate = false
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            var currentGold = -1L
            if(p0.exists()){
                currentGold = p0.value as Long
            }
            Log.d("PlayerGold", "Player has: " + currentGold + " gold")
            playerGold = currentGold
        }

        override fun onCancelled(p0: DatabaseError) {
            TODO("Not yet implemented")
        }
    }
}

fun readPlayerGold(player : Player, amount : Int){
    val db = FirebaseDatabase.getInstance()
    var goldAmount = -1
    upToDate = false
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            var currentGold = -1L
            if(p0.exists()){
                currentGold = p0.value as Long
            }
            else{
                currentGold = 0
            }
            Log.d("PlayerGold", "Player has: " + currentGold + " gold")
            playerGold = currentGold
            db.getReference("Users").child(player.id).child("gold").setValue(currentGold + amount).addOnCompleteListener {
                Log.d("GoldReward", "Successfully redeemed gold reward")
                playerGold += amount
            }.addOnFailureListener{
                Log.w("GoldReward", "Failed to redeem gold reward")
            }
        }

        override fun onCancelled(p0: DatabaseError) {
            TODO("Not yet implemented")
        }
    }
    db.getReference("Users").child(player.id).child("gold").addListenerForSingleValueEvent(dataListener)
}

fun addGoldToPlayer(player : Player, amount : Int){
    readPlayerGold(player, amount)
}

fun getUsername(uid : String){
    var uname = ""
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()) {
                playerName = p0.value as String
                Log.d("UsernameLookup", "Successfully found username")
            }
        }

        override fun onCancelled(p0: DatabaseError) {
            TODO("Not yet implemented")
        }
    }
    FirebaseDatabase.getInstance().getReference("Users").child(uid).child("username").addListenerForSingleValueEvent(dataListener)
}

fun getPlayerGold(player : Player){
    readPlayerGold(player)
}

