package com.asimplenerd.chromaglobs

class Card {

    private lateinit var name : String
    private lateinit var type : GlobType
    private var health = 0
    private var attackPower = 0
    private var defenseRating = 0
    private lateinit var rarity : Rarity

    fun Card(name : String, type : GlobType){
        this.name = name
        this.type = type
    }

    fun Card(name : String, type : GlobType, health : Int, ap : Int, dp : Int, rarity : Rarity){
        this.name = name
        this.type = type
        this.health = health
        this.attackPower = ap
        this.defenseRating = dp
        this.rarity = rarity
    }

    fun getCardName() : String{
        return name
    }

    fun getCardType() : GlobType{
        return type
    }

    fun getAttackPower() : Int {
        return attackPower
    }

    fun getDefenseRating() : Int{
        return defenseRating
    }

    fun modifyHealth(amount : Int){
        health += amount
    }

    fun takeDamage(amount : Int){
        modifyHealth(-1 * amount)
    }

    fun restoreHealth(amount : Int){
        modifyHealth(amount)
    }

}