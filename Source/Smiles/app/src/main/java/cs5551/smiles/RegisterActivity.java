package cs5551.smiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et2 ;
    private EditText et3 ;
    private Button bt ;
    //private TextView tv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

     et2 = (EditText) findViewById(R.id.editText2);
     et3 = (EditText) findViewById(R.id.editText3);
     bt =  (Button) findViewById(R.id.button2);


     bt.setOnClickListener(this);
     //tv.setOnClickListener(this);

    }

    public void creatUser(){
        String email = et2.getText().toString().trim();
        String password = et3.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter proper email.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter proper password.",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("User registering.");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User registered successfully.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"User registered failed.",Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == bt){
           creatUser();
        }

    }
}
