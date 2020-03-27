package com.asimplenerd.chromaglobs.Classes

import android.util.Log

class Daily() {

    private var complete = false
    private lateinit var description : String
    private var claimed = false
    private lateinit var missionType : MissionType

    constructor(complete: Boolean, description: String, claimed: Boolean) : this(){
        this.complete = complete
        this.description = description
        this.claimed = claimed
    }

    constructor(complete: Boolean, description: String, claimed: Boolean, missionType: MissionType) : this(complete, description, claimed){
        this.missionType = missionType
    }

    fun getComplete() : Boolean {
        return complete
    }

    fun setComplete(c : Boolean) {
        complete = c
    }

    fun getDescription() : String {
        return description
    }

    fun setDescription(d : String) {
        description = d
    }

    fun getClaimed() : Boolean{
        return claimed
    }

    fun claimReward(player : Player){
        when(missionType){
            MissionType.Card -> addCardToPlayer(player, selectCardReward())
            MissionType.Gold -> addGoldToPlayer(player, calculateGoldReward())
        }
        claimed = true
    }

    fun setMissionType(type : MissionType){
        missionType = type
    }

    fun selectCardReward() : Card{
        Log.d("CardReward", "Claiming card reward for user")
        return Card("Test card 2", GlobType.Air, 10, 10, 9, Rarity.Common, 1)
    }

    private fun calculateGoldReward() : Int{
        Log.d("GoldReward", "Claiming gold reward for user")
        //TODO create a formula to update gold count
        return 10
    }
}