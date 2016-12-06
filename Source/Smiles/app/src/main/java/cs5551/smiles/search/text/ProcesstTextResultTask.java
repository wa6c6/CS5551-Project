package cs5551.smiles.search.text;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cs5551.smiles.R;


public class ProcesstTextResultTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    JSONObject googleTextSearchJson;
    GoogleMap googleMap;

    final LatLng currLoc;
    final int radius;

    public ProcesstTextResultTask(LatLng currLoc, int radius){
        this.currLoc = currLoc;
        this.radius = radius;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        TextResult textResultParser = new TextResult();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googleTextSearchJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = textResultParser.parse(googleTextSearchJson);
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

            // location of clinic
            LatLng latLng = new LatLng(Double.parseDouble(googlePlace.get("lat")),
                                       Double.parseDouble(googlePlace.get("lng")));

            // if its not within the radius then skip
            Double distance = SphericalUtil.computeDistanceBetween(latLng, currLoc);
            if (distance.intValue() > radius) {
                System.out.println(googlePlace.get("place_name") + " is " +distance + " > " + radius);
                continue;
            }

            System.out.println(googlePlace.get("place_name") + " is " + distance + " < " + radius);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(googlePlace.get("place_name"));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.smiley_face_with_braces_pin));

            googleMap.addMarker(markerOptions);
        }
    }
}
