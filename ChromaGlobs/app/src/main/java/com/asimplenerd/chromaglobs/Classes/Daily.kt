package com.asimplenerd.chromaglobs.Classes

import android.util.Log

class Daily() {

    private var complete = false
    private lateinit var description : String
    private var claimed = false
    private lateinit var missionType : MissionType
    private var missionId = -1
    // TODO: add a date to missions, so we know when to give new ones.
    // OR just load a new mission once they've claimed the reward for the completed mission?

    constructor(complete: Boolean, description: String, claimed: Boolean) : this(){
        this.complete = complete
        this.description = description
        this.claimed = claimed
    }

    constructor(complete: Boolean, description: String, claimed: Boolean, missionType: MissionType) : this(complete, description, claimed){
        this.missionType = missionType
    }

    constructor(complete: Boolean, description: String, claimed: Boolean, missionType: MissionType, missionId : Int) : this(complete, description, claimed, missionType){
        this.missionId = missionId
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

    fun getMissionRewardType() : String {
        when(missionType){
            MissionType.Card -> return "Reward: card"
            MissionType.Gold -> return "Reward: gold"
        }
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