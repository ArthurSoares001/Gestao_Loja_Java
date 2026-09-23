package PlanoPagamento.view;


import NaturezaOperacao.model.NaturezaOperacao;
import PlanoPagamento.dao.PlanoPagamentoDAO;
import PlanoPagamento.model.PlanoPagamento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

public class FormPlanoPagamento extends JDialog {

    private JTextField edtNome = new JTextField();
    private JTextField edtDesconto = new JTextField();
    private JTextField edtDescontoMaximo = new JTextField();
    private JTextField edtMaxParcela = new JTextField();
    private JTextField edtTaxaServico = new JTextField();
    private JComboBox<NaturezaOperacao> cmbNatureza = new JComboBox<>();

    private JCheckBox chkImutavel = new JCheckBox("Imutável", false);
    private JCheckBox chkEnviarWeb = new JCheckBox("Enviar Web", true);
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    private boolean confirmado = false;
    private PlanoPagamento planoPagamento;
    private PlanoPagamentoDAO dao = new PlanoPagamentoDAO();

    public FormPlanoPagamento(Frame parent, PlanoPagamento planoPagamento) {
        super(parent, "Plano de Pagamento - Cadastro", true);
        this.planoPagamento = (planoPagamento != null) ? planoPagamento : new PlanoPagamento();
        initComponents();
        configurarAtalhos();
        carregarNaturezas();
        preencherCampos();
    }

    private void initComponents() {
        setSize(540, 380);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlCampos = new JPanel(new GridLayout(6, 2, 8, 8));
        pnlCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlCampos.add(new JLabel("Nome*:"));
        pnlCampos.add(edtNome);

        pnlCampos.add(new JLabel("Natureza de Operação:"));
        pnlCampos.add(cmbNatureza);

        pnlCampos.add(new JLabel("Desconto Padrão (%):"));
        pnlCampos.add(edtDesconto);

        pnlCampos.add(new JLabel("Desconto Máximo (%):"));
        pnlCampos.add(edtDescontoMaximo);

        pnlCampos.add(new JLabel("Máximo de Parcelas:"));
        pnlCampos.add(edtMaxParcela);

        pnlCampos.add(new JLabel("Taxa de Serviço (%):"));
        pnlCampos.add(edtTaxaServico);

        add(pnlCampos, BorderLayout.CENTER);

        // Painel Inferior: Opções e Botões
        JPanel pnlInferior = new JPanel(new BorderLayout());

        JPanel pnlChecks = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlChecks.add(chkImutavel);
        pnlChecks.add(chkEnviarWeb);
        pnlChecks.add(chkAtivo);
        pnlInferior.add(pnlChecks, BorderLayout.NORTH);

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

    private void carregarNaturezas() {
        try {
            cmbNatureza.removeAllItems();
            cmbNatureza.addItem(new NaturezaOperacao()); // Opção nula/vazia
            for (NaturezaOperacao n : dao.recuperarNaturezas()) {
                cmbNatureza.addItem(n);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar naturezas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCampos() {
        if (planoPagamento.getId() != null) {
            edtNome.setText(planoPagamento.getNome());
            edtDesconto.setText(planoPagamento.getDesconto() != null ? planoPagamento.getDesconto().toString() : "");
            edtDescontoMaximo.setText(planoPagamento.getDescontoMaximo() != null ? planoPagamento.getDescontoMaximo().toString() : "");
            edtMaxParcela.setText(planoPagamento.getMaxParcela() != null ? planoPagamento.getMaxParcela().toString() : "");
            edtTaxaServico.setText(planoPagamento.getTaxaServico() != null ? planoPagamento.getTaxaServico().toString() : "");

            chkImutavel.setSelected(planoPagamento.isImutavel());
            chkEnviarWeb.setSelected(planoPagamento.isEnviarWeb());
            chkAtivo.setSelected(planoPagamento.isAtivo());

            if (planoPagamento.getNaturezaOperacao() != null && planoPagamento.getNaturezaOperacao().getId() != null) {
                for (int i = 0; i < cmbNatureza.getItemCount(); i++) {
                    NaturezaOperacao nat = cmbNatureza.getItemAt(i);
                    if (nat.getId() != null && nat.getId().equals(planoPagamento.getNaturezaOperacao().getId())) {
                        cmbNatureza.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private void salvar() {
        try {
            planoPagamento.setNome(edtNome.getText().trim());

            if (!edtDesconto.getText().trim().isEmpty()) {
                planoPagamento.setDesconto(new BigDecimal(edtDesconto.getText().trim().replace(",", ".")));
            } else {
                planoPagamento.setDesconto(null);
            }

            if (!edtDescontoMaximo.getText().trim().isEmpty()) {
                planoPagamento.setDescontoMaximo(new BigDecimal(edtDescontoMaximo.getText().trim().replace(",", ".")));
            } else {
                planoPagamento.setDescontoMaximo(null);
            }

            if (!edtMaxParcela.getText().trim().isEmpty()) {
                planoPagamento.setMaxParcela(Integer.parseInt(edtMaxParcela.getText().trim()));
            } else {
                planoPagamento.setMaxParcela(null);
            }

            if (!edtTaxaServico.getText().trim().isEmpty()) {
                planoPagamento.setTaxaServico(new BigDecimal(edtTaxaServico.getText().trim().replace(",", ".")));
            } else {
                planoPagamento.setTaxaServico(null);
            }

            planoPagamento.setImutavel(chkImutavel.isSelected());
            planoPagamento.setEnviarWeb(chkEnviarWeb.isSelected());
            planoPagamento.setAtivo(chkAtivo.isSelected());

            NaturezaOperacao nat = (NaturezaOperacao) cmbNatureza.getSelectedItem();
            if (nat != null && nat.getId() != null && nat.getId() > 0) {
                planoPagamento.setNaturezaOperacao(nat);
            } else {
                planoPagamento.setNaturezaOperacao(null);
            }

            planoPagamento.validar();
            confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores numéricos, parcelas ou taxas contêm formato inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public PlanoPagamento getPlanoPagamento() {
        return planoPagamento;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
