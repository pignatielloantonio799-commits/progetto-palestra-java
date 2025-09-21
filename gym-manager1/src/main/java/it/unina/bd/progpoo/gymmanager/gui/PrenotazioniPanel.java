package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.AbbonamentoController;
import it.unina.bd.progpoo.gymmanager.controller.CorsoController;
import it.unina.bd.progpoo.gymmanager.controller.*;

import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import exceptions.*;

public class PrenotazioniPanel extends JPanel {

    private CorsoController corsoController;
    private AbbonamentoController abbonamentoController;
    private PrenotazioneController prenotazioneController; // 🟢 Ho aggiunto questo controller
    private JTable corsiTable;
    private JTable prenotazioniTable;
    private DefaultTableModel corsiTableModel;
    private DefaultTableModel prenotazioniTableModel;
    private JTextField codiceFiscaleField; // 🟢 Ho rinominato il campo per coerenza
    private JButton prenotaButton;
    private JButton annullaPrenotazioneButton;

    public PrenotazioniPanel(CorsoController corsoController, AbbonamentoController abbonamentoController) {
        this.corsoController = corsoController;
        this.abbonamentoController = abbonamentoController;
        this.prenotazioneController = new PrenotazioneController(); // 🟢 Inizializza il controller
        setLayout(new BorderLayout());

        // Pannello superiore per la prenotazione
        JPanel prenotazionePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; prenotazionePanel.add(new JLabel("Codice Fiscale Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; codiceFiscaleField = new JTextField(15);
        prenotazionePanel.add(codiceFiscaleField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        prenotaButton = new JButton("Prenota Corso Selezionato");
        prenotazionePanel.add(prenotaButton, gbc);

        // Tabella dei corsi disponibili
        String[] corsiColumnNames = {"ID Corso", "Nome Corso", "Istruttore", "Ora", "Posti Disp."};
        corsiTableModel = new DefaultTableModel(corsiColumnNames, 0);
        corsiTable = new JTable(corsiTableModel);
        JScrollPane corsiScrollPane = new JScrollPane(corsiTable);
        
        // Tabella delle prenotazioni del cliente
        String[] prenotazioniColumnNames = {"ID Prenotazione", "Nome Corso", "Data Prenotazione", "Stato"};
        prenotazioniTableModel = new DefaultTableModel(prenotazioniColumnNames, 0);
        prenotazioniTable = new JTable(prenotazioniTableModel);
        JScrollPane prenotazioniScrollPane = new JScrollPane(prenotazioniTable);

        // Pannello per le tabelle
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, corsiScrollPane, prenotazioniScrollPane);
        splitPane.setResizeWeight(0.5);

        // Pannello per i pulsanti di annullamento e ricerca
        JPanel bottomPanel = new JPanel();
        annullaPrenotazioneButton = new JButton("Annulla Prenotazione");
        JButton cercaPrenotazioniButton = new JButton("Cerca Prenotazioni Cliente");
        bottomPanel.add(cercaPrenotazioniButton);
        bottomPanel.add(annullaPrenotazioneButton);

        add(prenotazionePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadCorsiData();
        
        // Listener per il pulsante "Prenota"
        prenotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prenotaCorso();
            }
        });

        // Listener per il pulsante "Annulla Prenotazione"
        annullaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                annullaPrenotazione();
            }
        });

        // Listener per il pulsante "Cerca Prenotazioni Cliente"
        cercaPrenotazioniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPrenotazioniCliente();
            }
        });
    }

    private void loadCorsiData() {
        try {
            List<Corso> corsi = corsoController.visualizzaTuttiICorsi();
            corsiTableModel.setRowCount(0);
            for (Corso corso : corsi) {
                // 🟢 Ho usato getNome() che è corretto per il tuo modello Corso
                String nomeIstruttore = "N/A";
                if(corso.getIstruttore() != null) {
                    nomeIstruttore = corso.getIstruttore().getNome() + " " + corso.getIstruttore().getCognome();
                }
                corsiTableModel.addRow(new Object[]{
                    corso.getIdCorso(),
                    corso.getNome(),
                    nomeIstruttore,
                    corso.getOrario(),
                    (corso.getMaxPartecipanti() - corso.getPrenotazioniAttuali())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei corsi: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPrenotazioniCliente() {
        String codiceFiscale = codiceFiscaleField.getText(); // 🟢 Ho rinominato la variabile
        if (codiceFiscale.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il codice fiscale del cliente.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Cliente cliente = abbonamentoController.findByCodiceFiscale(codiceFiscale);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente non trovato.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Prenotazione> prenotazioni = prenotazioneController.visualizzaPrenotazioniPerCliente(cliente.getId());
            prenotazioniTableModel.setRowCount(0);
            for (Prenotazione p : prenotazioni) {
                prenotazioniTableModel.addRow(new Object[]{
                    p.getIdPrenotazione(),
                    p.getCorso().getNome(), // 🟢 getNome() è il nome corretto del metodo
                    p.getDataPrenotazione(),
                    p.getStato()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento delle prenotazioni: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prenotaCorso() {
        int selectedRow = corsiTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un corso da prenotare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codiceFiscale = codiceFiscaleField.getText(); // 🟢 Ho rinominato la variabile
        if (codiceFiscale.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci il codice fiscale del cliente.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idCorso = (int) corsiTableModel.getValueAt(selectedRow, 0);
            // 🟢 Ho chiamato il metodo del PrenotazioneController, che gestisce la logica
            prenotazioneController.prenotaCorso(codiceFiscale, idCorso);
            JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo!");
            
            // Aggiorna le tabelle dopo la prenotazione
            loadCorsiData();
            loadPrenotazioniCliente();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore nella prenotazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (AbbonamentoScadutoException | PostiEsauritiException | IllegalArgumentException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annullaPrenotazione() {
        int selectedRow = prenotazioniTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione da annullare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler annullare questa prenotazione?", "Conferma Annullamento", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idPrenotazione = (int) prenotazioniTableModel.getValueAt(selectedRow, 0);
                // 🟢 Ho chiamato il metodo del PrenotazioneController, che gestisce la logica
                prenotazioneController.cancellaPrenotazione(idPrenotazione);
                JOptionPane.showMessageDialog(this, "Prenotazione annullata con successo!");
                
                // Aggiorna le tabelle
                loadCorsiData();
                loadPrenotazioniCliente();
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Errore nell'annullamento: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}