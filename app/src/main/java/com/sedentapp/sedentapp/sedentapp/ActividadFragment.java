package com.sedentapp.sedentapp.sedentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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

    private RegistroPasosService registroPasosService;

//    private OnFragmentInteractionListener mListener;

    String[] dateArray = {};
    String[] stepsArray = {};

    List<String> dateList = new ArrayList<String>();
    List<String> stepList = new ArrayList<String>();

    CustomListActividadAdapter listActividad;

    ListView listView;

    Calendar fecha;


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


        this.registroPasosService = new RegistroPasosService();
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,1,100),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,2,600),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,3,100),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,4,300),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,5,220),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,6,160),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,7,300),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(3,4,2018,8,200),this.getContext());
//
//
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,1,200),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,2,100),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,3,100),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,4,500),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,5,720),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,6,360),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,7,500),this.getContext());
//        this.registroPasosService.save(new RegistroPasos(2,4,2018,8,300),this.getContext());

        Calendar calendar = Calendar.getInstance();
        List<RegistroPasos> registroPasos = this.registroPasosService.getRegistroPasosByDia(this.getContext(),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        fecha = calendar;

        for (RegistroPasos registroPaso : registroPasos){
            dateList.add(registroPaso.getDia()+"/"+registroPaso.getMes()+"/"+registroPaso.getAno()+ " " + registroPaso.getHora()+":00");
            stepList.add(String.valueOf(registroPaso.getPasos()));
        }

        dateArray = dateList.toArray(new String[dateList.size()]);
        stepsArray = stepList.toArray(new String[stepList.size()]);

        listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
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
                Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
                String s = spinnerActividad.getSelectedItem().toString();
                boolean moreResults;
                if (s.equalsIgnoreCase("Diario")){
                    moreResults = settearListView("Diario", -1, false);
                } else if (s.equalsIgnoreCase("Mensual")) {
                    moreResults=true;
                } else {
                    moreResults=true;
                }

                if (!moreResults){
                    Toast.makeText(getActivity(), "No hay más resultados anteriores.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton rightButton = (ImageButton) getView().findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
                String s = spinnerActividad.getSelectedItem().toString();
                boolean moreResults;
                if (s.equalsIgnoreCase("Diario")){
                    moreResults = settearListView("Diario", 1, false);
                } else if (s.equalsIgnoreCase("Mensual")) {
                    moreResults=true;
                } else {
                    moreResults=true;
                }

                if (!moreResults){
                    Toast.makeText(getActivity(), "No hay más resultados posteriores.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
        spinnerActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinnerActividad = (Spinner) getView().findViewById(R.id.spinner_actividad);
                String s = spinnerActividad.getSelectedItem().toString();
                if (s.equalsIgnoreCase("Diario")){
                    settearListView("Diario",0,true);
                } else if (s.equalsIgnoreCase("Mensual")) {
                    settearListView("Mensual",0,true);
                } else {
                    settearListView("Anual",0,true);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        settearListView("Diario", 0,true);

    }

    private boolean settearListView(String rango, Integer cambioDeFecha, Boolean primeraVez){
        View view = getView();

        Calendar calendar = Calendar.getInstance();

        listActividad.notifyDataSetChanged();
        if (rango.equalsIgnoreCase("Diario")) {
            if (primeraVez) {
                fecha = calendar;
            } else {
                fecha.add(Calendar.DAY_OF_MONTH, cambioDeFecha);
            }
            List<RegistroPasos> registroPasos = this.registroPasosService.getRegistroPasosByDia(this.getContext(),
                    fecha.get(Calendar.DAY_OF_MONTH), fecha.get(Calendar.MONTH), fecha.get(Calendar.YEAR));

            if (registroPasos.isEmpty()){
                if(!primeraVez) {
                    fecha.add(Calendar.DAY_OF_MONTH, -cambioDeFecha);
                }
                return false;
            }

            dateList.clear();
            stepList.clear();

            for (RegistroPasos registroPaso : registroPasos){
                dateList.add(registroPaso.getDia()+"/"+registroPaso.getMes()+"/"+registroPaso.getAno()+ " " + registroPaso.getHora()+":00");
                stepList.add(String.valueOf(registroPaso.getPasos()));
            }
            TextView textView = (TextView) view.findViewById(R.id.texto_fecha_grafica);
            textView.setText(fecha.get(Calendar.DAY_OF_MONTH)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.YEAR));
            dateArray = dateList.toArray(new String[dateList.size()]);
            stepsArray = stepList.toArray(new String[stepList.size()]);


            listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
            listView = (ListView)view.findViewById(R.id.listview);
            listView.setAdapter(listActividad);


            String[] labels = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};//horizontal axis
            float[] values = new float[24]; //values
            for (int i = 0; i<24; i++) {
                if (i < registroPasos.size()) {
                    values[i] = registroPasos.get(i).getPasos();
                } else {
                    values[i] = 0;
                }
            }
            createChart(labels, values);

        } else if (rango.equalsIgnoreCase("Mensual")) {
//            long a = registroPasosService.getPasosByDia(this.getContext(),
//                    fecha.get(Calendar.DAY_OF_MONTH), fecha.get(Calendar.MONTH), fecha.get(Calendar.YEAR));
        } else {

        }

        return true;
    }

    private void createChart(String[] labels, float[] values) {
        BarChartView chart = (BarChartView) this.getActivity().findViewById(R.id.chart);
        //llamamos a reset para evitar un crash de la app al volver del onPause
        chart.reset();
        BarSet dataset = new BarSet(labels, values);
        dataset.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        chart.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        chart.addData(dataset);
        chart.setLabelsColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        chart.setAxisColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        chart.show();
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
