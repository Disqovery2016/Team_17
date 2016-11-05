package tech.team17;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ryu on 5/11/16.
 */

public class BusClient {
    private static final String API_BASE_URL = "http://openbus.org/";
    private AsyncHttpClient client;

    public BusClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getBus(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void getExtraBusDetails(String openbusId, JsonHttpResponseHandler handler) {
        String url = getApiUrl("buses/");
        client.get(url + openbusId + ".json", handler);
    }
}
