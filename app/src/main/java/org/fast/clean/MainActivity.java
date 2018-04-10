package org.fast.clean;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.fast.clean.about.AboutActivity;
import org.fast.clean.bean.SDCardInfo;
import org.fast.clean.bean.StorageUtil;
import org.fast.clean.similar.SimilarPhotoActivity;
import org.fast.clean.utils.AppUtil;
import org.fast.clean.view.circleprogress.ArcProgress;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArcProgress mArcStore;

    private ArcProgress mArcProcess;

    private TextView mCapacity;

    private TextView mTvJunk;

    private TextView mTvPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (Build.VERSION.SDK_INT >= 21) {
//            getSupportActionBar().setElevation(0);
//        }

        this.initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    protected void onResume() {
        super.onResume();

        this.initData();
    }

    private void initView() {
        this.initMainView();
        this.initDrawLayout();
    }

    private void initMainView() {
        mArcStore = (ArcProgress) findViewById(R.id.arc_store);
        mArcProcess = (ArcProgress) findViewById(R.id.arc_process);
        mCapacity = (TextView) findViewById(R.id.capacity);
        mTvJunk = (TextView) findViewById(R.id.tv_junk);
        mTvPhoto = (TextView) findViewById(R.id.tv_photo);

        mTvPhoto.setOnClickListener(this);
        mTvJunk.setOnClickListener(this);
    }

    private void initDrawLayout() {
        findViewById(R.id.tv_about).setOnClickListener(this);
        Drawable info = getResources().getDrawable(R.drawable.ic_info_black_24dp);
        DrawableCompat.setTint(info, getResources().getColor(R.color.bg_card));
        info.setBounds(0, 0, info.getMinimumWidth(), info.getMinimumHeight());

//        Drawable logoDrawable = getResources().getDrawable(R.drawable.icon_main_left_logo);
//        DrawableCompat.setTint(logoDrawable, getResources().getColor(R.color.bg_card));
//        logoDrawable.setBounds(0, 0, logoDrawable.getMinimumWidth(), logoDrawable.getMinimumHeight());
        ((TextView) findViewById(R.id.tv_about)).setCompoundDrawables(info, null, null, null);
//        ((TextView) findViewById(R.id.tv_logo)).setCompoundDrawables(null, logoDrawable, null, null);
    }

    private void initData() {
        long l = AppUtil.getAvailMemory(this);
        long y = AppUtil.getTotalMemory(this);
        final double x = (((y - l) / (double) y) * 100);
        mArcProcess.setProgress((int) x);

//        mArcProcess.setProgress(0);


        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(this);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);

        mCapacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) + "/" + StorageUtil.convertStorage(TotalBlocks));
        mArcStore.setProgress((int) percentStore);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_junk:
                break;
            case R.id.tv_photo:
                startActivity(new Intent(this, SimilarPhotoActivity.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }
}
