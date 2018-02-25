package com.kontus.cryptocurrencymanager.models.fdb

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Post {

    var uid: Any? = null
    var author: Any? = null
    var title: Any? = null
    var body: Any? = null
    var starCount = 0
    var stars: Map<String, Boolean> = HashMap()

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    constructor(uid: String, author: String, title: String, body: String) {
        this.uid = uid
        this.author = author
        this.title = title
        this.body = body
    }

    @Exclude
    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["uid"] = uid!!
        result["author"] = author!!
        result["title"] = title!!
        result["body"] = body!!
        result["starCount"] = starCount
        result["stars"] = stars

        return result
    }

}