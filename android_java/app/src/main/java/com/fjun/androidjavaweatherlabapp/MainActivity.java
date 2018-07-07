package com.fjun.androidjavaweatherlabapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainActivityViewBinder {

    @Inject
    MainActivityPresenter mPresenter;

    private TextView mCurrentLocation;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentLocation = findViewById(R.id.current_location);
        mImageView = findViewById(R.id.icon);

        // Setup presenter: Attach lifecycle
        getLifecycle().addObserver(mPresenter);
    }

    @Override
    public void setTemperature(String temperature) {
        mCurrentLocation.setText(getString(R.string.current_location, temperature));
    }

    @Override
    public void setImageUrl(String imageUrl) {
        Picasso picasso = Picasso.get();
        picasso.load(imageUrl).into(mImageView);
    }

}
