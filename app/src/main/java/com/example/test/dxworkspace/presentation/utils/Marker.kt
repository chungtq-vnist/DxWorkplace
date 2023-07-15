package com.example.test.dxworkspace.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import com.example.test.dxworkspace.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

open class Marker constructor(
        context: Context?,
) :
        MarkerView(context, R.layout.custom_marker_view) {
    val textView: TextView = findViewById(R.id.tvContent)
    val layout: ViewGroup? = findViewById(R.id.layout)

    override fun refreshContent(
            e: Entry,
            highlight: Highlight,
    ) {
//        super.refreshContent(e, highlight)
        setOffset(-(width / 2.0f), -height.toFloat())
        super.refreshContent(e, highlight)
    }

    fun markerSetBackground(drawable: Drawable?) {
        if (layout != null) layout.background = drawable
    }

    init {
        setOffset(-(width / 2.0f), -height.toFloat())
    }
}