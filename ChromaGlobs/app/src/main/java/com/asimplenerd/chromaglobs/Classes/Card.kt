package com.asimplenerd.chromaglobs.Classes

import android.os.Parcel
import android.os.Parcelable

class Card() : Parcelable{
    private lateinit var name : String
    private lateinit var type : GlobType
    private var health = 0
    private var attackPower = 0
    private var defenseRating = 0
    private lateinit var rarity : Rarity

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()!!
        health = parcel.readInt()
        attackPower = parcel.readInt()
        defenseRating = parcel.readInt()
    }

    constructor(name : String, type : GlobType) : this(){
        this.name = name
        this.type = type
    }

    constructor (name : String, type : GlobType, health : Int, ap : Int, dp : Int, rarity : Rarity) : this(name, type){
        this.health = health
        this.attackPower = ap
        this.defenseRating = dp
        this.rarity = rarity
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeValue(type)
        dest?.writeInt(health)
        dest?.writeInt(attackPower)
        dest?.writeInt(defenseRating)
        dest?.writeValue(rarity)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getCardName() : String{
        return name
    }

    fun getCardType() : GlobType {
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

    override fun toString(): String {
        return name
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

}