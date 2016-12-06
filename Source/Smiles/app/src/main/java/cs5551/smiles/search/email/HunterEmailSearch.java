package cs5551.smiles.search.email;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs5551.smiles.LoginActivity;
import cs5551.smiles.ProviderSearchActivity;
import cs5551.smiles.User;
import cs5551.smiles.search.Http;


public class HunterEmailSearch extends AsyncTask<Object, Integer, String> {
    private DatabaseReference usersDBRef;
    private DatabaseReference usersPhotosDBRef;
    private String clinicName;
    private ProviderSearchActivity providerSearchActivity;
    private String email;
    private JSONObject resultJSON;

    public HunterEmailSearch(DatabaseReference usersDBRef, DatabaseReference usersPhotosDBRef, String clinicName, ProviderSearchActivity providerSearchActivity){
        this.usersDBRef = usersDBRef;
        this.usersPhotosDBRef = usersPhotosDBRef;
        this.clinicName = clinicName;
        this.providerSearchActivity = providerSearchActivity;
    }

    @Override
    protected String doInBackground(Object... params) {

        // param is placesId
        String hunterEmailResult = new String();

        try {
            JSONObject details = new JSONObject((String)params[0]);
            resultJSON = details.getJSONObject("result");

            String domain = resultJSON.getString("website");
            domain = domain.substring(domain.indexOf("www.") + 4);
            domain = domain.replace("/","");

            // ex. "https://api.hunter.io/v2/domain-search?domain=harmonysmiles.com&api_key=bb7b7e28b4e86096538657b762940c672d2e5d55"
            StringBuilder hunterUrl = new StringBuilder("https://api.hunter.io/v2/domain-search?");
            hunterUrl.append("domain=")
                    .append(domain)
                    .append("&api_key=")
                    .append("bb7b7e28b4e86096538657b762940c672d2e5d55");

            Http http = new Http();
            hunterEmailResult = http.read(hunterUrl.toString());
            JSONObject result= new JSONObject(hunterEmailResult);
            JSONArray emails = result.getJSONObject("data").getJSONArray("emails");
            email = ((JSONObject) emails.get(0)).getString("value");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String result) {

        try{
            StringBuilder sb = new StringBuilder();

            // Ratings
            double total = 0;
            JSONArray reviews = resultJSON.getJSONArray("reviews");
            for(int i = 0; i < reviews.length(); i++ ){
                String str = ((JSONObject) reviews.get(i)).getString("rating");
                total = total + new Double(str);
            }
            double rating = total/reviews.length();
            sb.append("Rating:").append(rating).append("/5.0").append(System.lineSeparator());

            // Address/Phone
            sb.append("Address:").append(resultJSON.getString("formatted_address")).append(System.lineSeparator())
                    .append("Phone:").append(resultJSON.getString("formatted_phone_number")).append(System.lineSeparator())
                    .append("Hours").append(System.lineSeparator());

            // Hours
            JSONArray days = resultJSON.getJSONObject("opening_hours").getJSONArray("weekday_text");
            for(int i = 0; i < days.length(); i++ ){
                sb.append(days.get(i).toString()).append(System.lineSeparator());
            }

            createEmail(sb.toString(), email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String createEmail(String clinicInfo, String em) {
        final String email = em;

        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(providerSearchActivity);
            dialogBuilder.setTitle("Email " + clinicName + "?")
                    // need to call some other google services to get ratings info, email, and even pics of clinic.
//                    .setMessage("** Additional Provider Info **")
                    .setMessage(clinicInfo)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            System.out.println("Clicked on 'Yes'");

                            usersDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // find user
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                            User user;
                                        // match on key (ex. foo@examplecom)
                                        if(child.getKey().equals(LoginActivity.getUSER().getEmail().replace(".","")) ) {
                                            final User user = child.getValue(User.class);

                                            // 1. Get paths to image files.
                                            usersPhotosDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    ArrayList<String> imagePaths = new ArrayList<String>();
                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                        // match on key (ex. foo@examplecom)
                                                        if(child.getKey().equals(LoginActivity.getUSER().getEmail().replace(".","")) ) {
                                                            for(DataSnapshot child2 : child.getChildren()) {
                                                                // 2. get all image paths
                                                                imagePaths.add(child2.getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    // 3. send email
                                                    sendEmail(email, user.toString(), imagePaths);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
//                                                // 3. send email
//                                                sendEmail(user.toString());
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("In onCancelled");
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int id) {
                            System.out.println("Clicked on 'No'");
                        }
                    });

            // Create dialogue
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return email;
    }

    private void sendEmail(String email, String message, List<String> imagePaths) {
        int i =0;
        StringBuilder sb = new StringBuilder(message);
        sb.append(System.lineSeparator()).append("Image Links -----------------------").append(System.lineSeparator());
        for(String path: imagePaths){
            i++;
//            sb.append("<a href='").append(path).append("'>").append("image-").append(i).append("</a>").append(System.lineSeparator());
            sb.append(path).append(System.lineSeparator());
        }
//        Log.i("Send email", "");
//        String[] TO = {"waulner@gmail.com"};
//        String[] CC = {""};
        ArrayList<String> to = new ArrayList<>();

        // load with "to" addresses.
        if(LoginActivity.getUSER().getEmail().contains("@example.com")) {
            to.add("waulner@gmail.com");
        }
        else {
            // doesn't email clinic, a
            to.add(LoginActivity.getUSER().getEmail());
        }

        // Will need to convert "to" ArryaList to array
        String [] arr = new String[to.size()];

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        emailIntent.setData(Uri.parse("mailto:"));
        // This to prompts email client only
        emailIntent.setType("message/rfc822");
//        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to.toArray(arr));
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Orthodontic Template From - " + LoginActivity.getUSER().getFirstName() + " " + LoginActivity.getUSER().getLastName()
        + " (mailto:" + email + ")");
        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

        try {
            providerSearchActivity.startActivity(Intent.createChooser(emailIntent, "Choose an Email Client:"));
//            finish();     // seems to exit the app?
//            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(providerSearchActivity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
