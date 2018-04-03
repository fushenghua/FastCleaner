LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#OPENCV_CAMERA_MODULES:=on
#OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=STATIC

#OPENCV_LIB_TYPE := STATIC

#OPENCV_INSTALL_MODULES:=off
#OPENCV_LIB_TYPE:=SHARED
ifdef OPENCV_ANDROID_SDK
  ifneq ("","$(wildcard $(OPENCV_ANDROID_SDK)/OpenCV.mk)")
    include ${OPENCV_ANDROID_SDK}/OpenCV.mk
  else
    include ${OPENCV_ANDROID_SDK}/sdk/native/jni/OpenCV.mk
  endif
else
  #include /home/silver/android/AndroidStudioOpenSource/OpenCVDemo/sdk/native/jni/OpenCV.mk
#include F:/AndroidStudioProjects/OpenCVDemo/sdk/native/jni/OpenCV.mk

endif

# opencv
#include /home/silver/android/AndroidStudioOpenSource/OpenCVDemo/sdk/native/jni/OpenCV.mk


LOCAL_SRC_FILES   := native-lib.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl
LOCAL_LDFLAGS += -ljnigraphics

LOCAL_MODULE     := native

include $(BUILD_SHARED_LIBRARY)