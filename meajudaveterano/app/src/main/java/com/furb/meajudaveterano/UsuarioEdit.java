package com.furb.meajudaveterano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.furb.meajudaveterano.chat.ChatActivity;
import com.furb.meajudaveterano.user.Usuario;

public class UsuarioEdit extends AppCompatActivity {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usario_edit);
        usuario = getIntent().getExtras().getParcelable("usuario");
    }

    public void save(Usuario usuarioEdited) {
        this.usuario = usuarioEdited;
        // TODO substituir o usuario no firebase
    }

}
