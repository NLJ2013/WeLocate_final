package test.ShareLocation.WeLocate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;


public class Request extends Activity {

    String message;
    Button etEmail, send;
    public Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_request);

    }
    public void buttons(View view) {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://fb.me/420870408092282";
        previewImageUrl = "https://lh3.ggpht.com/46wd7MhLsrE-RdSKERtAambeUGunerYb8sMsIyocdtXlMQbNsmujzLlah37wM8eDA0Rx=h900";

        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(appLinkUrl)
                .setPreviewImageUrl(previewImageUrl)
                .build();
        AppInviteDialog.show(this, content);
    }






    public void buttonClick(){

        etEmail = (Button)findViewById(R.id.email);
        etEmail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Request.this, SendEmail.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClick(){

        send = (Button)findViewById(R.id.send);



        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Request.this, SendSMS.class);
                        startActivity(intent);
                    }
                }
        );

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
