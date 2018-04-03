package org.fast.clean.similar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

/**
 * Created by fushenghua on 2018/4/2.
 */

public class BitmapUtils {

    public static int mBitmapWidth = 32;

    public static Bitmap createThumbnail(Bitmap source) {
        return Bitmap.createScaledBitmap(source, mBitmapWidth, mBitmapWidth, false);
    }

    public static Bitmap loadBitmapFromFile(Context context, long mId) {
        return MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(), mId,
                MediaStore.Images.Thumbnails.MICRO_KIND, null);
    }
}
