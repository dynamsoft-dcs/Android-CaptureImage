package com.example.dynamsoft.captureimage;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dynamsoft.camerasdk.exception.DcsCameraNotAuthorizedException;
import com.dynamsoft.camerasdk.exception.DcsException;
import com.dynamsoft.camerasdk.exception.DcsValueNotValidException;
import com.dynamsoft.camerasdk.exception.DcsValueOutOfRangeException;
import com.dynamsoft.camerasdk.model.DcsDocument;
import com.dynamsoft.camerasdk.model.DcsImage;
import com.dynamsoft.camerasdk.view.DcsVideoViewListener;
import com.dynamsoft.camerasdk.view.DcsViewListener;
import com.dynamsoft.camerasdk.view.DcsImageGalleryView;
import com.dynamsoft.camerasdk.view.DcsImageGalleryViewListener;
import com.dynamsoft.camerasdk.view.DcsVideoView;
import com.dynamsoft.camerasdk.view.DcsView;

public class MainActivity extends AppCompatActivity implements DcsViewListener,DcsImageGalleryViewListener{

    private DcsView dcsView;
    private TextView btnShowCamera;
    private TextView tvTitle;
    private static final int CAMERA_OK = 10;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DcsView.setLicense(getApplicationContext(),"your license number");
        } catch (DcsValueNotValidException e) {
            e.printStackTrace();
        }
        requestPermissions();
        tvTitle = findViewById(R.id.tv_title_id);
        dcsView = findViewById(R.id.dcsview_id);
        DcsView.setLogLevel(DcsView.DLLE_OFF);
        dcsView.setListener(this);
        dcsView.setCurrentView(DcsView.DVE_IMAGEGALLERYVIEW);
        try {
            dcsView.getVideoView().setMode(DcsVideoView.DME_IMAGE);
        } catch (DcsValueOutOfRangeException e) {
            e.printStackTrace();
        }


        dcsView.getVideoView().setNextViewAfterCapture(DcsView.DVE_EDITORVIEW);
        dcsView.getVideoView().setNextViewAfterCancel(DcsView.DVE_IMAGEGALLERYVIEW);

        dcsView.getImageEditorView().setNextViewAfterOK(DcsView.DVE_IMAGEGALLERYVIEW);
        dcsView.getImageEditorView().setNextViewAfterCancel(DcsView.DVE_IMAGEGALLERYVIEW);

        btnShowCamera = findViewById(R.id.tv_show_id);
        btnShowCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dcsView.setCurrentView(DcsView.DVE_VIDEOVIEW);
            }
        });

        dcsView.getVideoView().setListener(new DcsVideoViewListener() {
            @Override
            public boolean onPreCapture(DcsVideoView dcsVideoView) {
                return true;
            }

            @Override
            public void onCaptureFailure(DcsVideoView dcsVideoView, DcsException e) {

            }

            @Override
            public void onPostCapture(DcsVideoView dcsVideoView, DcsImage dcsImage) {

            }

            @Override
            public void onCancelTapped(DcsVideoView dcsVideoView) {

            }

            @Override
            public void onCaptureTapped(DcsVideoView dcsVideoView) {

            }

            @Override
            public void onDocumentDetected(DcsVideoView dcsVideoView, DcsDocument dcsDocument) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(dcsView.getCurrentView() == DcsView.DVE_VIDEOVIEW){
            try {
                dcsView.getVideoView().preview();
            } catch (DcsCameraNotAuthorizedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dcsView.getVideoView().stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dcsView.getVideoView().destroyCamera();
    }

    @Override
    public void onSingleTap(DcsImageGalleryView dcsImageGalleryView, int i) {

    }

    @Override
    public void onLongPress(DcsImageGalleryView dcsImageGalleryView, int i) {

    }

    @Override
    public void onSelectChanged(DcsImageGalleryView dcsImageGalleryView, int[] ints) {

    }

    @Override
    public void onCurrentViewChanged(DcsView dcsView, int i, int i1) {
        if(i1 == DcsView.DVE_IMAGEGALLERYVIEW){
            btnShowCamera.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
        }else{
            btnShowCamera.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
    }

    private void requestPermissions(){
        if (Build.VERSION.SDK_INT>22){
            try {
                if (ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE")!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.CAMERA},CAMERA_OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            // do nothing
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            DcsView.setLicense(getApplicationContext(),"your license number");
        } catch (DcsValueNotValidException e) {
            e.printStackTrace();
        }
    }
}
