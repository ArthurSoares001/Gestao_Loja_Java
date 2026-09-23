package NaturezaOperacao.view;

import NaturezaOperacao.dao.NaturezaOperacaoDAO;
import NaturezaOperacao.model.NaturezaOperacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormNaturezaOperacao extends JDialog {

    private JTextField edtNome = new JTextField();
    private JTextField edtFantasia = new JTextField();
    private JTextField edtCfop = new JTextField();
    private JTextField edtCfopFora = new JTextField();
    private JComboBox<String> cmbOperacao = new JComboBox<>(new String[]{"P", "O"});
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"S", "E"});
    private JComboBox<String> cmbFinalidade = new JComboBox<>(new String[]{"1", "2", "3", "4"});
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private NaturezaOperacao naturezaOperacao;
    private NaturezaOperacaoDAO dao = new NaturezaOperacaoDAO();

    public FormNaturezaOperacao(Frame parent, NaturezaOperacao naturezaOperacao) {
        super(parent, "Natureza de Operação - Cadastro", true);
        this.naturezaOperacao = (naturezaOperacao != null) ? naturezaOperacao : new NaturezaOperacao();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(520, 360);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(8, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Fantasia:"));
        pnlCampos.add(edtFantasia);

        pnlCampos.add(new JLabel("CFOP (Dentro UF):"));
        pnlCampos.add(edtCfop);

        pnlCampos.add(new JLabel("CFOP (Fora UF):"));
        pnlCampos.add(edtCfopFora);

        pnlCampos.add(new JLabel("Operação:"));
        pnlCampos.add(cmbOperacao);

        pnlCampos.add(new JLabel("Tipo (Entrada/Saída):"));
        pnlCampos.add(cmbTipo);

        pnlCampos.add(new JLabel("Finalidade:"));
        pnlCampos.add(cmbFinalidade);

        pnlCampos.add(new JLabel("Situação:"));
        pnlCampos.add(chkAtivo);

        add(pnlCampos, BorderLayout.CENTER);

        // Barra de botões Salvar / Cancelar
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar (F12)");
        JButton btnCancelar = new JButton("Cancelar (ESC)");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharComConfirmacao());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        add(pnlBotoes, BorderLayout.SOUTH);
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "salvar");
        root.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { salvar(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelar");
        root.getActionMap().put("cancelar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { fecharComConfirmacao(); }
        });
    }

    private void preencherCampos() {
        if (naturezaOperacao.getId() != null) {
            edtNome.setText(naturezaOperacao.getNome());
            edtFantasia.setText(naturezaOperacao.getFantasia() != null ? naturezaOperacao.getFantasia() : "");
            edtCfop.setText(naturezaOperacao.getCfop() != null ? naturezaOperacao.getCfop() : "");
            edtCfopFora.setText(naturezaOperacao.getCfopFora() != null ? naturezaOperacao.getCfopFora() : "");
            cmbOperacao.setSelectedItem(naturezaOperacao.getOperacao());
            cmbTipo.setSelectedItem(naturezaOperacao.getTipo());
            cmbFinalidade.setSelectedItem(naturezaOperacao.getFinalidade());
            chkAtivo.setSelected(naturezaOperacao.isAtivo());
        }
    }

    private void salvar() {
        try {
            naturezaOperacao.setNome(edtNome.getText().trim());
            naturezaOperacao.setFantasia(edtFantasia.getText().trim().isEmpty() ? null : edtFantasia.getText().trim());
            naturezaOperacao.setCfop(edtCfop.getText().trim().isEmpty() ? null : edtCfop.getText().trim());
            naturezaOperacao.setCfopFora(edtCfopFora.getText().trim().isEmpty() ? null : edtCfopFora.getText().trim());
            naturezaOperacao.setOperacao((String) cmbOperacao.getSelectedItem());
            naturezaOperacao.setTipo((String) cmbTipo.getSelectedItem());
            naturezaOperacao.setFinalidade((String) cmbFinalidade.getSelectedItem());
            naturezaOperacao.setAtivo(chkAtivo.isSelected());

            naturezaOperacao.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public NaturezaOperacao getNaturezaOperacao() {
        return naturezaOperacao;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
