package com.example.filterapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.content.Intent;
import android.widget.Toast;
import android.os.Environment;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import androidx.core.content.FileProvider;
import android.net.Uri;

public class ImageUtils {

    public static boolean saveImageToGallery(Context context, ImageView imageView, String filename) {
        // Obtener la imagen del ImageView
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Crear la ruta del directorio de almacenamiento externo
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InstaFilter");

        // Verificar si el directorio existe, si no, crearlo
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                return false; // Fallo al crear el directorio
            }
        }

        // Guardar la imagen en la memoria del dispositivo
        File imageFile = new File(storageDir, filename);
        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Escanear manualmente el archivo para que aparezca en la galería
            scanFile(context, imageFile.getAbsolutePath(), "image/png");

            return true; // Éxito al guardar la imagen
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Fallo al guardar la imagen
        }
    }

    private static void scanFile(Context context, String path, String mimeType) {
        MediaScannerConnection.scanFile(context,
                new String[]{path},
                new String[]{mimeType},
                null);
    }

    public static void shareImageFromGallery(Context context, String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            // Agregar tipos de datos adicionales para aplicaciones específicas
            shareIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/vnd.whatsapp", "text/plain", "text/*", "image/jpeg", "image/png"});

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            Toast.makeText(context, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }



}
