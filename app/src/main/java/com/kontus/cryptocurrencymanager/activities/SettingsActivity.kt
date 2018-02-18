package com.kontus.cryptocurrencymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import com.kontus.cryptocurrencymanager.helpers.Config
import com.kontus.cryptocurrencymanager.helpers.General

class SettingsActivity : AppCompatActivity() {
    private var mTextViewColumnsCSV: TextView? = null
    private var mAutoCompleteTextViewColumnsCSV: MultiAutoCompleteTextView? = null
    private var mConfirmButton: Button? = null
    private var mShared: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViews()

        mShared = SharedPreferencesHelper(this)

        val options = Config.BITTREX_COLUMNS_CSV.joinToString()
        mTextViewColumnsCSV?.text = "Options: " + options

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, Config.BITTREX_COLUMNS_CSV)
        mAutoCompleteTextViewColumnsCSV?.threshold = 0
        mAutoCompleteTextViewColumnsCSV?.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        mAutoCompleteTextViewColumnsCSV?.setAdapter(adapter)

        mConfirmButton?.setOnClickListener {
            val selectedItems = mAutoCompleteTextViewColumnsCSV?.text.toString()
            val selectedItemsSeparated = selectedItems.replace(", ", ",").removeSuffix(",").split(",").distinct()

            // val set = HashSet<String>(Arrays.asList(*selectedItemsSeparated.toTypedArray()))
            // println(set)

            val set = General.convertListToSet(selectedItemsSeparated)
            println(set)

            mShared?.selectedBittrexColumnsCSV = set

            // val list = General.convertSetToList(set)
            // println(list)
        }
    }

    private fun findViews() {
        mTextViewColumnsCSV = findViewById(R.id.columns_csv_options)
        mAutoCompleteTextViewColumnsCSV = findViewById(R.id.columns_csv)
        mConfirmButton = findViewById(R.id.confirm)
    }

}