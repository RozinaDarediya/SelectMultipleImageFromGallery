package com.example.ashish.selectmultipleimagefromgallery.demo.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ashish.selectmultipleimagefromgallery.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendList extends AppCompatActivity {

    CallbackManager mcaCallbackManager;
    LoginManager loginManager;
    Gson gson;
    AccessToken accessToken;
    ListView listView;
    List<String> friendsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.APP_ID));
        setContentView(R.layout.activity_friend_list);

        gson = new Gson();
        mcaCallbackManager = CallbackManager.Factory.create();
        //List<String> permissionNeeds = Arrays.asList("read_custom_friendlists");
        loginManager = LoginManager.getInstance();
        LoginManager.getInstance().logInWithReadPermissions(FriendList.this, Arrays.asList("user_friends"));
        accessToken = AccessToken.getCurrentAccessToken();

        listView = (ListView)findViewById(R.id.listview);
        friendsName = new ArrayList<String>();

        GraphRequest request = new GraphRequest(accessToken, "/me/taggable_friends", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.e("res",response.toString());
                        JSONObject obj = response.getJSONObject();
                        JSONArray arr;
                        try {
                            arr = obj.getJSONArray("data");
                            for (int l=0; l < arr.length(); l++) {
                                JSONObject oneByOne = arr.getJSONObject(l);

                                friendsName.add(oneByOne.opt("name").toString());
                                Log.e("name",oneByOne.opt("name").toString());

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_list_item_1,friendsName);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();

        /*GraphRequestAsyncTask request = new GraphRequest(accessToken, "/me/taggable_friends", null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.e("obj",response.toString());
                        String str = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            JSONObject graphObject = jsonObject.getJSONObject("graphObject");

                            JSONArray jsonArray = graphObject.getJSONArray("data");
                            for(int i = 0 ;i<10 ; i++){
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String a = String.valueOf(object.get("name"));
                                Log.e("name" + i,a);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error",e.toString());
                        }
                    }
                }).executeAsync();*/
        /*GraphRequestAsyncTask requestAsyncTask = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("obj",response.toString());
            }
        }).executeAsync();
*//*
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("obj",object.toString());
            }
        });
        Bundle param = new Bundle();
        param.putString("fields","id, name,friends.fields(data).fields(name)");  // you can give as many fields as you want
        request.setParameters(param);
        request.executeAsync();*/
    }
}
