package com.kontus.cryptocurrencymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.text.TextUtils
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.helpers.General
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper
import com.kontus.cryptocurrencymanager.models.fdb.User

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private var mLoggedUserField: TextView? = null
    private var mEmailField: EditText? = null
    private var mPasswordField: EditText? = null
    private var mFirstnameField: EditText? = null
    private var mLastnameField: EditText? = null

    private var mSignInButton: Button? = null
    private var mSignUpButton: Button? = null
    private var mSignOutButton: Button? = null

    private var mShared: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        mShared = SharedPreferencesHelper(this)

        // Views
        mLoggedUserField = findViewById(R.id.field_logged_user)
        mEmailField = findViewById(R.id.field_email)
        mPasswordField = findViewById(R.id.field_password)
        mFirstnameField = findViewById(R.id.field_firstname)
        mLastnameField = findViewById(R.id.field_lastname)

        mSignInButton = findViewById(R.id.button_sign_in)
        mSignUpButton = findViewById(R.id.button_sign_up)
        mSignOutButton = findViewById(R.id.button_sign_out)

        // Click listeners
        mSignInButton?.setOnClickListener(this)
        mSignUpButton?.setOnClickListener(this)
        mSignOutButton?.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()

        // Check auth on Activity start
        if (mAuth?.currentUser != null) {
            mAuth?.currentUser?.let { onAuthSuccess(it, mShared?.firebaseFirstname, mShared?.firebaseLastname) }
        }
        checkIfLogged()
    }

    private fun signIn() {
        println("signIn")

        if (!validateForm()) {
            return
        }

        // showProgressDialog()

        val email = mEmailField?.text.toString()
        val password = mPasswordField?.text.toString()
        val firstname = mFirstnameField?.text.toString()
        val lastname = mLastnameField?.text.toString()

        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            println("signIn: onComplete: " + task.isSuccessful)

            // hideProgressDialog()

            if (task.isSuccessful) {
                onAuthSuccess(task.result.user, firstname, lastname)
            } else {
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp() {
        println("signUp")

        if (!validateForm()) {
            return
        }

        // showProgressDialog()

        val email = mEmailField?.text.toString()
        val password = mPasswordField?.text.toString()
        val firstname = mFirstnameField?.text.toString()
        val lastname = mLastnameField?.text.toString()

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            println("createUser: onComplete:" + task.isSuccessful)

            // hideProgressDialog()

            if (task.isSuccessful) {
                onAuthSuccess(task.result.user, firstname, lastname)
            } else {
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOut() {
        println("signOut")
        FirebaseAuth.getInstance().signOut()
        checkIfLogged()
    }

    private fun checkIfLogged() {
        if (mAuth?.currentUser != null) {
            mLoggedUserField?.text = "Welcome " + mShared?.firebaseFirstname + " " + mShared?.firebaseLastname

            mLoggedUserField?.visibility = View.VISIBLE
            mSignInButton?.visibility = View.GONE
            mSignUpButton?.visibility = View.GONE
            mSignOutButton?.visibility = View.VISIBLE

            mEmailField?.visibility = View.GONE
            mPasswordField?.visibility = View.GONE
            mFirstnameField?.visibility = View.GONE
            mLastnameField?.visibility = View.GONE
        } else {
            mLoggedUserField?.visibility = View.GONE
            mEmailField?.visibility = View.VISIBLE
            mPasswordField?.visibility = View.VISIBLE
            mFirstnameField?.visibility = View.VISIBLE
            mLastnameField?.visibility = View.VISIBLE

            mSignInButton?.visibility = View.VISIBLE
            mSignUpButton?.visibility = View.VISIBLE
            mSignOutButton?.visibility = View.GONE
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(mEmailField?.text.toString())) {
            mEmailField?.error = getString(R.string.required)
            result = false
        } else {
            mEmailField?.error = null
        }

        if (TextUtils.isEmpty(mPasswordField?.text.toString())) {
            mPasswordField?.error = getString(R.string.required)
            result = false
        } else {
            mPasswordField?.error = null
        }

        if (TextUtils.isEmpty(mFirstnameField?.text.toString())) {
            mFirstnameField?.error = getString(R.string.required)
            result = false
        } else {
            mFirstnameField?.error = null
        }

        if (TextUtils.isEmpty(mLastnameField?.text.toString())) {
            mLastnameField?.error = getString(R.string.required)
            result = false
        } else {
            mLastnameField?.error = null
        }

        return result
    }

    private fun onAuthSuccess(user: FirebaseUser, firstname: String?, lastname: String?) {
        val username = General.usernameFromEmail(user.email!!)
        mShared?.firebaseFirstname = firstname!!
        mShared?.firebaseLastname = lastname!!

        // Write new user
        writeNewUser(user.uid, username, user.email, firstname, lastname)

        checkIfLogged()

//        // Go to MainActivity
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
    }

    private fun writeNewUser(userId: String, username: String, email: String?, firstname: String?, lastname: String?) {
        val user = User(username, email!!, firstname!!, lastname!!)

        mDatabase?.child("users")?.child(userId)?.child("profile")?.setValue(user)
    }

    override fun onClick(v: View?) {
        val i = v?.id
        when (i) {
            R.id.button_sign_in -> signIn()
            R.id.button_sign_up -> signUp()
            R.id.button_sign_out -> signOut()
        }
    }

}
