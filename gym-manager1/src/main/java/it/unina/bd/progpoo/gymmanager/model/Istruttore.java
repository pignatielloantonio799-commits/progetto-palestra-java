package it.unina.bd.progpoo.gymmanager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Istruttore extends Dipendente {
    private String specializzazione;
    private int anniEsperienza;
    private List<Corso> corsiInsegnati;

    // Questo costruttore ora funziona correttamente
    public Istruttore() {
        super();
        this.corsiInsegnati = new ArrayList<>();
    }

    public Istruttore(int id, String nome, String cognome, String email, String telefono, String matricola,
                      Date dataAssunzione, double stipendio, String specializzazione, int anniEsperienza) {
        super(id, nome, cognome, email, telefono, matricola, dataAssunzione, stipendio);
        this.specializzazione = specializzazione;
        this.anniEsperienza = anniEsperienza;
        this.corsiInsegnati = new ArrayList<>();
    }

    public int getIdIstruttore() {
        return super.getId();
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

    public int getAnniEsperienza() {
        return anniEsperienza;
    }

    public void setAnniEsperienza(int anniEsperienza) {
        this.anniEsperienza = anniEsperienza;
    }

    public List<Corso> getCorsiInsegnati() {
        return corsiInsegnati;
    }

    public void aggiungiCorsoInsegnato(Corso corso) {
        this.corsiInsegnati.add(corso);
    }
}