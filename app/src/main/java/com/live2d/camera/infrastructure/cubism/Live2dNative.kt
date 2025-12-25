package com.live2d.camera.infrastructure.cubism

class Live2DNative {
    external fun ping(): Int

    companion object {
        init {
            System.loadLibrary("live2d-camera")
        }
    }
}