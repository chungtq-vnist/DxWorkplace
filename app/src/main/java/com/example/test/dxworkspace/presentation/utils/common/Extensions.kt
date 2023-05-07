package com.example.test.dxworkspace.presentation.utils.common

import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.presentation.utils.common.decorator.LinearMarginDecoration

class Extensions {

    fun RecyclerView.addDefaultDecorator(margin: Int = resources.getDimensionPixelOffset(R.dimen._8sdp)) {
        addItemDecoration(
            LinearMarginDecoration(
            leftMargin = margin,
            topMargin = margin,
            rightMargin = margin,
            bottomMargin = margin,
            orientation = RecyclerView.VERTICAL
        )
        )
    }

}