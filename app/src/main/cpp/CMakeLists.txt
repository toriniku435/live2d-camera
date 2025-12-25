cmake_minimum_required(VERSION 3.10)
project(live2d-camera LANGUAGES C CXX)

# Set app name.
set(APP_NAME live2d-camera)
# Specify version of compiler.
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_subdirectory(modules/cubism)

# Make library for android app (JNI bridge)
add_library(${APP_NAME} SHARED
        bridge/AppJniBridge.cpp
)

if (TARGET CubismModule)
    message(STATUS "CubismModule is a TARGET")
else()
    message(FATAL_ERROR "CubismModule is NOT a target (will become -lLive2DCubismCore)")
endif()

# Link libraries to app.
target_link_libraries(${APP_NAME}
        CubismModule
        GLESv2
        log
)

# Android 16KB page-size option(NDK r26 and lower)
# https://developer.android.com/guide/practices/page-sizes?hl=en#compile-16-kb-alignment
target_link_options(${APP_NAME} PRIVATE "-Wl,-z,max-page-size=16384")