package Categoria.view;

import Categoria.dao.CategoriaDAO;
import Categoria.model.Categoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormCategoria extends JDialog {

    private JTextField edtNome = new JTextField();
    private JCheckBox chkEnviarWeb = new JCheckBox("Enviar Web", true);
    private JCheckBox chkParticipante = new JCheckBox("Participante", false);
    private JCheckBox chkMercadoria = new JCheckBox("Mercadoria", false);
    private JCheckBox chkConta = new JCheckBox("Conta", false);
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private Categoria categoria;
    private CategoriaDAO dao = new CategoriaDAO();

    public FormCategoria(Frame parent, Categoria categoria) {
        super(parent, "Categoria - Cadastro", true);
        this.categoria = (categoria != null) ? categoria : new Categoria();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(480, 290);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(6, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Enviar Web:"));
        pnlCampos.add(chkEnviarWeb);

        pnlCampos.add(new JLabel("Participante:"));
        pnlCampos.add(chkParticipante);

        pnlCampos.add(new JLabel("Mercadoria:"));
        pnlCampos.add(chkMercadoria);

        pnlCampos.add(new JLabel("Conta:"));
        pnlCampos.add(chkConta);

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
        if (categoria.getId() != null) {
            edtNome.setText(categoria.getNome());
            chkEnviarWeb.setSelected(categoria.isEnviarWeb());
            chkParticipante.setSelected(categoria.isParticipante());
            chkMercadoria.setSelected(categoria.isMercadoria());
            chkConta.setSelected(categoria.isConta());
            chkAtivo.setSelected(categoria.isAtivo());
        }
    }

    private void salvar() {
        try {
            categoria.setNome(edtNome.getText().trim());
            categoria.setEnviarWeb(chkEnviarWeb.isSelected());
            categoria.setParticipante(chkParticipante.isSelected());
            categoria.setMercadoria(chkMercadoria.isSelected());
            categoria.setConta(chkConta.isSelected());
            categoria.setAtivo(chkAtivo.isSelected());

            categoria.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
