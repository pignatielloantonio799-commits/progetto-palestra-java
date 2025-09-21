package it.unina.bd.progpoo.gymmanager.model;

import java.util.Date;

public class Receptionist extends Dipendente {
    private String turnoLavorativo;

    public Receptionist(int id, String nome, String cognome, String email, String telefono, String matricola,
                        Date dataAssunzione, double stipendio, String turnoLavorativo) {
        super(id, nome, cognome, email, telefono, matricola, dataAssunzione, stipendio);
        this.turnoLavorativo = turnoLavorativo;
    }

    public String getTurnoLavorativo() {
        return turnoLavorativo;
    }

    public void setTurnoLavorativo(String turnoLavorativo) {
        this.turnoLavorativo = turnoLavorativo;
    }
}
