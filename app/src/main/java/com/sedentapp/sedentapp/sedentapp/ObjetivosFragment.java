package com.sedentapp.sedentapp.sedentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ObjetivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjetivosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private int objetivo_pasos = 7000;
    private int objetivo_distancia = 7;
    private int objetivo_peso = 75;
    private int objetivo_tiempo_actividad = 2;

    public ObjetivosFragment() {
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
    public static ObjetivosFragment newInstance(String param1, String param2) {
        ObjetivosFragment fragment = new ObjetivosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_objetivos, container, false);
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
        //Inicializamos las preferencias
        Context context = getActivity();
        SharedPreferences objetivosPref = context.getSharedPreferences(
                getString(R.string.pref_objetivos), Context.MODE_PRIVATE);

        //recuperamos los valores de los objetivos y si están activos
        Integer objetivo_pasos = objetivosPref.getInt(getString(R.string.pref_objetivos_pasos), 0);
        Integer objetivo_distancia = objetivosPref.getInt(getString(R.string.pref_objetivos_distancia), 0);
        Integer objetivo_peso = objetivosPref.getInt(getString(R.string.pref_objetivos_peso), 0);
        Integer objetivo_actividad = objetivosPref.getInt(getString(R.string.pref_objetivos_actividad), 0);

        Boolean objetivo_pasos_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_pasos_activo),false);
        Boolean objetivo_distancia_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_distancia_activo),false);
        Boolean objetivo_peso_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_peso_activo),false);
        Boolean objetivo_actividad_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_actividad_activo),false);

        //actualizamos la vista con los valores recuperados
        //primero los textviews
        TextView tv_pasos = (TextView) getView().findViewById(R.id.objetivo_pasos_valor);
        tv_pasos.setText(objetivo_pasos.toString());
        TextView tv_distancia = (TextView) getView().findViewById(R.id.objetivo_distancia_valor);
        tv_distancia.setText(objetivo_distancia.toString());
        TextView tv_peso = (TextView) getView().findViewById(R.id.objetivo_peso_valor);
        tv_peso.setText(objetivo_peso.toString());
        TextView tv_actividad = (TextView) getView().findViewById(R.id.objetivo_actividad_valor);
        tv_actividad.setText(objetivo_actividad.toString());
        //luego los objetivos activos o inactivos
        cambioColorObjetivo(objetivo_pasos_activo,objetivo_distancia_activo,objetivo_peso_activo,objetivo_actividad_activo);



        ImageButton button = (ImageButton) getView().findViewById(R.id.boton_edit_pasos);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                String pasos = getResources().getString(R.string.pasos_titulo);
                personalizarObjetivoDialog(pasos);
            }
        });

        ImageButton buttondistancia = (ImageButton) getView().findViewById(R.id.boton_edit_distancia);
        buttondistancia.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                String distancia = getResources().getString(R.string.distancia);
                personalizarObjetivoDialog(distancia);
            }
        });


        ImageButton buttonpeso = (ImageButton) getView().findViewById(R.id.boton_edit_peso);
        buttonpeso.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                String peso = getResources().getString(R.string.peso);
                personalizarObjetivoDialog(peso);
            }
        });

        ImageButton buttonTiempoActividad = (ImageButton) getView().findViewById(R.id.boton_edit_tiempoActividad);
        buttonTiempoActividad.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                String tiempoActividad = getResources().getString(R.string.tiempoActividad);
                personalizarObjetivoDialog(tiempoActividad);
            }
        });

    }



    public int getObjetivo_pasos() {
        return objetivo_pasos;
    }

    public void setObjetivo_pasos(int objetivo_pasos) {
        this.objetivo_pasos = objetivo_pasos;
    }

    public int getObjetivo_distancia() {
        return objetivo_distancia;
    }

    public void setObjetivo_distancia(int objetivo_distancia) {
        this.objetivo_distancia = objetivo_distancia;
    }

    public int getObjetivo_peso() {
        return objetivo_peso;
    }

    public void setObjetivo_peso(int objetivo_peso) {
        this.objetivo_peso = objetivo_peso;
    }

    public int getObjetivo_tiempo_actividad() {
        return objetivo_tiempo_actividad;
    }

    public void setObjetivo_tiempo_actividad(int objetivo_tiempo_actividad) {
        this.objetivo_tiempo_actividad = objetivo_tiempo_actividad;
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


    /**
     * Abre el alert dialog de editar objetivos
     * @param objetivo nombre del objetivo a editar para mostrar en un textview
     */
    public void personalizarObjetivoDialog(final String objetivo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_objetivos, null);
        dialogBuilder.setView(dialogView);
        //cargamos las sharedpreferences
        Context context = getActivity();
        final SharedPreferences objetivosPref = context.getSharedPreferences(
                getString(R.string.pref_objetivos), Context.MODE_PRIVATE);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText_edit_objetivo);
        //recuperamos el textview y ponemos el texto segun el tipo de objetivo
        TextView tv_tipo_objetivo = dialogView.findViewById(R.id.textview_tipo_objetivo_alertdialog);
        tv_tipo_objetivo.setText(objetivo);

        String titulo = getResources().getString(R.string.objetivos_dialog_titulo);
        String guardar = getResources().getString(R.string.guardar);
        String cancelar = getResources().getString(R.string.cancelar);
        dialogBuilder.setTitle(titulo);
        dialogBuilder.setPositiveButton(guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CheckBox ck_activo = (CheckBox) dialogView.findViewById(R.id.objetivo_dialog_check_activo);
                if (edt.getText().toString().equals("")){
                    edt.setError(getString(R.string.texto_vacio));
                }else {
                    //recuperamos las si está activo para actualizarlo luego con el valor adecuado
                    Boolean objetivo_pasos_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_pasos_activo),false);
                    Boolean objetivo_distancia_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_distancia_activo),false);
                    Boolean objetivo_peso_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_peso_activo),false);
                    Boolean objetivo_actividad_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_actividad_activo),false);

                    //comprobamos los cambios y actualizamos
                    SharedPreferences.Editor editor = objetivosPref.edit();
                    if (objetivo == getResources().getString(R.string.pasos_titulo)){

                        editor.putInt(getString(R.string.pref_objetivos_pasos), Integer.parseInt(edt.getText().toString()));
                        TextView tv_pasos = (TextView) getView().findViewById(R.id.objetivo_pasos_valor);
                        tv_pasos.setText(edt.getText().toString());
                        editor.putBoolean(getString(R.string.pref_objetivos_pasos_activo),ck_activo.isChecked());
                        cambioColorObjetivo(ck_activo.isChecked(),objetivo_distancia_activo,objetivo_peso_activo,objetivo_actividad_activo);
                    }else if(objetivo == getResources().getString(R.string.distancia)){

                        editor.putInt(getString(R.string.pref_objetivos_distancia), Integer.parseInt(edt.getText().toString()));
                        TextView tv_distancia = (TextView) getView().findViewById(R.id.objetivo_distancia_valor);
                        tv_distancia.setText(edt.getText().toString());
                        editor.putBoolean(getString(R.string.pref_objetivos_distancia_activo),ck_activo.isChecked());
                        cambioColorObjetivo(objetivo_pasos_activo,ck_activo.isChecked(),objetivo_peso_activo,objetivo_actividad_activo);
                    }else if(objetivo == getResources().getString(R.string.peso)){

                        editor.putInt(getString(R.string.pref_objetivos_peso), Integer.parseInt(edt.getText().toString()));
                        TextView tv_peso = (TextView) getView().findViewById(R.id.objetivo_peso_valor);
                        tv_peso.setText(edt.getText().toString());
                        editor.putBoolean(getString(R.string.pref_objetivos_peso_activo),ck_activo.isChecked());
                        cambioColorObjetivo(objetivo_pasos_activo,objetivo_distancia_activo,ck_activo.isChecked(),objetivo_actividad_activo);
                    }else {

                        editor.putInt(getString(R.string.pref_objetivos_actividad), Integer.parseInt(edt.getText().toString()));
                        TextView tv_actividad = (TextView) getView().findViewById(R.id.objetivo_actividad_valor);
                        tv_actividad.setText(edt.getText().toString());
                        editor.putBoolean(getString(R.string.pref_objetivos_actividad_activo),ck_activo.isChecked());
                        cambioColorObjetivo(objetivo_pasos_activo,objetivo_distancia_activo,objetivo_peso_activo,ck_activo.isChecked());
                    }
                    editor.commit();
                }


            }
        });
        dialogBuilder.setNegativeButton(cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
        //una vez creado cargamos los valores de pasos
        CheckBox ck_activo = (CheckBox) dialogView.findViewById(R.id.objetivo_dialog_check_activo);
        if (objetivo == getResources().getString(R.string.pasos_titulo)){
            Integer objetivo_pasos = objetivosPref.getInt(getString(R.string.pref_objetivos_pasos), 0);
            edt.setText(objetivo_pasos.toString());
            Boolean objetivo_pasos_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_pasos_activo),false);
            ck_activo.setChecked(objetivo_pasos_activo);
        }else if(objetivo == getResources().getString(R.string.distancia)){

            Integer objetivo_distancia = objetivosPref.getInt(getString(R.string.pref_objetivos_distancia), 0);
            edt.setText(objetivo_distancia.toString());
            Boolean objetivo_distancia_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_distancia_activo),false);
            ck_activo.setChecked(objetivo_distancia_activo);
        }else if(objetivo == getResources().getString(R.string.peso)){

            Integer objetivo_peso = objetivosPref.getInt(getString(R.string.pref_objetivos_peso), 0);
            edt.setText(objetivo_peso.toString());
            Boolean objetivo_peso_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_peso_activo),false);
            ck_activo.setChecked(objetivo_peso_activo);
        }else {

            Integer objetivo_actividad = objetivosPref.getInt(getString(R.string.pref_objetivos_actividad), 0);
            edt.setText(objetivo_actividad.toString());
            Boolean objetivo_actividad_activo = objetivosPref.getBoolean(getString(R.string.pref_objetivos_actividad_activo),false);
            ck_activo.setChecked(objetivo_actividad_activo);
        }

    }

    private void cambioColorObjetivo(boolean objetivo_pasos_activo, boolean objetivo_distancia_activo, boolean objetivo_peso_activo, boolean objetivo_actividad_activo){
        CardView cv_pasos = (CardView) getView().findViewById(R.id.objetivo_pasos_base_layout);
        CardView cv_distancia = (CardView) getView().findViewById(R.id.objetivo_distancia_base_layout);
        CardView cv_peso = (CardView) getView().findViewById(R.id.objetivo_peso_base_layout);
        CardView cv_actividad = (CardView) getView().findViewById(R.id.objetivo_actividad_base_layout);
        if (objetivo_pasos_activo){
            cv_pasos.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else {
            cv_pasos.setCardBackgroundColor(getResources().getColor(R.color.colorAccent_light));
        }
        if (objetivo_distancia_activo){
            cv_distancia.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else {
            cv_distancia.setCardBackgroundColor(getResources().getColor(R.color.colorAccent_light));
        }
        if (objetivo_peso_activo){
            cv_peso.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else {
            cv_peso.setCardBackgroundColor(getResources().getColor(R.color.colorAccent_light));
        }
        if (objetivo_actividad_activo){
            cv_actividad.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else {
            cv_actividad.setCardBackgroundColor(getResources().getColor(R.color.colorAccent_light));
        }
    }


}
