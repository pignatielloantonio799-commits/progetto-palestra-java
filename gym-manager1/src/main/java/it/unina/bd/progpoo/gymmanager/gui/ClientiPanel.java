package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.AbbonamentoController;
import it.unina.bd.progpoo.gymmanager.controller.CorsoController;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import it.unina.bd.progpoo.gymmanager.model.Corso;
import it.unina.bd.progpoo.gymmanager.model.Prenotazione;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientiPanel extends JPanel {

    private AbbonamentoController abbonamentoController;
    private CorsoController corsoController;
    private JTable clientiTable;
    private DefaultTableModel tableModel;

    public ClientiPanel(AbbonamentoController abbonamentoController, CorsoController corsoController) {
        this.abbonamentoController = abbonamentoController;
        this.corsoController = corsoController;
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "Cognome", "Codice Fiscale", "Data Nascita", "Email", "Telefono", "Codice Cliente", "Data Iscrizione", "Data Scadenza", "Tipo Abbonamento"};
        tableModel = new DefaultTableModel(columnNames, 0);
        clientiTable = new JTable(tableModel);
        add(new JScrollPane(clientiTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi Cliente");
        JButton editButton = new JButton("Modifica Cliente");
        JButton deleteButton = new JButton("Elimina Cliente");
        JButton rinnovaButton = new JButton("Rinnova Abbonamento");
        JButton prenotaButton = new JButton("Prenota Corso");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(rinnovaButton);
        buttonPanel.add(prenotaButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField nomeField = new JTextField(15);
            JTextField cognomeField = new JTextField(15);
            JTextField cfField = new JTextField(15);
            JTextField dataNascitaField = new JTextField(15);
            JTextField emailField = new JTextField(15);
            JTextField telefonoField = new JTextField(15);
            JTextField codiceClienteField = new JTextField(15);
            String[] tipiAbbonamento = {"Mensile", "Annuale"};
            JComboBox<String> tipoAbbonamentoBox = new JComboBox<>(tipiAbbonamento);

            inputPanel.add(new JLabel("Nome:"));
            inputPanel.add(nomeField);
            inputPanel.add(new JLabel("Cognome:"));
            inputPanel.add(cognomeField);
            inputPanel.add(new JLabel("Codice Fiscale:"));
            inputPanel.add(cfField);
            inputPanel.add(new JLabel("Data Nascita (dd-MM-yyyy):"));
            inputPanel.add(dataNascitaField);
            inputPanel.add(new JLabel("Email:"));
            inputPanel.add(emailField);
            inputPanel.add(new JLabel("Telefono:"));
            inputPanel.add(telefonoField);
            inputPanel.add(new JLabel("Codice Cliente:"));
            inputPanel.add(codiceClienteField);
            inputPanel.add(new JLabel("Tipo Abbonamento:"));
            inputPanel.add(tipoAbbonamentoBox);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Aggiungi Cliente", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String nome = nomeField.getText();
                    String cognome = cognomeField.getText();
                    String cf = cfField.getText();
                    Date dataNascita = new SimpleDateFormat("dd-MM-yyyy").parse(dataNascitaField.getText());
                    String email = emailField.getText();
                    String telefono = telefonoField.getText();
                    String codiceCliente = codiceClienteField.getText();
                    String tipoAbbonamento = (String) tipoAbbonamentoBox.getSelectedItem();

                    abbonamentoController.registraCliente(nome, cognome, cf, dataNascita, email, telefono, codiceCliente, tipoAbbonamento);
                    refreshClientiTable();
                    JOptionPane.showMessageDialog(this, "Cliente aggiunto con successo!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Errore di formato della data. Usa dd-MM-yyyy.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = clientiTable.getSelectedRow();
            if (selectedRow >= 0) {
                String codiceFiscale = (String) tableModel.getValueAt(selectedRow, 3);
                try {
                    Cliente cliente = abbonamentoController.findByCodiceFiscale(codiceFiscale);
                    if (cliente != null) {
                        EditClienteDialog dialog = new EditClienteDialog((JFrame) SwingUtilities.getWindowAncestor(this), abbonamentoController, cliente);
                        dialog.setVisible(true);
                        refreshClientiTable();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore nel recupero dei dati del cliente: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente da modificare.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = clientiTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Recupera l'ID del cliente dalla prima colonna della tabella
                int clienteId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare questo cliente?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Chiama il metodo deleteCliente con l'ID
                        abbonamentoController.deleteCliente(clienteId);
                        refreshClientiTable();
                        JOptionPane.showMessageDialog(this, "Cliente eliminato con successo.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente da eliminare.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        rinnovaButton.addActionListener(e -> {
            int selectedRow = clientiTable.getSelectedRow();
            if (selectedRow >= 0) {
                String codiceFiscale = (String) tableModel.getValueAt(selectedRow, 3);
                String tipoAbbonamento = (String) tableModel.getValueAt(selectedRow, 10);
                try {
                    Date nuovaDataScadenza = abbonamentoController.calcolaDataScadenza(tipoAbbonamento);
                    abbonamentoController.rinnovaAbbonamento(codiceFiscale, nuovaDataScadenza);
                    refreshClientiTable();
                    JOptionPane.showMessageDialog(this, "Abbonamento rinnovato con successo!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante il rinnovo dell'abbonamento: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente per rinnovare l'abbonamento.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        prenotaButton.addActionListener(e -> {
            int selectedRow = clientiTable.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int clienteId = (int) tableModel.getValueAt(selectedRow, 0);
                    Cliente cliente = abbonamentoController.findById(clienteId);

                    List<Corso> corsiDisponibili = corsoController.visualizzaTuttiICorsi();
                    if (corsiDisponibili.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nessun corso disponibile per la prenotazione.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    Object[] corsiArray = corsiDisponibili.stream().map(Corso::getNome).toArray();
                    String corsoSelezionatoNome = (String) JOptionPane.showInputDialog(
                            this,
                            "Scegli un corso da prenotare:",
                            "Prenota Corso",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            corsiArray,
                            corsiArray[0]);

                    if (corsoSelezionatoNome != null) {
                        Corso corsoSelezionato = corsiDisponibili.stream().filter(c -> c.getNome().equals(corsoSelezionatoNome)).findFirst().orElse(null);
                        if (corsoSelezionato != null) {
                            corsoController.prenotaCorso(cliente, corsoSelezionato);
                            JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo!");
                        }
                    }
                } catch (SQLException | IllegalStateException ex) {
                    JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente per prenotare un corso.", "Avviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshClientiTable();
    }

    public void refreshClientiTable() {
        try {
            List<Cliente> clienti = abbonamentoController.getAllClienti();
            tableModel.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (Cliente cliente : clienti) {
                Object[] rowData = {
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getCognome(),
                    cliente.getCodiceFiscale(),
                    cliente.getDataNascita() != null ? sdf.format(cliente.getDataNascita()) : "N/A",
                    cliente.getEmail(),
                    cliente.getTelefono(),
                    cliente.getCodiceCliente(),
                    cliente.getDataIscrizione() != null ? sdf.format(cliente.getDataIscrizione()) : "N/A",
                    cliente.getDataScadenzaAbbonamento() != null ? sdf.format(cliente.getDataScadenzaAbbonamento()) : "N/A",
                    cliente.getTipoAbbonamento()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei dati dei clienti: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}