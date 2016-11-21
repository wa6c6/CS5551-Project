package cs5551.smiles;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MinimalRegisterActivity extends AppCompatActivity {


    private EditText et1,et2,et3,et4,et5,et6,et7,et8,et9,et10,et11,et12,et13,et14,et15,et16,et17,et18,et19,et20,et21;
    private  Button bt;
    private CheckBox ch1,ch2,ch3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_register);

        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText25);
        et3 = (EditText) findViewById(R.id.editText35);
        et4 = (EditText) findViewById(R.id.editText4);
        et5 = (EditText) findViewById(R.id.editText5);
        et6 = (EditText) findViewById(R.id.editText6);
        et7 = (EditText) findViewById(R.id.editText7);
        et8 = (EditText) findViewById(R.id.editText8);
        et9 = (EditText) findViewById(R.id.editText9);
        et10 = (EditText) findViewById(R.id.editText10);
        et11 = (EditText) findViewById(R.id.editText11);
        et12 = (EditText) findViewById(R.id.editText12);
        et13 = (EditText) findViewById(R.id.editText13);
        et14 = (EditText) findViewById(R.id.editText14);
        et15 = (EditText) findViewById(R.id.editText15);
        et16 = (EditText) findViewById(R.id.editText16);
        et17 = (EditText) findViewById(R.id.editText17);
        et18 = (EditText) findViewById(R.id.editText18);
        et19 = (EditText) findViewById(R.id.editText19);
        et20 = (EditText) findViewById(R.id.editText20);
        et21 = (EditText) findViewById(R.id.editText21);
        bt = (Button) findViewById(R.id.button1);
        ch1 = (CheckBox) findViewById(R.id.checkBox1);
        ch2 = (CheckBox) findViewById(R.id.checkBox2);
        ch3 = (CheckBox) findViewById(R.id.checkBox3);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final DatabaseReference ref = db.getReference("Registered");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //String data = dataSnapshot.getValue(String.class);
                //Log.v("Message",data);
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
            public void onClick(View v) {
                DatabaseReference dbref = ref.push();
                dbref.child("FirstName").setValue(et1.getText().toString());
                //DatabaseReference dbref2 = ref.push();
                dbref.child("LastName").setValue(et2.getText().toString());
                //DatabaseReference dbref3 = ref.push();
                dbref.child("Username").setValue(et3.getText().toString());
                dbref.child("Age").setValue(et4.getText().toString());
                dbref.child("Gender").setValue(et5.getText().toString());
                dbref.child("Address").setValue(et6.getText().toString());
                dbref.child("City").setValue(et7.getText().toString());
                dbref.child("State").setValue(et8.getText().toString());
                dbref.child("ZipCode").setValue(et9.getText().toString());
                dbref.child("InsuProvider").setValue(et10.getText().toString());
                dbref.child("InsuNumber").setValue(et11.getText().toString());
                dbref.child("ThirdPartyInsu").setValue(et12.getText().toString());
                dbref.child("HIPPA").setValue(et13.getText().toString());
                dbref.child("PrevOrthodontistTreatment").setValue(et14.getText().toString());
                dbref.child("CurrentDentist").setValue(et15.getText().toString());
                dbref.child("LastCleaning").setValue(et16.getText().toString());
                dbref.child("AnyCavities").setValue(et17.getText().toString());
                dbref.child("MedicalHistory").setValue(et18.getText().toString());
                dbref.child("ChangeSmile").setValue(et19.getText().toString());
                dbref.child("ChangeTeeth").setValue(et20.getText().toString());
                dbref.child("ChangeProfile").setValue(et21.getText().toString());


                if(ch1.isChecked()){
                    dbref.child("Braces").setValue(ch1.getText().toString());
                }
                else{
                    dbref.child("Braces").setValue("");
                }
                if(ch2.isChecked()){
                    dbref.child("Invisaling").setValue(ch2.getText().toString());
                }
                else{
                    dbref.child("Invisaling").setValue("");
                }
                if(ch3.isChecked()){
                    dbref.child("LingualBraces").setValue(ch3.getText().toString());
                }
                else{
                    dbref.child("LingualBraces").setValue("");
                }

         /*
                    dbref.child("Braces").setValue(" ");
                    dbref.child("Invisaling").setValue(" ");
                    dbref.child("LingualBraces").setValue(" ");
                    */


                Toast.makeText(MinimalRegisterActivity.this,"User details rgistered success.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
