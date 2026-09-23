package Cidade.view;

import Cidade.dao.CidadeDAO;
import Cidade.model.Cidade;
import Estado.model.Estado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FormCidade extends JDialog {

    private JTextField edtNome = new JTextField();
    private JTextField edtCodIbge = new JTextField();
    private JTextField edtCep = new JTextField();
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);
    private JComboBox<Estado> cmbEstado = new JComboBox<>();

    private boolean confirmado = false;
    private Cidade cidade;
    private CidadeDAO dao = new CidadeDAO();

    public FormCidade(Frame parent, Cidade cidade) {
        super(parent, "Cidade - Cadastro", true);
        this.cidade = (cidade != null) ? cidade : new Cidade();
        initComponents();
        configurarAtalhos();
        carregarEstados();
        preencherCampos();
    }

    private void initComponents() {
        setSize(480, 260);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(5, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Cód. IBGE*:"));
        pnlCampos.add(edtCodIbge);

        pnlCampos.add(new JLabel("Estado*:"));
        pnlCampos.add(cmbEstado);

        pnlCampos.add(new JLabel("CEP:"));
        pnlCampos.add(edtCep);

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

    private void carregarEstados() {
        try {
            cmbEstado.removeAllItems();
            for (Estado e : dao.recuperarEstados()) {
                cmbEstado.addItem(e);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        if (cidade.getId() != null) {
            edtNome.setText(cidade.getNome());
            edtCodIbge.setText(cidade.getCodigoIBGE() != null ? cidade.getCodigoIBGE().toString() : "");
            edtCep.setText(cidade.getCep() != null ? cidade.getCep().toString() : "");
            chkAtivo.setSelected(cidade.isAtivo());

            for (int i = 0; i < cmbEstado.getItemCount(); i++) {
                if (cmbEstado.getItemAt(i).getId().equals(cidade.getEstado().getId())) {
                    cmbEstado.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void salvar() {
        try {
            cidade.setNome(edtNome.getText().trim());
            cidade.setCodigoIBGE(Integer.parseInt(edtCodIbge.getText().trim()));
            cidade.setCep(edtCep.getText().trim().isEmpty() ? 0 : Integer.parseInt(edtCep.getText().trim()));
            cidade.setAtivo(chkAtivo.isSelected());
            cidade.setEstado((Estado) cmbEstado.getSelectedItem());

            cidade.validar();
            confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código IBGE e CEP devem ser números válidos.", "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
