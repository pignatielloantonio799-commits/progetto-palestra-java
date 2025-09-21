package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.CorsoController;
import it.unina.bd.progpoo.gymmanager.controller.IstruttoreController;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CorsiPanel extends JPanel {

    private CorsoController corsoController;
    private IstruttoreController istruttoreController;
    private JTable corsiTable;
    private DefaultTableModel tableModel;

    // Campi per la creazione/modifica del corso
    private JTextField nomeCorsoField;
    private JTextField descrizioneField;
    private JTextField orarioField; // Formato HH:mm
    private JTextField durataField;
    private JTextField maxPartecipantiField;
    private JTextField salaField;
    private JComboBox<String> istruttoreComboBox;

    // 🟢 Correzione 1: Modifica il costruttore per accettare due controller
    public CorsiPanel(CorsoController corsoController, IstruttoreController istruttoreController) {
        this.corsoController = corsoController;
        this.istruttoreController = istruttoreController; // Inizializzazione corretta
        setLayout(new BorderLayout(10, 10));

        // Pannello per l'input dei dati del corso
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campi di input
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Nome Corso:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nomeCorsoField = new JTextField(15); inputPanel.add(nomeCorsoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Descrizione:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; descrizioneField = new JTextField(15); inputPanel.add(descrizioneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Orario (HH:mm):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; orarioField = new JTextField(15); inputPanel.add(orarioField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Durata (min):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; durataField = new JTextField(15); inputPanel.add(durataField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("Max Partecipanti:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; maxPartecipantiField = new JTextField(15); inputPanel.add(maxPartecipantiField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(new JLabel("Sala:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; salaField = new JTextField(15); inputPanel.add(salaField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; inputPanel.add(new JLabel("Istruttore:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; istruttoreComboBox = new JComboBox<>(); inputPanel.add(istruttoreComboBox, gbc);

        loadIstruttori();

        // Pulsante Aggiungi Corso
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST;
        JButton aggiungiButton = new JButton("Aggiungi Corso");
        inputPanel.add(aggiungiButton, gbc);

        // Tabella per visualizzare i corsi
        String[] columnNames = {"ID Corso", "Nome", "Orario", "Durata", "Max Partecipanti", "Posti Disp.", "Istruttore"};
        tableModel = new DefaultTableModel(columnNames, 0);
        corsiTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(corsiTable);

        // Pannello per i pulsanti di gestione
        JPanel buttonPanel = new JPanel();
        JButton eliminaButton = new JButton("Elimina Corso");
        JButton modificaButton = new JButton("Modifica Corso");
        buttonPanel.add(modificaButton);
        buttonPanel.add(eliminaButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadCorsiData();

        // Listener per i pulsanti
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggiungiCorso();
            }
        });

        eliminaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminaCorso();
            }
        });

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificaCorso();
            }
        });
    }

    // Metodo per popolare la JComboBox degli istruttori
    private void loadIstruttori() {
        try {
            List<Istruttore> istruttori = istruttoreController.getAllIstruttori();
            istruttoreComboBox.removeAllItems();
            for (Istruttore istruttore : istruttori) {
                istruttoreComboBox.addItem(istruttore.getMatricola() + " - " + istruttore.getNome() + " " + istruttore.getCognome());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento degli istruttori: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per popolare la tabella dei corsi
    private void loadCorsiData() {
        try {
            List<Corso> corsi = corsoController.visualizzaTuttiICorsi();
            tableModel.setRowCount(0);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            for (Corso corso : corsi) {
                String nomeIstruttore = "N/A";
                if (corso.getIstruttore() != null) {
                    nomeIstruttore = corso.getIstruttore().getNome() + " " + corso.getIstruttore().getCognome();
                }
                tableModel.addRow(new Object[]{
                    corso.getIdCorso(),
                    corso.getNome(), 
                    corso.getOrario() != null ? timeFormat.format(corso.getOrario()) : "N/A",
                    corso.getDurata(),
                    corso.getMaxPartecipanti(),
                    (corso.getMaxPartecipanti() - corso.getPrenotazioniAttuali()),
                    nomeIstruttore
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei corsi: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiCorso() {
        try {
            String nome = nomeCorsoField.getText();
            String descrizione = descrizioneField.getText();
            String orarioStr = orarioField.getText();
            int durata = Integer.parseInt(durataField.getText());
            int maxPartecipanti = Integer.parseInt(maxPartecipantiField.getText());
            String sala = salaField.getText();

            String istruttoreSelezionato = (String) istruttoreComboBox.getSelectedItem();
            if (istruttoreSelezionato == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un istruttore.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String matricolaIstruttore = istruttoreSelezionato.split(" - ")[0];

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date orario = timeFormat.parse(orarioStr);

            corsoController.creaNuovoCorso(nome, descrizione, orario, durata, maxPartecipanti, sala, matricolaIstruttore);
            JOptionPane.showMessageDialog(this, "Corso aggiunto con successo!");

            // Pulisci i campi e ricarica i dati
            nomeCorsoField.setText("");
            descrizioneField.setText("");
            orarioField.setText("");
            durataField.setText("");
            maxPartecipantiField.setText("");
            salaField.setText("");

            loadCorsiData();
        } catch (NumberFormatException | ParseException ex) {
            JOptionPane.showMessageDialog(this, "Errore di formato. Controlla i dati inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminaCorso() {
        int selectedRow = corsiTable.getSelectedRow();
        if (selectedRow >= 0) {
            int idCorso = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare questo corso?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    corsoController.deleteCorso(idCorso);
                    JOptionPane.showMessageDialog(this, "Corso eliminato con successo!");
                    loadCorsiData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore SQL durante l'eliminazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un corso da eliminare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificaCorso() {
        int selectedRow = corsiTable.getSelectedRow();
        if (selectedRow >= 0) {
            int idCorso = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Corso corso = corsoController.findById(idCorso);
                if (corso != null) {
                    // Apri il dialogo di modifica
                    EditCorsoDialog dialog = new EditCorsoDialog((JFrame) SwingUtilities.getWindowAncestor(this), corsoController, istruttoreController, corso);
                    dialog.setVisible(true);

                    // Ricarica i dati dopo la chiusura del dialogo
                    loadCorsiData();
                } else {
                    JOptionPane.showMessageDialog(this, "Corso non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante il recupero del corso: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un corso da modificare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }
}