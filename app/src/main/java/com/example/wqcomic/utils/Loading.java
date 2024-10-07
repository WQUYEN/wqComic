package com.example.wqcomic.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.wqcomic.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class Loading {
    private final FrameLayout loadingLayout;
    private final ProgressBar progressBar;

    public Loading(LayoutInflater inflater, ViewGroup container) {
        // Inflate the loading layout
        loadingLayout = (FrameLayout) inflater.inflate(R.layout.loading, container, false);
        // Lấy tham chiếu đến ProgressBar
        progressBar = loadingLayout.findViewById(R.id.progressBar);
    }

    public void show() {
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    public void hide() {
        loadingLayout.setVisibility(View.GONE);
    }

    public View getView() {
        return loadingLayout;
    }
}