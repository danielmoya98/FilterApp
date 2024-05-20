package com.example.filterapp;

import android.Manifest;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.card.MaterialCardView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 101;  // Request code for gallery intent

    private Camera mCamera;
    private boolean isOn = false;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean isScrollViewVisible = false;
    private ImageButton mImageButton;
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSurfaceView = findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        MaterialButton captureButton= findViewById(R.id.btn_capture);
        mImageButton = findViewById(R.id.toggle_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = !isOn;
                v.setSelected(isOn);

                if (isOn) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

        ImageButton rotateCameraButton = findViewById(R.id.rotar_camara);
        rotateCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí deberías poner el código para capturar una foto y pasarla a la otra actividad
                // Pero como este proceso es más complejo y requiere el uso de la cámara, no puedo proporcionarte el código completo aquí.
                // En su lugar, puedo sugerirte algunos pasos generales:
                // 1. Utiliza la API de la cámara para capturar una foto.
                // 2. Guarda la foto en la memoria del dispositivo o en un directorio temporal.
                // 3. Crea un Intent para pasar la URI de la foto a la otra actividad.
                // 4. Inicia la otra actividad con ese Intent.
            }
        });

        CardView detailedCardView = findViewById(R.id.detailed_card_view);

        ImageButton btnGlass = findViewById(R.id.btn_glass);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontal_scroll_view);
        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScrollViewVisible = !isScrollViewVisible;
                horizontalScrollView.setVisibility(isScrollViewVisible ? View.VISIBLE : View.GONE);
                detailedCardView.setVisibility(isScrollViewVisible ? View.GONE : View.VISIBLE);
            }
        });

        ImageButton galleryButton = findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
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

        // Request Camera Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        // Initialize the ScaleGestureDetector
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            private float currentZoom = 0;

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (mCamera != null) {
                    Camera.Parameters params = mCamera.getParameters();
                    if (params.isZoomSupported()) {
                        currentZoom += (detector.getScaleFactor() - 1) * 50;
                        int maxZoom = params.getMaxZoom();
                        currentZoom = Math.max(0, Math.min(currentZoom, maxZoom));
                        params.setZoom((int) currentZoom);
                        mCamera.setParameters(params);
                    }
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });

        // Set touch listener to the SurfaceView to detect pinch zoom gestures
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            // Pass the selected image URI to DisplayImageActivity
            Intent intent = new Intent(MainActivity.this, DisplayImageActivity.class);
            intent.putExtra("imageUri", selectedImage);
            startActivity(intent);
        }
    }

    private void turnOnFlash() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if (mCamera != null) {
                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(params);
            }
        }
    }

    // TODO: Falta la implemetacion del boton para sacar una foto y pasarla mediante onactivtyResult
    // TODO: aplicar los filtro a las imagenes
    // TODO: aplicar el filtro en tiempo real



    private void turnOffFlash() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
        }
    }

    private void switchCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        mCurrentCameraId = (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) ?
                Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;

        try {
            mCamera = Camera.open(mCurrentCameraId);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            setCameraDisplayOrientation();
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e("Camera", "Error al abrir la cámara: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Error al abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCameraDisplayOrientation() {
        if (mCamera == null) {
            return;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCurrentCameraId, info);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(result);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            setCameraDisplayOrientation();
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e("Camera", "Error al abrir la cámara: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Error al abrir la cámara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos otorgados, abrir la cámara
                surfaceCreated(mSurfaceHolder);
            } else {
                // Permisos denegados, mostrar un mensaje o tomar alguna acción adecuada
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}