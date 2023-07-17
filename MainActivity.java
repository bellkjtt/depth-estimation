package com.example.myapplication;

import static org.opencv.imgproc.Imgproc.rectangle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button select, camera;
    ImageView imageView;
    Bitmap bitmap;
    Mat mat;
    int SELECT_CODE = 100, CAMERA_CODE =101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()) Log.d("LOADED","SUCCES");
        else Log.d("LOADED", "ERR");

        getPermission();


        camera = findViewById(R.id.camera);
        select = findViewById(R.id.select);
        imageView = findViewById(R.id.imageView);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_CODE);

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CODE);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_CODE && data!=null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                imageView.setImageBitmap(bitmap);

                mat =new Mat();
                Utils.bitmapToMat(bitmap, mat); //여기까지가 세팅

                testContour(mat);

                Utils.matToBitmap(mat, bitmap);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if(requestCode==CAMERA_CODE && data !=null){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

            mat =new Mat();
            Utils.bitmapToMat(bitmap, mat);
                            ;// 여기부터 변환
            testContour(mat);


            Utils.matToBitmap(mat, bitmap);
            imageView.setImageBitmap(bitmap);
        }

    }

    void getPermission(){
        if(checkSelfPermission(android.Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},102);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==102 && grantResults.length>0){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getPermission();
            }
        }
    }


    private void testContour(Mat imageMat) {
        Mat rgb = new Mat();  //rgb color matrix
        rgb = imageMat.clone();
        Mat grayImage = new Mat();  //grey color matrix
        Imgproc.cvtColor(rgb, grayImage, Imgproc.COLOR_RGB2GRAY);

      //  Mat gradThresh = new Mat();  //matrix for threshold
     //   Mat hierarchy = new Mat();    //matrix for contour hierachy
      //  List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
      //  Imgproc.threshold(grayImage,gradThresh, 127,255,0);  //global threshold;
       // Imgproc.adaptiveThreshold(grayImage, gradThresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 3, 12);  //block size 3
       // Imgproc.findContours(gradThresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
       // if(contours.size()>0) {
         //   for(int idx = 0; idx < contours.size(); idx++) {
          //      Rect rect = Imgproc.boundingRect(contours.get(idx));
            //    if (rect.height > 10 && rect.width > 10 && !(rect.width >= 512 - 5 && rect.height >= 512 - 5)){
              //      rectangle(imageMat, new Point(rect.br().x - rect.width, rect.br().y - rect.height)
                  //          , rect.br()
                //            , new Scalar(0, 255, 0), 5);
               // }

            //}
           // Imgcodecs.imwrite("/tmp/dev/doc_original.jpg", rgb);
            Imgcodecs.imwrite("/tmp/dev/doc_gray.jpg", grayImage);
          //  Imgcodecs.imwrite("/tmp/dev/doc_thresh.jpg", gradThresh);
          //  Imgcodecs.imwrite("/tmp/dev/doc_contour.jpg", imageMat);
       // }
    }

}