package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.AbbonamentoController;
import it.unina.bd.progpoo.gymmanager.model.Cliente;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date; // Aggiunto l'import
import javax.swing.*;

public class EditClienteDialog extends JDialog {

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField telefonoField;
    private JTextField codiceTesseraField;
    private JComboBox<String> tipoAbbonamentoComboBox;

    private AbbonamentoController abbonamentoController;
    private Cliente clienteToEdit;

    public EditClienteDialog(JFrame parent, AbbonamentoController controller, Cliente cliente) {
        super(parent, "Modifica Cliente", true);
        this.abbonamentoController = controller;
        this.clienteToEdit = cliente;
        
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo Nome (non modificabile)
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; nomeField = new JTextField(clienteToEdit.getNome());
        nomeField.setEditable(false);
        panel.add(nomeField, gbc);

        // Campo Cognome
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Cognome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; cognomeField = new JTextField(clienteToEdit.getCognome());
        panel.add(cognomeField, gbc);

        // Campo Email
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; emailField = new JTextField(clienteToEdit.getEmail());
        panel.add(emailField, gbc);
        
        // Campo Telefono
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; telefonoField = new JTextField(clienteToEdit.getTelefono());
        panel.add(telefonoField, gbc);
        
        // Campo Codice Tessera (non modificabile)
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Codice Tessera:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; codiceTesseraField = new JTextField(clienteToEdit.getCodiceCliente());
        codiceTesseraField.setEditable(false);
        panel.add(codiceTesseraField, gbc);
        
        // Campo Tipo Abbonamento
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Tipo Abbonamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; String[] tipiAbbonamento = {"Mensile", "Annuale"};
        tipoAbbonamentoComboBox = new JComboBox<>(tipiAbbonamento);
        tipoAbbonamentoComboBox.setSelectedItem(clienteToEdit.getTipoAbbonamento());
        panel.add(tipoAbbonamentoComboBox, gbc);

        // Pulsante Salva
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.EAST;
        JButton salvaButton = new JButton("Salva Modifiche");
        panel.add(salvaButton, gbc);

        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ottieni il vecchio tipo di abbonamento per il confronto
                    String vecchioTipo = clienteToEdit.getTipoAbbonamento();
                    String nuovoTipo = (String) tipoAbbonamentoComboBox.getSelectedItem();

                    // Aggiorna l'oggetto cliente con i nuovi dati
                    clienteToEdit.setCognome(cognomeField.getText());
                    clienteToEdit.setEmail(emailField.getText());
                    clienteToEdit.setTelefono(telefonoField.getText());
                    clienteToEdit.setTipoAbbonamento(nuovoTipo);

                    // Se il tipo di abbonamento è cambiato, ricalcola la data di scadenza
                    if (!vecchioTipo.equalsIgnoreCase(nuovoTipo)) {
                        Date nuovaDataScadenza = abbonamentoController.calcolaDataScadenza(nuovoTipo);
                        clienteToEdit.setDataScadenzaAbbonamento(nuovaDataScadenza);
                    }
                    
                    // Chiama il metodo update nel DAO tramite il controller
                    abbonamentoController.updateCliente(clienteToEdit);
                    JOptionPane.showMessageDialog(EditClienteDialog.this, "Modifiche salvate con successo!");
                    dispose(); // Chiude la finestra di dialogo
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(EditClienteDialog.this, "Errore SQL: " + ex.getMessage(), "Errore Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
    }
}