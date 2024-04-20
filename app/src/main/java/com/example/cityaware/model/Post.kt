package com.example.cityaware.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Post {
    @PrimaryKey
    var id = ""
    var title: String? = ""
    var imgUrl: String? = ""
    var details: String? = ""

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
            return Post(id!!, name, image, details)
        }
    }


    fun toJson(): Map<String, Any> {
        val json: MutableMap<String, Any> = HashMap()
        json["id"] = getId()
        json["title"] = getTitle()
        json["image"] = getImgUrl()
        json["details"] = getDetails()
        return json
    }


    fun setId( id: String?) {
        this.id = id!!
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun setImgUrl(imgUrl: String?) {
        this.imgUrl = imgUrl
    }

    fun setDetails(details: String?) {
        this.details = details
    }

    fun getId(): String {
        return id
    }

    fun getTitle(): String {
        return title!!
    }

    fun getImgUrl(): String {
        return imgUrl!!
    }

    fun getDetails(): String {
        return details!!
    }
}