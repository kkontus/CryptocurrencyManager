package com.kontus.cryptocurrencymanager.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.MultiAutoCompleteTextView
import com.kontus.cryptocurrencymanager.helpers.Config
import com.kontus.cryptocurrencymanager.helpers.General
import com.kontus.cryptocurrencymanager.views.InstantMultiAutoComplete


class SettingsActivity : AppCompatActivity() {
    private var mAutoCompleteTextViewColumnsCSV: InstantMultiAutoComplete? = null
    private var mConfirmButton: Button? = null
    private var mShared: SharedPreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViews()

        mShared = SharedPreferencesHelper(this)

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
        mAutoCompleteTextViewColumnsCSV = findViewById(R.id.columns_csv)
        mConfirmButton = findViewById(R.id.confirm)
    }

}