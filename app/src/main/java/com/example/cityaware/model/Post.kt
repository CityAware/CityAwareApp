package com.example.cityaware.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Post {
    @PrimaryKey
    var id: String = ""
    var title: String? = null
    var imgUrl: String? = null
    var details: String? = null

    constructor()
    constructor(id: String, title: String?, imgUrl: String?, details: String?) {
        this.title = title
        this.id = id
        this.imgUrl = imgUrl
        this.details = details
    }

    companion object {
        const val COLLECTION = "posts"
        fun fromJson(json: Map<String?, Any?>): Post {
            val id = json["id"] as String?
            val name = json["title"] as String?
            val image = json["image"] as String?
            val details = json["details"] as String?
            return Post(id ?: "", name, image, details)
        }
    }

    fun toJson(): Map<String, Any> {
        val json: MutableMap<String, Any> = HashMap()
        json["id"] = id
        json["title"] = title ?: ""
        json["image"] = imgUrl ?: ""
        json["details"] = details ?: ""
        return json
    }

}