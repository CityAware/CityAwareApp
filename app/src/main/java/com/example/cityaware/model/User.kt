package com.example.cityaware.model

class User {
    var email: String? = ""
    var label: String? = ""

    internal constructor()
    constructor(email: String?, label: String?) {
        this.email = email
        this.label = label
    }

    fun toJson(): Map<String, Any?> {
        val json: MutableMap<String, Any?> = HashMap()
        json[EMAIL] = email
        json[ACCOUNT_LABEL] = label
        return json
    }

    companion object {
        const val EMAIL = "email"
        const val ACCOUNT_LABEL = "label"
        const val COLLECTION = "users"
        fun fromJson(json: Map<String?, Any?>): User {
            val email =
                json[EMAIL] as String?
            val label =
                json[ACCOUNT_LABEL] as String?
            return User(email, label)
        }
    }
}
