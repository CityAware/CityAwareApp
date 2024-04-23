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
    var location: String? = ""
    var label: String? = ""
    var timestamp: Long? = null

    @Ignore
    constructor()
    constructor(
        id: String,
        title: String?,
        imgUrl: String?,
        details: String?,
        location: String?,
        label: String?,
        timestamp: Long?
    ) {
        this.title = title
        this.id = id
        this.imgUrl = imgUrl
        this.details = details
        this.location = location
        this.label = label
        this.timestamp = timestamp
    }

    fun toJson(): Map<String, Any?> {
        val json: MutableMap<String, Any?> = HashMap()
        json[ID] = id
        json[TITLE] = title
        json[IMAGE] = imgUrl
        json[DETAILS] = details
        json[LOCATION] = location
        json[LABEL] = label
        json[TIMESTAMP] = timestamp
        return json
    }

    companion object {
        const val TITLE = "title"
        const val LABEL = "label"
        const val ID = "id"
        const val IMAGE = "image"
        const val DETAILS = "details"
        const val LOCATION = "location"
        const val TIMESTAMP = "timestamp"
        const val COLLECTION = "posts"
        fun fromJson(json: Map<String?, Any?>): Post {
            val id = json[ID] as String?
            val label = json[LABEL] as String?
            val name = json[TITLE] as String?
            val image = json[IMAGE] as String?
            val details = json[DETAILS] as String?
            val location = json[LOCATION] as String?
            val timestamp = json[TIMESTAMP] as Long?
            return Post(id!!, name, image, details, location, label, timestamp)
        }
    }
}

