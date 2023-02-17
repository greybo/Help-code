package com.example.help_code.presentation.swipe

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout

class Swipe(context: Context) {
    private var startTransitionX = 0f
    private var histori = 0f
    private var scale = 0f
    private var width = 2000f
    private var height = 2000f
    private var threshold = 200
    private var callback: SwipeCallback? = null
    private var views: MutableList<View>? = null
    private var viewChanges: View? = null

    abstract class SwipeCallback {
        abstract fun move(view: View?, transitionX: Float)
        abstract fun end(view: View?)
        abstract fun click(view: View?)
    }

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display: Display? = null
        val size = Point()
        if (wm != null) {
            display = wm.defaultDisplay
            display.getSize(size)
        }
        width = size.x.toFloat()
        height = size.y.toFloat()
        threshold = (width / 5).toInt()
    }

    private fun touchMotionEvent(e: MotionEvent) {
        if (e.action == MotionEvent.ACTION_DOWN) {
            histori = e.x - startTransitionX
            isContain(e)
        }
        scale = e.x - histori
        move(scale)
        if (e.action == MotionEvent.ACTION_UP) {
            if (Math.abs(scale) < threshold) {
                if (scale == 0f) {
                    click(viewChanges)
                } else {
                    move(0f)
                }
                viewChanges = null
            } else {
                taskTransition()
            }
        }
    }

    private fun click(view: View?) {
        if (callback != null) {
            callback!!.click(view)
        }
    }

    private fun end(view: View) {
        if (callback != null) {
            callback!!.end(view)
        }
        view.visibility = View.GONE
    }

    private fun move(translationX: Float) {
        if (viewChanges != null) {
            if (callback != null) {
                callback!!.move(viewChanges, translationX)
            }
            viewChanges!!.translationX = translationX
        }
    }

    private fun taskTransition() {
        val thread: Thread = object : Thread() {
            override fun run() {
                super.run()
                try {
                    sleep(1)
                    Handler(Looper.getMainLooper()).post {
                        if (scale < width && scale > -width) {
                            if (scale > 0) scale += 100f else scale -= 100f
                            move(scale)
                            taskTransition()
                        } else {
                            if (viewChanges != null) {
                                end(viewChanges!!)
                            }
                            move(0f)
                            histori = 0f
                            viewChanges = null
                        }
                    }
                } catch (e1: InterruptedException) {
                    e1.printStackTrace()
                }
            }
        }
        thread.start()
    }

    private fun isContain(ev: MotionEvent) {
        for (view in views!!) {
            val left = view.x.toInt()
            val top = view.y.toInt()
            val right = view.x.toInt() + view.width
            val bottom = view.y.toInt() + view.height
            if (left < ev.x && ev.x < right && top < ev.y && ev.y < bottom) {
                viewChanges = view
                return
            }
        }
    }

    fun setCallback(callback: SwipeCallback?) {
        this.callback = callback
    }

    fun setStartTransitionX(transitionX: Float) {
        startTransitionX = transitionX
    }

    fun addView(view: View) {
        if (views == null) {
            views = ArrayList()
        }
        views!!.add(view)
    }

    fun setLayout(layout: RelativeLayout) {
        layout.setOnTouchListener { view, motionEvent ->
            touchMotionEvent(motionEvent)
            true
        }
    }

    companion object {
        private const val TAG = "Swipe_tag"
    }
}