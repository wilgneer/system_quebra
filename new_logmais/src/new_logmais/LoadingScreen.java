package new_logmais;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe responsável por exibir uma tela de carregamento enquanto tenta conectar ao banco de dados.
 */
public class LoadingScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JLabel lblLoading;

    /**
     * Lançamento da aplicação.
     */
    public static void main(String[] args) {
        // Configura a aparência para o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inicia a tela de carregamento
        EventQueue.invokeLater(() -> {
            try {
                LoadingScreen frame = new LoadingScreen();
                frame.setVisible(true);
                frame.connectToDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Cria o frame da tela de carregamento.
     */
    public LoadingScreen() {
        setTitle("Carregando...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null); // Centraliza na tela
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(panel);

        lblLoading = new JLabel("Conectando ao banco de dados...");
        lblLoading.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblLoading.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblLoading, BorderLayout.NORTH);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Barra de progresso indeterminada
        panel.add(progressBar, BorderLayout.CENTER);
    }

    /**
     * Tenta conectar ao banco de dados em uma thread separada.
     */
    private void connectToDatabase() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() { // Tipos explícitos
            @Override
            protected Void doInBackground() throws Exception {
                // Simula o processo de conexão
                // Você pode ajustar o tempo conforme necessário

                try {
                    Connection conn = Conn.getConnection();
                    if (conn != null && !conn.isClosed()) {
                        // Conexão bem-sucedida
                    } else {
                        throw new SQLException("Conexão inválida.");
                    }
                } catch (SQLException ex) {
                    throw ex;
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Verifica se houve exceção
                    // Conexão bem-sucedida, abre a tela de login
                    dispose(); // Fecha a tela de carregamento
                    SwingUtilities.invokeLater(() -> {
                        try {
                            login loginFrame = new login();
                            loginFrame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    // Erro na conexão, exibe mensagem e fecha a aplicação
                    JOptionPane.showMessageDialog(LoadingScreen.this, "Banco fora do ar!", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        };
        worker.execute(); // Executa o SwingWorker
    }
}