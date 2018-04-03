package org.fast.clean.similar.algorithm;

import android.graphics.Bitmap;

/**
 * Created by fushenghua on 2017/3/2.
 */

public interface ImageAlgorithm {

    String createFinger(Bitmap source);

    boolean hasSimilar(String desValue, String targetValue);

}
