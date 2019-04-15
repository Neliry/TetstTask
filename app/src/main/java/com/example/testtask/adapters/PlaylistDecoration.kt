package com.example.testtask.viewmodels

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

internal class PlaylistDecoration(context: Context): androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint()

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        mPaint.style = Paint.Style.FILL
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            mPaint.color = Color.parseColor("#ababab")
            c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + 1.px).toFloat(), mPaint)
        }
    }

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        a.recycle()
    }

    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}