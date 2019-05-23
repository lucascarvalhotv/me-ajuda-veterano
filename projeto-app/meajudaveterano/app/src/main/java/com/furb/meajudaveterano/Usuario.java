package com.furb.meajudaveterano;

public class Usuario {

    private final String uuid;
    private final String nome;
    private final String profileUrl;
    private final String email;

    public Usuario(String uuid, String nome, String profileUrl, String email) {
        this.uuid = uuid;
        this.nome = nome;
        this.profileUrl = profileUrl;
        this.email = email;
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
}