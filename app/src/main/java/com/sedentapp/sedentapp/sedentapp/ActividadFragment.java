package com.sedentapp.sedentapp.sedentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ActividadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActividadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private String s1 = "";


    String[] dateArray = {"01/04/2018","02/04/2018","03/04/2018","04/04/2018","05/04/2018",
            "06/04/2018","07/04/2018","08/04/2018","09/04/2018","10/04/2018","11/04/2018",
            "12/04/2018","13/04/2018","14/04/2018","15/04/2018","16/04/2018","17/04/2018",
            "18/04/2018","19/04/2018","20/04/2018","21/04/2018","22/04/2018","23/04/2018",
            "24/04/2018","25/04/2018","26/04/2018","27/04/2018","28/04/2018","29/04/2018","30/04/2018"};

    String[] stepsArray = {
            "10500", "8000", "6900", "8000", "6500", "10500", "8000", "6900", "8000", "6500",
            "10500", "8000", "6900", "8000", "6500", "10500", "8000", "6900", "8000", "6500",
            "10500", "8000", "6900", "8000", "6500", "10500", "8000", "6900", "8000", "6500"
    };


    ListView listView;


    public ActividadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ObjetivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActividadFragment newInstance(String param1, String param2) {
        ActividadFragment fragment = new ActividadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_actividad, container, false);

        CustomListActividadAdapter listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
        listView = (ListView)view.findViewById(R.id.listview);
        listView.setAdapter(listActividad);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageButton leftButton = (ImageButton) getView().findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Left Clicked", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton rightButton = (ImageButton) getView().findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button Right Clicked", Toast.LENGTH_LONG).show();
            }
        });

        Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
        s1 = spinnerActividad.getSelectedItem().toString();
        spinnerActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
                String s = spinnerActividad.getSelectedItem().toString();
                if(s!=s1){
                    Toast.makeText(getActivity(), "Spinner on change", Toast.LENGTH_LONG).show();
                    s1 = s;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }



}
