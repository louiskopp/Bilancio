package edu.hope.cs.bilancioandroid.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.hope.cs.bilancioandroid.Controller.IntentIntegrator;
import edu.hope.cs.bilancioandroid.Controller.IntentResult;
import edu.hope.cs.bilancioandroid.R;

public class Prebuilt extends AppCompatActivity implements View.OnClickListener {

    EditText idNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TeachingMode);
        setContentView(R.layout.activity_prebuilt);
        idNumber = (EditText) findViewById(R.id.input_id);
        TextView barcode = (TextView) findViewById(R.id.open_barcode);
        barcode.setOnClickListener(this);
        final TextView go = (TextView) findViewById(R.id.go_prebuilt);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idNumber.getText().toString();
                if(!id.matches("\\d{1}\\.\\d{1}\\.\\d{1}\\.\\d{8}")){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(Prebuilt.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    a_builder.setMessage("Please make sure the entered ID follows the format 0.0.0.00000000").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Unrecognized ID!");
                    alert.show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Homework.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void onClick(View v) {

        if (v.getId() == R.id.open_barcode) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            idNumber.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
