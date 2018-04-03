#include <jni.h>
#include <string>
#include <sstream>
#include <cmath>


using namespace std;

#define   LENGTH(s)   (sizeof(s)   /   sizeof(int))

extern "C" {

JNIEXPORT jstring JNICALL
Java_org_fast_clean_similar_core_ImgNative_createFingerBypHash(JNIEnv *env, jclass type,
                                                               jintArray buf_) {
    jint *buf = env->GetIntArrayElements(buf_, NULL);

    // TODO

    env->ReleaseIntArrayElements(buf_, buf, 0);

    return env->NewStringUTF("hello");
}


/**
     * 计算数组的平均值
     *
     * @param pixels 数组
     * @return int 平均值
     */
static int average(int pixels[]) {
    float m = 0;
    for (int i = 0; i < LENGTH(pixels); ++i) {
        m += pixels[i];
    }
    m = m / LENGTH(pixels);
    return (int) m;
}

static int rgbToGray(int pixels) {
    int _red = (pixels >> 16) & 0xFF;
    int _green = (pixels >> 8) & 0xFF;
    int _blue = (pixels) & 0xFF;
    return (int) (0.3 * _red + 0.59 * _green + 0.11 * _blue);
}

static char binaryToHex(int binary) {
    char ch = ' ';
    switch (binary) {
        case 0:
            ch = '0';
            break;
        case 1:
            ch = '1';
            break;
        case 2:
            ch = '2';
            break;
        case 3:
            ch = '3';
            break;
        case 4:
            ch = '4';
            break;
        case 5:
            ch = '5';
            break;
        case 6:
            ch = '6';
            break;
        case 7:
            ch = '7';
            break;
        case 8:
            ch = '8';
            break;
        case 9:
            ch = '9';
            break;
        case 10:
            ch = 'a';
            break;
        case 11:
            ch = 'b';
            break;
        case 12:
            ch = 'c';
            break;
        case 13:
            ch = 'd';
            break;
        case 14:
            ch = 'e';
            break;
        case 15:
            ch = 'f';
            break;
        default:
            ch = ' ';
    }
    return ch;
}

string clacuateFingerUseAverage(jint *cbuf) {
    const int width = 32;
    const int height = 32;
    int len;
    string rst;

    // 第二步，简化色彩。
    int pixels[width * height];
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            pixels[i * width + j] = rgbToGray(cbuf[i, j]);
        }
    }

    // 第三步，计算平均值。
    int avgPixel = average(pixels);
    // 第四步，比较像素的灰度。
    int comps[width * height];
    int compsSize = LENGTH(pixels);
    for (int i = 0; i < compsSize; i++) {
        if (pixels[i] >= avgPixel) {
            comps[i] = 1;
        } else {
            comps[i] = 0;
        }
    }
    // 第五步，计算哈希值
    int cSize = LENGTH(comps);
    for (int i = 0; i < cSize; i += 4) {
        string s;
        int result = comps[i] * (int) pow(2, 3)
                     + comps[i + 1] * (int) pow(2, 2)
                     + comps[i + 2] * (int) pow(2, 1)
                     + comps[i + 3];
        rst += binaryToHex(result);
    }
    ostringstream oss;
    oss << "-" << avgPixel;
    rst.append(oss.str());
    return rst;
}


JNIEXPORT jstring JNICALL
Java_org_fast_clean_similar_core_ImgNative_createFingerByaHash(JNIEnv *env, jclass type,
                                                               jintArray buf_) {

    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf_, 0);
    if (cbuf == NULL) {
        return 0; /* exception occurred */
    }

    string result = clacuateFingerUseAverage(cbuf);

    env->ReleaseIntArrayElements(buf_, cbuf, 0);


    return env->NewStringUTF(result.data());
}
}