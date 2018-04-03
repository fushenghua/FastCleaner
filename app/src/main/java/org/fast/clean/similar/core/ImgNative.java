package org.fast.clean.similar.core;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class ImgNative {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
    public static native String createFingerBypHash(int[] buf);

    public static native String createFingerByaHash(int[] buf);

}
