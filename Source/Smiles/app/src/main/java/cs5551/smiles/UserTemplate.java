package cs5551.smiles;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserTemplate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_template);


        final EditText et =(EditText) findViewById(R.id.editText);
        final EditText et2 =(EditText) findViewById(R.id.editText2);
        final EditText et3 =(EditText) findViewById(R.id.editText3);
        final Button bt =(Button) findViewById(R.id.button);


        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final DatabaseReference ref = db.getReference("Registered");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //String data = dataSnapshot.getValue(String.class);
                // Log.v("Message",data);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                DatabaseReference  dbref = ref.push();
                dbref.child("Username").setValue(et.getText().toString());
                //DatabaseReference dbref2 = ref.push();
                dbref.child("Email").setValue(et2.getText().toString());
                //DatabaseReference dbref3 = ref.push();
                dbref.child("Password").setValue(et3.getText().toString());
                Toast.makeText(UserTemplate.this,"User details rgistered success.",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
