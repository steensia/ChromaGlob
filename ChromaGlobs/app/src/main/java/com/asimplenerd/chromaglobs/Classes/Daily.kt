package com.asimplenerd.chromaglobs.Classes

class Daily(complete : Boolean, description : String, claimed : Boolean) {

    private var complete = false
    private lateinit var description : String
    private var claimed = false

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
}