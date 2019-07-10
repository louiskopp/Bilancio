package edu.hope.cs.bilancioandroid.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import edu.hope.cs.bilancioandroid.R;

public class SelectIcon extends AppCompatActivity {

    IconAdapter iconAdapter;
    String category;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon);
        iconAdapter = new IconAdapter(this);
        ListView listView = (ListView) findViewById(R.id.new_icon_list);
        listView.setAdapter(iconAdapter);
        category = getIntent().getStringExtra("category");
        description = getIntent().getStringExtra("description");

    }

    public void getIcon(int image) {
        Intent intent = new Intent(getApplicationContext(), AddNewBudget.class);
        intent.putExtra("icon", image);
        intent.putExtra("category", category);
        intent.putExtra("description", description);
        startActivity(intent);
    }
}
