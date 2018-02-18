package com.kontus.cryptocurrencymanager.helpers

object Config {
    // shared preferences
    const val PREFS_NAME = "CryptoCurrencyManagerPrefs"

    // request results
    const val REQUEST_LOAD_CSV = 0
    const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

    // defaults
    const val DEFAULT_EXCHANGE = "BITTREX"
    val DEFAULT_BITTREX_COLUMNS_CSV = setOf("Exchange", "Type", "Quantity", "Price")
    val BITTREX_COLUMNS_CSV = arrayOf("OrderUuid", "Exchange", "Type", "Quantity", "Limit", "CommissionPaid", "Price", "Opened", "Closed")
}