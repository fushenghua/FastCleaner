package org.fast.clean.similar.core;

import android.content.Context;

import org.fast.clean.similar.algorithm.ImageAlgorithm;
import org.fast.clean.similar.algorithm.AHashAlgorithm;
import org.fast.clean.similar.bean.GroupData;

import java.util.List;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class SimilarPhotosCleaner {

    public static String TAG = SimilarPhotosCleaner.class.getSimpleName();

    private SimilarCallback mSimilarPhotoCallback;


    public void setSimilarPhotoCallback(SimilarCallback mSimilarPhotoCallback) {
        this.mSimilarPhotoCallback = mSimilarPhotoCallback;
    }

    public ImageAlgorithm getAlgorithm() {
        return new AHashAlgorithm();
    }

    private Context mContext;

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

    public void onScanEnd(List<GroupData> groupDatas) {
        if (mSimilarPhotoCallback != null) {
            mSimilarPhotoCallback.onScanEnd(groupDatas);
        }
    }

    public void onFoundItem(int index, int count) {
        if (mSimilarPhotoCallback != null) {
            mSimilarPhotoCallback.onFoundItem(index, count);
        }
    }

    public interface SimilarCallback {
        void onFoundItem(int i, int count);

        void onScanEnd(List<GroupData> result);
    }

    public void start() {
        SimilarPicTask similarTask = new SimilarPicTask(this);
        similarTask.execute();
    }
}
