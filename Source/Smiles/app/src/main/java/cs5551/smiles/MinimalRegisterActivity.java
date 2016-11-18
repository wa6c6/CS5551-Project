package cs5551.smiles;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MinimalRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimal_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        final DatabaseReference ref = db.getReferenceFromUrl("https://smiles-149822.firebaseio.com/");
//
//       final EditText email  =(EditText) findViewById(R.id.email_input);
//       final EditText passwd =(EditText) findViewById(R.id.password_input);
//       final EditText fname  =(EditText) findViewById(R.id.firstname_input);
//       final EditText lname  =(EditText) findViewById(R.id.lastname_input);
//
//        Button btn =(Button) findViewById(R.id.submit_btn);
//        btn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                DatabaseReference  dbref = ref.push();
//                dbref.child("email").setValue(email.getText().toString());
////                DatabaseReference dbref2 = ref.push();
//                dbref.child("password").setValue(passwd.getText().toString());
////                DatabaseReference dbref3 = ref.push();
//                dbref.child("first_name").setValue(fname.getText().toString());
////                DatabaseReference dbref4 = ref.push();
//                dbref.child("last_name").setValue(lname.getText().toString());
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
