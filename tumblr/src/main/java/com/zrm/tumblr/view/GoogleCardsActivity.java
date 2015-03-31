package com.zrm.tumblr.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.zrm.tumblr.R;
import com.zrm.tumblr.adapter.GoogleCardsAdapter;
import com.zrm.tumblr.model.Photo;
import com.zrm.tumblr.net.DataAcquire;
import com.zrm.tumblr.utils.Logger;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleCardsActivity extends Activity implements OnDismissCallback {

    private static final String TAG = GoogleCardsActivity.class.getSimpleName();
    private GoogleCardsAdapter mGoogleCardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviews_googlecards);

        ListView listView = (ListView) findViewById(R.id.activity_googlecards_listview);
        mGoogleCardsAdapter = new GoogleCardsAdapter(this);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);
        listView.setAdapter(swingBottomInAnimationAdapter);

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
                int status = response.optInt(DataAcquire.STATUS);
                if (status == 200) {
                    JSONArray array = response.optJSONArray(DataAcquire.CONTENT);
                    if (array != null && array.length() > 0) {
                        List<Photo> list = new ArrayList<Photo>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject photo = array.optJSONObject(i);
                            Logger.i(TAG,photo.toString());
                            Long id = photo.optLong(DataAcquire.ID);
                            String name = photo.optString(DataAcquire.NAME);
                            String url = photo.optString(DataAcquire.URL);
                            list.add(new Photo(id,url, name));
                        }
                        mGoogleCardsAdapter.addAll(list);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(GoogleCardsActivity.this, "error", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mGoogleCardsAdapter.remove(position);
        }

        if(mGoogleCardsAdapter.getCount() == 0){
            requestData();
        }
    }
}
