package cs5551.smiles.search.places_details;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;

import cs5551.smiles.ProviderSearchActivity;
import cs5551.smiles.search.Http;
import cs5551.smiles.search.email.HunterEmailSearch;


public class GooglePlacesDetailsSearch extends AsyncTask<Object, Integer, String> {

    private LatLng currentLocationCorodinates;
    private int radius;
    private String clinicName;
    private String result;
    private DatabaseReference usersDBRef;
    private DatabaseReference usersPhotosDBRef;
    private ProviderSearchActivity providerSearchActivity;


    public GooglePlacesDetailsSearch(LatLng currentLocationCorodinates, int radius, String clinicName, DatabaseReference usersDBRef, DatabaseReference usersPhotosDBRef, ProviderSearchActivity providerSearchActivity){
        this.currentLocationCorodinates = currentLocationCorodinates;
        this.radius = radius;
        this.clinicName = clinicName;
        this.providerSearchActivity = providerSearchActivity;
        this.usersDBRef = usersDBRef;
        this.usersPhotosDBRef = usersPhotosDBRef;
    }

    @Override
    protected String doInBackground(Object... params) {
        // param is placesId
        String googlePlacesSearchResult = new String();

        try {
            // ex. https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            googlePlacesUrl.append("placeid=")
                    .append((String)params[0])
                    .append("&key=")
                    .append(ProviderSearchActivity.google_map_key);

            Http http = new Http();
            googlePlacesSearchResult = http.read(googlePlacesUrl.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesSearchResult;
    }

    @Override
    protected void onPostExecute(String result) {
        HunterEmailSearch hunterEmailSearch = new HunterEmailSearch(usersDBRef,
                usersPhotosDBRef,
                clinicName,
                providerSearchActivity);
        // entire places detail result
        hunterEmailSearch.execute(result);
    }
}
