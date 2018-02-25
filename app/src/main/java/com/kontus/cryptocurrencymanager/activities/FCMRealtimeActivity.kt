package com.kontus.cryptocurrencymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kontus.cryptocurrencymanager.R
import android.support.design.widget.FloatingActionButton
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.internal.FirebaseAppHelper.getUid
import android.text.TextUtils
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseApp.DEFAULT_APP_NAME
import com.kontus.cryptocurrencymanager.models.fdb.Post
import com.kontus.cryptocurrencymanager.models.fdb.User
import java.util.*

class FCMRealtimeActivity : AppCompatActivity(), View.OnClickListener {
    private var mDatabase: DatabaseReference? = null

    private var mTitleField: EditText? = null
    private var mBodyField: EditText? = null
    private var mSubmitButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fcmrealtime)

        mDatabase = FirebaseDatabase.getInstance().reference

        // Views
        mTitleField = findViewById(R.id.field_title)
        mBodyField = findViewById(R.id.field_body)
        mSubmitButton = findViewById(R.id.fab_submit_post)

        // Click listeners
        mSubmitButton?.setOnClickListener(this)
    }

    private fun submitPost() {
        val title = mTitleField?.text.toString()
        val body = mBodyField?.text.toString()

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField?.error = getString(R.string.required)
            return
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField?.error = getString(R.string.required)
            return
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false)
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show()

        // [START single_value_read]
        val userId = getUid(FirebaseApp.getInstance(DEFAULT_APP_NAME))
        mDatabase?.child("users")?.child(userId)?.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get user value
                        val user = dataSnapshot.getValue<User>(User::class.java)

                        // [START_EXCLUDE]
                        if (user == null) {
                            println("User $userId is unexpectedly null")
                            Toast.makeText(baseContext, "Error: could not fetch user.", Toast.LENGTH_SHORT).show()
                        } else {
                            writeNewPost(userId, user.username!!, title, body)
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true)
                        finish()
                        // [END_EXCLUDE]
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("getUser: onCancelled " + databaseError.toException())
                        // [START_EXCLUDE]
                        setEditingEnabled(true)
                        // [END_EXCLUDE]
                    }
                })
        // [END single_value_read]
    }

    private fun setEditingEnabled(enabled: Boolean) {
        mTitleField?.isEnabled = enabled
        mBodyField?.isEnabled = enabled
        if (enabled) {
            mSubmitButton?.visibility = View.VISIBLE
        } else {
            mSubmitButton?.visibility = View.GONE
        }
    }

    private fun writeNewPost(userId: String, username: String, title: String, body: String) {
        // Create new post at /user-posts/$userid/$postid and at /posts/$postid simultaneously
        val key = mDatabase?.child("posts")?.push()?.key
        val post = Post(userId, username, title, body)
        val postValues = post.toMap()

        val childUpdates = HashMap<String, String?>()
        childUpdates["/posts/" + key] = postValues.toString()
        childUpdates["/user-posts/$userId/$key"] = postValues.toString()

        mDatabase?.updateChildren(childUpdates as Map<String, Any>?)
    }

    override fun onClick(v: View?) {
        val i = v?.id
        when (i) {
            R.id.fab_submit_post -> submitPost()
        }
    }

}