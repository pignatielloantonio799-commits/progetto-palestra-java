package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.IstruttoreController;
import it.unina.bd.progpoo.gymmanager.model.Istruttore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class IstruttoriPanel extends JPanel {

    private IstruttoreController istruttoreController;
    private JTable istruttoriTable;
    private DefaultTableModel tableModel;

    public IstruttoriPanel(IstruttoreController controller) {
        this.istruttoreController = controller;
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nome", "Cognome", "Email", "Telefono", "Matricola", "Data Assunzione", "Stipendio", "Specializzazione", "Anni Esperienza"};
        tableModel = new DefaultTableModel(columnNames, 0);
        istruttoriTable = new JTable(tableModel);
        
        add(new JScrollPane(istruttoriTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi Istruttore");
        JButton editButton = new JButton("Modifica Istruttore");
        JButton deleteButton = new JButton("Elimina Istruttore");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            EditIstruttoreDialog dialog = new EditIstruttoreDialog((JFrame) SwingUtilities.getWindowAncestor(this), istruttoreController, null);
            dialog.setVisible(true);
            refreshIstruttoriTable();
        });

        editButton.addActionListener(e -> {
            int selectedRow = istruttoriTable.getSelectedRow();
            if (selectedRow >= 0) {
                String matricola = (String) tableModel.getValueAt(selectedRow, 5);
                try {
                    Istruttore istruttore = istruttoreController.findByMatricola(matricola);
                    if (istruttore != null) {
                        EditIstruttoreDialog dialog = new EditIstruttoreDialog((JFrame) SwingUtilities.getWindowAncestor(this), istruttoreController, istruttore);
                        dialog.setVisible(true);
                        refreshIstruttoriTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Istruttore non trovato nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Errore nel recupero dei dati dell'istruttore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un istruttore da modificare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = istruttoriTable.getSelectedRow();
            if (selectedRow >= 0) {
                String matricola = (String) tableModel.getValueAt(selectedRow, 5);
                int confirm = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare questo istruttore?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        istruttoreController.deleteIstruttore(matricola);
                        refreshIstruttoriTable();
                        JOptionPane.showMessageDialog(this, "Istruttore eliminato con successo.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Errore SQL: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un istruttore da eliminare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshIstruttoriTable();
    }

    public void refreshIstruttoriTable() {
        try {
            List<Istruttore> istruttori = istruttoreController.getAllIstruttori();
            tableModel.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Istruttore istruttore : istruttori) {
                Object[] rowData = {
                    istruttore.getId(),
                    istruttore.getNome(),
                    istruttore.getCognome(),
                    istruttore.getEmail(),
                    istruttore.getTelefono(),
                    istruttore.getMatricola(),
                    istruttore.getDataAssunzione() != null ? sdf.format(istruttore.getDataAssunzione()) : "N/A",
                    istruttore.getStipendio(),
                    istruttore.getSpecializzazione(),
                    istruttore.getAnniEsperienza()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei dati: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}