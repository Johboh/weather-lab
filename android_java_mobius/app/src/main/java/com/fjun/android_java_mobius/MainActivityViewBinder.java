package com.fjun.android_java_mobius;

/**
 * View Binder For Main Activity
 */
public interface MainActivityViewBinder {
    void setTemperature(String temperature);

    void showSomethingWentWrong(String error);

    void showWaitingForPosition();

    void showWaitingForWeather();

    void setImageUrl(String imageUrl);
}
