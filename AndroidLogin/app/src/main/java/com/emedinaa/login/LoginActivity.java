package com.emedinaa.login;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.emedinaa.login.model.entity.response.LoginResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "HomeActivity";
    private EditText eteUsername,etePassword;
    private View btnLogin,vLoading,tviSignIn;

    private String username, password;

    private RequestQueue queue;
    private LoginResponse loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eteUsername = (EditText)findViewById(R.id.eteUsername);
        etePassword = (EditText)findViewById(R.id.etePassword);
        btnLogin = findViewById(R.id.btnLogin);
        vLoading = findViewById(R.id.vLoading);
        tviSignIn = findViewById(R.id.tviSignIn);

        //events
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //validate
                if(validate())
                {
                    //ir al servidor
                    login();
                }
            }
        });
        tviSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn();
            }
        });
        vLoading.setVisibility(View.GONE);
    }

    private boolean validate() {

        username = eteUsername.getText().toString().trim();
        password = etePassword.getText().toString().trim();

        eteUsername.setError(null);
        etePassword.setError(null);
        if(username.isEmpty())
        {
            eteUsername.setError(getString(R.string.msg_ingresar));
            return false;
        }
        if(password.isEmpty())
        {
            etePassword.setError(getString(R.string.msg_ingresar));
            return false;
        }
        return true;
    }

    private void gotoSignIn() {

    }

    private void login()
    {

        vLoading.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(this);

        String url = getString(R.string.url_login)+"?username="+username+"&password="+password;
        Log.i(TAG, "url "+url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.i(TAG, "response "+response.toString());
                        GsonBuilder gsonb = new GsonBuilder();
                        Gson gson = gsonb.create();

                        loginResponse=null;
                        try
                        {
                            loginResponse= gson.fromJson(response.toString(),LoginResponse.class);
                            if(loginResponse!=null)
                            {
                                Log.i(TAG, "loginResponse "+loginResponse.toString());

                                gotoHome();
                            }

                        }catch (Exception e)
                        {

                        }
                        vLoading.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                vLoading.setVisibility(View.GONE);

            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Parse-Application-Id", getString(R.string.app_id));
                params.put("X-Parse-REST-API-Key", getString(R.string.rest_key));


                return params;
            }
        };
        queue.add(jsonObjReq);
    }

    private void gotoHome() {
       startActivity(new Intent(this,MainActivity.class));
       finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return false;
    }
}
