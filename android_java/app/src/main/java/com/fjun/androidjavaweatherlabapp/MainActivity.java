package com.fjun.androidjavaweatherlabapp;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjun.androidjavaweatherlabapp.Yr.Yr;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements MainActivityViewBinder {

    private MainActivityPresenter mPresenter;

    private TextView mCurrentLocation;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentLocation = findViewById(R.id.current_location);
        mImageView = findViewById(R.id.icon);

        // Setup presenter: Attach the view and register for lifecycle listener
        final GpsLocationListener gpsLocationListener = new GpsLocationListener(this, (LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
        final Yr yr = new Yr();
        mPresenter = new MainActivityPresenter(gpsLocationListener, yr);
        mPresenter.attachView(this);
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
