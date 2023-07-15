package com.example.test.dxworkspace.presentation.utils

import android.content.Context
import android.widget.TextView
import com.example.test.dxworkspace.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF


open class MarkerHorizontal constructor(
        context: Context?,
) : MarkerView(context, R.layout.custom_marker_view_horizontal) {
    val textView: TextView = findViewById(R.id.tvContent)

    fun markerSetBackground(resId: Int) {
        textView.setBackgroundResource(resId)
    }

    override fun getOffset(): MPPointF? {
        return MPPointF((-(width / 1.5)).toFloat(), (-height).toFloat())
    }
}