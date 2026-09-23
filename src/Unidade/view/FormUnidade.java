package Unidade.view;

import Unidade.dao.UnidadeDAO;
import Unidade.model.Unidade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormUnidade extends JDialog {

    private JTextField edtNome = new JTextField();
    private JTextField edtSigla = new JTextField();
    private JCheckBox chkPesagem = new JCheckBox("Pesagem / Balança", false);
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private Unidade unidade;
    private UnidadeDAO dao = new UnidadeDAO();

    public FormUnidade(Frame parent, Unidade unidade) {
        super(parent, "Unidade - Cadastro", true);
        this.unidade = (unidade != null) ? unidade : new Unidade();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(460, 230);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(4, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Sigla*:"));
        pnlCampos.add(edtSigla);

        pnlCampos.add(new JLabel("Controle de Balança:"));
        pnlCampos.add(chkPesagem);

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
        if (unidade.getId() != null) {
            edtNome.setText(unidade.getNome());
            edtSigla.setText(unidade.getSigla());
            chkPesagem.setSelected(unidade.isPesagem());
            chkAtivo.setSelected(unidade.isAtivo());
        }
    }

    private void salvar() {
        try {
            unidade.setNome(edtNome.getText().trim());
            unidade.setSigla(edtSigla.getText().trim().toUpperCase());
            unidade.setPesagem(chkPesagem.isSelected());
            unidade.setAtivo(chkAtivo.isSelected());

            unidade.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
