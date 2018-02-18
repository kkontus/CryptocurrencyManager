package com.kontus.cryptocurrencymanager.helpers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private var mSharedPreferences: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    init {
        this.mSharedPreferences = context.getSharedPreferences(Config.PREFS_NAME, Context.MODE_PRIVATE)
        this.mEditor = mSharedPreferences?.edit()
    }

    var selectedExchange: String
        get() = mSharedPreferences!!.getString(SELECTED_EXCHANGE, Config.DEFAULT_EXCHANGE)
        set(selectedExchange) {
            if (mEditor != null) {
                mEditor?.putString(SELECTED_EXCHANGE, selectedExchange)
                mEditor?.apply()
            }
        }

    var selectedBittrexColumnsCSV: Set<String>
        get() = mSharedPreferences!!.getStringSet(SELECTED_BITTREX_COLUMNS_CSV, Config.DEFAULT_BITTREX_COLUMNS_CSV)
        set(selectedBittrexColumnsCSV) {
            if (mEditor != null) {
                mEditor?.putStringSet(SELECTED_BITTREX_COLUMNS_CSV, selectedBittrexColumnsCSV)
                mEditor?.apply()
            }
        }

    companion object {
        private const val SELECTED_EXCHANGE = "selectedExchange"
        private const val SELECTED_BITTREX_COLUMNS_CSV = "selectedBittrexColumnsCSV"
    }

}