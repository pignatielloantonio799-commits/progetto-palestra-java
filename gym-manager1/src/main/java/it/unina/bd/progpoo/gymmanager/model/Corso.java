package it.unina.bd.progpoo.gymmanager.model;

import java.sql.Time;

public class Corso {
    
    private int idCorso;
    private String nome;
    private String descrizione;
    private Time orario;
    private int durata; // in minuti
    private int maxPartecipanti;
    private String sala;
    private Istruttore istruttore;
    private int prenotazioniAttuali;

    // Costruttore predefinito
    public Corso() {
    }

    // Costruttore completo
    public Corso(int idCorso, String nome, String descrizione, Time orario, int durata, int maxPartecipanti, String sala, Istruttore istruttore, int prenotazioniAttuali) {
        this.idCorso = idCorso;
        this.nome = nome;
        this.descrizione = descrizione;
        this.orario = orario;
        this.durata = durata;
        this.maxPartecipanti = maxPartecipanti;
        this.sala = sala;
        this.istruttore = istruttore;
        this.prenotazioniAttuali = prenotazioniAttuali;
    }

    // Metodo per verificare la disponibilità dei posti (NUOVO)
    public boolean isDisponibile() {
        return this.prenotazioniAttuali < this.maxPartecipanti;
    }

    // Getters and Setters

    public int getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Time getOrario() {
        return orario;
    }

    public void setOrario(Time orario) {
        this.orario = orario;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Istruttore getIstruttore() {
        return istruttore;
    }

    public void setIstruttore(Istruttore istruttore) {
        this.istruttore = istruttore;
    }

    public int getPrenotazioniAttuali() {
        return prenotazioniAttuali;
    }

    public void setPrenotazioniAttuali(int prenotazioniAttuali) {
        this.prenotazioniAttuali = prenotazioniAttuali;
    }
}