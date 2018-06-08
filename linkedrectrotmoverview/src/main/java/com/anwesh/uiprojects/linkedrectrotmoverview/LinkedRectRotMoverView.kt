package com.anwesh.uiprojects.linkedrectrotmoverview

/**
 * Created by anweshmishra on 09/06/18.
 */

import android.app.Activity
import android.view.MotionEvent
import android.content.Context
import android.view.View
import android.graphics.*

val RRMNODES : Int = 5

class LinkedRectRotMoverView (ctx : Context) : View (ctx) {

    private val renderer : Renderer = Renderer(this)

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        var scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {

        fun update(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class RRMNode(val i : Int) {

        private val state : State = State()

        private var next : RRMNode? = null

        private var prev : RRMNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < RRMNODES - 1) {
                next = RRMNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val wGap : Float = (w / RRMNODES)
            val size : Float = wGap/5
            canvas.save()
            canvas.translate(i * wGap, h/2)
            canvas.save()
            canvas.translate(wGap * state.scales[2], 0f)
            canvas.drawRect(RectF(-size/2, -size/2, size/2, size/2), paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(-90f * state.scales[1])
            canvas.drawLine(0f, size * state.scales[2], 0f, size * state.scales[0], paint)
            canvas.restore()
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : RRMNode {
            var curr : RRMNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedRectRotMover (val i : Int) {

        var curr : RRMNode = RRMNode(0)

        var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#4527A0")
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }

    data class Renderer (var view : LinkedRectRotMoverView) {

        private val animator : Animator = Animator(view)

        private val lrrm : LinkedRectRotMover = LinkedRectRotMover(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lrrm.draw(canvas, paint)
            animator.update {
                lrrm.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lrrm.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : LinkedRectRotMoverView {
            val view : LinkedRectRotMoverView = LinkedRectRotMoverView(activity)
            activity.setContentView(view)
            return view
        }
    }

}