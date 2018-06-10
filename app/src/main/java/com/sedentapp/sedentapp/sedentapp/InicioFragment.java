package com.sedentapp.sedentapp.sedentapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.RegistroPasos;
import com.sedentapp.sedentapp.sedentapp.entities.registropasos.service.RegistroPasosService;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InicioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final UpdateDailyStepCounterBroadcastReceiver receiver = new UpdateDailyStepCounterBroadcastReceiver();
    private final UpdateInactivityTimeBroadcastReceiver inactivityTimeReceiver = new UpdateInactivityTimeBroadcastReceiver();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_inicio_pasos_valor;

    private OnFragmentInteractionListener mListener;

    private RegistroPasosService registroPasosService;

    private final String TAG = "SedentApp";

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[InicioFragment] onCreate");
        this.registroPasosService = new RegistroPasosService();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "[InicioFragment] onStart");
    }


    public void updateChart(float[] values) {

        BarChartView chart = (BarChartView) this.getActivity().findViewById(R.id.chart);
        //llamamos a reset para evitar un crash de la app al volver del onPause
        chart.reset();
        String[] labels = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};//horizontal axis
        BarSet dataset = new BarSet(labels, values);
        dataset.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        chart.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        chart.addData(dataset);
        chart.setLabelsColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        chart.setAxisColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        chart.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "[InicioFragment] onResume");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.sedentapp.update_daily_step_counter");
        getContext().registerReceiver(receiver, intentFilter);

        IntentFilter inactivityTimeIntentFilter = new IntentFilter();
        inactivityTimeIntentFilter.addAction("com.sedentapp.update_inactivity_time_counter");
        getContext().registerReceiver(inactivityTimeReceiver, inactivityTimeIntentFilter);

        Intent intent = new Intent();
        intent.setAction("com.sedentapp.com.sedentapp.read_daily_step_counter");
        getContext().sendBroadcast(intent);

        Calendar calendar = Calendar.getInstance();
        long steps = this.registroPasosService.getPasosByDia(this.getContext(),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        List<RegistroPasos> registroPasosByDia = this.registroPasosService.getRegistroPasosByDia(this.getContext(),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        updateDailyStepCounter(steps);

        float[] values = buildValues(registroPasosByDia);
        updateChart(values);

        int inactivityHours = getInactivityHours(registroPasosByDia);
        TextView tv_inicio_tiempo_inactividad_valor = (TextView) getView().findViewById(R.id.tv_inicio_tiempo_inactividad_valor);
        tv_inicio_tiempo_inactividad_valor.setText("" + inactivityHours);

    }

    private int getInactivityHours(List<RegistroPasos> registroPasosByDia) {

        int hora1 = 0;
        int hora2 = 0;

        int max_diff = 0;
        int diff = 0;

        for (int i = 0; i < registroPasosByDia.size(); i++) {
            hora2 = registroPasosByDia.get(i).getHora();
            diff = hora2 - hora1;
            if (diff > max_diff) {
                max_diff = diff;
            }
        }

        return max_diff;
    }

    public float[] buildValues(List<RegistroPasos> registroPasosByDia) {

        int numHoras = 24;
        String[] labels = new String[numHoras];//horizontal axis
        float[] values = new float[numHoras]; //values
        for (int i = 0; i<numHoras; i++) {
            labels[i] = String.valueOf(i);
            if (i < registroPasosByDia.size()) {
                values[i] = registroPasosByDia.get(i).getPasos();
            } else {
                values[i] = 0;
            }
        }

        return values;
    }

    public boolean estaVisible(){
        return this.isVisible();
    }

    private class UpdateDailyStepCounterBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(estaVisible()){
                Bundle extras = intent.getExtras();
                long dailyStepCounter = extras.getLong("dailyStepCounter");
                //Toast.makeText(getContext(), "Daily step counter: " + dailyStepCounter, Toast.LENGTH_SHORT);
                updateDailyStepCounter(dailyStepCounter);// update your textView in the main layout
            }
        }
    }

    private class UpdateInactivityTimeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(estaVisible()){
                Bundle extras = intent.getExtras();
                int inactivityTime = extras.getInt("inactivityTime");
                //Toast.makeText(getContext(), "Inactivity time: " + inactivityTime, Toast.LENGTH_SHORT);

                TextView tv_inicio_tiempo_inactividad_valor = (TextView) getView().findViewById(R.id.tv_inicio_tiempo_inactividad_valor);
                tv_inicio_tiempo_inactividad_valor.setText("" + inactivityTime);

            }
        }
    }

    private void updateDailyStepCounter(long dailyStepCounter) {
        Log.d(TAG, "[InicioFragment] updateDailyStepCounter");
        TextView tv_inicio_pasos_valor = (TextView) getView().findViewById(R.id.tv_inicio_pasos_valor);
        tv_inicio_pasos_valor.setText("" + dailyStepCounter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    /*    if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    /*    if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
