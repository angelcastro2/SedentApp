package com.sedentapp.sedentapp.sedentapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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


    @Override
    public void onStart() {
        super.onStart();
        Context context = getActivity();
        final SharedPreferences perfilPref = context.getSharedPreferences(
                getString(R.string.pref_perfil), Context.MODE_PRIVATE);
        String perfil_nombre = perfilPref.getString(getString(R.string.pref_perfil_nombre), "Nombre");
        TextView tv_nombre_perfil = (TextView) getView().findViewById(R.id.nombre_perfil);
        tv_nombre_perfil.setText(perfil_nombre);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        updateUI(account);

        SignInButton btnSignIn = (SignInButton) getView().findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                signIn();
                Toast.makeText(getActivity(), "Sign in Google", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton calibrateStepButton = (ImageButton) getView().findViewById(R.id.calibrate_step_button);
        // Capture button clicks
        calibrateStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Start NewActivity.class
                Intent calibrationIntent = new Intent(getActivity(), CalibrationActivity.class);
                startActivity(calibrationIntent);
            }
        });

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

//        final EditText edt = (EditText) dialogView.findViewById(R.id.editText_edit_objetivo);
//        //recuperamos el textview y ponemos el texto segun el tipo de objetivo
//        TextView tv_tipo_objetivo = dialogView.findViewById(R.id.textview_tipo_objetivo_alertdialog);
//        tv_tipo_objetivo.setText(objetivo);

        String titulo = getResources().getString(R.string.datos_personales_perfil);
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
