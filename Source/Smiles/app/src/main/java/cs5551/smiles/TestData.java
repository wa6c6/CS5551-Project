package cs5551.smiles;

/**
 * Created by narenpalep on 11/24/16.
 */
import com.firebase.client.Firebase;
public class TestData extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
