package com.kontus.cryptocurrencymanager.models

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object Purchase {
    val ITEMS: MutableList<PurchaseItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, PurchaseItem> = HashMap()
    private const val COUNT = 25

    init {
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: PurchaseItem) {
        ITEMS.add(item)
        ITEM_MAP[item.coinSymbol] = item
    }

    private fun createDummyItem(position: Int): PurchaseItem {
        return PurchaseItem(
                "Civic " + position,
                "CVC" + position,
                (490.94124123 + position),
                (0.06336579 + position),
                (193.87 + position),
                (0.07436579 + position),
                (257.17 + position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    class PurchaseItem(val coinName: String, val coinSymbol: String, val quantity: Double,
                       val purchasePriceBTC: Double, val purchasePriceFiat: Double,
                       val currentPriceBTC: Double, val currentPriceFiat: Double) {

//        override fun toString(): String {
//            return content
//        }
    }
}
