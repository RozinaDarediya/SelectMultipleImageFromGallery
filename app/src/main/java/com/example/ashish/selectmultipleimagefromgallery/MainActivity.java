package com.example.ashish.selectmultipleimagefromgallery;

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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout img;
    private Button add;
    private Button save;
    private ArrayList<String> imgPath;
     Bitmap bitmap;
    private Bitmap resized;
    private final int PICK_IMAGE_MULTIPLE =1;

    Button fb;
    LoginButton loginButton;

    CallbackManager mcaCallbackManager;

    String[] images;


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
        setContentView(R.layout.activity_main);

        mcaCallbackManager = CallbackManager.Factory.create();

        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken","publish_actions");

        img = (LinearLayout)findViewById(R.id.img);
        add = (Button)findViewById(R.id.add);
        save = (Button)findViewById(R.id.save);

        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPublishPermissions("publish_actions");

        add.setOnClickListener(this);
        save.setOnClickListener(this);

        loginButton.registerCallback(mcaCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "loged in successfully", Toast.LENGTH_SHORT).show();

                postOnWall("pic from android application");
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "loged in onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "loged in error", Toast.LENGTH_SHORT).show();
            }
        });

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
                        Toast.makeText(MainActivity.this, "Successfully posted to Facebook", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("error",response.getError().toString());
                        Toast.makeText(MainActivity.this, "Facebook: There was an error, Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
            };
            Toast.makeText(this, "Uploading image", Toast.LENGTH_SHORT).show();

            GraphRequest request = new GraphRequest(at, path, parameters, HttpMethod.POST, cb);
            GraphRequestAsyncTask asynTaskGraphRequest = new GraphRequestAsyncTask (request);
            asynTaskGraphRequest.execute();
        }
        else {
            Toast.makeText(this, "not logedin", Toast.LENGTH_SHORT).show();
        }

    }

    public void post(View v) {
        if (v == fb) {
            loginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PICK_IMAGE_MULTIPLE){

                 images = data.getStringExtra("data").split("\\|");
                img.removeAllViews();

                Toast.makeText(this, "You have selected " + images.length +" images", Toast.LENGTH_SHORT).show();
                for (int i = 0; i <images.length ; i++) {
                    imgPath.add(images[i]);
                    bitmap = BitmapFactory.decodeFile(imgPath.get(i));
                    fb.setVisibility(View.VISIBLE);
                    /*ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);
                    imageView.setAdjustViewBounds(true);
                    img.addView(imageView);*/
                }
            }

        else {
            super.onActivityResult(requestCode, resultCode, data);
            mcaCallbackManager.onActivityResult(requestCode,resultCode,data);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:

                Intent intent = new Intent(MainActivity.this,CustomPhotoGalleryActivity.class);
                startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
                break;

            case R.id.save:
                if(imgPath != null){
                    if(imgPath.size()>1){
                        Toast.makeText(this, imgPath.size() + " images are selected..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, imgPath.size() + " images are selected..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this,"No image selected..", Toast.LENGTH_SHORT).show();
                }
                break;
        }//switch
    }//onclick
}
