package new_logmais;

import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import javax.swing.JLabel;
//import javax.swing.ImageIcon;
//import javax.swing.JTextField;
//import javax.swing.JPanel;
import javax.swing.JTextPane;

public class menu {

    private JFrame frame;

    /**
     * Lançar a aplicação.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    menu window = new menu();
                    window.getFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Criar a aplicação.
     */
    public menu() {
        initialize();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Inicializar o conteúdo do frame.
     */
    private void initialize() {
        setFrame(new JFrame());
        getFrame().getContentPane().setBackground(new Color(255, 255, 255));
        getFrame().setTitle("Menu Principal");
        getFrame().setBounds(100, 100, 450, 400);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);

        // Botão 1 - Atualizar Alq. Processo
        JButton btnAtualizarProcesso = new JButton("Atualizar Alq. Processo");
        btnAtualizarProcesso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            broker brokerWindow = new broker();
                            brokerWindow.setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        btnAtualizarProcesso.setBounds(140, 113, 170, 46);
        getFrame().getContentPane().add(btnAtualizarProcesso);

        // Botão 6 - Sair
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnSair.setBounds(140, 197, 170, 25);
        getFrame().getContentPane().add(btnSair);
        
        JTextPane txtpnDesignByRga = new JTextPane();
        txtpnDesignByRga.setText("©-Wilgner and RGA");
        txtpnDesignByRga.setBounds(10, 330, 108, 20);
        frame.getContentPane().add(txtpnDesignByRga);
    }

    /**
     * Implementa o método setVisible para o frame
     */
    public void setVisible(boolean visible) {
        if (frame != null) {
            frame.setVisible(visible);
        }
    }
}