package cs5551.smiles.search.text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TextResult {

    public List<HashMap<String, String>> parse(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googleTextSearchJson) {
//        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
//        String placeName = "-NA-";
//        String vicinity = "-NA-";
//        String latitude = "";
//        String longitude = "";
//        String reference = "";
//
//        try {
//            if (!googleTextSearchJson.isNull("name")) {
//                placeName = googleTextSearchJson.getString("name");
//            }
//            if (!googleTextSearchJson.isNull("vicinity")) {
//                vicinity = googleTextSearchJson.getString("vicinity");
//            }
//            latitude = googleTextSearchJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
//            longitude = googleTextSearchJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
//            reference = googleTextSearchJson.getString("reference");
//            googlePlaceMap.put("place_name", placeName);
//            googlePlaceMap.put("vicinity", vicinity);
//            googlePlaceMap.put("lat", latitude);
//            googlePlaceMap.put("lng", longitude);
//            googlePlaceMap.put("reference", reference);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return googlePlaceMap;
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();

        try {
            googlePlaceMap.put("place_name", googleTextSearchJson.getString("name"));
            googlePlaceMap.put("lat",
                                googleTextSearchJson.getJSONObject("geometry").getJSONObject("location").getString("lat"));
            googlePlaceMap.put("lng",
                                googleTextSearchJson.getJSONObject("geometry").getJSONObject("location").getString("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
