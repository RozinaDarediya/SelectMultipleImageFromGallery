package com.example.ashish.selectmultipleimagefromgallery.demo.screens;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.example.ashish.selectmultipleimagefromgallery.demo.constant.Global;
import com.example.ashish.selectmultipleimagefromgallery.demo.constant.MyConstant;
import com.example.ashish.selectmultipleimagefromgallery.demo.mFacebook.PostPublisher;
import com.example.ashish.selectmultipleimagefromgallery.demo.model.MyProfile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DisplayProfile extends AppCompatActivity {

    ImageView pro_pic;
    EditText username, name, surname, gender, email;
    Button feed, post, postMultiple, friendlist;

    PostPublisher postPublisher;
    ShareDialog shareDialog;
    CallbackManager mcaCallbackManager;
    LoginManager loginManager;
    String stext, smsg ,sdes, slink;
    Gson gson;
    Dialog dialog;
    AccessToken accessToken;
    MyProfile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.APP_ID));
        setContentView(R.layout.activity_display_profile);

        gson = new Gson();
        mcaCallbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this,permissionNeeds);
        accessToken = AccessToken.getCurrentAccessToken();

        shareDialog = new ShareDialog(this);


        init();

        final String str = Global.getPreference(MyConstant.USER_DATA,"");
         myProfile = gson.fromJson(str,MyProfile.class);

        displayData();

        postPublisher = new PostPublisher();

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataDialog();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });

        postMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProfile.this,AddPhotos.class);
                startActivity(intent);
            }
        });

        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayProfile.this,FriendList.class);
                startActivity(intent);
            }
        });

    }

    private void displayData() {
        if(myProfile != null){
            username.setText(myProfile.getUsername());
            if(myProfile.getName()== null){
                name.setVisibility(View.GONE);
            } else{name.setText(myProfile.getName());}
            if(myProfile.getGender()== null){
                gender.setVisibility(View.GONE);
            }else {
                gender.setText(myProfile.getGender());}
            if(myProfile.getSurname()== null){
                surname.setVisibility(View.GONE);
            }else {
                surname.setText(myProfile.getSurname());}
            if(myProfile.getEmail()== null){
                email.setVisibility(View.GONE);
            }else {
                email.setText(myProfile.getEmail());}
        }
    }

    private void init() {
        pro_pic = (ImageView)findViewById(R.id.pro_pic);
        name = (EditText) findViewById(R.id.name);
        username = (EditText)findViewById(R.id.username);
        surname = (EditText) findViewById(R.id.surname);
        gender = (EditText) findViewById(R.id.gender);
        email = (EditText) findViewById(R.id.email);

        feed = (Button)findViewById(R.id.feed);
        post = (Button)findViewById(R.id.post);
        postMultiple = (Button)findViewById(R.id.postMultiple);
        friendlist = (Button)findViewById(R.id.friendlist);

    }
    private void dataDialog() {

        dialog = new Dialog(DisplayProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mylayout);
        dialog.setTitle("data");

        final TextView text = (TextView)dialog.findViewById(R.id.text);
        final TextView msg = (TextView)dialog.findViewById(R.id.msg);
        final TextView des = (TextView)dialog.findViewById(R.id.des);
        final TextView link = (TextView)dialog.findViewById(R.id.link);
        Button sent = (Button)dialog.findViewById(R.id.sent);

       sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stext = text.getText().toString();
                smsg = msg.getText().toString();
                sdes = des.getText().toString();
                slink = link.getText().toString();
                postdata();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //posting feed
    private void postdata() {
        shareDialog.registerCallback(mcaCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(DisplayProfile.this, "posted data", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(DisplayProfile.this, "cancle data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(DisplayProfile.this, "error data", Toast.LENGTH_SHORT).show();
            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(stext)
                    .setQuote(smsg)
                    .setContentDescription(sdes)
                    .setContentUrl(Uri.parse(slink))
                    .build();
            shareDialog.show(linkContent);
        }
    }//postData

    //posting single pic
    public void postPic(Bitmap bm) {
        if(accessToken != null){

            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.monkey);

            ByteArrayOutputStream blob=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,50,blob);

            byte[] bitmapdata=blob.toByteArray();
            String path = "me/photos";

            Bundle parameters = new Bundle();
            //  parameters.putString("message", status);
            parameters. putByteArray ("source", bitmapdata);

            GraphRequest.Callback cb = new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    if(response.getError()==null){
                        Toast.makeText(DisplayProfile.this, "Successfully posted to Facebook", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("error",response.getError().toString());
                        Toast.makeText(DisplayProfile.this, "Facebook: There was an error, Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                }
            };
            Toast.makeText(this, "Uploading image", Toast.LENGTH_SHORT).show();

            GraphRequest request = new GraphRequest(accessToken, path, parameters, HttpMethod.POST, cb);
            GraphRequestAsyncTask asynTaskGraphRequest = new GraphRequestAsyncTask (request);
            asynTaskGraphRequest.execute();
        }
        else {
            Toast.makeText(this, "not logedin", Toast.LENGTH_SHORT).show();
        }

    }//postPic

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null) {
            Uri uri=data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                postPic(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }//imageView.setImageBitmap(bm);
        }
    }
}
