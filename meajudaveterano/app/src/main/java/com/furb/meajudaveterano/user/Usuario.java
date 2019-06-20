package com.furb.meajudaveterano.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Usuario implements Parcelable {

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
    private String uuid;
    private String nome;
    private String profileUrl;
    private String email;
    private String telefone;
    private String college;
    private String curso;
    private ArrayList<String> disciplinaFazendo = new ArrayList<String>();;
    private ArrayList<String> disciplinaFeita = new ArrayList<String>();;

    public Usuario() {
    }

    public Usuario(String uuid, String nome, String profileUrl, String email, String telefone) {
        this.uuid = uuid;
        this.nome = nome;
        this.profileUrl = profileUrl;
        this.email = email;
        this.telefone = telefone;
        disciplinaFazendo = new ArrayList<String>();
        disciplinaFeita = new ArrayList<String>();
    }

    protected Usuario(Parcel in) {
        uuid = in.readString();
        nome = in.readString();
        profileUrl = in.readString();
        email = in.readString();
        telefone = in.readString();
        disciplinaFazendo = in.readArrayList(ArrayList.class.getClassLoader());
        disciplinaFeita = in.readArrayList(ArrayList.class.getClassLoader());
    }

    public String getUuid() {
        return uuid;
    }

    public String getNome() {
        return nome;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nome);
        dest.writeString(profileUrl);
        dest.writeString(email);
        dest.writeString(telefone);
    }

    public String getCollege() {
        return college;
    }

    public String getCurso() {
        return curso;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ArrayList<String> getDisciplinaFazendo() {
        return disciplinaFazendo;
    }

    public void setDisciplinaFazendo(ArrayList<String> disciplinaFazendo) {
        this.disciplinaFazendo = disciplinaFazendo;
    }

    public ArrayList<String> getDisciplinaFeita() {
        return disciplinaFeita;
    }

    public void setDisciplinaFeita(ArrayList<String> disciplinaFeita) {
        this.disciplinaFeita = disciplinaFeita;
    }
}