package org.fast.clean.similar.bean;

import android.text.TextUtils;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class PhotoData {

    public String hmValue;

    public String filePath;

    public String group;

    public double blurValue;

    public boolean isSelected;

    public long mSize = 0L;

    public boolean isMayShoot = false;

    public int avgPixel;

    public long lastModified;

    public long id;

    public void setHmAvgValue(String result) {
        if (!TextUtils.isEmpty(result)) {
            String[] array = result.split("-");
            this.hmValue = array[0];
            if (array.length > 1 && array[1] != null) {
                this.avgPixel = Integer.valueOf(array[1]);
            }
        }
    }
}
