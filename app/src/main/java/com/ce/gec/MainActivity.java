package com.ce.gec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class MainActivity extends AppCompatActivity {
        DatabaseReference databaseReference;
        ValueEventListener eventListener;
        RecyclerView recyclerView;
        List<DataClass> dataList;
        MyAdapter adapter;
    SearchView searchView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            recyclerView = findViewById(R.id.recyclerView);
            searchView = findViewById(R.id.search);
            searchView.clearFocus();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
//            gridLayoutManager.setReverseLayout(true);
//            gridLayoutManager.setStackFromEnd(false);
            recyclerView.setLayoutManager(gridLayoutManager);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            dataList = new ArrayList<>();

            adapter = new MyAdapter(MainActivity.this, dataList);
            recyclerView.setAdapter(adapter);

            databaseReference = FirebaseDatabase.getInstance().getReference("Departments");
            dialog.show();
            eventListener = databaseReference.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Long totalItems = snapshot.getChildrenCount();
// Create a Toast object.
                    Toast.makeText(MainActivity.this, "Total Notice : ( " + totalItems + " )", Toast.LENGTH_LONG).show();
                    dataList.clear();
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        DataClass dataClass = itemSnapshot.getValue(DataClass.class);

                        dataClass.setKey(itemSnapshot.getKey());

                        dataList.add(dataClass);
                    }
                    Collections.reverse(dataList);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    dialog.dismiss();
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    searchList(newText);
                    return true;
                }
            });

        } public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
    }