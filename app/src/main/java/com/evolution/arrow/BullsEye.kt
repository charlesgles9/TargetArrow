package com.evolution.arrow

import com.graphics.glcanvas.engine.Batch
import com.graphics.glcanvas.engine.Update
import com.graphics.glcanvas.engine.maths.ColorRGBA
import com.graphics.glcanvas.engine.maths.Vector2f
import com.graphics.glcanvas.engine.structures.RectF
import kotlin.random.Random

class BullsEye(private val vector:Vector2f,private val width:Float,private val height:Float):Update {


    private val blocks= mutableListOf(RectF(0f,0f,20f,25f))
    private val timer=Timer(3000L)
    var velocity=5f
      init {

          val color=ColorRGBA(ColorRGBA.red)

          blocks[0].setColor(color)

          timer.setCallback(object :Timer.Update{
                 override fun tick(){
                     randomY()
                 }
          })

      }

     fun randomY(){

        vector.y+=velocity
        vector.x=width*0.5f+ Random.nextFloat()*(width/2-50f)
        vector.y= Random.nextFloat()*(height-80f)
        if(vector.y<80f)
            vector.y=90f
    }

    private fun initPosition(){
        blocks[0].set(vector.x,vector.y)
    }

    override fun draw(batch: Batch) {
      blocks.forEach { batch.draw(it) }

    }

    fun getBlocks():MutableList<RectF>{
        return blocks
    }

    override fun update(delta: Long) {
       initPosition()
        timer.update(delta)
    }

}