package Participante.view;


import Participante.dao.ParticipanteDAO;
import Participante.model.Participante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormParticipante extends JDialog {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField edtNome = new JTextField();
    private JTextField edtFantasia = new JTextField();
    private JTextField edtCpfCnpj = new JTextField();
    private JTextField edtRgIe = new JTextField();
    private JTextField edtEmail = new JTextField();
    private JTextField edtTelefone1 = new JTextField();
    private JTextField edtTelefone2 = new JTextField();
    private JTextField edtDataNasc = new JTextField(); // formato dd/MM/yyyy
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"CLI", "FOR", "FUN"});
    private JTextField edtComissao = new JTextField();
    private JTextField edtObservacao = new JTextField();
    private JTextField edtLogin = new JTextField();
    private JPasswordField edtSenha = new JPasswordField();

    private JCheckBox chkEnviarWeb = new JCheckBox("Enviar Web", true);
    private JCheckBox chkAutorizaLgpd = new JCheckBox("Autoriza LGPD", true);
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private Participante participante;
    private ParticipanteDAO dao = new ParticipanteDAO();

    public FormParticipante(Frame parent, Participante participante) {
        super(parent, "Participante - Cadastro", true);
        this.participante = (participante != null) ? participante : new Participante();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(680, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(12, 2, 8, 6));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Nome Fantasia:"));
        pnlCampos.add(edtFantasia);

        pnlCampos.add(new JLabel("CPF/CNPJ*:"));
        pnlCampos.add(edtCpfCnpj);

        pnlCampos.add(new JLabel("RG/IE:"));
        pnlCampos.add(edtRgIe);

        pnlCampos.add(new JLabel("E-mail:"));
        pnlCampos.add(edtEmail);

        pnlCampos.add(new JLabel("Telefone 1:"));
        pnlCampos.add(edtTelefone1);

        pnlCampos.add(new JLabel("Telefone 2:"));
        pnlCampos.add(edtTelefone2);

        pnlCampos.add(new JLabel("Data Nascimento (dd/mm/aaaa):"));
        pnlCampos.add(edtDataNasc);

        pnlCampos.add(new JLabel("Tipo:"));
        pnlCampos.add(cmbTipo);

        pnlCampos.add(new JLabel("Comissão (%):"));
        pnlCampos.add(edtComissao);

        pnlCampos.add(new JLabel("Login:"));
        pnlCampos.add(edtLogin);

        pnlCampos.add(new JLabel("Senha:"));
        pnlCampos.add(edtSenha);

        add(new JScrollPane(pnlCampos), BorderLayout.CENTER);

        // Painel Inferior com Checkboxes e Botões
        JPanel pnlInferior = new JPanel(new BorderLayout());

        JPanel pnlOpcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlOpcoes.add(chkEnviarWeb);
        pnlOpcoes.add(chkAutorizaLgpd);
        pnlOpcoes.add(chkAtivo);
        pnlInferior.add(pnlOpcoes, BorderLayout.NORTH);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar (F12)");
        JButton btnCancelar = new JButton("Cancelar (ESC)");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharComConfirmacao());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        pnlInferior.add(pnlBotoes, BorderLayout.SOUTH);

        add(pnlInferior, BorderLayout.SOUTH);
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
        if (participante.getId() != null) {
            edtNome.setText(participante.getNome());
            edtFantasia.setText(participante.getFantasia() != null ? participante.getFantasia() : "");
            edtCpfCnpj.setText(participante.getCpfCnpj());
            edtRgIe.setText(participante.getRgIe() != null ? participante.getRgIe() : "");
            edtEmail.setText(participante.getEmail() != null ? participante.getEmail() : "");
            edtTelefone1.setText(participante.getTelefone1() != null ? participante.getTelefone1() : "");
            edtTelefone2.setText(participante.getTelefone2() != null ? participante.getTelefone2() : "");
            if (participante.getDataNascimento() != null) {
                edtDataNasc.setText(participante.getDataNascimento().format(dtf));
            }
            cmbTipo.setSelectedItem(participante.getTipo());
            edtComissao.setText(participante.getComissao() != null ? participante.getComissao().toString() : "");
            edtLogin.setText(participante.getLogin() != null ? participante.getLogin() : "");
            edtSenha.setText(participante.getSenha() != null ? participante.getSenha() : "");
            chkEnviarWeb.setSelected(participante.isEnviarWeb());
            chkAutorizaLgpd.setSelected(participante.isAutorizaLgpd());
            chkAtivo.setSelected(participante.isAtivo());
        }
    }

    private void salvar() {
        try {
            participante.setNome(edtNome.getText().trim());
            participante.setFantasia(edtFantasia.getText().trim().isEmpty() ? null : edtFantasia.getText().trim());
            participante.setCpfCnpj(edtCpfCnpj.getText().trim());
            participante.setRgIe(edtRgIe.getText().trim().isEmpty() ? null : edtRgIe.getText().trim());
            participante.setEmail(edtEmail.getText().trim().isEmpty() ? null : edtEmail.getText().trim());
            participante.setTelefone1(edtTelefone1.getText().trim().isEmpty() ? null : edtTelefone1.getText().trim());
            participante.setTelefone2(edtTelefone2.getText().trim().isEmpty() ? null : edtTelefone2.getText().trim());

            if (!edtDataNasc.getText().trim().isEmpty()) {
                participante.setDataNascimento(LocalDate.parse(edtDataNasc.getText().trim(), dtf));
            } else {
                participante.setDataNascimento(null);
            }

            participante.setTipo((String) cmbTipo.getSelectedItem());

            if (!edtComissao.getText().trim().isEmpty()) {
                participante.setComissao(new BigDecimal(edtComissao.getText().trim().replace(",", ".")));
            } else {
                participante.setComissao(null);
            }

            participante.setLogin(edtLogin.getText().trim().isEmpty() ? null : edtLogin.getText().trim());
            String pwd = new String(edtSenha.getPassword()).trim();
            participante.setSenha(pwd.isEmpty() ? null : pwd);

            participante.setEnviarWeb(chkEnviarWeb.isSelected());
            participante.setAutorizaLgpd(chkAutorizaLgpd.isSelected());
            participante.setAtivo(chkAtivo.isSelected());

            participante.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Participante getParticipante() {
        return participante;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
