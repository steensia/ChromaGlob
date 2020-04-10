package com.asimplenerd.chromaglobs.Classes

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.asimplenerd.chromaglobs.Dailies
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

class Daily() {

    private var complete = false
    private lateinit var description : String
    private var claimed = false
    private lateinit var missionType : MissionType
    private var missionId = -1
    private lateinit var descriptionField : TextView
    private lateinit var dailyFrag : Dailies
    private  var missionGoal : Int = 0
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

    constructor(complete: Boolean, description: String, claimed: Boolean, missionType: MissionType, missionId : Int, d : TextView) : this(complete, description, claimed, missionType, missionId){
        this.descriptionField = d
        var f = File(d.context.applicationContext.filesDir, "missions/$missionId.xml")
        if(f.exists())
        {
            val fileIn = XmlReader()
            val theWholeFile = fileIn.parse(FileInputStream(f))
            val desc = theWholeFile.get("Description")
            Log.d("mission desc", desc)
            this.description = desc
            val rewardType = theWholeFile.get("RewardType", "none")
            val completed = theWholeFile.get("Complete", "false")
            val rewardClaimed = theWholeFile.get("Claimed", "no")
            this.missionType = missionType
            this.complete = completed == "true"
            this.claimed = rewardClaimed == "true"
            val goal = theWholeFile.get("Goal", "no goal")
            if(rewardType != "none") {
                Log.d("reward type", "this mission has no reward!")
                this.missionType = MissionType.valueOf(rewardType)
            }
            if(goal != "no goal"){
                this.missionGoal = goal.toInt()
            }
        }
        else{
            Log.d("mission desc", "Failed to open file: ${d.context.applicationContext.filesDir}/missions/$missionId.xml")
        }
    }

    fun setDailyFragment(f : Dailies) {
        dailyFrag = f
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

    fun setClaimed(bool : Boolean){
        claimed = bool
    }

    fun setDescription(d : String) {
        description = d
        if(d != null)
            descriptionField.setText(d)
        if(dailyFrag != null)
            dailyFrag.updateDescription();

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
        updateXmlTag("Claimed", "yes")
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

    fun getMissionCompletionCondition() : Int{
        return missionGoal
    }

    fun setTextView(v : TextView) {
        descriptionField = v
    }

    private fun updateXmlTag(tag : String, value : String){
        when(tag.toLowerCase()){
            "description" -> description = value
            "rewardtype" -> missionType = MissionType.valueOf(value)
            "complete" -> complete = value.toLowerCase() == "true"
            "claimed" -> claimed = value.toLowerCase() == "true"
            "goal" -> missionGoal = value.toInt()
        }
        updateXmlFile();
    }

    fun updateXmlFile(){
        val file = File(descriptionField.context?.filesDir, "/missions/$missionId.xml")
        val writer = XmlWriter(FileWriter(file))
        writer.element("Mission").element("Description", description).element("RewardType", missionType).element("Complete", complete).element("Claimed", claimed).element("Goal", missionGoal).pop()
        writer.close()
    }

    fun updateXmlFile(context : Context){
        val file = File(context.filesDir, "/missions/$missionId.xml")
        val writer = XmlWriter(FileWriter(file))
        writer.element("Mission").element("Description", description).element("RewardType", missionType).element("Complete", complete).element("Claimed", claimed).element("Goal", missionGoal).pop()
        writer.close()
    }
}