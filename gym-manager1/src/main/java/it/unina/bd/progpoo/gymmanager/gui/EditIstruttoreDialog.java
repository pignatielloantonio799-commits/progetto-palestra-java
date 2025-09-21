package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.IstruttoreController;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;

public class EditIstruttoreDialog extends JDialog {
    
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JTextField specializzazioneField;
    private JTextField matricolaField;
    private JTextField dataAssunzioneField;
    private JTextField stipendioField;
    private JTextField anniEsperienzaField;
    
    private IstruttoreController istruttoreController;
    private Istruttore istruttoreToEdit;

    public EditIstruttoreDialog(JFrame parent, IstruttoreController controller, Istruttore istruttore) {
        super(parent, istruttore == null ? "Registra Istruttore" : "Modifica Istruttore", true);
        this.istruttoreController = controller;
        this.istruttoreToEdit = istruttore;
        
        setSize(400, 450);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nomeField = new JTextField(20); panel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; cognomeField = new JTextField(20); panel.add(cognomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; emailField = new JTextField(20); panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; telefonoField = new JTextField(20); panel.add(telefonoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Specializzazione:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; specializzazioneField = new JTextField(20); panel.add(specializzazioneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Matricola:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; matricolaField = new JTextField(20); panel.add(matricolaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Data Assunzione (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; dataAssunzioneField = new JTextField(20); panel.add(dataAssunzioneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Stipendio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; stipendioField = new JTextField(20); panel.add(stipendioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; panel.add(new JLabel("Anni Esperienza:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8; anniEsperienzaField = new JTextField(20); panel.add(anniEsperienzaField, gbc);
        
        if (istruttore != null) {
            nomeField.setText(istruttore.getNome());
            cognomeField.setText(istruttore.getCognome());
            emailField.setText(istruttore.getEmail());
            telefonoField.setText(istruttore.getTelefono());
            specializzazioneField.setText(istruttore.getSpecializzazione());
            matricolaField.setText(istruttore.getMatricola());
            matricolaField.setEditable(false); 
            dataAssunzioneField.setText(istruttore.getDataAssunzione() != null ? new SimpleDateFormat("yyyy-MM-dd").format(istruttore.getDataAssunzione()) : "");
            stipendioField.setText(String.valueOf(istruttore.getStipendio()));
            anniEsperienzaField.setText(String.valueOf(istruttore.getAnniEsperienza()));
        }

        JButton saveButton = new JButton("Salva");
        gbc.gridx = 1; gbc.gridy = 9; gbc.anchor = GridBagConstraints.EAST;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                if (nomeField.getText().isEmpty() || cognomeField.getText().isEmpty() || matricolaField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome, Cognome e Matricola sono campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date dataAssunzione = new SimpleDateFormat("yyyy-MM-dd").parse(dataAssunzioneField.getText());
                double stipendio = Double.parseDouble(stipendioField.getText());
                int anniEsperienza = Integer.parseInt(anniEsperienzaField.getText());
                
                if (istruttoreToEdit == null) {
                    Istruttore nuovoIstruttore = new Istruttore();
                    nuovoIstruttore.setNome(nomeField.getText());
                    nuovoIstruttore.setCognome(cognomeField.getText());
                    nuovoIstruttore.setEmail(emailField.getText());
                    nuovoIstruttore.setTelefono(telefonoField.getText());
                    nuovoIstruttore.setMatricola(matricolaField.getText());
                    nuovoIstruttore.setDataAssunzione(dataAssunzione);
                    nuovoIstruttore.setStipendio(stipendio);
                    nuovoIstruttore.setSpecializzazione(specializzazioneField.getText());
                    nuovoIstruttore.setAnniEsperienza(anniEsperienza);
                    
                    istruttoreController.creaIstruttore(nuovoIstruttore);
                    JOptionPane.showMessageDialog(this, "Istruttore registrato con successo!");
                } else {
                    istruttoreToEdit.setNome(nomeField.getText());
                    istruttoreToEdit.setCognome(cognomeField.getText());
                    istruttoreToEdit.setEmail(emailField.getText());
                    istruttoreToEdit.setTelefono(telefonoField.getText());
                    istruttoreToEdit.setSpecializzazione(specializzazioneField.getText());
                    istruttoreToEdit.setDataAssunzione(dataAssunzione);
                    istruttoreToEdit.setStipendio(stipendio);
                    istruttoreToEdit.setAnniEsperienza(anniEsperienza);
                    istruttoreController.updateIstruttore(istruttoreToEdit);
                    JOptionPane.showMessageDialog(this, "Modifiche salvate con successo!");
                }
                dispose();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Errore di formato della data. Usa YYYY-MM-DD.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stipendio e Anni Esperienza devono essere numeri validi.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        add(panel);
    }
}