package com.fjun.android_java_mobius.mobius;

import android.location.Location;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
interface Effect_dataenum {
    dataenum_case requestWeather(Location location);
}
