package com.sedentapp.sedentapp.sedentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Update;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PerfilFragment extends Fragment {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    boolean init_calibration_flag = true;
    private SharedPreferences myPreferences = null;
    private SharedPreferences.Editor myEditor = null;
    private boolean isLoggedIn;

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

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
            Log.d(TAG, "onCurrentAccessTokenChanged()");
            if (accessToken == null) {
                isLoggedIn = true;
                updateUI(null);
            } else if (accessToken2 == null) {
                isLoggedIn = false;
                updateUI(null);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        final TextView nameText = view.findViewById(R.id.nombre_perfil);
        final TextView yearText = view.findViewById(R.id.tv_edad_valor);

        myPreferences = this.getActivity().getSharedPreferences("perfil", Context.MODE_PRIVATE);
        myEditor = myPreferences.edit();

        callbackManager = CallbackManager.Factory.create();
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String name = "";
                            String uriPicture = "";
                            String birthday = "";
                            if (object.getString("birthday") != null) {
                                birthday = object.getString("birthday");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date fechaInicial = dateFormat.parse(birthday);
                                Date ahora = new Date();
                                int dias = (int) ((ahora.getTime()-fechaInicial.getTime())/86400000);
                                birthday = String.valueOf(dias/365);
                            }
                            if (object.getString("name") != null) {
                                name = object.getString("name");
                            }
                            if (object.getString("picture") != null) {
                                JSONObject imagen = new JSONObject(object.getString("picture"));
                                JSONObject imagen2 = new JSONObject(imagen.getString("data"));
                                uriPicture = imagen2.getString("url");
                            }
                            // save profile information to preferences
                            myEditor.putString("name", name).apply();
                            myEditor.putString("years", birthday).apply();
                            myEditor.putString("uriPicture", uriPicture).apply();
                            myEditor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                isLoggedIn = true;
                updateUI(null);
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
        if (isLoggedIn){
            Toast.makeText(getActivity(), "Logeado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Logeate", Toast.LENGTH_SHORT).show();
        }
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


    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        updateUI(account);

        SignInButton btnSignIn = (SignInButton) getView().findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                signIn();
                Toast.makeText(getActivity(), "Sign in Google", Toast.LENGTH_SHORT).show();

            }
        });

        final Button calibrateStepButton = (Button) getView().findViewById(R.id.calibrate_step_button);
        // Capture button clicks
        calibrateStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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


        Button btnSignOut = (Button) getView().findViewById(R.id.sign_out_button);
        btnSignOut.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                signOut();
                Toast.makeText(getActivity(), "Sign out Google", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton boton_edit = (ImageButton) getView().findViewById(R.id.btn_edit_nombre_perfil);
        boton_edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(), "Editar nombre", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton boton_edit2 = (ImageButton) getView().findViewById(R.id.btn_edit_datos_perfil);
        boton_edit2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(), "Editar datos", Toast.LENGTH_SHORT).show();

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
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
        TextView nameText = this.getView().findViewById(R.id.nombre_perfil);
        TextView yearText = this.getView().findViewById(R.id.tv_edad_valor);
        if (account != null) {
            Toast.makeText(getActivity(), "No nulo", Toast.LENGTH_SHORT).show();
            this.getView().findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            this.getView().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            String personName = account.getDisplayName();
            Uri personPhoto = account.getPhotoUrl();
            myEditor.putString("name", personName).apply();
            myEditor.putString("uriPicture", personPhoto.toString()).apply();
            myEditor.commit();
            if (myPreferences.getString("uriPicture", "none") != "none") {
                Glide.with(this).load(myPreferences.getString("uriPicture", "none"))
                        .apply(bitmapTransform(new CircleCrop()))
                        .into((ImageView) this.getView().findViewById(R.id.profile_image));
            }
            yearText.setText("N/D");
            nameText.setText(myPreferences.getString("name", getString(R.string.nombre_perfil)));
        } else {
            Toast.makeText(getActivity(), "Nulo", Toast.LENGTH_SHORT).show();
            if (isLoggedIn){
                nameText.setText(getString(R.string.nombre_usuario));
                yearText.setText(myPreferences.getString("years", "N/D"));
                if (myPreferences.getString("uriPicture", "none") != "none") {
                    Glide.with(this).load(myPreferences.getString("uriPicture", "none"))
                            .apply(bitmapTransform(new CircleCrop()))
                            .into((ImageView) this.getView().findViewById(R.id.profile_image));
                }
                Log.d("D", myPreferences.getString("name", getString(R.string.nombre_perfil)));
                Log.d("D", myPreferences.getString("years", getString(R.string.nombre_perfil)));
                Log.d("D", myPreferences.getString("uriPicture", getString(R.string.nombre_perfil)));
            }else{
                ImageView image = this.getView().findViewById(R.id.profile_image);
                image.setImageResource(R.drawable.man);
                myEditor.putString("name", getString(R.string.nombre_perfil)).apply();
                yearText.setText("N/D");
                nameText.setText(myPreferences.getString("name", getString(R.string.nombre_perfil)));
            }
            this.getView().findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            this.getView().findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }

    }

    /**
     * Abre el alert dialog de editar objetivos
     * @param objetivo nombre del objetivo a editar para mostrar en un textview
     */
    /*public void personalizarObjetivoDialog(String objetivo) {
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
    }*/

}
