package org.fast.clean.similar.algorithm;

import android.graphics.Bitmap;
import android.text.TextUtils;

import org.fast.clean.similar.core.ImgNative;
import org.fast.clean.similar.utils.BitmapUtils;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class AHashAlgorithm implements ImageAlgorithm {


    @Override
    public String createFinger(Bitmap source) {
        Bitmap thumb = BitmapUtils.createThumbnail(source);
        int w = thumb.getWidth(), h = thumb.getHeight();
        int[] pix = new int[w * h];
        source.getPixels(pix, 0, w, 0, 0, w, h);
        String result = ImgNative.createFingerByaHash(pix);
        return result;
    }

    @Override
    public boolean hasSimilar(String desValue, String targetValue) {
        if (TextUtils.isEmpty(desValue) || TextUtils.isEmpty(targetValue)) return false;
        return hammingDistance(desValue, targetValue) <= 10;
    }

    /**
     * 计算"汉明距离"（Hamming distance）。 如果不相同的数据位不超过5，就说明两张图片很相似；如果大于10，就说明这是两张不同的图片。
     */
    public static int hammingDistance(String desValue, String targetValue) {
        int difference = 0;
        final int len = desValue.length();
        final int hlen = targetValue.length();
        int size = 0;
        if (len >= hlen) {
            size = hlen;
        }
        for (int i = 0; i < size; i++) {
            if (desValue.charAt(i) != targetValue.charAt(i)) {
                difference++;
            }
        }
        return difference;
    }

}
