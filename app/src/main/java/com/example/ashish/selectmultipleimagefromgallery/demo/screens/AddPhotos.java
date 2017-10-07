package com.example.ashish.selectmultipleimagefromgallery.demo.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPhotos extends AppCompatActivity implements View.OnClickListener {

    Button add;
    LinearLayout linear;

    CallbackManager mcaCallbackManager;
    LoginManager loginManager;
    AccessToken accessToken;

    private final int PICK_IMAGE_MULTIPLE =123;
    private ArrayList<String> imgPath;
    String[] images;
    Bitmap bitmap;

    @Override
    protected void onStart() {
        super.onStart();
        imgPath = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.APP_ID));
        setContentView(R.layout.activity_add_photos);

        mcaCallbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this,permissionNeeds);
        accessToken = AccessToken.getCurrentAccessToken();

        add = (Button)findViewById(R.id.add);
        linear = (LinearLayout)findViewById(R.id.linear);
        add.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE && resultCode==RESULT_OK){

            images = data.getStringExtra("data").split("\\|");
            linear.removeAllViews();

            Toast.makeText(this, "You have selected " + images.length +" images", Toast.LENGTH_SHORT).show();
            for (int i = 0; i <images.length ; i++) {
                imgPath.add(images[i]);
                bitmap = BitmapFactory.decodeFile(imgPath.get(i));
                   /* ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);
                    imageView.setAdjustViewBounds(true);
                    img.addView(imageView);*/
            }
            postOnWall("From gallery");
        }
    }
    private void postOnWall(String status) {
        for (int i = 0; i < images.length; i++) {
            PostPic(status, images[i], i);
        }
    }

    private void PostPic(String status, String camImg, int i) {
        AccessToken at = AccessToken.getCurrentAccessToken();
        if(at != null){
            Bitmap bitmapimg = BitmapFactory.decodeFile(camImg);

            ByteArrayOutputStream blob=new ByteArrayOutputStream();
            bitmapimg.compress(Bitmap.CompressFormat.JPEG,50,blob);

            byte[] bitmapdata=blob.toByteArray();
            String path = "me/photos";

            Bundle parameters = new Bundle();
            parameters.putString("message", status);
            parameters. putByteArray ("source", bitmapdata);

            GraphRequest.Callback cb = new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    if(response.getError()==null){
                        Toast.makeText(AddPhotos.this, "Successfully posted to Facebook", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("error",response.getError().toString());
                        Toast.makeText(AddPhotos.this, "Facebook: There was an error, Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
            };
    //        Toast.makeText(this, "Uploading image", Toast.LENGTH_SHORT).show();

            GraphRequest request = new GraphRequest(at, path, parameters, HttpMethod.POST, cb);
            GraphRequestAsyncTask asynTaskGraphRequest = new GraphRequestAsyncTask (request);
            asynTaskGraphRequest.execute();
        }
        else {
            Toast.makeText(this, "not logedin", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(AddPhotos.this,CustomPhotoGallery.class);
        startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
    }
}
