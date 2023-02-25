package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class MyLongClickListener(val onItemClick: (Int) -> Unit, val onLongItemClick: (Int) -> Unit , recyclerView: RecyclerView) : RecyclerView.OnItemTouchListener {

    private val gestureDetector = GestureDetectorCompat(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            if (childView != null) {
                onLongItemClick.invoke(recyclerView.getChildAdapterPosition(childView))
            }
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && gestureDetector.onTouchEvent(e)) {
            onItemClick.invoke(rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
