package estado.view;

import estado.dao.EstadoDAO;
import estado.model.Estado;
import pais.model.Pais;

import javax.swing.*;
import java.awt.*;

public class FormEstado extends JDialog {

    private JTextField edtNome = new JTextField();
    private JTextField edtSigla = new JTextField();
    private JComboBox<Pais> cmbPais = new JComboBox<>();
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private Estado estado;
    private EstadoDAO dao = new EstadoDAO();

    public FormEstado(Frame parent, Estado estado) {
        super(parent, "Estado - Cadastro", true);
        this.estado = (estado != null) ? estado : new Estado();
        initComponents();
        carregarPaises();
        preencherCampos();
    }

    private void initComponents() {
        setSize(480, 230);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(4, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome do Estado*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Sigla*:"));
        pnlCampos.add(edtSigla);

        pnlCampos.add(new JLabel("País*:"));
        pnlCampos.add(cmbPais);

        pnlCampos.add(new JLabel("Situação:"));
        pnlCampos.add(chkAtivo);

        add(pnlCampos, BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar (F12)");
        JButton btnCancelar = new JButton("Cancelar (ESC)");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        add(pnlBotoes, BorderLayout.SOUTH);
    }

    private void carregarPaises() {
        try {
            cmbPais.removeAllItems();
            for (Pais p : dao.recuperarPaises()) {
                cmbPais.addItem(p);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar países: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        if (estado.getId() != null) {
            edtNome.setText(estado.getNome());
            edtSigla.setText(estado.getSigla());
            chkAtivo.setSelected(estado.isAtivo());

            for (int i = 0; i < cmbPais.getItemCount(); i++) {
                if (cmbPais.getItemAt(i).getId().equals(estado.getPais().getId())) {
                    cmbPais.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void salvar() {
        try {
            estado.setNome(edtNome.getText().trim());
            estado.setSigla(edtSigla.getText().trim().toUpperCase());
            estado.setAtivo(chkAtivo.isSelected());
            estado.setPais((Pais) cmbPais.getSelectedItem());

            estado.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Estado getEstado() {
        return estado;
    }
}
