package com.example.ashish.selectmultipleimagefromgallery.demo.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.example.ashish.selectmultipleimagefromgallery.demo.constant.Global;
import com.example.ashish.selectmultipleimagefromgallery.demo.constant.MyConstant;
import com.example.ashish.selectmultipleimagefromgallery.demo.model.MyProfile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessToken accessToken;

    String username;
    String name;
    String surname;
    Uri link;
    String email;
    String gen;
    String url;
    String user_id = null;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.APP_ID));
        setContentView(R.layout.activity_content_main);

        callbackManager = CallbackManager.Factory.create();


        gson = new Gson();

        loginButton = (LoginButton)findViewById(R.id.loginButton);


        getdata();


    }

    private void getdata() {loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_birthday");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("success","Logged in successfully");
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                Global.storePreference(MyConstant.PREF_KEY_IS_LOGGED_IN,true);

                accessToken = AccessToken.getCurrentAccessToken();

                Log.e("token",accessToken.getToken().toString());
                Global.storePreference(MyConstant.ACCESS_TOKEN,accessToken.getToken().toString());

                final GraphRequestAsyncTask request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            user_id = (String) object.get("id");
                             username = (String) object.get("name");
                             //email = (String) object.get("email");
                             //gen = (String) object.get("gender");


                            MyProfile obj_myProfile = new MyProfile();

                            obj_myProfile.setName(username);
                           // obj_myProfile.setEmail(email);
                         //   obj_myProfile.setGender(gen);
                            obj_myProfile.setImgUri(user_id);

                            String str = gson.toJson(obj_myProfile,MyProfile.class);

                           Global.storePreference(MyConstant.USER_DATA,str);
                            Intent intent = new Intent(LoginActivity.this,DisplayProfile.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();


              /*  GraphRequestAsyncTask profileRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            user_id = (String) object.get("id");
                            username = (String)object.get("name");
                            email = (String)object.get("email");
                            gen = (String)object.get("gender");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).executeAsync();
*/
                //url = "https://graph.facebook.com/" + user_id+ "/picture?type=large";

                Profile profile = Profile.getCurrentProfile();
                ProfileTracker profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                        name = currentProfile.getFirstName();
                        surname = currentProfile.getLastName();
                        link = currentProfile.getLinkUri();
                    }
                };
            }

            @Override
            public void onCancel() {
                Log.e("cancle","Logged in cancle");
                Toast.makeText(LoginActivity.this, "Logged in cancle", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("error","Logged in error");
                Toast.makeText(LoginActivity.this, "Logged in error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
