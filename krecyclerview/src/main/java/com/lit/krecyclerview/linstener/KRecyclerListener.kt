package com.lit.krecyclerview.linstener

import android.view.MotionEvent

interface KRecyclerListener {

    fun onTouch(event : MotionEvent, deltaY : Float) : Boolean

    fun onUp(event: MotionEvent) :Boolean
}