package com.fjun.android_java_mobius.mobius;

import android.support.annotation.Nullable;

import com.fjun.android_java_mobius.Yr.Symbol;
import com.fjun.android_java_mobius.Yr.Temperature;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Model {
    public static Builder builder() {
        return new AutoValue_Model.Builder().isFetchingLocation(false).isFetchingWeather(false);
    }

    public abstract boolean isFetchingLocation();

    public abstract boolean isFetchingWeather();

    @Nullable
    public abstract Temperature temperature();

    @Nullable
    public abstract Symbol symbol();

    @Nullable
    public abstract String errorString();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder isFetchingLocation(boolean isFetchingLocation);

        public abstract Builder isFetchingWeather(boolean isFetchingLocation);

        public abstract Builder temperature(Temperature temperature);

        public abstract Builder symbol(Symbol symbol);

        public abstract Builder errorString(String errorString);

        public abstract Model build();
    }
}
