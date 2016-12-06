package cs5551.smiles.search.places;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class ProcessPlacesResultTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    JSONObject googlePlacesJson;
    GoogleMap googleMap;

    final LatLng currLoc;
    final int radius;

    public ProcessPlacesResultTask(LatLng currLoc, int radius){
        this.currLoc = currLoc;
        this.radius = radius;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        PlacesResult placeJsonParser = new PlacesResult();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
//        googleMap.clear();
//        for (int i = 0; i < list.size(); i++) {
//            MarkerOptions markerOptions = new MarkerOptions();
//            HashMap<String, String> googlePlace = list.get(i);
//            double lat = Double.parseDouble(googlePlace.get("lat"));
//            double lng = Double.parseDouble(googlePlace.get("lng"));
//            String placeName = googlePlace.get("place_name");
//            String vicinity = googlePlace.get("vicinity");
//            LatLng latLng = new LatLng(lat, lng);
//            markerOptions.position(latLng);
//            markerOptions.title(placeName + " : " + vicinity);
//            googleMap.addMarker(markerOptions);
//        }
        // get places ID
        // call places detail
    }
}
