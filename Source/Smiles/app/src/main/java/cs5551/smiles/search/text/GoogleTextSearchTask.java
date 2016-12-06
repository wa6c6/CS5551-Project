package cs5551.smiles.search.text;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import cs5551.smiles.search.Http;


public class GoogleTextSearchTask extends AsyncTask<Object, Integer, String> {
    String googleTextSearchResult = null;
    GoogleMap googleMap;

    final LatLng currLoc;
    final int radius;

    public GoogleTextSearchTask(LatLng currLoc, int radius){
        this.currLoc = currLoc;
        this.radius = radius;
    }

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (GoogleMap) inputObj[0];
            String googleTextSearchUrl = (String) inputObj[1];
            Http http = new Http();
            googleTextSearchResult = http.read(googleTextSearchUrl);
        } catch (Exception e) {
            Log.d("Google Text Search Task", e.toString());
        }
        return googleTextSearchResult;
    }

    @Override
    protected void onPostExecute(String result) {
        ProcesstTextResultTask processTextResultTask = new ProcesstTextResultTask(currLoc, radius);
        Object[] args = new Object[2];
        args[0] = googleMap;
        args[1] = result;
        processTextResultTask.execute(args);
    }
}
