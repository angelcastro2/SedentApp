package com.sedentapp.sedentapp.sedentapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by elena on 14/03/2018.
 */

public class CustomListActividadAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final String[] dateArray;

    //to store the list of countries
    private final String[] stepsArray;

    public CustomListActividadAdapter(Activity context, String[] dateArray, String[] stepsArray){

        super(context,R.layout.list_row_actividad , dateArray);

        this.context=context;
        this.dateArray = dateArray;
        this.stepsArray = stepsArray;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row_actividad, null,true);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.textDateID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.textStepsID);

        nameTextField.setText(dateArray[position]);
        infoTextField.setText(stepsArray[position] + " pasos");

        return rowView;

    };
}
