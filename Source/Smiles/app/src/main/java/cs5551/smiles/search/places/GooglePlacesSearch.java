package cs5551.smiles.search.places;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cs5551.smiles.ProviderSearchActivity;
import cs5551.smiles.search.Http;
import cs5551.smiles.search.places_details.GooglePlacesDetailsSearch;


public class GooglePlacesSearch extends AsyncTask<Object, Integer, String> {

    private LatLng currentLocationCorodinates;
    private LatLng clinicLocationCorodinates;
    private int radius;
    private String clinicName;
    private String result;
    private DatabaseReference usersDBRef;
    private DatabaseReference usersPhotosDBRef;
    private ProviderSearchActivity providerSearchActivity;

    public GooglePlacesSearch(LatLng currentLocationCorodinates, int radius, String clinicName, DatabaseReference usersDBRef, DatabaseReference usersPhotosDBRef, ProviderSearchActivity providerSearchActivity, LatLng clinicLocationCorodinates){
        this.currentLocationCorodinates = currentLocationCorodinates;
        this.radius = radius;
        this.clinicName = clinicName;
        this.usersDBRef = usersDBRef;
        this.usersPhotosDBRef = usersPhotosDBRef;
        this.providerSearchActivity = providerSearchActivity;
        this.clinicLocationCorodinates = clinicLocationCorodinates;
    }

    @Override
    protected String doInBackground(Object... params) {
        String placeId = new String();
        String googlePlacesSearchResult = null;

        try {

            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + currentLocationCorodinates.latitude + "," + currentLocationCorodinates.longitude);
            googlePlacesUrl.append("&name=" + clinicName);
//                googlePlacesUrl.append("&sensor=true");
//                googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
            googlePlacesUrl.append("&key=" + ProviderSearchActivity.google_map_key);

            Http http = new Http();
            googlePlacesSearchResult = http.read(googlePlacesUrl.toString());
            JSONObject result= new JSONObject(googlePlacesSearchResult);

            // parse results
            JSONArray results = result.getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                String lat = ((JSONObject) results.get(i)).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = ((JSONObject) results.get(i)).getJSONObject("geometry").getJSONObject("location").getString("lng");
                if(lat.equals((new Double(clinicLocationCorodinates.latitude)).toString()) &&
                        lng.equals((new Double(clinicLocationCorodinates.longitude)).toString())){
                    placeId = ((JSONObject) results.get(i)).getString("place_id");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return placeId;
    }

    @Override
    protected void onPostExecute(String result) {
        GooglePlacesDetailsSearch googlePlacesDetailsSearch = new GooglePlacesDetailsSearch(currentLocationCorodinates,
                radius,
                clinicName,
                usersDBRef,
                usersPhotosDBRef,
                providerSearchActivity);
        // placeId
        googlePlacesDetailsSearch.execute(result);
    }


/////////////////////////////////////////
/////////////////////////////////////////

    // gets placesId
//    private String placesSearch() {
////    private String placesSearch(LatLng userCurrentLocationCorodinates, int radius, String clinicName) {
//        String placeId = null;
//        String googlePlacesSearchResult = null;
//
//        try {
//
//            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//            googlePlacesUrl.append("location=" + currentLocationCorodinates.latitude + "," + currentLocationCorodinates.longitude);
//            googlePlacesUrl.append("&name=" + clinicName);
////                googlePlacesUrl.append("&sensor=true");
////                googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
//            googlePlacesUrl.append("&key=" + "AIzaSyCGeqWbwxzG6zHv2Nxgg3w4RJKisDepayo");
//
//            Http http = new Http();
//            googlePlacesSearchResult = http.read(googlePlacesUrl.toString());
//            JSONObject result= new JSONObject(googlePlacesSearchResult);
//
//            // parse results
//            JSONArray results = result.getJSONArray("results");
//            for(int i = 0; i < results.length(); i++){
//                String lat = ((JSONObject) results.get(i)).getJSONObject("geometry").getJSONObject("location").getString("lat");
//                String lng = ((JSONObject) results.get(i)).getJSONObject("geometry").getJSONObject("location").getString("lng");
//                if(lat.equals((new Double(clinicLocationCorodinates.latitude)).toString()) &&
//                   lng.equals((new Double(clinicLocationCorodinates.longitude)).toString())){
//                    placeId = ((JSONObject) results.get(i)).getString("place_id");
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return placeId;
//    }
//
/////////////////////////////////////////
//    public String search(LatLng userCurrentLocationCorodinates, int radius, String clinicName) {
//        String clinicInfo = null;
//
//        try {
//
//            // 1. Places Search by name to get placesId
//            String placesId = placesSearch(userCurrentLocationCorodinates, radius, clinicName);
//
//            // 2. Places detail search to get all details on clinic
//            clinicInfo = placesDetailSearch(placesId);
//
//        } catch (Exception e) {
////            Log.d("HTTP Error ", e.printStackTrace());
//            e.printStackTrace();
//        }
//
//        return clinicInfo;
//    }



//    private String placesDetailSearch(String placeId) {
//        String domain = null;
//
//        try {
//            // ex. https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY
//            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
//            googlePlacesUrl.append("placeid=")
//                           .append(placeId)
//                           .append("&key=")
//                           .append("AIzaSyCGeqWbwxzG6zHv2Nxgg3w4RJKisDepayo");
//
//            Http http = new Http();
//            String googlePlacesSearchResult = http.read(googlePlacesUrl.toString());
//            JSONObject result= new JSONObject(googlePlacesSearchResult);
//
//            // parse out domain (ex. www.)
////            domain = ;
//
//            // 3. Get email of place to send email.
////            String email = emailSearch(domain);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return domain;
//    }
//
//    private String emailSearch(String domain) {
//        String email = null;
//
//        try {
//
//            // ex. "https://api.hunter.io/v2/domain-search?domain=harmonysmiles.com&api_key=bb7b7e28b4e86096538657b762940c672d2e5d55"
//            StringBuilder hunterUrl = new StringBuilder("https://api.hunter.io/v2/domain-search?");
//            hunterUrl.append("domain=")
//                     .append(domain)
//                     .append("&api_key=")
//                     .append("bb7b7e28b4e86096538657b762940c672d2e5d55");
//
//            // parse emails values
////            String email =
//        } catch (Exception e) {
//            Log.d("Error ", e.toString());
//        }
//
//        return email;
//    }
}
