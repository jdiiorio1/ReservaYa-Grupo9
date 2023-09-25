package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                Toast.makeText(getApplicationContext(),"Su cuenta fue creada con exito", Toast.LENGTH_LONG).show();
            }
        }
    };

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