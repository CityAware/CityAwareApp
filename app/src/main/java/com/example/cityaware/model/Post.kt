package com.example.cityaware.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
class Post {
    @PrimaryKey
    var id = ""
    var title: String? = ""
    var imgUrl: String? = ""
    var details: String? = ""

    @Ignore
    constructor()
    constructor(id: String, title: String?, imgUrl: String?, details: String?) {
        this.title = title
        this.id = id
        this.imgUrl = imgUrl
        this.details = details
    }

    fun toJson(): Map<String, Any?> {
        val json: MutableMap<String, Any?> = HashMap()
        json[ID] = id
        json[TITLE] = title
        json[IMAGE] = imgUrl
        json[DETAILS] = details
        return json
    }

    companion object {
        const val TITLE = "title"
        const val ID = "id"
        const val IMAGE = "image"
        const val DETAILS = "details"
        const val COLLECTION = "posts"
        fun fromJson(json: Map<String?, Any?>): Post {
            val id = json[ID] as String?
            val name = json[TITLE] as String?
            val image = json[IMAGE] as String?
            val details = json[DETAILS] as String?
            return Post(id!!, name, image, details)
        }
    }
}