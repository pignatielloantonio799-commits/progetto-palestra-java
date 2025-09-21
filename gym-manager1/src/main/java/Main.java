public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                it.unina.bd.progpoo.gymmanager.gui.MainFrame frame = new it.unina.bd.progpoo.gymmanager.gui.MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
