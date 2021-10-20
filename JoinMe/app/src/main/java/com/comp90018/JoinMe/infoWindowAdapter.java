package com.comp90018.JoinMe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class infoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public infoWindowAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View infoview = LayoutInflater.from(mContext).inflate(R.layout.info_window, null);
        TextView title = infoview.findViewById(R.id.title);
        TextView snippet = infoview.findViewById(R.id.snippet);
        title.setText(marker.getTitle());
        snippet.setText(marker.getSnippet());
        return infoview;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }
}
