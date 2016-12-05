package cs5551.smiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    // database connection
    private final DatabaseReference usersDBRef = FirebaseDatabase.getInstance().getReference("users_2");

    private User user = null;

    private EditText email, password, firstName, lastName, age, address, city, state, zipCode,
                     insuranceProvider, planNumber, dentalProvider, lastCleaning, medicalConditions,
                     changeSmile, changeProfile, changeTeeth;
    private CheckBox financing, historyOfOrthoTreatment, anyKnownCavaties,
                     braces, lingualBraces, invisalign;
    private RadioButton male, female;
    private RadioGroup gender;

    private boolean newUser = false;

    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        // login
        email = (EditText) findViewById(R.id.email_input);
        password = (EditText) findViewById(R.id.password_input);
        // user
        firstName = (EditText) findViewById(R.id.first_name);
        lastName  = (EditText) findViewById(R.id.last_name);
        age     = (EditText) findViewById(R.id.age);
        gender  = (RadioGroup) findViewById(R.id.gender);
        male    = (RadioButton) findViewById(R.id.male);
        female  = (RadioButton) findViewById(R.id.female);
        address = (EditText) findViewById(R.id.address);
        city    = (EditText) findViewById(R.id.city);
        state   = (EditText) findViewById(R.id.state);
        zipCode = (EditText) findViewById(R.id.zip_code);
        // insurance
        insuranceProvider = (EditText) findViewById(R.id.insurance_provider);
        planNumber = (EditText) findViewById(R.id.plan_number);
        financing  = (CheckBox) findViewById(R.id.financing);
        // history
        dentalProvider = (EditText) findViewById(R.id.dental_provider);
        lastCleaning   = (EditText) findViewById(R.id.last_cleaning);
        medicalConditions = (EditText) findViewById(R.id.medical_conditions);
        historyOfOrthoTreatment = (CheckBox) findViewById(R.id.history_of_orth_treatment);
        anyKnownCavaties = (CheckBox) findViewById(R.id.any_known_cavaties);
        // complaints
        changeSmile   = (EditText) findViewById(R.id.change_smile);
        changeProfile = (EditText) findViewById(R.id.change_profile);
        changeTeeth   = (EditText) findViewById(R.id.change_teeth);
        // treatment
        braces = (CheckBox) findViewById(R.id.braces);
        lingualBraces = (CheckBox) findViewById(R.id.lingual_braces);
        invisalign = (CheckBox) findViewById(R.id.invisalign);
        // submit
        submitBtn = (Button) findViewById(R.id.submitBtn);

        // Should execute once and pull back all users
        usersDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // no user so must be registering
                if(LoginActivity.getUSER() == null){
                    System.out.println("No USER so initial registration.");
                    newUser = true;
                    return;
                }
                newUser = false;

                // else user exists so populate form
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getKey().equals(LoginActivity.getUSER().getEmail().replace(".",""))) {
                        user = child.getValue(User.class);

                        // login
                        email.setText(user.getEmail());
                        password.setText(user.getPassword());
                        // user
                        firstName.setText(user.getFirstName());
                        lastName.setText(user.getLastName());
                        age.setText(user.getAge());
                        address.setText(user.getAddress());
                        city.setText(user.getCity());
                        state.setText(user.getState());
                        zipCode.setText(user.getZipCode());
                        if(user.getMale()){
                            male.setChecked(true);
                            female.setChecked(false);
                        }
                        else{
                            male.setChecked(false);
                            female.setChecked(true);
                        }
                        // insurance
                        insuranceProvider.setText(user.getInsuranceProvider());
                        planNumber.setText(user.getPlanNumber());
                        financing.setChecked(user.getFinancing());
                        // history
                        dentalProvider.setText(user.getDentalProvider());
                        lastCleaning.setText(user.getLastCleaning());
                        medicalConditions.setText(user.getMedicalConditions());
                        historyOfOrthoTreatment.setChecked(user.getHistoryOfOrthoTreatment());
                        anyKnownCavaties.setChecked(user.getAnyKnownCavaties());
                        // complaints
                        changeSmile.setText(user.getChangeSmile());
                        changeProfile.setText(user.getChangeProfile());
                        changeTeeth.setText(user.getChangeTeeth());
                        // treatment
                        braces.setChecked(user.getBraces());
                        lingualBraces.setChecked(user.getLingualBraces());
                        invisalign.setChecked(user.getInvisalign());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("In onCancelled");
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // set any updated fields
                user = new User();
                // login
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                // user
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setAge(age.getText().toString());
                user.setAddress(address.getText().toString());
                user.setCity(city.getText().toString());
                user.setState(state.getText().toString());
                user.setZipCode(zipCode.getText().toString());
                if(((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getId() == R.id.male){
                    user.setMale(true);
                    user.setFemale(false);
                }
                else{
                    user.setMale(false);
                    user.setFemale(true);
                }
                // insurance
                user.setInsuranceProvider(insuranceProvider.getText().toString());
                user.setPlanNumber(planNumber.getText().toString());
                user.setFinancing(financing.isChecked());
                // history
                user.setDentalProvider(dentalProvider.getText().toString());
                user.setLastCleaning(lastCleaning.getText().toString());
                user.setMedicalConditions(medicalConditions.getText().toString());
                user.setHistoryOfOrthoTreatment(historyOfOrthoTreatment.isChecked());
                user.setAnyKnownCavaties(anyKnownCavaties.isChecked());
                // complaints
                user.setChangeProfile(changeProfile.getText().toString());
                user.setChangeSmile(changeSmile.getText().toString());
                user.setChangeTeeth(changeTeeth.getText().toString());
                // treatment
                user.setBraces(braces.isChecked());
                user.setLingualBraces(lingualBraces.isChecked());
                user.setInvisalign(invisalign.isChecked());

                // update/save to db
                usersDBRef.child(user.getEmail().replace(".","")).setValue(user);

                // Its a new user so set on login, becaue its used other places.
                if(newUser) {
                    LoginActivity.setUSER(user);
                }

                Toast.makeText(ProfileActivity.this,"Profile Saved.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submit(View v){
        // take them to set up their provider search
        startActivity( new Intent(getApplicationContext(), ProviderSearchActivity.class) );
    }

    // not sure i need this for anything
    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.male:
                if (checked)
                    System.out.println("male");
                    break;
            case R.id.female:
                if (checked)
                    System.out.println("female");
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pics:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(getApplicationContext(),PhotoDisplayActivity.class));
                return true;

            case R.id.action_provider_search:
                // direct them to profile
                startActivity( new Intent(getApplicationContext(), ProviderSearchActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
