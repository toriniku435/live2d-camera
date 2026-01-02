package com.live2d.camera

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.live2d.camera.infrastructure.cubism.GLRenderer
import com.live2d.camera.infrastructure.cubism.JniBridgeJava


class MainActivity : ComponentActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var glRenderer: GLRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        JniBridgeJava.SetActivityInstance(this)
        JniBridgeJava.SetContext(this)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glRenderer = GLRenderer()
        glSurfaceView.setRenderer(glRenderer)
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
        setContentView(glSurfaceView)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(
                WindowInsetsCompat.Type.systemBars()
            )
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onStart() {
        super.onStart()
        JniBridgeJava.nativeOnStart()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
        JniBridgeJava.nativeOnPause()
    }

    override fun onStop() {
        super.onStop()
        JniBridgeJava.nativeOnStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        JniBridgeJava.nativeOnDestroy()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointX = event.getX()
        val pointY = event.getY()

        // GLSurfaceViewのイベント処理キューにタッチイベントを追加する。
        glSurfaceView.queueEvent(
            object : Runnable {
                override fun run() {
                    when (event.getAction()) {
                        MotionEvent.ACTION_DOWN -> JniBridgeJava.nativeOnTouchesBegan(
                            pointX,
                            pointY
                        )

                        MotionEvent.ACTION_UP -> JniBridgeJava.nativeOnTouchesEnded(pointX, pointY)
                        MotionEvent.ACTION_MOVE -> JniBridgeJava.nativeOnTouchesMoved(
                            pointX,
                            pointY
                        )
                    }
                }
            }
        )
        return super.onTouchEvent(event)
    }

}