package it.unina.bd.progpoo.gymmanager.gui;

import it.unina.bd.progpoo.gymmanager.controller.*;
import it.unina.bd.progpoo.gymmanager.dao.*;
import it.unina.bd.progpoo.gymmanager.dao.impl.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Dichiarazione dei controller
    private AbbonamentoController abbonamentoController;
    private IstruttoreController istruttoreController;
    private CorsoController corsoController;
    private ClienteController clienteController;
    private PrenotazioneController prenotazioneController;
    private AccessoController accessoController;
    private SchedaAllenamentoController schedaAllenamentoController;

    public MainFrame() throws SQLException {
        setTitle("Gym Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Risoluzione delle dipendenze in un metodo dedicato
        setupDependencies();

        // Inizializza i pannelli della GUI con i controller corretti
        JPanel clientiPanel = new ClientiPanel(clienteController, abbonamentoController);
        JPanel istruttoriPanel = new IstruttoriPanel(istruttoreController);
        JPanel corsiPanel = new CorsiPanel(corsoController, istruttoreController, prenotazioneController);
        
        // Pannello per i bottoni di navigazione
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton clientiButton = new JButton("Gestisci Clienti");
        JButton istruttoriButton = new JButton("Gestisci Istruttori");
        JButton corsiButton = new JButton("Gestisci Corsi");
        menuPanel.add(clientiButton);
        menuPanel.add(istruttoriButton);
        menuPanel.add(corsiButton);

        // Pannello principale con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Aggiungi i pannelli al CardLayout con un nome univoco
        mainPanel.add(clientiPanel, "Clienti");
        mainPanel.add(istruttoriPanel, "Istruttori");
        mainPanel.add(corsiPanel, "Corsi");
        
        // Aggiungi i pannelli al frame principale
        add(menuPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // ActionListener per i bottoni del menu
        clientiButton.addActionListener(e -> cardLayout.show(mainPanel, "Clienti"));
        istruttoriButton.addActionListener(e -> cardLayout.show(mainPanel, "Istruttori"));
        corsiButton.addActionListener(e -> cardLayout.show(mainPanel, "Corsi"));
    }

    private void setupDependencies() throws SQLException {
        // 1. Istanziare le classi DAO senza dipendenze o con dipendenze già risolte
        IstruttoreDAO istruttoreDAO = new IstruttoreDAOImpl();

        // 2. Risolvere la dipendenza circolare tra ClienteDAO e PrenotazioneDAO
        ClienteDAO clienteDAO = new ClienteDAOImpl(null); // Inizializzato a null temporaneamente
        CorsoDAO corsoDAO = new CorsoDAOImpl(istruttoreDAO);
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAOImpl(clienteDAO, corsoDAO);
        ((ClienteDAOImpl) clienteDAO).setPrenotazioneDAO(prenotazioneDAO);
        
        // 3. Istanziare gli altri DAO
        AbbonamentoDAO abbonamentoDAO = new AbbonamentoDAOImpl(clienteDAO);
        AccessoDAO accessoDAO = new AccessoDAOImpl(clienteDAO);
        SchedaAllenamentoDAO schedaAllenamentoDAO = new SchedaAllenamentoDAOImpl(clienteDAO, istruttoreDAO);

        // 4. Istanziare i Controller, iniettando i DAO risolti
        this.clienteController = new ClienteController(clienteDAO);
        this.istruttoreController = new IstruttoreController(istruttoreDAO);
        this.corsoController = new CorsoController(corsoDAO, istruttoreDAO, prenotazioneDAO);
        this.abbonamentoController = new AbbonamentoController(clienteDAO, abbonamentoDAO);
        
        // 🟢 CORREZIONE: Passa tutte le dipendenze corrette
        this.prenotazioneController = new PrenotazioneController(clienteDAO, corsoDAO, prenotazioneDAO, abbonamentoDAO);
        this.accessoController = new AccessoController(clienteDAO, accessoDAO, abbonamentoDAO);
        this.schedaAllenamentoController = new SchedaAllenamentoController(schedaAllenamentoDAO, clienteDAO, istruttoreDAO);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Errore di connessione al database: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}