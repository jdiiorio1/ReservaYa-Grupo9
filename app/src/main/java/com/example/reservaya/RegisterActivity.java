package com.example.reservaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private TextView backLogin;
    private EditText nombre;
    private EditText email;
    private EditText pass;
    private EditText repass;
    private Button registrar;

    private Switch mySwitch;

    private RadioGroup radioGroup;

    private EditText razonSocial;

    RequestQueue requestQueue;
    private static final String urlInsertarAficionado="http://192.168.1.47/backend/insertarAficionado.php";
    private static final String urlInsertarPropietario="http://192.168.1.47/backend/insertarPropietario.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        setTitle("Registrese");
        /*
        * Asocio las instancias a la interfaz
        * */
        nombre = findViewById(R.id.et_name);
        email = findViewById(R.id.et_mail);
        pass = findViewById(R.id.et_password);
        repass = findViewById(R.id.et_repassword);
        registrar = findViewById(R.id.bt_registrar);
        backLogin = findViewById(R.id.tv_back_login);

        /*
        * Creo los listener de los elementos clickeables
        * */
        backLogin.setOnClickListener(backLoginistener);
        registrar.setOnClickListener(registrarListener);
        mySwitch = findViewById(R.id.switchRol);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setVisibility(View.INVISIBLE);
        razonSocial = findViewById(R.id.et_razonSocial);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioGroup.setVisibility(View.VISIBLE);
                    razonSocial.setVisibility(View.VISIBLE);
                } else {
                    radioGroup.setVisibility(View.GONE);
                    razonSocial.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void guardarUsuario(View view){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        if (radioGroup.getVisibility()==view.VISIBLE){
            StringRequest stringRequest= new StringRequest(
                    Request.Method.POST,
                    urlInsertarPropietario,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Su usuario fue creada con exito", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Error, su usuario no fue creado.", Toast.LENGTH_LONG).show();
                        }
                    }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map <String, String> params = new HashMap<>();
                    params.put("nombre", nombre.getText().toString());
                    params.put("correo_electronico", email.getText().toString());
                    params.put("contrasena", pass.getText().toString());
                    boolean esPropietario = true;
                    params.put("propietario", esPropietario ? "1" : "0");
                    params.put("cuil", razonSocial.getText().toString());

                    return params;
                }
            };
            requestQueue.add(stringRequest);

        } else {
            if (radioGroup.getVisibility()==view.INVISIBLE){

                StringRequest stringRequest= new StringRequest(
                        Request.Method.POST,
                        urlInsertarAficionado,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(),"Su cuenta fue creada con exito", Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"Error, su cuenta no fue creada", Toast.LENGTH_LONG).show();
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map <String, String> params = new HashMap<>();
                        params.put("nombre", nombre.getText().toString());
                        params.put("correo_electronico", email.getText().toString());
                        params.put("contrasena", pass.getText().toString());
                        boolean esPropietario = false;
                        params.put("propietario", esPropietario ? "1" : "0");
                        String equipo = "Boca Jr";
                        params.put("equipo", equipo.toString());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        }
    }

    public View.OnClickListener backLoginistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentRegistrar = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intentRegistrar);
            RegisterActivity.this.finish();
        }
    };

    public View.OnClickListener registrarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validarDatos()) {
                guardarUsuario(v);
                loginActivity(v);
            }
        }
    };

    public void loginActivity(View view) {
        // Crear una intención para ir a OtraActivity
        Intent intent = new Intent(this, LoginActivity.class);

        // Iniciar la actividad OtraActivity
        startActivity(intent);
    }


    public boolean validarDatos() {
        boolean error = true;
        if (TextUtils.isEmpty(nombre.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "El campo nombre de usuario es obligatorio", Toast.LENGTH_LONG).show();
            error = false;
        }

        if (TextUtils.isEmpty(email.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            Toast.makeText(RegisterActivity.this, "Ingrese una direccion de correo valida", Toast.LENGTH_LONG).show();
            error = false;
        }

        if (TextUtils.isEmpty(pass.getText().toString()) || (pass.getText().toString().length() < 8 )) {
            Toast.makeText(RegisterActivity.this, "La contraseña debe contener al menos 8 caracteres", Toast.LENGTH_LONG).show();
            error = false;
        }

        if (TextUtils.isEmpty(repass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Repita la contraseña", Toast.LENGTH_LONG).show();
            error = false;
        }

        if (!pass.getText().toString().equals(repass.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Las contraseñas ingresadas no son iguales", Toast.LENGTH_LONG).show();
            error = false;
        }

        if(radioGroup.getVisibility()==View.VISIBLE && razonSocial.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese su Cuil o Cuit", Toast.LENGTH_LONG).show();
            error = false;
        }

        return error;

    };


}