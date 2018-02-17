package com.kontus.cryptocurrencymanager.interfaces

import com.kontus.cryptocurrencymanager.models.Purchase

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 */
interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(item: Purchase.PurchaseItem)
}