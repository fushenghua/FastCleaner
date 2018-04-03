package org.fast.clean.similar.algorithm;

import android.graphics.Bitmap;

import org.fast.clean.similar.bean.PhotoData;

/**
 * Created by fushenghua on 2017/3/2.
 */

public interface ImageAlgorithm {

    String createFinger(Bitmap source);

    boolean hasSimilar(PhotoData des, PhotoData target);

}
