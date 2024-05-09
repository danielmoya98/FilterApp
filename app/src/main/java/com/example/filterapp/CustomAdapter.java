package com.example.filterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class CustomAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mImages;

    public CustomAdapter(Context context, String[] images) {
        super(context, R.layout.list_item, images);
        this.mContext = context;
        this.mImages = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        ImageView imageView = rowView.findViewById(R.id.image_view);
        // Cargar la imagen usando Glide u otra biblioteca de carga de imágenes
        Glide.with(mContext).load(mImages[position]).into(imageView);

        return rowView;
    }
}
