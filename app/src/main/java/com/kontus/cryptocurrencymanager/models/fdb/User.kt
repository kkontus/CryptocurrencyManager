package com.kontus.cryptocurrencymanager.models.fdb

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {

    var username: String? = null
    var email: String? = null
    var firstname: String? = null
    var lastname: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(username: String, email: String, firstname: String, lastname: String) {
        this.username = username
        this.email = email
        this.firstname = firstname
        this.lastname = lastname
    }

}