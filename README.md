# FastCleaner

* Similar Photos in the Android
* Junk Clean

![](https://raw.githubusercontent.com/fushenghua/FastCleaner/d6b3c6c1f813853e8ea576a5a1f4b9e5a62ef41f/img/fastcleanerPhone.png)

#### 示例:

![](https://raw.githubusercontent.com/fushenghua/FastCleaner/d6b3c6c1f813853e8ea576a5a1f4b9e5a62ef41f/img/fastcleaner.jpg)

### 特性

 * 相似图片清理
 * 垃圾清理
 * 清晰图分析
 
### 原理说明



### 下载安装

### 使用方法


#### similar photos

``` java
 private void initData() {
        mSimilarPhotosCleaner = new SimilarPhotosCleaner();
        mSimilarPhotosCleaner.setContext(this);
        mSimilarPhotosCleaner.setSimilarPhotoCallback(new SimilarPhotosCleaner.SimilarCallback() {
            @Override
            public void onFoundItem(int index, int count) {
                mTvProgress.setText(String.format(getResources().getString(R.string.similar_photos_search_txt), index, count));
            }

            @Override
            public void onScanEnd(List<GroupData> result) {
                Log.d(SimilarPhotosCleaner.TAG, " GroupDatas size" + result.size());
            }
        });
        mSimilarPhotosCleaner.start();
    }
```

### 注意事项

### TODO

 * [x]相似图片清理
 * [ ] 清晰图识别
 * [ ] 内存加速
 * [ ] 垃圾清理

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


```
MIT License

Copyright (c) 2017 fushenghua

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


