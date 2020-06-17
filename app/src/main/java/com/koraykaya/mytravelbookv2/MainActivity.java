package com.koraykaya.mytravelbookv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        static ArrayList<String> names = new ArrayList<>();
        static ArrayList<LatLng> locations = new ArrayList<>();
        static ArrayAdapter arrayAdapter ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);
        return super.onCreateOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== R.id.add_place) {
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("info" , "new");
            startActivity(intent);


        }
            return super.onOptionsItemSelected(item);
        }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        try {
            MapsActivity.database = this.openOrCreateDatabase("Places",MODE_PRIVATE,null);

            Cursor cursor = MapsActivity.database.rawQuery("SELECT * FROM places",null);
            int nameIx = cursor.getColumnIndex("name");
            int latIx = cursor.getColumnIndex("latitude");
            int longIx = cursor.getColumnIndex("longitude");


            while (cursor.moveToNext()) {

                String nameFromDatabase = cursor.getString(nameIx);
                String latFromDatabase = cursor.getString(latIx);
                String longFromDatabase = cursor.getString(longIx);

                names.add(nameFromDatabase);
                Double l1= Double.parseDouble(latFromDatabase);
                Double l2 = Double.parseDouble(longFromDatabase);

                LatLng locFromDatabase = new LatLng(l1,l2);
                locations.add(locFromDatabase);

            }

            cursor.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,names) ;
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("info" , "old");
                intent.putExtra("position" , position);

                startActivity(intent);
            }
        });
    }


}