package com.evolution.arrow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.graphics.glcanvas.engine.GLCanvasSurfaceView

class MainActivity : AppCompatActivity() {

    private var renderer:Renderer?=null
    private var surface:GLCanvasSurfaceView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderer= Renderer(this,1024f,720f)
        surface=GLCanvasSurfaceView(this,renderer!!)
        setContentView(surface)
      
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        surface?.onRelease()
    }
}