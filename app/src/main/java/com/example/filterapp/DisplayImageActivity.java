package com.example.filterapp;

import com.example.filterapp.ImageUtils;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.os.Environment;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;

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
