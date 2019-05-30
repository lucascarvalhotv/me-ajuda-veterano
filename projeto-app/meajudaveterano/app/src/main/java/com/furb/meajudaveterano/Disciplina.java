package com.furb.meajudaveterano;

import android.os.Parcel;
import android.os.Parcelable;

public class Disciplina implements Parcelable {

    private String uuid;
    private String nome;

    public static final Creator<Disciplina> CREATOR = new Creator<Disciplina>() {
        @Override
        public Disciplina createFromParcel(Parcel in) {
            return new Disciplina(in);
        }

        @Override
        public Disciplina[] newArray(int size) {
            return new Disciplina[size];
        }
    };

    public Disciplina(String uuid, String nome) {
        this.uuid = uuid;
        this.nome = nome;
    }

    protected Disciplina(Parcel in) {
        this.uuid = in.readString();
        this.nome = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nome);
    }
}
