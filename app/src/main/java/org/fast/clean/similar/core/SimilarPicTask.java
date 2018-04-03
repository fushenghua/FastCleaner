package org.fast.clean.similar.core;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.fast.clean.similar.algorithm.ImageAlgorithm;
import org.fast.clean.similar.bean.GroupData;
import org.fast.clean.similar.bean.PhotoData;
import org.fast.clean.similar.utils.BitmapUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class SimilarPicTask extends AsyncTask<Void, Integer, List<GroupData>> {


    private static final String TAG = SimilarPicTask.class.getSimpleName();

    private final List<GroupData> mGroupList = new ArrayList<>();

    private WeakReference<SimilarPhotosCleaner> mDiffpicWeak;

    public SimilarPicTask(SimilarPhotosCleaner similarPhotoCleaner) {
        mDiffpicWeak = new WeakReference<SimilarPhotosCleaner>(similarPhotoCleaner);
    }

    @Override
    protected List<GroupData> doInBackground(Void... params) {
        List listPhotos = listPhotos();
        diffSimilarPic(listPhotos);
        return mGroupList;
    }

    @Override
    protected void onPostExecute(List<GroupData> groupDatas) {
        super.onPostExecute(groupDatas);
        if (mDiffpicWeak.get() != null) {
            mDiffpicWeak.get().onScanEnd(groupDatas);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mDiffpicWeak.get() != null) {
            mDiffpicWeak.get().onFoundItem(values[0], values[1]);
        }
    }

    public List listPhotos() {
        final List<PhotoData> mPhotoFiles = new ArrayList<>();

        Cursor cursor = null;
        try {
            ImageAlgorithm imgAlgorithm = mDiffpicWeak.get().getAlgorithm();
            Context mContext = mDiffpicWeak.get().getContext();
            if (mContext != null) {
                String[] selectionArgs = new String[]{"image/jpeg", "image/png"};
                String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC ";

                cursor = mContext.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", selectionArgs, sortOrder);
                int index = 0;

                if (null != cursor && cursor.moveToFirst()) {

                    int count = cursor.getCount();
                    publishProgress(0, count);
                    index++;

                    do {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                        long lastModified = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));


                        PhotoData photoFile = new PhotoData();
                        photoFile.filePath = path;
                        photoFile.id = id;
                        photoFile.mSize = size;
                        photoFile.lastModified = lastModified;

                        if (TextUtils.isEmpty(photoFile.hmValue)) {
                            createFinger(photoFile, mContext, photoFile.id, imgAlgorithm);
                        }

                        postItem(mPhotoFiles, photoFile, index++, count);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }

        return mPhotoFiles;
    }

    private void diffSimilarPic(List<PhotoData> mAllSourcePic) {
        int i = 0;
        int k = 1;
        List<PhotoData> similarItem = new ArrayList<>();
        GroupData groupData = new GroupData();
        int size = mAllSourcePic.size();

        ImageAlgorithm imgAlgorithm = mDiffpicWeak.get().getAlgorithm();
        Context mContext = mDiffpicWeak.get().getContext();
        while (k < size) {

            PhotoData pointFile = mAllSourcePic.get(i);
            PhotoData currFile = mAllSourcePic.get(k);
            Log.i(TAG, "pointFile" + pointFile.filePath + "i =" + i);

            if (TextUtils.isEmpty(pointFile.hmValue)) {
                createFinger(pointFile, mContext, pointFile.id, imgAlgorithm);
            }

            if (TextUtils.isEmpty(currFile.hmValue)) {
                createFinger(currFile, mContext, currFile.id, imgAlgorithm);
            }

            boolean featureDistance = imgAlgorithm.hasSimilar(pointFile, currFile);
            Log.i(TAG, "featureDistance =" + featureDistance + "  " + currFile.filePath + " and " + pointFile.filePath);
            if (featureDistance) {
                if (similarItem.indexOf(pointFile) < 0) {
                    similarItem.add(pointFile);
                }
                if (similarItem.indexOf(currFile) < 0) {
                    similarItem.add(currFile);
                }
            } else {
                if (similarItem.size() > 1) {
                    groupData.setPhotos(similarItem);
                    addGroup(groupData);
                }
                similarItem = new ArrayList<>();
                groupData = new GroupData();
            }

            i++;
            k++;
        }
    }

    private void createFinger(PhotoData photoData, Context context, long id, ImageAlgorithm imgAlgorithm) {
        long start = System.currentTimeMillis();
        String finger = imgAlgorithm.createFinger(loadBitmap(context, photoData.id));
        photoData.setHmAvgValue(finger);
        Log.i(TAG, "cvtHash time=" + (System.currentTimeMillis() - start));
    }

    private Bitmap loadBitmap(Context context, long id) {
        return BitmapUtils.loadBitmapFromFile(context, id);
    }

    private void addGroup(GroupData groupData) {
        mGroupList.add(groupData);
    }

    private void postItem(List<PhotoData> mPhotoFiles, PhotoData photo, int index, int count) {
        if (canHandler(photo.filePath)) {
            mPhotoFiles.add(photo);
        }
        publishProgress(index, count);
    }

    //过滤部分路径
    private boolean canHandler(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (path.contains("/DCIM/100MEDIA") || path.contains("/DCIM/Camera/")
                    || path.contains("DCIM/100Andro") || path.contains("相机") || path.contains("DCIM")) {
                return true;
            }
        }
        return false;
    }
}
