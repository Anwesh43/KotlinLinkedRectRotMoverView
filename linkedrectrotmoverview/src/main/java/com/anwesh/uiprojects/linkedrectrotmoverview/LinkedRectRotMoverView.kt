package com.anwesh.uiprojects.linkedrectrotmoverview

/**
 * Created by anweshmishra on 09/06/18.
 */

import android.view.MotionEvent
import android.content.Context
import android.view.View
import android.graphics.*

class LinkedRectRotMoverView (ctx : Context) : View (ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}