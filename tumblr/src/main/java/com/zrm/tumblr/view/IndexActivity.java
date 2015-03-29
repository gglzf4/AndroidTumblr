package com.zrm.tumblr.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zrm.tumblr.R;
import com.zrm.tumblr.adapter.IndexListViewAdapter;
import com.zrm.tumblr.model.Photo;
import com.zrm.tumblr.net.DataAcquire;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends Activity {

    public static final String TAG = IndexActivity.class.getSimpleName();
    private ListView indexListView;
    private IndexListViewAdapter listViewAdapter;
    private List<Photo> photos = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        findViewById();



        requestData();
    }



    private void requestData() {
        DataAcquire.getPhotoList(new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                int status = response.optInt("status");
                if(status == 200){
                    JSONArray array = response.optJSONArray("content");
                    if(array != null && array.length() >0){
                        List<Photo> list = new ArrayList<Photo>();
                        for(int i=0;i<array.length();i++){
                            JSONObject photo = array.optJSONObject(i);
                            String name = photo.optString("name");
                            String url = photo.optString("url");
                            list.add(new Photo(url,name));
                        }
                        listViewAdapter.notifyDataSetChanged(list);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void findViewById(){
        listViewAdapter = new IndexListViewAdapter(this,photos);

        indexListView = (ListView) findViewById(R.id.index_listview);
        indexListView.setAdapter(listViewAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
