package com.example.ashish.selectmultipleimagefromgallery.demo.mFacebook;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

/**
 * Created by ashish on 6/10/17.
 */

public class PostPic {
    public void PostPic1() {
        Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.superman);
        SharePhoto sharephoto = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("pic uploaded from android application not from the facebook...")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(sharephoto)
                .build();
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
              //  Toast.makeText(g, "posted", Toast.LENGTH_SHORT).show();
                Log.e("post","posted");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
}
