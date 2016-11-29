package cs5551.smiles;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    // login
    private String email;
    private String password;
    // user
    private String firstName;
    private String lastName;
    private String age;
    private boolean male;
    private boolean female;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    // insurance
    private String insuranceProvider;
    private String planNumber;
    private boolean financing;
    // history
    private String dentalProvider;
    private String lastCleaning;
    private String medicalConditions;
    private boolean historyOfOrthoTreatment;
    private boolean anyKnownCavaties;
    // complaints
    private String changeSmile;
    private String changeProfile;
    private String changeTeeth;
    // treatment
    private boolean braces;
    private boolean lingualBraces;
    private boolean invisalign;


    public User(){
    };

    public User(String email, String password){
        this.email = email;
        this.password = password;

    };

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean getFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public boolean getFinancing() {
        return financing;
    }

    public void setFinancing(boolean financing) {
        this.financing = financing;
    }

    public String getDentalProvider() {
        return dentalProvider;
    }

    public void setDentalProvider(String dentalProvider) {
        this.dentalProvider = dentalProvider;
    }

    public String getLastCleaning() {
        return lastCleaning;
    }

    public void setLastCleaning(String lastCleaning) {
        this.lastCleaning = lastCleaning;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public boolean getHistoryOfOrthoTreatment() {
        return historyOfOrthoTreatment;
    }

    public void setHistoryOfOrthoTreatment(boolean historyOfOrthoTreatment) {
        this.historyOfOrthoTreatment = historyOfOrthoTreatment;
    }

    public boolean getAnyKnownCavaties() {
        return anyKnownCavaties;
    }

    public void setAnyKnownCavaties(boolean anyKnownCavaties) {
        this.anyKnownCavaties = anyKnownCavaties;
    }

    public String getChangeSmile() {
        return changeSmile;
    }

    public void setChangeSmile(String changeSmile) {
        this.changeSmile = changeSmile;
    }

    public String getChangeProfile() {
        return changeProfile;
    }

    public void setChangeProfile(String changeProfile) {
        this.changeProfile = changeProfile;
    }

    public String getChangeTeeth() {
        return changeTeeth;
    }

    public void setChangeTeeth(String changeTeeth) {
        this.changeTeeth = changeTeeth;
    }

    public boolean getBraces() {
        return braces;
    }

    public void setBraces(boolean braces) {
        this.braces = braces;
    }

    public boolean getLingualBraces() {
        return lingualBraces;
    }

    public void setLingualBraces(boolean lingualBraces) {
        this.lingualBraces = lingualBraces;
    }

    public boolean getInvisalign() {
        return invisalign;
    }

    public void setInvisalign(boolean invisalign) {
        this.invisalign = invisalign;
    }

}
