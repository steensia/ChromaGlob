package com.asimplenerd.chromaglobs.Classes

import android.util.Log
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import org.xml.sax.XMLReader
import java.io.File
import java.io.FileWriter

class Deck {
    var cards = ArrayList<Card>(20)
    var name = "DefaultName"

    constructor(cards : ArrayList<Card>){
        this.cards = cards
    }

    constructor(fileName: String){
        val reader = XmlReader()
        Log.d(
                "CardField",
                reader.parse("$fileName/$name.xml").toString()
        )
    }

    fun writeToXml(fileName : String){
        val file = File("$fileName/$name.xml")
        if(!file.exists())
            file.createNewFile()
        val xmlWriter = XmlWriter(FileWriter(file))
        xmlWriter.element("Deck").attribute("name", name)
        cards.forEachIndexed{ i: Int, card: Card -> card.writeXmlTag(xmlWriter, i) }
        xmlWriter.close()
    }
}