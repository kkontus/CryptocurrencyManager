package com.kontus.cryptocurrencymanager.fragments

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.models.Purchase
import com.kontus.cryptocurrencymanager.models.Purchase.PurchaseItem
import com.kontus.cryptocurrencymanager.interfaces.OnListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display a [PurchaseItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class PurchaseItemRecyclerViewAdapter(private val mValues: List<Purchase.PurchaseItem>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<PurchaseItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_purchase_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
//        holder.mIdView.text = mValues[position].id
//        holder.mContentView.text = mValues[position].content
//        holder.mDetailsView.text = mValues[position].details
//        holder.mCoinNameView.text = mValues[position].coinName
//        holder.mCoinSymbolView.text = mValues[position].coinSymbol
        holder.mCoinNameSymbolView.text = mValues[position].coinName + "(" + mValues[position].coinSymbol + ")"
        holder.mQuantityView.text = mValues[position].quantity.toString()
        holder.mPurchasePriceBTCView.text = mValues[position].purchasePriceBTC.toString() + " BTC"
        holder.mPurchasePriceFiatView.text = mValues[position].purchasePriceFiat.toString() + " USD"
        holder.mCurrentPriceBTCView.text = mValues[position].currentPriceBTC.toString() + " BTC"
        holder.mCurrentPriceFiatView.text = mValues[position].currentPriceFiat.toString() + " USD"


        val calculateAbsolute = mValues[position].currentPriceFiat - mValues[position].purchasePriceFiat
        val calculateRelative = ((mValues[position].currentPriceFiat / mValues[position].purchasePriceFiat) * 100) - 100
        val calculateAbsoluteFormatted = "%.2f".format(calculateAbsolute)
        val calculateRelativeFormatted = "%.2f".format(calculateRelative)

        holder.mProfitLossRatioView.text = calculateAbsoluteFormatted + " USD (" + calculateRelativeFormatted + "%)"

        if (calculateAbsolute > 0) {
            holder.mProfitLossRatioView.setTextColor(Color.GREEN)
        } else {
            holder.mProfitLossRatioView.setTextColor(Color.RED)
        }

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Purchase.PurchaseItem? = null
//        val mIdView: TextView = mView.findViewById(R.id.id) as TextView
//        val mContentView: TextView = mView.findViewById(R.id.content) as TextView
//        val mDetailsView: TextView = mView.findViewById(R.id.details) as TextView
//        val mCoinNameView: TextView = mView.findViewById(R.id.coinName) as TextView
//        val mCoinSymbolView: TextView = mView.findViewById(R.id.coinSymbol) as TextView

        val mCoinNameSymbolView: TextView = mView.findViewById<TextView>(R.id.coinNameSymbol) as TextView
        val mQuantityView: TextView = mView.findViewById<TextView>(R.id.quantity) as TextView
        val mPurchasePriceBTCView: TextView = mView.findViewById<TextView>(R.id.purchasePriceBTC) as TextView
        val mPurchasePriceFiatView: TextView = mView.findViewById<TextView>(R.id.purchasePriceFiat) as TextView
        val mCurrentPriceBTCView: TextView = mView.findViewById<TextView>(R.id.currentPriceBTC) as TextView
        val mCurrentPriceFiatView: TextView = mView.findViewById<TextView>(R.id.currentPriceFiat) as TextView
        val mProfitLossRatioView: TextView = mView.findViewById<TextView>(R.id.profitLossRatio) as TextView

        override fun toString(): String {
//            return super.toString() + " '" + mContentView.text + "'"
            return super.toString() + " '" + mCoinNameSymbolView.text + "'"
        }
    }
}