import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Look and Feel nativo do Windows
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Abre a tela principal correspondente ao Delphi
                FormPrincipal frmPrincipal = new FormPrincipal();
                frmPrincipal.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}