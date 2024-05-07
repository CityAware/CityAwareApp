package com.example.cityaware.model

class User {
    var email: String? = ""
    var label: String? = ""
    var id = ""
    var imgUrl: String? = null

    internal constructor()
    constructor(email: String?, label: String? ) {
        this.email = email
        this.label = label
        this.id = id
        this.imgUrl = imgUrl
    }

    fun toJson(): Map<String, Any?> {
        val json: MutableMap<String, Any?> = HashMap()
        json[EMAIL] = email
        json[ACCOUNT_LABEL] = label
//        json[ID] = id
//        json[IMAGE] = imgUrl
        return json
    }

    companion object {
        const val EMAIL = "email"
        const val ACCOUNT_LABEL = "label"
        const val COLLECTION = "users"
//        const val IMAGE = "imgUrl"
//        const val ID = "id"
        fun fromJson(json: Map<String?, Any?>): User {
            val email =
                json[EMAIL] as String?
            val label =
                json[ACCOUNT_LABEL] as String?
//            val id = json[ID] as String
//            val imgUrl = json[IMAGE] as String
            return User(email, label)
        }
    }
}
