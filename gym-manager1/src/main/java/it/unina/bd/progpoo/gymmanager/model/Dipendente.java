package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Dipendente {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String matricola;
    private Date dataAssunzione;
    private double stipendio;

    // 🟢 Aggiungi questo costruttore vuoto.
    public Dipendente() {
        // Il corpo può essere vuoto.
    }

    public Dipendente(int id, String nome, String cognome, String email, String telefono, String matricola, Date dataAssunzione, double stipendio) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.matricola = matricola;
        this.dataAssunzione = dataAssunzione;
        this.stipendio = stipendio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public Date getDataAssunzione() {
        return dataAssunzione;
    }

    public void setDataAssunzione(Date dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    public double getStipendio() {
        return stipendio;
    }

    public void setStipendio(double stipendio) {
        this.stipendio = stipendio;
    }
}