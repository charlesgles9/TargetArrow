package com.evolution.arrow

import android.content.Context
import android.opengl.GLES32
import com.evolution.arrow.ai.NeuralNetwork
import com.graphics.glcanvas.engine.Batch
import com.graphics.glcanvas.engine.Camera2D
import com.graphics.glcanvas.engine.GLRendererView
import com.graphics.glcanvas.engine.maths.Vector2f
import com.graphics.glcanvas.engine.structures.RectF
import com.graphics.glcanvas.engine.utils.TextureLoader
import kotlin.math.min
import kotlin.random.Random

class Renderer(private val context: Context,private val width:Float,private val height:Float) :GLRendererView(width, height){

    private val camera=Camera2D(10f)
    private val batch=Batch()
    private val bullsEye=BullsEye(Vector2f(),width,height)
    private val soldiers= mutableListOf<Soldier>()
    private val inactive= mutableListOf<Soldier>()
    private val walls= mutableListOf<RectF>()
    private val population=250
    private var best:Soldier?=null
    private var selectBest=true
    private var timer=Timer(10000L)
    override fun prepare() {
        batch.initShader(context)
        camera.setOrtho(width, height)
        for(i in 0 until population)
        soldiers.add(Soldier(300f,height*0.5f,80f,5f,width,height,bullsEye,walls))
        walls.add(RectF(width*0.5f,20f,width,40f))
        walls.add(RectF(width*0.5f,height-20f,width,40f))
        walls.add(RectF(width,height*0.5f,30f,height))
        walls.add(RectF(250f,height*0.5f,30f,height))
        timer.setCallback(object:Timer.Update{
            override fun tick() {
              selectBest=true
            }
        })

    }


    private fun resetBest(){
        if(soldiers.isNotEmpty()) {
            best = soldiers[soldiers.size - 1]
            best?.network?.start?.set(10f, height * 0.5f)
        }
    }

    override fun draw() {
       GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)
       GLES32.glClearColor(0.2f,0.2f,0.2f,1f)
        batch.begin(camera)

       // soldiers.forEach { it.draw(batch) }
        best?.draw(batch)
        best?.network?.initGraphics()
        best?.network?.draw(batch)
        walls.forEach {
            batch.draw(it)
        }
        bullsEye.draw(batch)
        batch.end()


    }


    private fun evolution(delta: Long){
        val cycle=1
        for( c in 0 until cycle){

            soldiers.sortBy { it.score }

            if(selectBest){
                resetBest()
                selectBest=false
            }
            for(soldier in soldiers){
                if(!soldier.active&&!inactive.contains(soldier))
                    inactive.add(soldier)
            }

           soldiers.removeAll { !it.active }
            // start a new generation is all soldiers are dead
            if(soldiers.isEmpty()) {
                inactive.sortBy { it.score }
                val children = mutableListOf<Soldier>()
                for (i in 0 until population / 2) {
                    //selection
                    val parent = inactive[min(
                        population / 2 + Random.nextInt(population / 2),
                        population - 1
                    )]
                    //cross over
                    val child = parent.createChild()
                    //mutation
                    NeuralNetwork.mutate(child.network, 1f)
                   children.add(child)

                }
                soldiers.addAll(inactive)
                inactive.clear()
                for (i in 0 until children.size) {
                    soldiers[i] = children[i]
                }
                soldiers.forEach { it.active=true }
             //   println(soldiers[0].score)
             //   println(soldiers[soldiers.size-1].score)
            }

            soldiers.forEach {
                it.update(delta)
            }
            bullsEye.update(delta)
        }


    }

    override fun update(delta: Long) {
        evolution(delta)
       timer.update(delta)
    }

    override fun onRelease() {
        super.onRelease()
        batch.cleanUp()
        TextureLoader.getInstance().clearTextures()

    }


}