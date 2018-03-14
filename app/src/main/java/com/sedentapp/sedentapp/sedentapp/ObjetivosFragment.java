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
        ImageButton button = (ImageButton) getView().findViewById(R.id.boton_edit_pasos);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Button Clicked", Toast.LENGTH_LONG).show();
                String pasos = getResources().getString(R.string.pasos_titulo);
                personalizarObjetivoDialog(pasos);
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
    public void personalizarObjetivoDialog(String objetivo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_objetivos, null);
        dialogBuilder.setView(dialogView);

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
                //hacer algo con edt.getText().toString();
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
    }

}
