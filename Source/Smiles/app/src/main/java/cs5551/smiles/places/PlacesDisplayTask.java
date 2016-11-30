package cs5551.smiles.places;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cs5551.smiles.R;
import cs5551.smiles.places.Places;


public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {


    JSONObject googlePlacesJson;
    GoogleMap googleMap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

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
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> googlePlace = list.get(i);

            LatLng latLng = new LatLng(Double.parseDouble(googlePlace.get("lat")),
                                       Double.parseDouble(googlePlace.get("lng")));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(googlePlace.get("place_name"));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.smiley_face_with_braces_pin));

            googleMap.addMarker(markerOptions);
        }
    }
}
