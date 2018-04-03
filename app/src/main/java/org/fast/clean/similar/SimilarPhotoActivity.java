package org.fast.clean.similar;

import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.fast.clean.R;
import org.fast.clean.similar.adapter.SimilarPicAdapter;
import org.fast.clean.similar.bean.GroupData;
import org.fast.clean.similar.bean.PhotoData;
import org.fast.clean.similar.core.SimilarPhotosCleaner;

import java.util.List;

public class SimilarPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY_COLUMM_COUNT = 4;

    SimilarPhotosCleaner mSimilarPhotosCleaner;

    private TextView mTvProgress;

    private RecyclerView mRecyclerView;

    private SimilarPicAdapter mAdapter;

    private Button mBtnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_similar_photo);

        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mBtnDelete = (Button) findViewById(R.id.btn_delete);
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);
        mBtnDelete.setOnClickListener(this);

        initData();
    }

    private void initData() {
        mSimilarPhotosCleaner = new SimilarPhotosCleaner();
        mSimilarPhotosCleaner.setContext(this);
        mSimilarPhotosCleaner.setSimilarPhotoCallback(new SimilarPhotosCleaner.SimilarCallback() {
            @Override
            public void onFoundItem(int i, int count) {
//                Log.d(SimilarPhotosCleaner.TAG, " i=" + i + "count=" + count);
                mTvProgress.setText(String.format(getResources().getString(R.string.similar_photos_search_txt), i, count));
            }

            @Override
            public void onScanEnd(List<GroupData> result) {
                Log.d(SimilarPhotosCleaner.TAG, " GroupDatas size" + result.size());

                if (result.isEmpty()) {
                    mTvProgress.setText("无相似图片");
                    return;
                }
                mTvProgress.setVisibility(View.GONE);
                setShowScanResult(result);
            }
        });
        mSimilarPhotosCleaner.start();
        mTvProgress.setVisibility(View.VISIBLE);
    }


    private void setShowScanResult(List<GroupData> result) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GALLERY_COLUMM_COUNT, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.mGroupData != null) {
                    if (mAdapter.getItemViewType(position) == SimilarPicAdapter.VIEW_TYPE_SECTION) {
                        return GALLERY_COLUMM_COUNT;
                    }
                }
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new SimilarPicAdapter(this);
        mAdapter.setGroupDatas(result);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SimilarPicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoData photoFile = mAdapter.getChildForPosition(position);
                if (photoFile == null) return;
            }

            @Override
            public void onItemSelected(View view, int position) {
                mAdapter.toggleSelect(position);
                refreshButton();
            }
        });
        refreshButton();
    }

    private void refreshButton() {
        mBtnDelete.setVisibility(View.VISIBLE);
        mBtnDelete.setText(String.format(getResources().getString(R.string.similar_photos_btn_delete), mAdapter.getItemCount()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                break;
            default:
                break;
        }
    }
}
