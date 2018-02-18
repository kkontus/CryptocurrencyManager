package com.kontus.cryptocurrencymanager.views


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView

class InstantMultiAutoComplete : AppCompatMultiAutoCompleteTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (focused && adapter != null) {
            performFiltering("", 0)
        }
    }
}
