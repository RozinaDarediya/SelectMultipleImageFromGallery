package com.example.ashish.selectmultipleimagefromgallery.demo.mFacebook;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

/**
 * Created by ashish on 6/10/17.
 */

public class PostPublisher extends AppCompatActivity {

    CallbackManager mcaCallbackManager;
    ShareDialog shareDialog;
    String stext, smsg ,sdes, slink;
    int PICK_IMAGE_MULTIPLE = 1;
    String[] images;
    private ArrayList<String> imgPath;
    Bitmap bitmap;


    public void postFeed(){
        mcaCallbackManager = CallbackManager.Factory.create();
        //dataDialog();
     //  postdata();

    }

    private void dataDialog() {

        final Dialog dialog = new Dialog(PostPublisher.this);
         dialog.setContentView(R.layout.mylayout);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


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
            }
        });
        dialog.show();
    }


    /*public void postPic(){
        PostPic postPic = new PostPic();
        Toast.makeText(PostPublisher.this, "image posted", Toast.LENGTH_SHORT).show();
    }*/

    /*public void postMultiple(){
      *//*  Intent intent = new Intent(PostPublisher.this,CustomPhotoGalleryActivity.class);
        startActivityForResult(intent,PICK_IMAGE_MULTIPLE);*//*
    }*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE){

            images = data.getStringExtra("data").split("\\|");
            //img.removeAllViews();

            Toast.makeText(this, "You have selected " + images.length +" images", Toast.LENGTH_SHORT).show();
            for (int i = 0; i <images.length ; i++) {
                imgPath.add(images[i]);
                bitmap = BitmapFactory.decodeFile(imgPath.get(i));
               // fb.setVisibility(View.VISIBLE);
                    *//*ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);
                    imageView.setAdjustViewBounds(true);
                    img.addView(imageView);*//*
            }
        }
    }*/
}
