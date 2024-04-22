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
    var timestamp: Long? =null

    @Ignore
    constructor()
    constructor(id: String, title: String?, imgUrl: String?, details: String?,timestamp:Long?) {
        this.title = title
        this.id = id
        this.imgUrl = imgUrl
        this.details = details
        this.timestamp=timestamp
    }

    fun toJson(): Map<String, Any?> {
        val json: MutableMap<String, Any?> = HashMap()
        json[ID] = id
        json[TITLE] = title
        json[IMAGE] = imgUrl
        json[DETAILS] = details
        json[TIMESTAMP] = timestamp
        return json
    }

    companion object {
        const val TITLE = "title"
        const val ID = "id"
        const val IMAGE = "image"
        const val DETAILS = "details"
        const val COLLECTION = "posts"
        const val TIMESTAMP = "timestamp"
        fun fromJson(json: Map<String?, Any?>): Post {
            val id = json[ID] as String?
            val name = json[TITLE] as String?
            val image = json[IMAGE] as String?
            val details = json[DETAILS] as String?
            val timestamp = json[TIMESTAMP] as Long?
            return Post(id!!, name, image, details,timestamp)
        }
    }

    fun getTimestamp(): Long? {
        return timestamp
    }
    fun getId(): String {
        return id
    }
}