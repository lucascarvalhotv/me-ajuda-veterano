package com.furb.meajudaveterano;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loader {

    public static void loadCurso() {

        Disciplina d1 = new Disciplina(UUID.randomUUID().toString(),
                "Projetos de Software II");

        List<Disciplina> disciplinas = new ArrayList();
        disciplinas.add(d1);

        Curso c1 = new Curso(UUID.randomUUID().toString(),
                "Bacharel em Sistemas de Informação", disciplinas);

        List<Curso> cursos = new ArrayList();
        cursos.add(c1);

        Instituicao instituicao = new Instituicao(UUID.randomUUID().toString(),
                "Universidade Regional de Blumenau", cursos);

        FirebaseFirestore.getInstance().collection("cursos")
                .document()
                .set(instituicao)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Teste", "Curso criado com sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });
    }

}
