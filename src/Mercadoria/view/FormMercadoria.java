package Mercadoria.view;

import ConfigFiscal.model.ConfigFiscal;
import Mercadoria.dao.MercadoriaDAO;
import Mercadoria.model.Mercadoria;
import Unidade.model.Unidade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

public class FormMercadoria extends JDialog {

    private JTextField edtNome = new JTextField();
    private JComboBox<Unidade> cmbUnidade = new JComboBox<>();
    private JComboBox<ConfigFiscal> cmbConfigFiscal = new JComboBox<>();
    private JCheckBox chkAtivo = new JCheckBox("Ativo", true);

    // Valores e Formação de Preço
    private JTextField edtValCompra = new JTextField("0.00");
    private JTextField edtMargemCusto = new JTextField("0.00");
    private JTextField edtValCusto = new JTextField("0.00");
    private JTextField edtMargemLucro = new JTextField("0.00");
    private JTextField edtValVenda = new JTextField("0.00");
    private JTextField edtMargemMinimo = new JTextField("0.00");
    private JTextField edtValMinimo = new JTextField("0.00");

    // Identificação e Estoque
    private JTextField edtCodBarra = new JTextField();
    private JTextField edtReferencia = new JTextField();
    private JTextField edtNcm = new JTextField();
    private JTextField edtCest = new JTextField();
    private JTextField edtCprod = new JTextField();
    private JTextField edtQtdEstoque = new JTextField("0");
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"PRO", "SER", "INS"});

    // Físico e Pesos
    private JTextField edtPesoBruto = new JTextField("0.00");
    private JTextField edtPesoLiquido = new JTextField("0.00");
    private JTextField edtComissao = new JTextField("0.00");

    private boolean confirmado = false;
    private Mercadoria mercadoria;
    private MercadoriaDAO dao = new MercadoriaDAO();

    public FormMercadoria(Frame parent, Mercadoria mercadoria) {
        super(parent, "Mercadoria - Cadastro", true);
        this.mercadoria = (mercadoria != null) ? mercadoria : new Mercadoria();
        initComponents();
        configurarAtalhos();
        carregarCombos();
        preencherCampos();
    }

    private void initComponents() {
        setSize(700, 480);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Topo: Nome, Unidade e Ativo
        JPanel pnlTopo = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlTopo.setBorder(BorderFactory.createEmptyBorder(10, 15, 8, 15));
        pnlTopo.add(new JLabel("Descrição da Mercadoria*:"));
        pnlTopo.add(edtNome);
        pnlTopo.add(new JLabel("Unidade de Medida*:"));

        JPanel pnlUnidAtivo = new JPanel(new BorderLayout(5, 0));
        pnlUnidAtivo.add(cmbUnidade, BorderLayout.CENTER);
        pnlUnidAtivo.add(chkAtivo, BorderLayout.EAST);
        pnlTopo.add(pnlUnidAtivo);
        add(pnlTopo, BorderLayout.NORTH);

        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Preços e Margens", criarPainelPrecos());
        abas.addTab("Fiscal e Identificação", criarPainelFiscal());
        abas.addTab("Pesos e Estoque", criarPainelPesos());
        add(abas, BorderLayout.CENTER);

        // Barra de botões
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar (F12)");
        JButton btnCancelar = new JButton("Cancelar (ESC)");

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharComConfirmacao());

        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        add(pnlBotoes, BorderLayout.SOUTH);
    }

    private JPanel criarPainelPrecos() {
        JPanel pnl = new JPanel(new GridLayout(4, 4, 8, 8));
        pnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        pnl.add(new JLabel("Valor Compra (R$)*:"));
        pnl.add(edtValCompra);
        pnl.add(new JLabel("Margem Custo (%):"));
        pnl.add(edtMargemCusto);

        pnl.add(new JLabel("Valor Custo (R$)*:"));
        pnl.add(edtValCusto);
        pnl.add(new JLabel("Margem Lucro (%):"));
        pnl.add(edtMargemLucro);

        pnl.add(new JLabel("Valor Venda (R$)*:"));
        pnl.add(edtValVenda);
        pnl.add(new JLabel("Margem Mínima (%):"));
        pnl.add(edtMargemMinimo);

        pnl.add(new JLabel("Valor Mínimo (R$)*:"));
        pnl.add(edtValMinimo);
        pnl.add(new JLabel("Comissão (%):"));
        pnl.add(edtComissao);

        return pnl;
    }

    private JPanel criarPainelFiscal() {
        JPanel pnl = new JPanel(new GridLayout(5, 2, 8, 8));
        pnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        pnl.add(new JLabel("Configuração Fiscal*:"));
        pnl.add(cmbConfigFiscal);

        pnl.add(new JLabel("NCM:"));
        pnl.add(edtNcm);

        pnl.add(new JLabel("CEST:"));
        pnl.add(edtCest);

        pnl.add(new JLabel("Cód. Barras (EAN):"));
        pnl.add(edtCodBarra);

        pnl.add(new JLabel("Cód. Próprio (cProd) / Ref:"));
        JPanel pnlRef = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlRef.add(edtCprod);
        pnlRef.add(edtReferencia);
        pnl.add(pnlRef);

        return pnl;
    }

    private JPanel criarPainelPesos() {
        JPanel pnl = new JPanel(new GridLayout(4, 2, 8, 8));
        pnl.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        pnl.add(new JLabel("Tipo Mercadoria:"));
        pnl.add(cmbTipo);

        pnl.add(new JLabel("Estoque Atual:"));
        pnl.add(edtQtdEstoque);

        pnl.add(new JLabel("Peso Bruto (Kg)*:"));
        pnl.add(edtPesoBruto);

        pnl.add(new JLabel("Peso Líquido (Kg)*:"));
        pnl.add(edtPesoLiquido);

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

    private void carregarCombos() {
        try {
            cmbUnidade.removeAllItems();
            for (Unidade u : dao.recuperarUnidades()) {
                cmbUnidade.addItem(u);
            }

            cmbConfigFiscal.removeAllItems();
            for (ConfigFiscal cf : dao.recuperarConfiguracoesFiscais()) {
                cmbConfigFiscal.addItem(cf);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados auxiliares: " + ex.getMessage());
        }
    }

    private void preencherCampos() {
        if (mercadoria.getId() != null) {
            edtNome.setText(mercadoria.getNome());
            chkAtivo.setSelected(mercadoria.isAtivo());

            edtValCompra.setText(mercadoria.getValCompra().toString());
            edtMargemCusto.setText(mercadoria.getMargemCusto().toString());
            edtValCusto.setText(mercadoria.getValCusto().toString());
            edtMargemLucro.setText(mercadoria.getMargemLucro().toString());
            edtValVenda.setText(mercadoria.getValVenda().toString());
            edtMargemMinimo.setText(mercadoria.getMargemMinimo().toString());
            edtValMinimo.setText(mercadoria.getValMinimo().toString());

            edtCodBarra.setText(mercadoria.getCodBarra() != null ? mercadoria.getCodBarra() : "");
            edtReferencia.setText(mercadoria.getReferencia() != null ? mercadoria.getReferencia() : "");
            edtNcm.setText(mercadoria.getNcm() != null ? mercadoria.getNcm() : "");
            edtCest.setText(mercadoria.getCest() != null ? mercadoria.getCest() : "");
            edtCprod.setText(mercadoria.getCprod() != null ? mercadoria.getCprod() : "");
            cmbTipo.setSelectedItem(mercadoria.getTipo());
            edtQtdEstoque.setText(mercadoria.getQuantidade() != null ? mercadoria.getQuantidade().toString() : "0");

            edtPesoBruto.setText(mercadoria.getPesoBruto().toString());
            edtPesoLiquido.setText(mercadoria.getPesoLiquido().toString());
            edtComissao.setText(mercadoria.getComissao() != null ? mercadoria.getComissao().toString() : "0.00");

            if (mercadoria.getUnidade() != null && mercadoria.getUnidade().getId() != null) {
                for (int i = 0; i < cmbUnidade.getItemCount(); i++) {
                    if (cmbUnidade.getItemAt(i).getId().equals(mercadoria.getUnidade().getId())) {
                        cmbUnidade.setSelectedIndex(i);
                        break;
                    }
                }
            }

            if (mercadoria.getConfigFiscal() != null && mercadoria.getConfigFiscal().getId() != null) {
                for (int i = 0; i < cmbConfigFiscal.getItemCount(); i++) {
                    if (cmbConfigFiscal.getItemAt(i).getId().equals(mercadoria.getConfigFiscal().getId())) {
                        cmbConfigFiscal.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private void salvar() {
        try {
            mercadoria.setNome(edtNome.getText().trim());
            mercadoria.setUnidade((Unidade) cmbUnidade.getSelectedItem());
            mercadoria.setConfigFiscal((ConfigFiscal) cmbConfigFiscal.getSelectedItem());
            mercadoria.setAtivo(chkAtivo.isSelected());

            mercadoria.setValCompra(new BigDecimal(edtValCompra.getText().trim().replace(",", ".")));
            mercadoria.setMargemCusto(new BigDecimal(edtMargemCusto.getText().trim().replace(",", ".")));
            mercadoria.setValCusto(new BigDecimal(edtValCusto.getText().trim().replace(",", ".")));
            mercadoria.setMargemLucro(new BigDecimal(edtMargemLucro.getText().trim().replace(",", ".")));
            mercadoria.setValVenda(new BigDecimal(edtValVenda.getText().trim().replace(",", ".")));
            mercadoria.setMargemMinimo(new BigDecimal(edtMargemMinimo.getText().trim().replace(",", ".")));
            mercadoria.setValMinimo(new BigDecimal(edtValMinimo.getText().trim().replace(",", ".")));

            mercadoria.setCodBarra(edtCodBarra.getText().trim().isEmpty() ? null : edtCodBarra.getText().trim());
            mercadoria.setReferencia(edtReferencia.getText().trim().isEmpty() ? null : edtReferencia.getText().trim());
            mercadoria.setNcm(edtNcm.getText().trim().isEmpty() ? null : edtNcm.getText().trim());
            mercadoria.setCest(edtCest.getText().trim().isEmpty() ? null : edtCest.getText().trim());
            mercadoria.setCprod(edtCprod.getText().trim().isEmpty() ? null : edtCprod.getText().trim());
            mercadoria.setTipo((String) cmbTipo.getSelectedItem());
            mercadoria.setQuantidade(edtQtdEstoque.getText().trim().isEmpty() ? 0 : Integer.parseInt(edtQtdEstoque.getText().trim()));

            mercadoria.setPesoBruto(new BigDecimal(edtPesoBruto.getText().trim().replace(",", ".")));
            mercadoria.setPesoLiquido(new BigDecimal(edtPesoLiquido.getText().trim().replace(",", ".")));
            mercadoria.setComissao(edtComissao.getText().trim().isEmpty() ? null : new BigDecimal(edtComissao.getText().trim().replace(",", ".")));

            mercadoria.validar();
            confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores monetários, pesos ou estoque com formato numérico inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() { return confirmado; }
    public Mercadoria getMercadoria() { return mercadoria; }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
