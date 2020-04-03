package com.asimplenerd.chromaglobs.Classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.ImageReader
import android.os.Parcel
import android.os.Parcelable
import com.badlogic.gdx.utils.XmlWriter
import java.io.File

class Card() : Parcelable{
    private lateinit var name : String
    private lateinit var type : GlobType
    private var health = 0
    private var attackPower = 0
    private var specialAttackPower = 99
    private var defenseRating = 0
    private lateinit var rarity : Rarity
    private  var cardId = -1
    private var level = 10
    private var mana = 99

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

    constructor (name: String, type: GlobType, health: Int, ap: Int, dp: Int, rarity: Rarity, cardId : Int) : this(name, type, health, ap, dp, rarity){
        this.cardId = cardId
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

    fun getCardLevel() : Int{
        return level
    }

    fun getCardType() : GlobType {
        return type
    }

    fun getCardHealth() : Int{
        return health
    }

    fun getCardImage(context: Context, scaleFactorX : Int, scaleFactorY : Int) : Bitmap{
        var name = getLetter(cardId)
        var bmap = File(context.filesDir,  "images/$name.png")
        var loadedImage = BitmapFactory.decodeFile(bmap.absolutePath)
        val image = Bitmap.createScaledBitmap(loadedImage, loadedImage.width / scaleFactorX, loadedImage.height / scaleFactorY, false)
        return image
    }

    fun getScaledCardImage(context: Context, width : Int, height : Int){

    }

    private fun getLetter(index : Int) : String{
        when(index){
            0 -> return "A"
            1 -> return "B"
            2 -> return "C"
            3 -> return "D"
            4 -> return "E"
            5 -> return "F"
            6 -> return "G"
            7 -> return "H"
            8 -> return "I"
            9 -> return "J"
            10 -> return "K"
            11 -> return "L"
            12 -> return "M"
            13 -> return "N"
            14 -> return "O"
            15 -> return "P"
            16 -> return "Q"
            17 -> return "R"
            18 -> return "S"
            19 -> return "T"
            20 -> return "U"
            21 -> return "V"
            22 -> return "W"
            23 -> return "X"
            24 -> return "Y"
            25 -> return "Z"
            26 -> return "Dez"
            27 -> return "BB"
            28 -> return "Glucky"
            29 -> return "Starry"
            30 -> return "Steen"
            31 -> return "Poop"
            32 -> return "Grampy"
            33 -> return "0"
            34 -> return "1"
            35 -> return "2"
            36 -> return "3"
            37 -> return "4"
            38 -> return "5"
            39 -> return "6"
            40 -> return "7"
            41 -> return "8"
            42 -> return "9"
        }
        return "0"
    }

    fun getAttackPower() : Int {
        return attackPower
    }

    fun getSpecialAttackPower() : Int{
        return specialAttackPower
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

    fun getId() : Int{
        return cardId
    }

    fun setLevel(l : Int){
        level = l
    }

    fun getAvailableMana() : Int{
        return mana
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

    fun getRarity() : Rarity{
        return rarity
    }

    fun getCardId() : Int{
        return cardId
    }

    fun writeXmlTag(writer : XmlWriter, index : Int){
        writer.element("Card").attribute("name", getCardName()).attribute("index", index)
                .element("type", getCardType()).element("health", getCardHealth()).element("attack", getAttackPower())
                .element("special", getSpecialAttackPower()).element("defense", getDefenseRating()).element("mana", getAvailableMana())
                .element("rarity", getRarity()).element("level", getCardLevel()).element("id", getCardId()).pop()
    }

}