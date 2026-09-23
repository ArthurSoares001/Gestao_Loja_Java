package ConfigFiscal.view;

import ConfigFiscal.dao.ConfigFiscalDAO;
import ConfigFiscal.model.ConfigFiscal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

public class FormConfigFiscal extends JDialog {

    private JTextField edtNome = new JTextField();
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    // Aba Geral / ICMS / PIS / COFINS / IPI
    private JTextField edtCstIcms = new JTextField();
    private JTextField edtCstIcmsFora = new JTextField();
    private JTextField edtAliqIcms = new JTextField();
    private JTextField edtReducaoBase = new JTextField();
    private JTextField edtReducaoBaseFora = new JTextField();
    private JTextField edtCstPis = new JTextField();
    private JTextField edtAliqPis = new JTextField();
    private JTextField edtCstCofins = new JTextField();
    private JTextField edtAliqCofins = new JTextField();
    private JTextField edtCstIpi = new JTextField();
    private JTextField edtAliqIpi = new JTextField();
    private JTextArea txtMensagem = new JTextArea(3, 20);

    // Aba Reforma Tributária
    private JCheckBox chkUsarReforma = new JCheckBox("Habilitar Reforma Tributária", false);
    private JTextField edtClassTrib = new JTextField();
    private JTextField edtCstIbs = new JTextField();
    private JTextField edtAliqIbs = new JTextField();
    private JTextField edtCstCbs = new JTextField();
    private JTextField edtAliqCbs = new JTextField();
    private JTextField edtCstIs = new JTextField();
    private JTextField edtAliqIs = new JTextField();
    private JCheckBox chkSplitPayment = new JCheckBox("Split Payment", false);
    private JTextField edtPercSplit = new JTextField();
    private JCheckBox chkCashback = new JCheckBox("Cashback", false);
    private JTextField edtPercCashback = new JTextField();

    private boolean confirmado = false;
    private ConfigFiscal configFiscal;
    private ConfigFiscalDAO dao = new ConfigFiscalDAO();

