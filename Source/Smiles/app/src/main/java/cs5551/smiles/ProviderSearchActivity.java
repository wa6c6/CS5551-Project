package cs5551.smiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cs5551.smiles.search.places.GooglePlacesSearch;
import cs5551.smiles.search.text.GoogleTextSearchTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

//public class ProviderSearchActivity extends FragmentActivity implements OnMapReadyCallback {
public class ProviderSearchActivity extends AppCompatActivity implements OnMapReadyCallback { //}, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    public Geocoder geocoder;
    public final static String google_map_key = "AIzaSyDjBnQC0MIH4GwMvZ9fhqtVr0NZgy2t84o";

    private final DatabaseReference usersDBRef = FirebaseDatabase.getInstance().getReference("users_2");
    private final DatabaseReference usersPhotosDBRef = FirebaseDatabase.getInstance().getReference("users_photos_2");
//    private final StorageReference usersPhotosStorageRef = FirebaseStorage.getInstance().getReference("users_photos_2");

    // in meters
    private int radius = 8100;        // approx meters in 5 miles
    private int metersInMile = 1609; // approx meters in 1 mile
    private String[] radiiArray;

    private LatLng currentLocationCorodinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.providerSearchToolbar);
        setSupportActionBar(toolbar);

        radiiArray = getResources().getStringArray(R.array.radii);

        // Initialize
        geocoder = new Geocoder(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // here?
        mMap.clear();

        // Setup
        LocationManager userCurrentLocation = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        LocationListener userCurrentLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Shouldn't need this, because we could never have gottento this activity
        // unless we had permission (unless the user said to the prompt possibly?)
        if (checkSelfPermission(ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //show message or ask permissions from the user.
            return;
        }

//        LatLng currentLocationCorodinates = null;
//        double latitude = 0, longitude = 0;

        //Getting the current location of the user.
        userCurrentLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, userCurrentLocationListener);
//        latitude = userCurrentLocation
//                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                .getLatitude();
//        longitude = userCurrentLocation
//                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                .getLongitude();
        currentLocationCorodinates = new LatLng(userCurrentLocation
                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                .getLatitude(),
                userCurrentLocation
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        .getLongitude());

        // used in the map marker
        StringBuilder userAddress = new StringBuilder();

        // Set current
        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            List<Address> addresses = geocoder.getFromLocation(currentLocationCorodinates.latitude, currentLocationCorodinates.longitude, 1);
            Address address = addresses.get(0);
            userAddress =  new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                userAddress.append(address.getAddressLine(i)).append("\t");
            }
            userAddress.append(address.getCountryName()).append("\t");

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        // Add Map Marker
        mMap.addMarker(new MarkerOptions().position(currentLocationCorodinates)
                .title("Your current address.").snippet(userAddress.toString()));

        // Move camera and zoom level of the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationCorodinates, 15));

        // 1. Text Search
        String type = "Orthodontics";
        StringBuilder googleTextSearchUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googleTextSearchUrl.append("query=" + type);
        googleTextSearchUrl.append("&location=" + currentLocationCorodinates.latitude + "," + currentLocationCorodinates.longitude);
        System.out.println("radius = " + radius);
        googleTextSearchUrl.append("&radius=" + radius);
        googleTextSearchUrl.append("&key=" + ProviderSearchActivity.google_map_key);


        GoogleTextSearchTask googleTextSearch = new GoogleTextSearchTask(currentLocationCorodinates, radius);
        Object[] args = new Object[2];
        args[0] = mMap;
        args[1] = googleTextSearchUrl.toString();
        googleTextSearch.execute(args);

        //
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override public boolean onMarkerClick(final Marker marker) {
                System.out.println("Clicked on a marker.");

                // ? - i think this allows the pin tag to still show up after touching
                marker.showInfoWindow();

                GooglePlacesSearch googlePlacesSearch = new GooglePlacesSearch(currentLocationCorodinates,
                        radius,
                        marker.getTitle(),
                        usersDBRef,
                        usersPhotosDBRef,
                        ProviderSearchActivity.this,
                        marker.getPosition());
//                String clinicInfo = googlePlacesSearch.search(currentLocationCorodinates, radius, marker.getTitle());
                googlePlacesSearch.execute();

//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProviderSearchActivity.this);
//                dialogBuilder.setTitle("Email " + marker.getTitle() + "?")
//                        // need to call some other google services to get ratings info, email, and even pics of clinic.
//                        .setMessage("** Additional Provider Info **")
////                        .setMessage(clinicInfo)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,int id) {
//                                System.out.println("Clicked on 'Yes'");
//
//                                usersDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        // find user
//                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
////                                            User user;
//                                            // match on key (ex. foo@examplecom)
//                                            if(child.getKey().equals(LoginActivity.getUSER().getEmail().replace(".","")) ) {
//                                                final User user = child.getValue(User.class);
//
//                                                // 1. Get paths to image files.
//                                                usersPhotosDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        ArrayList<String> imagePaths = new ArrayList<String>();
//                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                                            // match on key (ex. foo@examplecom)
//                                                            if(child.getKey().equals(LoginActivity.getUSER().getEmail().replace(".","")) ) {
//                                                                for(DataSnapshot child2 : child.getChildren()) {
//                                                                    // 2. get all image paths
//                                                                    imagePaths.add(child2.getValue().toString());
//                                                                }
//                                                            }
//                                                        }
//
//                                                        // 3. send email
//                                                        sendEmail(user.toString(), imagePaths);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
////                                                // 3. send email
////                                                sendEmail(user.toString());
//                                                break;
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                        System.out.println("In onCancelled");
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,int id) {
//                                System.out.println("Clicked on 'No'");
//                            }
//                        });
//
//                // Create dialogue
//                AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.show();

                return true;
            }
        });
    }

