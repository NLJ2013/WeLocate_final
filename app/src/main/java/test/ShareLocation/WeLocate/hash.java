package test.ShareLocation.WeLocate;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nipuna Jayawardana on 5/18/2015.
 */
public class hash extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        printKeyHash();

    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("test.ShareLocation.WeLocate", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("NLJ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
