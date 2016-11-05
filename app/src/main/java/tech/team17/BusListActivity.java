package tech.team17;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BusListActivity extends ActionBarActivity {
    public static final String Bus_DETAIL_KEY = "book";
    private ListView lvBus;
    private BusAdapter busAdapter;
    private BusClient client;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        lvBus = (ListView) findViewById(R.id.lvBus);
        ArrayList<Bus> aBus = new ArrayList<Bus>();
        // initialize the adapter
        busAdapter = new BusAdapter(this, aBus);
        // attach the adapter to the ListView
        lvBus.setAdapter(busAdapter);
        progress = (ProgressBar) findViewById(R.id.progress);
        setupBookSelectedListener();
    }

    public void setupBookSelectedListener() {
        lvBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(BusListActivity.this, BusDetailsActivity.class);
                intent.putExtra(Bus_DETAIL_KEY, busAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {
        // Show progress bar before making network request
        progress.setVisibility(ProgressBar.VISIBLE);
        client = new BusClient();
        client.getBus(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // hide progress bar
                    progress.setVisibility(ProgressBar.GONE);
                    JSONArray docs = null;
                    if (response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Bus> bus = Bus.fromJson(docs);
                        // Remove all books from the adapter
                        busAdapter.clear();
                        // Load model objects into the adapter
                        for (Bus bus1 : bus) {
                            busAdapter.add(bus1); // add book through the adapter
                        }
                        busAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);
            }
        });
    }
}
