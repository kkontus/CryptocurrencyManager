package com.kontus.cryptocurrencymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper

import android.widget.TextView


class SettingsActivity : AppCompatActivity() {
    private var mTextViewSettings: TextView? = null
    private var mSharedPreferencesHelper: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViews()

        mSharedPreferencesHelper = SharedPreferencesHelper(this)
    }

    private fun findViews() {
        mTextViewSettings = findViewById(R.id.settings_test)
        mTextViewSettings?.text = "Settings Fragment"
    }
}