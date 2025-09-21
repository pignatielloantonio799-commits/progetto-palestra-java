package it.unina.bd.progpoo.gymmanager.model;

public abstract class Persona {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;

    public Persona() {
        // Costruttore vuoto necessario per le sottoclassi
    }
    
    public Persona(int id, String nome, String cognome, String email, String telefono) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
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

    public String getAnagrafica() {
        return this.nome + " " + this.cognome;
    }
}