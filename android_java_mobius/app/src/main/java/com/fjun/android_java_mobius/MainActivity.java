package com.fjun.android_java_mobius;

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

    private TextView mStatusTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusTextView = findViewById(R.id.status);
        mImageView = findViewById(R.id.icon);

        // Setup presenter: Attach lifecycle
        getLifecycle().addObserver(mPresenter);
    }

    @Override
    public void setTemperature(String temperature) {
        // TODO (johboh): Figure out mobius threading.
        runOnUiThread(() -> mStatusTextView.setText(getString(R.string.temperature, temperature)));
    }

    @Override
    public void showSomethingWentWrong(String error) {
        // TODO (johboh): Figure out mobius threading.
        runOnUiThread(() -> mStatusTextView.setText(getString(R.string.something_went_wrong, error)));
    }

    @Override
    public void showWaitingForPosition() {
        // TODO (johboh): Figure out mobius threading.
        runOnUiThread(() -> mStatusTextView.setText(getString(R.string.waiting_for_position)));
    }

    @Override
    public void showWaitingForWeather() {
        // TODO (johboh): Figure out mobius threading.
        runOnUiThread(() -> mStatusTextView.setText(getString(R.string.waiting_for_weather)));
    }

    @Override
    public void setImageUrl(String imageUrl) {
        runOnUiThread(() -> {
            Picasso picasso = Picasso.get();
            picasso.load(imageUrl).into(mImageView);
        });
    }
}
