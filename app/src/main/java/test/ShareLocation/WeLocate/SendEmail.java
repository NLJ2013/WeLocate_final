package test.ShareLocation.WeLocate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendEmail extends Activity {

    EditText etName, etgetEmail;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        sendEmail = (Button) findViewById(R.id.sendEmail);
        etName = (EditText) findViewById(R.id.name);
        etgetEmail = (EditText) findViewById(R.id.getEmail);



        sendEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = ""+etgetEmail.getText().toString();
                String name = ""+etName.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite for WeLocate");

                emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm "+ name +" inviting you to join Welocate. And share your location with your friends and family ");

                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."));

            }
        });
    }


}
