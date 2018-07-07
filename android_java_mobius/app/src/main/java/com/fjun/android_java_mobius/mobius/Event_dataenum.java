package com.fjun.android_java_mobius.mobius;

import android.location.Location;

import com.fjun.android_java_mobius.Yr.YrResult;
import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
interface Event_dataenum {
    dataenum_case init();

    dataenum_case gotLocation(Location location);

    dataenum_case gotWeather(YrResult yrResult);

    dataenum_case gotError(String error);
}
