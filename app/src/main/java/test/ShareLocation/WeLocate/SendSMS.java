package test.ShareLocation.WeLocate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SendSMS extends Activity {

    EditText number, name;
    Button etSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        etSend = (Button) findViewById(R.id.send);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);

        etSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etNumber = ""+number.getText().toString();
                String etName = ""+name.getText().toString();

                Log.i("Send SMS", "");

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");

                smsIntent.putExtra("address"  , new String(etNumber));

                smsIntent.putExtra("sms_body"  , "I'm "+ etName +" inviting you to join Welocate. And share your location with your friends and family ");

                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SendSMS.this,
                            "SMS FAILED !", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_sm, menu);
        return true;
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
