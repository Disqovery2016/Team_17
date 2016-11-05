package tech.team17;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ryu on 5/11/16.
 */

public class Bus implements Serializable {
    private String openbusId;
    private String NoPass;
    private String title;
    private String time;

    public String getOpenbusId() {
        return openbusId;
    }

    public String getTitle() {
        return title;
    }

    public String getNoPass() {
        return NoPass;
    }

    public String getTime() { return time; }

    public String getCoverUrl() {
        return "http://covers.openbus.org/b/olid/" + openbusId + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl() {
        return "http://covers.openbus.org/b/olid/" + openbusId + "-L.jpg?default=false";
    }

    // Returns a Book given the expected JSON
    public static Bus fromJson(JSONObject jsonObject) {
        Bus bus = new Bus();
        try {
            if (jsonObject.has("cover_edition_key"))  {
                bus.openbusId = jsonObject.getString("cover_edition_key");
            } else if(jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                bus.openbusId = ids.getString(0);
            }
            bus.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            bus.NoPass = getNoPass(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return bus;
    }

    // Return comma separated bus list when there is more than one author
    private static String getNoPass(final JSONObject jsonObject) {
        try {
            final JSONArray NoPass = jsonObject.getJSONArray("bus_name");
            int numPass = NoPass.length();
            final String[] authorStrings = new String[numPass];
            for (int i = 0; i < numPass; ++i) {
                authorStrings[i] = NoPass.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    // Decodes array of book json results into business model objects
    public static ArrayList<Bus> fromJson(JSONArray jsonArray) {
        ArrayList<Bus> buses = new ArrayList<Bus>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Bus bus = Bus.fromJson(bookJson);
            if (bus != null) {
                buses.add(bus);
            }
        }
        return buses;
    }
}
