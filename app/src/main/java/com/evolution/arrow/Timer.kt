package com.evolution.arrow

import com.graphics.glcanvas.engine.Batch
import com.graphics.glcanvas.engine.structures.Text
import com.graphics.glcanvas.engine.utils.FpsCounter

class Timer(private val delay:Long) {
        private val instance= FpsCounter()
        private var pt=0L
        private var counter=0L
        private var callback:Update?=null


    fun setCallback(callback:Update){
        this.callback=callback
    }

    fun update(time:Long){
        val delta=pt+ delay
        if(delta<=time){
            callback?.tick()
            pt=time
            counter=0
        }else {
            counter++
        }

    }

    interface Update{
        fun tick();
    }
}