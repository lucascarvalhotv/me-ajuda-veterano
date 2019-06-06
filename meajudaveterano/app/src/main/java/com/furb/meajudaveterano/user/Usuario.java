package com.furb.meajudaveterano.user;

import android.os.Parcel;
import android.os.Parcelable;
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

    public Usuario() {
    }

    public Usuario(String uuid, String nome, String profileUrl, String email) {
        this.uuid = uuid;
        this.nome = nome;
        this.profileUrl = profileUrl;
        this.email = email;
    }

    protected Usuario(Parcel in) {
        uuid = in.readString();
        nome = in.readString();
        profileUrl = in.readString();
        email = in.readString();
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
    }

}