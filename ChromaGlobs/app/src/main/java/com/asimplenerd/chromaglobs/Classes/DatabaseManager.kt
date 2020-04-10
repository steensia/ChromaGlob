package com.asimplenerd.chromaglobs.Classes

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.asimplenerd.chromaglobs.MainActivity
import com.asimplenerd.chromaglobs.R
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

var playerGold = -1L
var upToDate = false
var currentCardId = -1
var currentCardCount = 0
var playerOwnsCurrentCard = false
lateinit var mainActivity : MainActivity
var playerName = ""
var missionDesc = ""

lateinit var context : Context

fun initialize(main : MainActivity){
    mainActivity = main
    context = main.applicationContext
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

fun readMissionDesc(missionId : Int, daily: Daily) {
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()){
                var h = p0.getValue() as HashMap<*, *>
                missionDesc = h["Description"] as String
                daily.setDescription(missionDesc)
                Log.d("missionDesc", missionDesc)
            }
            else{
                Log.w("MissionDesc", "Mission not found")
            }
        }

        override fun onCancelled(p0: DatabaseError) {
            missionDesc = ""
            Log.w("MissionDesc", "Unable to retrieve mission id.")
        }
    }
    FirebaseDatabase.getInstance().getReference("MissionsList").child(missionId.toString()).addListenerForSingleValueEvent(dataListener)
}

fun getMissionDesc(missionId : Int, daily : Daily){
    readMissionDesc(missionId, daily)
}

fun updatePlayerLogin(player: Player) {

    var date = Calendar.getInstance().time
    var db = FirebaseDatabase.getInstance()
    db.getReference("Users").child(player.id).child("LastLogin").setValue(date)
}

fun updatePlayersMissions(player: Player) {
    var db = FirebaseDatabase.getInstance()
    var dataListener = object : ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()){
                Log.d("MissionCreation", "Previous missions detected.")
                db.getReference("Users").child(player.id).child("LastLogin").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()) {
                            var dateFormatter = SimpleDateFormat("yyyyMMdd", Locale.US)
                            var currentDate = dateFormatter.format(System.currentTimeMillis())
                            if (!p0.hasChild("time")) {
                                makeNewMissions(player);
                            } else {
                                var oldDate = dateFormatter.format(p0.child("time").value)
                                Log.d("UserLogin", "User last logged in on: $oldDate")
                                if (oldDate < currentDate) {
                                    Log.d("MissionCreation", "New day since last login. Player is getting new misisons.")
                                    makeNewMissions(player);
                                    updatePlayerLogin(player)
                                } else {
                                    Log.d("MissionCreation", "Date: $oldDate is the same as: $currentDate")
                                    db.getReference("Users").child(player.id).child("Missions").addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            if (p0.exists()) {
                                                var missionIds = p0.getValue() as ArrayList<Long>
                                                player.setPlayerMissions(missionIds[0].toInt(), missionIds[1].toInt(), missionIds[2].toInt())
                                            }
                                        }
                                    })
                                }
                            }
                        }
                        else {
                            Log.d("MissionCreation", "Could not find last login field.")
                            updatePlayerLogin(player)
                            makeNewMissions(player)
                        }

                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
            }
            else{
                makeNewMissions(player)
            }
        }

        override fun onCancelled(p0: DatabaseError) {
        }
    }
    db.getReference("Users").child(player.id).child("Missions").addListenerForSingleValueEvent(dataListener)
}

fun setMissionGoal(player: Player, daily : Daily){
    val db = FirebaseDatabase.getInstance()
    val missionId = daily.getMissionId()
    Log.d("MissionCompletion", "beginning to check for user mission goal requirements.")
    val dataListener = object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            //TODO what do we want if the data for a goal cannot be read?
        }

        override fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()){
                val currentValue = p0.value as Long
                Log.d("MissionCompletion", "value of goal currently is $currentValue")
                daily.setMissionGoal(currentValue.toInt() + 1)
            }
            else{
                Log.d("MissionCompletion", "player has never done the requisite action. Setting mission goal to one...")
                daily.setMissionGoal(1); //The player has never done this action. Assume a value of zero.
            }
        }

    }
    db.getReference("Users").child(player.id).child("Stats").child(getMissionFieldName(missionId)).addListenerForSingleValueEvent(dataListener)
}

fun checkMissionCompletion(player : Player, daily : Daily, status : TextView, button : Button){
    val db = FirebaseDatabase.getInstance()
    val dataListener = object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(p0: DataSnapshot) {
            if(p0.exists()){
                val currentValue = p0.value as Long
                daily.setComplete(currentValue >= daily.getMissionGoal())
            }
            button.isEnabled = daily.getComplete() && !daily.getClaimed()
            status.text = (if (daily.getComplete()) "Complete" else "Incomplete")
            status.setTextColor(if (daily.getComplete()) Color.GREEN else Color.RED)
        }

    }
    db.getReference("Users").child(player.id).child("Stats").child(getMissionFieldName(daily.getMissionId())).addListenerForSingleValueEvent(dataListener)
}

private fun makeNewMissions(player: Player) {
    var stuff = HashMap<String, Any>()

    // no missions exist, create some
    val rand = Random()
    var id1 = rand.nextInt(7)
    stuff.put("0", id1)
    var id2 = rand.nextInt(7)
    stuff.put("1", id2)
    var id3 = rand.nextInt(7)
    stuff.put("2", id3)

    revertMission(player.nextMissionID)
    revertMission(player.nextMissionID)
    revertMission(player.nextMissionID)

    FirebaseDatabase.getInstance().getReference("Users").child(player.id).child("Missions").updateChildren(stuff)
    player.setPlayerMissions(id1, id2, id3)
}

private fun revertMission(id : Int) {
    //FIXME
    var myFile = File(context.filesDir, "missions/${id.toString()}.xml")
    Log.d("MissionRevert", "Reverting previous missions ($id) to default value")
    if(myFile.exists()) {
        var reader = XmlReader()
        var missionFile = reader.parse(FileInputStream(myFile))
        var desc = missionFile.get("Description")

        var writer = XmlWriter(FileWriter(myFile))
        writer.element("Mission").element("Description", desc).element("RewardType", "none").element("Complete", "false").element("Claimed", "false").element("Goal").pop()
        writer.close()
    }
    else {
        Log.d("mission rewards", "no file!!!!!")
    }
}

private fun repopulateMissions(player: Player, context : Context) {
    if(context != null){
        player.nextMissionID
    }
}

private fun getMissionFieldName(id : Int) : String{
    when(id){
        0 -> return "TotalTrades"
        1 -> return "MatchesPlayed"
        2 -> return "CardsBought"
        3 -> return "FriendsAdded"
        4 -> return "DecksCreated"
        5 -> return "Level"
        6 -> return "MatchesWon"
    }
    return ""
}

