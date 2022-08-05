package com.evolution.arrow

import com.evolution.arrow.ai.NeuralNetwork
import com.graphics.glcanvas.engine.Batch
import com.graphics.glcanvas.engine.maths.AxisABB
import com.graphics.glcanvas.engine.maths.ColorRGBA
import com.graphics.glcanvas.engine.maths.Vector2f
import com.graphics.glcanvas.engine.structures.Line
import com.graphics.glcanvas.engine.structures.RectF
import kotlin.math.*

class Soldier(x:Float,y:Float, width:Float,height:Float,val srcW:Float,val srcH:Float,val bullsEye: BullsEye,val walls:MutableList<RectF>):RectF(x,y,width,height) {


    private val ray=Line(x,y,width,y)
    private val arrows= MutableList(1,init = {Arrow()})
    // outputs-> angle, shoot
     val network=NeuralNetwork(3,8,1)
    private val axis=AxisABB()
    var score=0f
    var active=true
    var rayDir=0.0f
    init {
        arrows.forEach { arrow->arrow.setVisibility(false) }


    }
    fun draw(batch: Batch){
         batch.draw(ray)
         ray.setColor(ColorRGBA.red)
         batch.draw(this)
        arrows.forEach { arrow->batch.draw(arrow) }
    }


    fun getRay():Line{
        return ray
    }


    private fun activateArrow(){

        if(active)
        for(arrow in arrows){
            if(!arrow.getVisibility()){
                arrow.set(getX(),getY())
                arrow.setAngle(getRotationZ())
                arrow.setVisibility(true)
                break
            }
        }
    }

    fun getArrows():MutableList<Arrow>{
        return arrows
    }



    private fun moveRay(direction:Int){
        rayDir = if(direction==0){
            -1f
        }else
            1f

    }


    fun createChild():Soldier{
        val child= Soldier(100f,getY(),80f,5f,srcW,srcH ,bullsEye,walls)
            child.network.copy(network)
            child.setRotationZ(getRotationZ())

        return child
    }

    override fun update(delta:Long){
         super.update(delta)
        val radians=(getRotationZ()+90f)*(Math.PI/180f).toFloat()
        ray.set(getX(),getY(),getX()+srcW* sin(radians),getY()+srcW* cos(radians))
        arrows.forEach {
            if(it.getVisibility()){
                it.update(delta)
            }
        }

        //arrows to wall collision detection
        for (arrow in arrows){
            for(wall in walls){
                if(axis.isIntersecting(arrow,wall)){
                    arrow.setVisibility(false)
                    arrow.start(getX(),getY())
                    active=false
                    break
                }
            }
        }

        //arrow to bullsEye collision
        for(arrow in arrows){
            for(eye in bullsEye.getBlocks()){
                if(axis.isIntersecting(arrow,eye)&&arrow.getVisibility()){
                    arrow.setVisibility(false)
                    arrow.start(getX(),getY())
                    score+=5
                    break
                }
            }
        }

        //difference between ray position and first target
        val target= bullsEye.getBlocks()[0]
        val y=ray.getStopY().toDouble()
        val x=ray.getStopX().toDouble()
        val d=y-target.getY()
        val a1= (90f-atan2(target.getY()-getY(),target.getX()-getX())*180.0/Math.PI)
        val a2=(getRotationZ()+90f).toDouble()
        val a3=(a1-a2)

       val output= network.predict(mutableListOf(a3,a1,a2))



       //pick highest priority
        when (if(output[0]>=0.5) 1 else 0) {
            0 -> {
              moveRay(0)
            }
            1 -> {
              moveRay(1)
            }

        }

        activateArrow()
        setRotationZ(getRotationZ()+rayDir)
        setRotationZ(min(getRotationZ(),60f))
        setRotationZ(max(getRotationZ(),-60f))
    }


}