//    private void sendEmail(String message, List<String> imagePaths) {
//        int i =0;
//        StringBuilder sb = new StringBuilder(message);
//        sb.append(System.lineSeparator()).append("Image Links -----------------------").append(System.lineSeparator());
//        for(String path: imagePaths){
//            i++;
////            sb.append("<a href='").append(path).append("'>").append("image-").append(i).append("</a>").append(System.lineSeparator());
//            sb.append(path).append(System.lineSeparator());
//        }
////        Log.i("Send email", "");
////        String[] TO = {"waulner@gmail.com"};
////        String[] CC = {""};
//        ArrayList<String> to = new ArrayList<>();
//
//        // load with "to" addresses.
//        if(LoginActivity.getUSER().getEmail().contains("@example.com")) {
//            to.add("waulner@gmail.com");
//        }
//        else {
//            // doesn't email clinic, a
//            to.add(LoginActivity.getUSER().getEmail());
//        }
//
//        // Will need to convert "to" ArryaList to array
//        String [] arr = new String[to.size()];
//
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
////        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        // This to prompts email client only
//        emailIntent.setType("message/rfc822");
////        emailIntent.setType("text/html");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, to.toArray(arr));
////        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
////        emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Orthodontic Template From - " + LoginActivity.getUSER().getFirstName() + " " + LoginActivity.getUSER().getLastName() );
//        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Choose an Email Client:"));
////            finish();     // seems to exit the app?
////            Log.i("Finished sending email...", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(ProviderSearchActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.provider_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_radius:
                // prompt for new radius
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProviderSearchActivity.this);
                dialogBuilder.setTitle("Pick a Radius")

                        .setItems(R.array.radii, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                radius = new Integer(radiiArray[which]) * metersInMile;
                                onMapReady(mMap);
                            }
                        });
//                             .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,int id) {
//                                    // refresh map with new radius
//                                    radius = 500;
//                                    onMapReady(mMap);
//                                 }
//                            })
//                             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,int id) {
//                                    System.out.println("Clicked on 'No'");
//                                    // nothing to do
//                                 }
//                            });
                // Create dialogue
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                return true;

            case R.id.action_profile:
                // direct them to profile
                startActivity( new Intent(getApplicationContext(), ProfileActivity.class) );
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
