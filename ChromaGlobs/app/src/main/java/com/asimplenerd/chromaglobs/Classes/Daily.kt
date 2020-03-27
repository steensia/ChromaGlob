package com.asimplenerd.chromaglobs.Classes

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

    fun claimReward(){
        claimed = true
        TODO("Use mission type to give player rewards")
    }

    fun setMissionType(type : MissionType){
        missionType = type
    }
}