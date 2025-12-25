#include <jni.h>

extern "C" JNIEXPORT jint JNICALL
Java_com_live2d_camera_infrastructure_cubism_Live2DNative_ping(JNIEnv* env, jobject thiz) {
    return 1;
}

