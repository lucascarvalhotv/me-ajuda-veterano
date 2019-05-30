package com.furb.meajudaveterano;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Instituicao implements Parcelable {
    
    private String uuid;
    private String nome;
    private List<Curso> cursos;

    public Instituicao() {
    }

    public Instituicao(String uuid, String nome, List<Curso> cursos) {
        this.uuid = uuid;
        this.nome = nome;
        this.cursos = cursos;
    }

    protected Instituicao(Parcel in) {
        this.uuid = in.readString();
        this.nome = in.readString();
        cursos = new ArrayList<>();
        in.readList(cursos, Curso.class.getClassLoader());
    }

    public static final Parcelable.Creator<Instituicao> CREATOR = new Parcelable.Creator<Instituicao>() {
        @Override
        public Instituicao createFromParcel(Parcel in) {
            return new Instituicao(in);
        }

        @Override
        public Instituicao[] newArray(int size) {
            return new Instituicao[size];
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
        dest.writeList(cursos);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
}