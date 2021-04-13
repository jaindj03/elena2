package com.example.elena2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class wayPtsActivity extends AppCompatActivity {
  ListView list;
  Button addLoc;
  EditText addLocation;
  EditText name;
  ArrayAdapter arrayAdapter;
  ArrayList<String> wayPts;
    DatabaseReference reff;
    places place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_pts);
        list = findViewById(R.id.list);
        addLoc=findViewById(R.id.addLoc);
        addLocation=findViewById(R.id.location);
        name=findViewById(R.id.name);
        wayPts=new ArrayList<>();
        reff= FirebaseDatabase.getInstance().getReference("Location");
        place =new places();

        addLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc=addLocation.getText().toString().trim();
                place.setLocation(loc);
                reff.child(name.getText().toString()).setValue(place);

addLocation.setText(" ");
name.setText(" ");
            }
        });
       reff=FirebaseDatabase.getInstance().getReference("Location").child(name.getText().toString());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
wayPts.clear();
for(DataSnapshot dataSnapshot:snapshot.getChildren()){
places user=dataSnapshot.getValue(places.class);
wayPts.add(user.getLocation());
}
arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,wayPts);
list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(wayPtsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("place",wayPts.get(position));
                intent.putExtra("number",position);
                startActivity(intent);
            }
        });
    }
}