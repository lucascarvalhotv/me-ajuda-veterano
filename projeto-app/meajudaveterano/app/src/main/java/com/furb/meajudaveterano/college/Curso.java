package com.furb.meajudaveterano.college;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Curso implements Parcelable {

    private String uuid;
    private String nome;
    private List<Disciplina> disciplinas;

    public Curso(String uuid, String nome, List<Disciplina> disciplinas) {
        this.uuid = uuid;
        this.nome = nome;
        this.disciplinas = disciplinas;
    }

    protected Curso(Parcel in) {
        this.uuid = in.readString();
        this.nome = in.readString();
        disciplinas = new ArrayList<>();
        in.readList(disciplinas, Disciplina.class.getClassLoader());
    }

    public static final Creator<Curso> CREATOR = new Creator<Curso>() {
        @Override
        public Curso createFromParcel(Parcel in) {
            return new Curso(in);
        }

        @Override
        public Curso[] newArray(int size) {
            return new Curso[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nome);
        dest.writeList(disciplinas);
    }
}
