package new_logmais;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
//import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

public class broker extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtProcesso;
    private JCheckBox chkII, chkIPI, chkPIS, chkCOFINS, chkICMS;
    private JTextField txtAliquota;
    private JComboBox<String> cmbQuebra;
    private JButton btnAtualizar;
    private JTextArea txtResultado;

    private String processoSelecionado;
    private String impostoSelecionado;
    private double aliquotaValor;
    private String quebraSelecionada;

    private JPopupMenu popupProcesso;

    /**
     * Cria o frame.
     */
    public broker() {
        setTitle("Atualizador de Alíquotas v.1.3");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(577, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblProcesso = new JLabel("Selecione o processo:");
        lblProcesso.setBounds(89, 23, 121, 14);
        lblProcesso.setFont(new Font("Tahoma", Font.BOLD, 11));
        contentPane.add(lblProcesso);

        txtProcesso = new JTextField();
        txtProcesso.setBounds(220, 20, 295, 23);
        contentPane.add(txtProcesso);
        txtProcesso.setColumns(10);

        popupProcesso = new JPopupMenu();

        txtProcesso.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                suggestProcesso();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                suggestProcesso();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                suggestProcesso();
            }
        });

        txtProcesso.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                SwingUtilities.invokeLater(() -> popupProcesso.setVisible(false));
            }
        });

        JLabel lblImposto = new JLabel("Selecione o imposto:");
        lblImposto.setBounds(89, 59, 116, 14);
        lblImposto.setFont(new Font("Tahoma", Font.BOLD, 11));
        contentPane.add(lblImposto);

        JPanel panelImpostos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelImpostos.setBounds(220, 50, 295, 23);
        contentPane.add(panelImpostos);

        chkII = new JCheckBox("II");
        chkIPI = new JCheckBox("IPI");
        chkPIS = new JCheckBox("PIS");
        chkCOFINS = new JCheckBox("COFINS");
        chkICMS = new JCheckBox("ICMS");

        ActionListener chkListener = e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            if (source.isSelected()) {
                desmarcarOutrosCheckBoxes(source);
            }
        };

        chkII.addActionListener(chkListener);
        chkIPI.addActionListener(chkListener);
        chkPIS.addActionListener(chkListener);
        chkCOFINS.addActionListener(chkListener);
        chkICMS.addActionListener(chkListener);

        panelImpostos.add(chkII);
        panelImpostos.add(chkIPI);
        panelImpostos.add(chkPIS);
        panelImpostos.add(chkCOFINS);
        panelImpostos.add(chkICMS);

        JLabel lblAliquota = new JLabel("Digite o valor da alíquota:");
        lblAliquota.setBounds(66, 86, 144, 14);
        lblAliquota.setFont(new Font("Tahoma", Font.BOLD, 11));
        contentPane.add(lblAliquota);

        txtAliquota = new JTextField();
        txtAliquota.setBounds(220, 83, 295, 20);
        contentPane.add(txtAliquota);
        txtAliquota.setColumns(10);

        ((AbstractDocument) txtAliquota.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        txtAliquota.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatarAliquota(txtAliquota);
            }
        });

        JLabel lblQuebra = new JLabel("Selecione a quebra:");
        lblQuebra.setBounds(100, 116, 110, 14);
        lblQuebra.setFont(new Font("Tahoma", Font.BOLD, 11));
        contentPane.add(lblQuebra);

        cmbQuebra = new JComboBox<>(new String[]{"1", "2", "3", "4", "11", "12", "22", "33", "44", "88", "111", "222", "333", "444"});
        cmbQuebra.setBounds(220, 113, 295, 20);
        contentPane.add(cmbQuebra);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(151, 151, 247, 34);
        btnAtualizar.setFont(new Font("Tahoma", Font.BOLD, 11));
        contentPane.add(btnAtualizar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBounds(20, 196, 495, 129);
        contentPane.add(scrollPane);

        btnAtualizar.addActionListener(e -> atualizarDados());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menu mainMenu = new menu();
                mainMenu.setVisible(true);
            }
        });
    }

    private void atualizarDados() {
        processoSelecionado = txtProcesso.getText().trim();
        impostoSelecionado = getImpostoSelecionado();
        String aliquotaStr = txtAliquota.getText().trim();
        quebraSelecionada = (String) cmbQuebra.getSelectedItem();

        if (processoSelecionado.isEmpty() || impostoSelecionado == null || aliquotaStr.isEmpty() || quebraSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Dados incompletos, preencha tudo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            aliquotaValor = Double.parseDouble(aliquotaStr.replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor da alíquota inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!verificarProcessoExistente(processoSelecionado)) {
            JOptionPane.showMessageDialog(this, "Processo não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Conn.getConnection()) {
            String sql;
            PreparedStatement pst;

            if ("ICMS".equals(impostoSelecionado)) {
                sql = "UPDATE DIADICAO SET vlaliqicms = ?, vlaliqicmsauxiliar = ? " +
                        "WHERE idprocesso IN (SELECT idprocesso FROM processo WHERE nrprocesso = ?) " +
                        "AND QUEBRAAUXILIAR = ?";
                pst = conn.prepareStatement(sql);
                pst.setDouble(1, aliquotaValor); // Primeiro parâmetro
                pst.setDouble(2, aliquotaValor); // Segundo parâmetro
                pst.setString(3, processoSelecionado); // Terceiro parâmetro
                pst.setString(4, quebraSelecionada); // Quarto parâmetro
            } else {
                String coluna = getColunaImposto(impostoSelecionado);
                sql = "UPDATE DIADICAO SET " + coluna + " = ? " +
                        "WHERE idprocesso IN (SELECT idprocesso FROM processo WHERE nrprocesso = ?) " +
                        "AND QUEBRAAUXILIAR = ?";
                pst = conn.prepareStatement(sql);
                pst.setDouble(1, aliquotaValor); // Primeiro parâmetro
                pst.setString(2, processoSelecionado); // Segundo parâmetro
                pst.setString(3, quebraSelecionada); // Terceiro parâmetro
            }

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
            	txtResultado.append("O Processo \"" + processoSelecionado 
                        + "\" teve a alíquota de \"" + impostoSelecionado 
                        + "\" atualizada para o itens com a quebra auxiliar \"" 
                        + quebraSelecionada + "\" com sucesso.\n");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma linha foi atualizada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getImpostoSelecionado() {
        if (chkPIS.isSelected()) return "PIS";
        if (chkCOFINS.isSelected()) return "COFINS";
        if (chkII.isSelected()) return "II";
        if (chkIPI.isSelected()) return "IPI";
        if (chkICMS.isSelected()) return "ICMS";
        return null;
    }

    private String getColunaImposto(String imposto) {
        switch (imposto) {
            case "II": return "vlaliqii";
            case "IPI": return "vlaliqipi";
            case "PIS": return "vlaliqpis";
            case "COFINS": return "vlaliqcofins";
            default: return "";
        }
    }

    private void limparCampos() {
        txtProcesso.setText("");
        txtAliquota.setText("");
        cmbQuebra.setSelectedIndex(0);
        desmarcarOutrosCheckBoxes(null);
    }

    private void desmarcarOutrosCheckBoxes(JCheckBox checkBox) {
        for (JCheckBox chk : new JCheckBox[]{chkII, chkIPI, chkPIS, chkCOFINS, chkICMS}) {
            if (chk != checkBox) chk.setSelected(false);
        }
    }

    private void suggestProcesso() {
        // Sugestões automáticas (não implementado para simplificar)
    }

    private boolean verificarProcessoExistente(String processo) {
        // Verifica no banco se o processo existe (não implementado para simplificar)
        return true;
    }

    private void formatarAliquota(JTextField campo) {
        try {
            String text = campo.getText().trim();
            if (!text.isEmpty()) {
                double value = Double.parseDouble(text.replace(",", "."));
                campo.setText(new DecimalFormat("#0.00").format(value));
            }
        } catch (NumberFormatException ignored) {
        }
    }
}