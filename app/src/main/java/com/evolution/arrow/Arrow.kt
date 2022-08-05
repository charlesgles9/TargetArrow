package com.evolution.arrow

import com.graphics.glcanvas.engine.structures.RectF
import kotlin.math.cos
import kotlin.math.sin

class Arrow():RectF(0f,0f,20f,5f) {

    private var pAngle=0f

    fun setAngle(angle:Float){
        pAngle=angle
    }

    fun start(x:Float,y:Float){
        set(x,y)
    }

    override fun update(delta: Long) {
        super.update(delta)
        val radians=(pAngle+90f)*(Math.PI/180f).toFloat()
        setRotationZ(pAngle+90f)
        set(getX()+ sin(radians) *20f,getY()+ cos(radians) *20f)
    }
}