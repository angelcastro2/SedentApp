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
import java.util.Locale;
import java.util.Map;


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

    String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

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
        // Si no se tienen datos, descomentar la siguiente línea, acceder a la función y crear datos (POR LO MENOS) para el día actual
        // (porque es donde se inicia el fragment de actividad por defecto). Ejecutar la app, acceder una vez a actividad, y luego comentar la linea
        // y volver a rearrancar.
        // rellenarDatos();

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
                    moreResults = settearListView("Mensual", -1, false);
                } else {
                   moreResults = settearListView("Anual", -1, false);
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
                     moreResults = settearListView("Mensual", 1, false);
                } else {
                    moreResults = settearListView("Anual", 1, false);
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
                dateList.add(registroPaso.getHora()-1+":00 - "+registroPaso.getHora()+":00");
                stepList.add(String.valueOf(registroPaso.getPasos()));
            }
            TextView textView = (TextView) view.findViewById(R.id.texto_fecha_grafica);
            textView.setText(fecha.get(Calendar.DAY_OF_MONTH)+"/"+fecha.get(Calendar.MONTH)+"/"+fecha.get(Calendar.YEAR));
            dateArray = dateList.toArray(new String[dateList.size()]);
            stepsArray = stepList.toArray(new String[stepList.size()]);


            listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
            listView = (ListView)view.findViewById(R.id.listview);
            listView.setAdapter(listActividad);

            int numHoras = 24;
            String[] labels = new String[numHoras];//horizontal axis
            float[] values = new float[numHoras]; //values
            for (int i = 0; i<numHoras; i++) {
                labels[i] = String.valueOf(i);
                if (i < registroPasos.size()) {
                    values[i] = registroPasos.get(i).getPasos();
                } else {
                    values[i] = 0;
                }
            }
            createChart(labels, values);

        } else if (rango.equalsIgnoreCase("Mensual")) {
            if (primeraVez) {
                fecha = calendar;
            } else {
                fecha.add(Calendar.MONTH, cambioDeFecha);
            }

            Map<Integer, Integer> registroPasosMap = this.registroPasosService.getMapaPasosMensualesByDia(this.getContext(),
                    fecha.get(Calendar.MONTH), fecha.get(Calendar.YEAR));

            if (registroPasosMap.isEmpty()){
                if(!primeraVez) {
                    fecha.add(Calendar.MONTH, -cambioDeFecha);
                }
                return false;
            }

            dateList.clear();
            stepList.clear();

            Locale spanish = new Locale("es", "ES");
            for (Map.Entry<Integer, Integer> entry : registroPasosMap.entrySet()){
                Calendar fechaPrint = Calendar.getInstance();
                fechaPrint.set(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), entry.getKey());
                dateList.add(String.valueOf(entry.getKey())+", "+fechaPrint.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish));
                stepList.add(String.valueOf(entry.getValue()));
            }

            TextView textView = (TextView) view.findViewById(R.id.texto_fecha_grafica);
            textView.setText(MES[fecha.get(Calendar.MONTH)]);
            dateArray = dateList.toArray(new String[dateList.size()]);
            stepsArray = stepList.toArray(new String[stepList.size()]);


            listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
            listView = (ListView)view.findViewById(R.id.listview);
            listView.setAdapter(listActividad);

            int numDias = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
            String[] labels = new String[numDias];
            float[] values = new float[numDias]; //values
            for (int i = 0; i<numDias; i++){
                labels[i] = String.valueOf(i+1);
                if (registroPasosMap.containsKey(i+1)) {
                    values[i] = registroPasosMap.get(i+1);
                } else {
                    values[i] = 0;
                }
            }
            createChart(labels, values);

        } else {
            if (primeraVez) {
                fecha = calendar;
            } else {
                fecha.add(Calendar.YEAR, cambioDeFecha);
            }

            Map<Integer, Integer> registroPasosMap = this.registroPasosService.getMapaPasosAnualesByMes(getContext(),fecha.get(Calendar.YEAR));


            dateList.clear();
            stepList.clear();

            for (Map.Entry<Integer, Integer> entry : registroPasosMap.entrySet()){
                dateList.add(MES[entry.getKey()]);
                stepList.add(String.valueOf(entry.getValue()));
            }

            TextView textView = (TextView) view.findViewById(R.id.texto_fecha_grafica);
            textView.setText(String.valueOf(fecha.get(Calendar.YEAR)));
            dateArray = dateList.toArray(new String[dateList.size()]);
            stepsArray = stepList.toArray(new String[stepList.size()]);


            listActividad = new CustomListActividadAdapter(this.getActivity(), dateArray, stepsArray);
            listView = (ListView)view.findViewById(R.id.listview);
            listView.setAdapter(listActividad);

            int numMeses = 12;
            String[] labels = new String[numMeses];
            float[] values = new float[numMeses]; //values
            for (int i = 0; i<numMeses; i++){
                labels[i] = String.valueOf(i+1);
                if (registroPasosMap.containsKey(i+1)) {
                    values[i] = registroPasosMap.get(i+1);
                } else {
                    values[i] = 0;
                }
            }
            createChart(labels, values);

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

    private void rellenarDatos(){

        // TENED EN CUENTA QUE LOS MESES EN CALENDAR EMPIEZAN EN 0 !!!
        
        this.registroPasosService.save(new RegistroPasos(2,3,2017,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2017,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(3,3,2017,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2017,8,200),this.getContext());

        this.registroPasosService.save(new RegistroPasos(2,4,2017,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2017,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(3,4,2017,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2017,8,200),this.getContext());



        this.registroPasosService.save(new RegistroPasos(3,5,2017,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2017,8,200),this.getContext());


        this.registroPasosService.save(new RegistroPasos(2,5,2017,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2017,8,300),this.getContext());

        /* AÑO 2 */

        this.registroPasosService.save(new RegistroPasos(2,3,2018,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2018,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(3,3,2018,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2018,8,200),this.getContext());

        this.registroPasosService.save(new RegistroPasos(5,4,2018,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(5,4,2018,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(6,4,2018,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(6,4,2018,8,200),this.getContext());



        this.registroPasosService.save(new RegistroPasos(3,5,2018,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2018,8,200),this.getContext());


        this.registroPasosService.save(new RegistroPasos(2,5,2018,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2018,8,300),this.getContext());

        /* AÑO 3 */

        this.registroPasosService.save(new RegistroPasos(2,3,2019,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,3,2019,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(3,3,2019,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,3,2019,8,200),this.getContext());

        this.registroPasosService.save(new RegistroPasos(2,4,2019,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,4,2019,8,300),this.getContext());

        this.registroPasosService.save(new RegistroPasos(3,4,2019,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,4,2019,8,200),this.getContext());



        this.registroPasosService.save(new RegistroPasos(3,5,2019,1,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,2,600),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,4,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,5,220),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,6,160),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,7,300),this.getContext());
        this.registroPasosService.save(new RegistroPasos(3,5,2019,8,200),this.getContext());
                                                               
                                                               
        this.registroPasosService.save(new RegistroPasos(2,5,2019,1,200),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,2,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,3,100),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,4,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,5,720),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,6,360),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,7,500),this.getContext());
        this.registroPasosService.save(new RegistroPasos(2,5,2019,8,300),this.getContext());
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