    public FormConfigFiscal(Frame parent, ConfigFiscal configFiscal) {
        super(parent, "Configuração Fiscal - Cadastro", true);
        this.configFiscal = (configFiscal != null) ? configFiscal : new ConfigFiscal();
        initComponents();
        configurarAtalhos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(680, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Topo: Nome e Situação
        JPanel pnlTopo = new JPanel(new BorderLayout(8, 8));
        pnlTopo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        JPanel pnlNome = new JPanel(new BorderLayout(5, 5));
        pnlNome.add(new JLabel("Nome da Regra*:"), BorderLayout.WEST);
        pnlNome.add(edtNome, BorderLayout.CENTER);
        pnlTopo.add(pnlNome, BorderLayout.CENTER);
        pnlTopo.add(chkAtivo, BorderLayout.EAST);
        add(pnlTopo, BorderLayout.NORTH);

        // Abas de Configurações
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Tributação Padrão (ICMS/IPI/PIS/COFINS)", criarPainelPadrao());
        abas.addTab("Reforma Tributária (IBS/CBS/IS)", criarPainelReforma());
        add(abas, BorderLayout.CENTER);

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

    private JPanel criarPainelPadrao() {
        JPanel pnl = new JPanel(new GridLayout(6, 4, 8, 8));
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnl.add(new JLabel("CST ICMS:"));
        pnl.add(edtCstIcms);
        pnl.add(new JLabel("CST Fora UF:"));
        pnl.add(edtCstIcmsFora);

        pnl.add(new JLabel("Alíq. ICMS (%):"));
        pnl.add(edtAliqIcms);
        pnl.add(new JLabel("Redução Base (%):"));
        pnl.add(edtReducaoBase);

        pnl.add(new JLabel("CST PIS:"));
        pnl.add(edtCstPis);
        pnl.add(new JLabel("Alíq. PIS (%):"));
        pnl.add(edtAliqPis);

        pnl.add(new JLabel("CST COFINS:"));
        pnl.add(edtCstCofins);
        pnl.add(new JLabel("Alíq. COFINS (%):"));
        pnl.add(edtAliqCofins);

        pnl.add(new JLabel("CST IPI:"));
        pnl.add(edtCstIpi);
        pnl.add(new JLabel("Alíq. IPI (%):"));
        pnl.add(edtAliqIpi);

        pnl.add(new JLabel("Msg Fiscal:"));
        pnl.add(new JScrollPane(txtMensagem));
        pnl.add(new JLabel("Redução Base Fora:"));
        pnl.add(edtReducaoBaseFora);

        return pnl;
    }

    private JPanel criarPainelReforma() {
        JPanel pnl = new JPanel(new GridLayout(7, 2, 8, 8));
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnl.add(chkUsarReforma);
        pnl.add(new JLabel(""));

        pnl.add(new JLabel("Classificação Tributária:"));
        pnl.add(edtClassTrib);

        pnl.add(new JLabel("CST IBS / Alíquota (%):"));
        JPanel pnlIbs = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlIbs.add(edtCstIbs);
        pnlIbs.add(edtAliqIbs);
        pnl.add(pnlIbs);

        pnl.add(new JLabel("CST CBS / Alíquota (%):"));
        JPanel pnlCbs = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlCbs.add(edtCstCbs);
        pnlCbs.add(edtAliqCbs);
        pnl.add(pnlCbs);

        pnl.add(new JLabel("CST IS / Alíquota (%):"));
        JPanel pnlIs = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlIs.add(edtCstIs);
        pnlIs.add(edtAliqIs);
        pnl.add(pnlIs);

        pnl.add(chkSplitPayment);
        pnl.add(edtPercSplit);

        pnl.add(chkCashback);
        pnl.add(edtPercCashback);

        return pnl;
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
        if (configFiscal.getId() != null) {
            edtNome.setText(configFiscal.getNome());
            chkAtivo.setSelected(configFiscal.isAtivo());

            edtCstIcms.setText(configFiscal.getCst() != null ? configFiscal.getCst() : "");
            edtCstIcmsFora.setText(configFiscal.getCstFora() != null ? configFiscal.getCstFora() : "");
            edtAliqIcms.setText(configFiscal.getAliqIcms() != null ? configFiscal.getAliqIcms().toString() : "");
            edtReducaoBase.setText(configFiscal.getReducaoBase() != null ? configFiscal.getReducaoBase().toString() : "");
            edtReducaoBaseFora.setText(configFiscal.getReducaoBaseFora() != null ? configFiscal.getReducaoBaseFora().toString() : "");

            edtCstPis.setText(configFiscal.getCstPis() != null ? configFiscal.getCstPis() : "");
            edtAliqPis.setText(configFiscal.getAliqPis() != null ? configFiscal.getAliqPis().toString() : "");

            edtCstCofins.setText(configFiscal.getCstCofins() != null ? configFiscal.getCstCofins() : "");
            edtAliqCofins.setText(configFiscal.getAliqCofins() != null ? configFiscal.getAliqCofins().toString() : "");

            edtCstIpi.setText(configFiscal.getCstIpi() != null ? configFiscal.getCstIpi() : "");
            edtAliqIpi.setText(configFiscal.getAliqIpi() != null ? configFiscal.getAliqIpi().toString() : "");

            txtMensagem.setText(configFiscal.getMensagem() != null ? configFiscal.getMensagem() : "");

            chkUsarReforma.setSelected(configFiscal.isUsarReformaTrib());
            edtClassTrib.setText(configFiscal.getClassTrib() != null ? configFiscal.getClassTrib() : "");
            edtCstIbs.setText(configFiscal.getCstIbs() != null ? configFiscal.getCstIbs() : "");
            edtAliqIbs.setText(configFiscal.getAliqIbs() != null ? configFiscal.getAliqIbs().toString() : "");
            edtCstCbs.setText(configFiscal.getCstCbs() != null ? configFiscal.getCstCbs() : "");
            edtAliqCbs.setText(configFiscal.getAliqCbs() != null ? configFiscal.getAliqCbs().toString() : "");
            edtCstIs.setText(configFiscal.getCstIs() != null ? configFiscal.getCstIs() : "");
            edtAliqIs.setText(configFiscal.getAliqIs() != null ? configFiscal.getAliqIs().toString() : "");

            chkSplitPayment.setSelected(configFiscal.isUtilizaSplitPayment());
            edtPercSplit.setText(configFiscal.getPercSplitPayment() != null ? configFiscal.getPercSplitPayment().toString() : "");
            chkCashback.setSelected(configFiscal.isUtilizaCashback());
            edtPercCashback.setText(configFiscal.getPercCashback() != null ? configFiscal.getPercCashback().toString() : "");
        }
    }

    private void salvar() {
        try {
            configFiscal.setNome(edtNome.getText().trim());
            configFiscal.setAtivo(chkAtivo.isSelected());

            configFiscal.setCst(edtCstIcms.getText().trim().isEmpty() ? null : edtCstIcms.getText().trim());
            configFiscal.setCstFora(edtCstIcmsFora.getText().trim().isEmpty() ? null : edtCstIcmsFora.getText().trim());
            configFiscal.setAliqIcms(edtAliqIcms.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqIcms.getText().trim().replace(",", ".")));
            configFiscal.setReducaoBase(edtReducaoBase.getText().trim().isEmpty() ? null : new BigDecimal(edtReducaoBase.getText().trim().replace(",", ".")));
            configFiscal.setReducaoBaseFora(edtReducaoBaseFora.getText().trim().isEmpty() ? null : new BigDecimal(edtReducaoBaseFora.getText().trim().replace(",", ".")));

            configFiscal.setCstPis(edtCstPis.getText().trim().isEmpty() ? null : edtCstPis.getText().trim());
            configFiscal.setAliqPis(edtAliqPis.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqPis.getText().trim().replace(",", ".")));

            configFiscal.setCstCofins(edtCstCofins.getText().trim().isEmpty() ? null : edtCstCofins.getText().trim());
            configFiscal.setAliqCofins(edtAliqCofins.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqCofins.getText().trim().replace(",", ".")));

            configFiscal.setCstIpi(edtCstIpi.getText().trim().isEmpty() ? null : edtCstIpi.getText().trim());
            configFiscal.setAliqIpi(edtAliqIpi.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqIpi.getText().trim().replace(",", ".")));

