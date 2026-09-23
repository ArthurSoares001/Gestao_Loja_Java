package Pais.view;

import Pais.model.Pais;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormPais extends JDialog {

    private JTextField edtNome = new JTextField();
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private Pais pais;

    public FormPais(Frame parent, Pais pais) {
        super(parent, "Pais - Cadastro", true);
        this.pais = (pais != null) ? pais : new Pais();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(450, 180);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

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
        if (pais.getId() != null) {
            edtNome.setText(pais.getNome());
            chkAtivo.setSelected(pais.isAtivo());
        }
    }

    private void salvar() {
        try {
            pais.setNome(edtNome.getText().trim());
            pais.setAtivo(chkAtivo.isSelected());

            pais.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Pais getPais() {
        return pais;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
