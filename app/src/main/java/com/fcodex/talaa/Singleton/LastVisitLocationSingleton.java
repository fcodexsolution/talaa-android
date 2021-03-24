package com.fcodex.talaa.Singleton;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.fcodex.talaa.Modal.Modal;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class LastVisitLocationSingleton extends Application {
    @SuppressLint("StaticFieldLeak")
    private  static Context mContext;
    @Getter @Setter private int city_id = -1;

    public List<Modal> getmRecentClicks() {
        return mRecentClicks;
    }

    public void setmRecentClicks(List<Modal> mRecentClicks) {
        this.mRecentClicks = mRecentClicks;
    }

    private  List<Modal> mRecentClicks;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mRecentClicks = new ArrayList<>();
    }

    public  static @NonNull
    LastVisitLocationSingleton instance(){
        return (LastVisitLocationSingleton)mContext;
    }

}
