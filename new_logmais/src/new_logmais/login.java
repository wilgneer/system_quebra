package new_logmais;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;

    /**
     * Create the frame.
     */
    public login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 411, 345);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 128, 192));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JButton btnNewButton = new JButton("Conectar");
        btnNewButton.setBounds(145, 207, 113, 36);
        contentPane.add(btnNewButton);
        
        textField = new JTextField();
        textField.setBounds(99, 92, 206, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("USUÁRIO");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(174, 67, 72, 14);
        contentPane.add(lblNewLabel);
        
        JLabel lblSenha = new JLabel("SENHA");
        lblSenha.setForeground(new Color(255, 255, 255));
        lblSenha.setBounds(182, 127, 42, 14);
        contentPane.add(lblSenha);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(99, 152, 207, 20);
        contentPane.add(passwordField);
        
        // Ação para o botão de login
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText();
                String password = new String(passwordField.getPassword());

                // Verifica se os campos estão preenchidos
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                // Tenta realizar a consulta no banco
                try {
                    Connection conn = Conn.getConnection();
                    String query = "SELECT * FROM usuario WHERE nmusername = ? AND cdpassword = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);

                    ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        // Credenciais corretas, abre o arquivo menu.java
                        JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                        dispose();  // Fecha a janela de login
                        menu menuScreen = new menu();
                        menuScreen.setVisible(true);
                    } else {
                        // Credenciais incorretas
                        JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }

                    // Fechar conexões
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}