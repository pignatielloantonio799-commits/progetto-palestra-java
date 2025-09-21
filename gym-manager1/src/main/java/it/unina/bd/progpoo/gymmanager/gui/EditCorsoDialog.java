package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.CorsoController;
import it.unina.bd.progpoo.gymmanager.controller.IstruttoreController;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditCorsoDialog extends JDialog {

    private JTextField nomeField;
    private JTextField descrizioneField;
    private JTextField orarioField;
    private JTextField durataField;
    private JTextField maxPartecipantiField;
    private JTextField salaField;
    private JComboBox<String> istruttoreComboBox;

    private CorsoController corsoController;
    private IstruttoreController istruttoreController;
    private Corso corsoToEdit;

    public EditCorsoDialog(JFrame parent, CorsoController corsoController, IstruttoreController istruttoreController, Corso corsoToEdit) {
        super(parent, "Modifica Corso", true);
        this.corsoController = corsoController;
        this.istruttoreController = istruttoreController;
        this.corsoToEdit = corsoToEdit;

        setSize(400, 500);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nomeField = new JTextField(20); panel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Descrizione:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; descrizioneField = new JTextField(20); panel.add(descrizioneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Orario (HH:mm:ss):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; orarioField = new JTextField(20); panel.add(orarioField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Durata (min):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; durataField = new JTextField(20); panel.add(durataField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Max Partecipanti:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; maxPartecipantiField = new JTextField(20); panel.add(maxPartecipantiField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Sala:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; salaField = new JTextField(20); panel.add(salaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Istruttore:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; istruttoreComboBox = new JComboBox<>(); panel.add(istruttoreComboBox, gbc);

        loadIstruttoriAndSetSelected(corsoToEdit);

        if (corsoToEdit != null) {
            nomeField.setText(corsoToEdit.getNome());
            descrizioneField.setText(corsoToEdit.getDescrizione());
            if (corsoToEdit.getOrario() != null) {
                orarioField.setText(new SimpleDateFormat("HH:mm:ss").format(corsoToEdit.getOrario()));
            }
            durataField.setText(String.valueOf(corsoToEdit.getDurata()));
            maxPartecipantiField.setText(String.valueOf(corsoToEdit.getMaxPartecipanti()));
            salaField.setText(corsoToEdit.getSala());
        }

        JButton saveButton = new JButton("Salva Modifiche");
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                if (nomeField.getText().isEmpty() || descrizioneField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e descrizione sono campi obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String orarioStr = orarioField.getText();
                Date orario = new SimpleDateFormat("HH:mm:ss").parse(orarioStr);
                
                // Correzione: Converte java.util.Date in java.sql.Time
                corsoToEdit.setOrario(new java.sql.Time(orario.getTime()));
                
                corsoToEdit.setNome(nomeField.getText());
                corsoToEdit.setDescrizione(descrizioneField.getText());
                corsoToEdit.setDurata(Integer.parseInt(durataField.getText()));
                corsoToEdit.setMaxPartecipanti(Integer.parseInt(maxPartecipantiField.getText()));
                corsoToEdit.setSala(salaField.getText());

                String istruttoreSelezionato = (String) istruttoreComboBox.getSelectedItem();
                if (istruttoreSelezionato != null) {
                    String matricolaIstruttore = istruttoreSelezionato.split(" - ")[0];
                    // Correzione: Chiama il nuovo metodo findByMatricola del controller
                    Istruttore istruttore = istruttoreController.findByMatricola(matricolaIstruttore);
                    if (istruttore != null) {
                        corsoToEdit.setIstruttore(istruttore);
                    } else {
                        JOptionPane.showMessageDialog(this, "Istruttore selezionato non trovato nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                corsoController.updateCorso(corsoToEdit);
                JOptionPane.showMessageDialog(this, "Corso modificato con successo!");
                dispose();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato orario non valido. Usa HH:mm:ss", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Durata e Max Partecipanti devono essere numeri validi.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }

    private void loadIstruttoriAndSetSelected(Corso corso) {
        try {
            List<Istruttore> istruttori = istruttoreController.getAllIstruttori();
            String selectedMatricola = corso != null && corso.getIstruttore() != null ? corso.getIstruttore().getMatricola() : null;
            
            for (Istruttore istruttore : istruttori) {
                String item = istruttore.getMatricola() + " - " + istruttore.getNome() + " " + istruttore.getCognome();
                istruttoreComboBox.addItem(item);
                if (selectedMatricola != null && istruttore.getMatricola().equals(selectedMatricola)) {
                    istruttoreComboBox.setSelectedItem(item);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento degli istruttori: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}