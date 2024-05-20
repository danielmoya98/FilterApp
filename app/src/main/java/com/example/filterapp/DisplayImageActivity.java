package com.example.filterapp;

import com.example.filterapp.ImageUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.os.Environment;
import android.content.Intent;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private boolean isScrollViewVisible = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        imageView = findViewById(R.id.image_view);

        ImageButton arrowButton = findViewById(R.id.arrow_button);

        // Establecer OnClickListener para el botón
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la SecondActivity
                Intent intent = new Intent(DisplayImageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        CardView detailedCardView = findViewById(R.id.detailed_card_view);

        MaterialButton btnGlass = findViewById(R.id.btn_glass);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontal_scroll_view);
        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScrollViewVisible = !isScrollViewVisible;
                horizontalScrollView.setVisibility(isScrollViewVisible ? View.VISIBLE : View.GONE);
                detailedCardView.setVisibility(isScrollViewVisible ? View.GONE : View.VISIBLE);
            }
        });


        // Handling MaterialCardView clicks
        MaterialCardView cardView1 = findViewById(R.id.card_view_1);
        MaterialCardView cardView2 = findViewById(R.id.card_view_2);
        MaterialCardView cardView3 = findViewById(R.id.card_view_3);
        MaterialCardView cardView4 = findViewById(R.id.card_view_4);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.setVisibility(View.GONE);
                detailedCardView.setVisibility(View.VISIBLE);
            }
        };

        cardView1.setOnClickListener(onClickListener);
        cardView2.setOnClickListener(onClickListener);
        cardView3.setOnClickListener(onClickListener);
        cardView4.setOnClickListener(onClickListener);


        // Obtener la URI de la imagen desde el Intent
        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        if (imageUri != null) {
            imageView.setImageURI(imageUri);
        }

        // Agregar funcionalidad al botón de descarga
        final Context context = this; // Guardar el contexto en una variable
        findViewById(R.id.download_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = ImageUtils.saveImageToGallery(context, imageView, "image_filename.png");
                if (success) {
                    Toast.makeText(context, "Imagen guardada correctamente", Toast.LENGTH_SHORT).show();

                    // Obtener la ruta de la imagen guardada
                    String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + "/InstaFilter/image_filename.png";

                } else {
                    Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la ruta de la imagen guardada
                String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/InstaFilter/image_filename.png";
                // Compartir la imagen
                ImageUtils.shareImageFromGallery(DisplayImageActivity.this, imagePath);
            }
        });

    }
}
