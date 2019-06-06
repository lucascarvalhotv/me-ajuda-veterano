package com.furb.meajudaveterano;

import android.support.annotation.NonNull;
import android.util.Log;

import com.furb.meajudaveterano.college.Curso;
import com.furb.meajudaveterano.college.Disciplina;
import com.furb.meajudaveterano.college.Instituicao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loader {

    public static void loadCurso() {

        List<Disciplina> disciplinas = new ArrayList();
        disciplinas.add(new Disciplina(UUID.randomUUID().toString(),
                "Banco de Dados I"));
        disciplinas.add(new Disciplina(UUID.randomUUID().toString(),
                "Banco de Dados II"));
        disciplinas.add(new Disciplina(UUID.randomUUID().toString(),
                "Arquitetura de Software"));
        disciplinas.add(new Disciplina(UUID.randomUUID().toString(),
                "Compiladores"));

        Curso c1 = new Curso(UUID.randomUUID().toString(),
                "Bacharel em Sistemas de Informação", disciplinas);

        List<Curso> cursos = new ArrayList();
        cursos.add(c1);

        Instituicao instituicao = new Instituicao(UUID.randomUUID().toString(),
                "Universidade Regional de Blumenau", cursos);

        for (Disciplina d: disciplinas) {
            FirebaseFirestore.getInstance().collection("disciplinas")
                    .document(d.getUuid())
                    .set(d)
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

}
