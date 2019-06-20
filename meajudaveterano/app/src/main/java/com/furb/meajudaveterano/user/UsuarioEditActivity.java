package com.furb.meajudaveterano.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.furb.meajudaveterano.R;
import com.furb.meajudaveterano.chat.ChatActivity;
import com.furb.meajudaveterano.login.SigninActivity;
import com.furb.meajudaveterano.user.Usuario;

public class UsuarioEditActivity extends AppCompatActivity {

    Usuario me = new Usuario();

    private Button mButtonSalvar;
    private TextView nome;
    private TextView sobrenome;
    private TextView email;
    private TextView college;
    private TextView curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usario_edit);
        me = getIntent().getExtras().getParcelable("me");

        mButtonSalvar = findViewById(R.id.buttonSalvar);
        nome = findViewById(R.id.textNome);
        sobrenome = findViewById(R.id.textSobrenome);
        email = findViewById(R.id.textEmail);
        college = findViewById(R.id.textCollege);
        curso = findViewById(R.id.textCurso);

        mButtonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    public void save() {
        setInfo();
        new SigninActivity().saveUserOnFirebase(me);
    }

    private void setInfo() {
        if (nome.getText().toString().isEmpty()) {
            if (sobrenome.getText().toString().isEmpty()) {
                me.setNome(nome.getText().toString() + ' ' + sobrenome.getText().toString());
            } else {
                me.setNome(nome.getText().toString());
            }
        }

        if (email.getText().toString().isEmpty()) {
            me.setEmail(email.getText().toString());
        }

        if (college.getText().toString().isEmpty()) {
            me.setCollege(college.getText().toString());
        }

        if (curso.getText().toString().isEmpty()) {
            me.setCurso(curso.getText().toString());
        }
    }

}
