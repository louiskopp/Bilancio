package edu.hope.cs.bilancioandroid.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.hope.cs.bilancioandroid.R;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;

public class Contacts extends AppCompatActivity {
    static final int CONTACT = 1;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String fromSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(getResources().getString(R.string.channelName), Context.MODE_PRIVATE);
        editor = prefs.edit();
        int size = prefs.getInt("Text Size", 18);
        if(prefs.getString("Teaching Mode", "Off").equals("On")) {
            setTheme(R.style.TeachingMode);
        } else {
            setTheme(prefs.getInt("Theme", R.style.White));
        }
        setContentView(R.layout.activity_contacts);
        fromSettings = getIntent().getStringExtra("settings");
        TextView loginMsg = findViewById(R.id.loginMsg);
        if(prefs.getInt("Text Size", 2) != 2) {
            loginMsg.setTextSize(size);
        }
        final EditText name = (EditText) findViewById(R.id.contactsName);
        name.setTextSize(size);
        name.setInputType(TYPE_TEXT_FLAG_CAP_WORDS);
        final EditText phone = (EditText) findViewById(R.id.contactPhone);
        phone.setTextSize(size);
        TextView goToLogin = (TextView) findViewById(R.id.go_to_budget);
        goToLogin.setTextSize(size);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = name.getText().toString().trim();
                String phoneNumber = phone.getText().toString().trim();
                if (contactName.equals("") || phoneNumber.equals("")) {
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Contacts.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("You need to have a contact set here in case something happens.").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("No contact found!");
                    alert.show();

                }
                //phone number must be 10 digits long
                else if(!phoneNumber.matches("^[0-9]{10}$")){
                    final AlertDialog.Builder a_builder1 = new AlertDialog.Builder(Contacts.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder1.setMessage("You need to enter a real phone number. \nEx: (555) 555-5555").setCancelable(false)
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert1 = a_builder1.create();
                    alert1.setTitle("Not a real phone number!");
                    alert1.show();
                }
                else {
                    editor.putString("Name", contactName);
                    editor.putString("Phone", phoneNumber);
                    editor.apply();
                    if(prefs.getString("Settings", "No").equals("Yes")){
                        Intent nextIntent = new Intent(getApplicationContext(), AdvancedSettings.class);
                        startActivity(nextIntent);
                    }
                    else {
                        Intent nextIntent = new Intent(getApplicationContext(), Password.class);
                        startActivity(nextIntent);
                    }
                }

            }
        });

        TextView contacts = (TextView) findViewById(R.id.goToContacts);
        contacts.setTextSize(size);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(contactIntent,CONTACT);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request it is that we're responding to
        if (requestCode == CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = intent.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                int column2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(column2);
                number = number.replaceAll("[^\\d.]", "");
                EditText contactName = findViewById(R.id.contactsName);
                contactName.setText(name);
                EditText contactNumber = findViewById(R.id.contactPhone);
                contactNumber.setText(number);
            }
        }
    }

}
