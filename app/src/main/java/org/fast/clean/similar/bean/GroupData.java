package org.fast.clean.similar.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fushenghua on 2017/3/2.
 */

public class GroupData {

    public List<PhotoData> photos;

    public String title;

    public int groupId;

    public List<PhotoData> getPhotos() {
        return photos;
    }

    public List<PhotoData> getCheckList() {
        List<PhotoData> tmpList = new ArrayList<>();
        if (photos == null || photos.isEmpty()) return null;
        for (PhotoData photoFile : photos) {
            if (photoFile.isSelected) tmpList.add(photoFile);
        }
        return tmpList;
    }

    public void setPhotos(List<PhotoData> photos) {
        this.photos = photos;
    }
}
