package cs5551.smiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class PhotoDisplayActivity extends AppCompatActivity {

    private ImageView imgv;
//    private Button pic;
    private ImageView pic;

    // File Storage
    private final StorageReference usersPhotosStorageRef = FirebaseStorage.getInstance().getReference("users_photos_2");
    // Mirror File Storage becasue there is no way to perform a dir/ls in Firebase Storage
    // (see https://groups.google.com/forum/#!topic/firebase-talk/ZUy3hHWPShU)
    // but if I mirror in the paths in the Realtime Database I will essentially create a list of files in a given folder that I can query.
    //https://github.com/firebase/quickstart-ios/issues/90
    private final DatabaseReference usersPhotosDBRef = FirebaseDatabase.getInstance().getReference("users_photos_2");

    private ProgressDialog progess;

    private static final int CAMERA_REQUEST_CODE = 1 ;
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        //savedInstanceState.putString("media_url", path.toString());

        // Always call the superclass so it can save the view hierarchy state
        //super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (savedInstanceState != null) {
            //path = Uri.parse(savedInstanceState.getString("media_url"));
        //}
        setContentView(R.layout.activity_photo_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.photoToolbar);
        setSupportActionBar(toolbar);

//        usersPhotosStorageRef = FirebaseStorage.getInstance().getReference();
        progess = new ProgressDialog(this);
        imgv = (ImageView) findViewById(R.id.imageView);
//        pic =  (Button) findViewById(R.id.capture);
//
//        pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent nav = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(nav, CAMERA_REQUEST_CODE);
//            }
//        });
        pic = (ImageView) findViewById(R.id.photoView);
        pic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent nav = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(nav, CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
       // if(true){
            progess.setMessage("Pic Uploading ........");
            progess.show();

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            try {
                 Integer name = photo.getGenerationId();
                String picName = name.toString();
                FileOutputStream fos = new FileOutputStream(
                        new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),picName));
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                //progess.dismiss();

                File t = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), picName);
                Uri uriPath = Uri.fromFile(t);
//                StorageReference storePic = usersPhotosStorageRef.child("Photo").child(uriPath.getLastPathSegment());
                final String uuid = UUID.randomUUID().toString();
                StorageReference storePic = usersPhotosStorageRef.child(LoginActivity.getUSER().getEmail().replace(".","")).child(uuid);
                storePic.putFile(uriPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progess.dismiss();

                        Uri getImage = taskSnapshot.getDownloadUrl();
                        Picasso.with(PhotoDisplayActivity.this).load(getImage).resize(750,750).into(imgv);
//                        Toast.makeText(PhotoDisplayActivity.this,"Getting image..",Toast.LENGTH_SHORT).show();

                        // Set to visible
                        imgv.setVisibility(View.VISIBLE);

                        // write path to realtime db
                        usersPhotosDBRef.child(LoginActivity.getUSER().getEmail().replace(".","")).child(uuid).setValue(getImage.toString());

                        Toast.makeText(PhotoDisplayActivity.this,"Uploaded successfully",Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progess.dismiss();
                        Toast.makeText(PhotoDisplayActivity.this,"Uploaded failed",Toast.LENGTH_SHORT).show();
                    }
                });


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile_2:
                // direct them to profile
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                return true;

            case R.id.action_provider_search_2:
                // direct them to provider search
                startActivity( new Intent(getApplicationContext(), ProviderSearchActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
