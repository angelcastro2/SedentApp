package com.sedentapp.sedentapp.sedentapp;

import android.Manifest;
import android.app.Activity;
//import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PerfilFragment extends Fragment {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    boolean init_calibration_flag = true;

    int MY_PERMISSIONS_REQUEST_FINE_LOCATION;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void StoreCalibrationInitLocation(Location location){
        SharedPreferences sharedInitialLocation = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedInitialLocation.edit();
        editor.putFloat("initialLatitude", (float)location.getLatitude());
        editor.putFloat("initialLongitude", (float)location.getLongitude());
        editor.commit();

        init_calibration_flag = false;
    }

    public void StoreCalibrationDistance(Location location){
        SharedPreferences initialLocation = getActivity().getPreferences(Context.MODE_PRIVATE);
        float initialLatitude = initialLocation.getFloat("initialLatitude", 0);
        float initialLongitude = initialLocation.getFloat("initialLongitude", 0);

        SharedPreferences calibrationDistance = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = calibrationDistance.edit();
        editor.putFloat("laditudeDistance", initialLatitude - (float)location.getLatitude());
        editor.putFloat("longitudeDistance", initialLongitude - (float) location.getLongitude());
        editor.commit();

        // reset the calibration flag to repeat calibration
        init_calibration_flag = true;

        // Show final distance, must be be modified
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Calibración de pasos");
        alertDialog.setMessage("La distancia recorrida es: \n" + calibrationDistance.getFloat("latitudeDistance", 0));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle b = intent.getBundleExtra("location");
            Location lastKnownLoc = (Location) b.getParcelable("location");
            if (lastKnownLoc != null) {
                if ( init_calibration_flag ){
                    StoreCalibrationInitLocation(lastKnownLoc);
                }else{
                    StoreCalibrationDistance(lastKnownLoc);
                }
            }
        }
    };

    private void checkLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Context context = getActivity();
        final SharedPreferences perfilPref = context.getSharedPreferences(
                getString(R.string.pref_perfil), Context.MODE_PRIVATE);
        //cargamos los valores iniciales
        String perfil_nombre = perfilPref.getString(getString(R.string.pref_perfil_nombre), "Nombre");
        String perfil_edad = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_edad), 0));
        String perfil_estatura = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_estatura), 0));
        String perfil_peso = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_peso), 0));
        String perfil_zancada = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_zancada), 0));
        TextView tv_nombre_perfil = (TextView) getView().findViewById(R.id.nombre_perfil);
        tv_nombre_perfil.setText(perfil_nombre);
        TextView tv_edad = (TextView) getView().findViewById(R.id.tv_edad_valor);
        tv_edad.setText(perfil_edad);
        TextView tv_estatura = (TextView) getView().findViewById(R.id.tv_estatura_valor);
        tv_estatura.setText(perfil_estatura);
        TextView tv_peso = (TextView) getView().findViewById(R.id.tv_peso_valor);
        tv_peso.setText(perfil_peso);
        TextView tv_zancada = (TextView) getView().findViewById(R.id.tv_zancada_valor);
        tv_zancada.setText(perfil_zancada);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        updateUI(account);

        SignInButton btnSignIn = (SignInButton) getView().findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                signIn();
                Toast.makeText(getActivity(), "Sign in Google", Toast.LENGTH_SHORT).show();

            }
        });

        final Button calibrateStepButton = (Button)getView().findViewById(R.id.calibrate_step_button);
        calibrateStepButton.setText("Calibrar");
        // Capture button clicks
        calibrateStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                checkLocationPermission();
                if (init_calibration_flag) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Calibración de pasos");
                    alertDialog.setMessage("Se obtendrá tu ubicación. Pasea un poco y ...");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    calibrateStepButton.setText("Finalizar");
                                    getActivity().startService(new Intent(getActivity(), ServiceCalibration.class));

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }else{
                    calibrateStepButton.setText("Calibrar");
                    getActivity().startService(new Intent(getActivity(), ServiceCalibration.class));

                }
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocation"));

        /*Button button2 = (Button) getView().findViewById(R.id.fb_login_button);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sign in FB", Toast.LENGTH_SHORT).show();

            }
        });*/

        ImageButton boton_edit = (ImageButton) getView().findViewById(R.id.btn_edit_nombre_perfil);
        boton_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                personalizarDialogPerfilNombreFoto();

            }
        });

        ImageButton boton_edit2 = (ImageButton) getView().findViewById(R.id.btn_edit_datos_perfil);
        boton_edit2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                personalizarDialogDatosPersonales();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            Toast.makeText(getActivity(), "No nulo", Toast.LENGTH_SHORT).show();
            this.getView().findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            this.getView().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            Toast.makeText(getActivity(), "Nulo", Toast.LENGTH_SHORT).show();
            this.getView().findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            this.getView().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }


    public void personalizarDialogPerfilNombreFoto() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_perfil_nombre_foto, null);
        dialogBuilder.setView(dialogView);
        Context context = getActivity();
        final SharedPreferences perfilPref = context.getSharedPreferences(
                getString(R.string.pref_perfil), Context.MODE_PRIVATE);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_nf_nombre);

        String titulo = getResources().getString(R.string.perfil_dialog_nombre);
        String guardar = getResources().getString(R.string.guardar);
        String cancelar = getResources().getString(R.string.cancelar);
        dialogBuilder.setTitle(titulo);
        dialogBuilder.setPositiveButton(guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                SharedPreferences.Editor editor = perfilPref.edit();
                EditText edt = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_nf_nombre);
                edt.setText(edt.getText().toString());
                if (edt.getText().toString().equals("")){
                    edt.setError(getString(R.string.texto_vacio));
                }else {
                    editor.putString(getString(R.string.pref_perfil_nombre), edt.getText().toString());
                    editor.commit();
                    TextView tv_nombre_perfil = (TextView) getView().findViewById(R.id.nombre_perfil);
                    tv_nombre_perfil.setText(edt.getText().toString());
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
        String perfil_nombre = perfilPref.getString(getString(R.string.pref_perfil_nombre),"Nombre");
        EditText edt_pasos = (EditText) getView().findViewById(R.id.edt_dialog_perfil_nf_nombre);
        edt.setText(perfil_nombre);
    }


    public void personalizarDialogDatosPersonales() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_perfil_datos_personales, null);
        dialogBuilder.setView(dialogView);
        Context context = getActivity();
        final SharedPreferences perfilPref = context.getSharedPreferences(
                getString(R.string.pref_perfil), Context.MODE_PRIVATE);

        final EditText edt_edad = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_dp_edad);
        final EditText edt_estatura = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_dp_estatura);
        final EditText edt_peso = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_dp_peso);
        final EditText edt_zancada = (EditText) dialogView.findViewById(R.id.edt_dialog_perfil_dp_zancada);
        //cargamos los valores iniciales
        String perfil_nombre = perfilPref.getString(getString(R.string.pref_perfil_nombre), "Nombre");
        String perfil_edad = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_edad), 0));
        String perfil_estatura = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_estatura), 0));
        String perfil_peso = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_peso), 0));
        String perfil_zancada = Integer.toString(perfilPref.getInt(getString(R.string.pref_perfil_zancada), 0));
        //rellenamos los editText con los valores
        edt_edad.setText(perfil_edad);
        edt_estatura.setText(perfil_estatura);
        edt_peso.setText(perfil_peso);
        edt_zancada.setText(perfil_zancada);

        String titulo = getResources().getString(R.string.datos_personales_perfil);
        String guardar = getResources().getString(R.string.guardar);
        String cancelar = getResources().getString(R.string.cancelar);
        dialogBuilder.setTitle(titulo);
        dialogBuilder.setPositiveButton(guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SharedPreferences.Editor editor = perfilPref.edit();

                if ((edt_edad.getText().toString().equals(""))){
                    //no hacer nada
                }else{
                    editor.putInt(getString(R.string.pref_perfil_edad),Integer.parseInt(edt_edad.getText().toString()));
                    TextView tv_edad = (TextView) getView().findViewById(R.id.tv_edad_valor);
                    tv_edad.setText(edt_edad.getText().toString());
                }
                if ((edt_estatura.getText().toString().equals(""))){
                    //no hacer nada
                }else{
                    editor.putInt(getString(R.string.pref_perfil_estatura),Integer.parseInt(edt_estatura.getText().toString()));
                    TextView tv_estatura = (TextView) getView().findViewById(R.id.tv_estatura_valor);
                    tv_estatura.setText(edt_estatura.getText().toString());
                }
                if ((edt_peso.getText().toString().equals(""))){
                    //no hacer nada
                }else{
                    editor.putInt(getString(R.string.pref_perfil_peso),Integer.parseInt(edt_peso.getText().toString()));
                    TextView tv_peso = (TextView) getView().findViewById(R.id.tv_peso_valor);
                    tv_peso.setText(edt_peso.getText().toString());
                }
                if ((edt_zancada.getText().toString().equals(""))){
                    //no hacer nada
                }else{
                    editor.putInt(getString(R.string.pref_perfil_zancada),Integer.parseInt(edt_zancada.getText().toString()));
                    TextView tv_zancada = (TextView) getView().findViewById(R.id.tv_zancada_valor);
                    tv_zancada.setText(edt_zancada.getText().toString());
                }
                editor.commit();
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
