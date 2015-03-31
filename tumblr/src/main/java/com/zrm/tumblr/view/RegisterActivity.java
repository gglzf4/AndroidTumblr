package com.zrm.tumblr.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zrm.tumblr.R;
import com.zrm.tumblr.app.Session;
import com.zrm.tumblr.model.Photo;
import com.zrm.tumblr.net.DataAcquire;
import com.zrm.tumblr.utils.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends Activity implements View.OnClickListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText emailEditText,passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);


        registerButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == registerButton.getId()){
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(StringUtils.isBlank(email)){
                Toast.makeText(this,"email is not blank",Toast.LENGTH_LONG).show();
            }else if(StringUtils.isBlank(password)){
                Toast.makeText(this,"password is not blank",Toast.LENGTH_LONG).show();
            }else{
                Map<String,String> map = new HashMap<String, String>();
                map.put(DataAcquire.EMAIL,email);
                map.put(DataAcquire.PASSWORD,password);
                DataAcquire.register(map,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Logger.i(TAG, response.toString());
                        int status = response.optInt(DataAcquire.STATUS);
                        if (status == 200) {
                            JSONObject jsonObject = response.optJSONObject(DataAcquire.CONTENT);
                            Session.saveInfo(jsonObject.optString(DataAcquire.ACCESS_TOKEN));
                            Toast.makeText(RegisterActivity.this,"success",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this,response.optString(DataAcquire.MSG),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(RegisterActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