            configFiscal.setMensagem(txtMensagem.getText().trim().isEmpty() ? null : txtMensagem.getText().trim());

            configFiscal.setUsarReformaTrib(chkUsarReforma.isSelected());
            configFiscal.setClassTrib(edtClassTrib.getText().trim().isEmpty() ? null : edtClassTrib.getText().trim());
            configFiscal.setCstIbs(edtCstIbs.getText().trim().isEmpty() ? null : edtCstIbs.getText().trim());
            configFiscal.setAliqIbs(edtAliqIbs.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqIbs.getText().trim().replace(",", ".")));
            configFiscal.setCstCbs(edtCstCbs.getText().trim().isEmpty() ? null : edtCstCbs.getText().trim());
            configFiscal.setAliqCbs(edtAliqCbs.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqCbs.getText().trim().replace(",", ".")));
            configFiscal.setCstIs(edtCstIs.getText().trim().isEmpty() ? null : edtCstIs.getText().trim());
            configFiscal.setAliqIs(edtAliqIs.getText().trim().isEmpty() ? null : new BigDecimal(edtAliqIs.getText().trim().replace(",", ".")));

            configFiscal.setUtilizaSplitPayment(chkSplitPayment.isSelected());
            configFiscal.setPercSplitPayment(edtPercSplit.getText().trim().isEmpty() ? null : new BigDecimal(edtPercSplit.getText().trim().replace(",", ".")));
            configFiscal.setUtilizaCashback(chkCashback.isSelected());
            configFiscal.setPercCashback(edtPercCashback.getText().trim().isEmpty() ? null : new BigDecimal(edtPercCashback.getText().trim().replace(",", ".")));

            configFiscal.validar();
            confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Alíquotas e percentuais devem ser numéricos válidos.", "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public ConfigFiscal getConfigFiscal() {
        return configFiscal;
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
