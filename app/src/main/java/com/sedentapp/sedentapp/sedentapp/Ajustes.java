package com.sedentapp.sedentapp.sedentapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Ajustes extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Spinner sound_spinner = (Spinner) findViewById(R.id.sound_spinner);
        Spinner led_spinner = (Spinner) findViewById(R.id.led_spinner);
        Spinner vibration_spinner = (Spinner) findViewById(R.id.vibration_spinner);

        sound_spinner.setOnItemSelectedListener(this);
        led_spinner.setOnItemSelectedListener(this);
        vibration_spinner.setOnItemSelectedListener(this);

        List<String> sounds = new ArrayList<String>();
        sounds.add("Fresh.mp3");
        sounds.add("Superhit.mp3");
        sounds.add("Song2");

        List<String> vibrations = new ArrayList<String>();
        vibrations.add("Corta");
        vibrations.add("Larga");

        List<String> colors = new ArrayList<String>();
        colors.add("Rojo");
        colors.add("Verde");
        colors.add("Azul");
        colors.add("Blanco");

        ArrayAdapter<String> soundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sounds);
        ArrayAdapter<String> vibrationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vibrations);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);

        // Drop down layout style - list view with radio button
        soundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vibrationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sound_spinner.setAdapter(soundAdapter);
        vibration_spinner.setAdapter(vibrationAdapter);
        led_spinner.setAdapter(colorAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

     public void onNothingSelected(AdapterView<?> arg0) {
      // TODO Auto-generated method stub
   }

}
