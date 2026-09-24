package Compra.view;

import CompraItem.model.CompraItem;
import Mercadoria.model.Mercadoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class FormCompraEscolherItem extends JDialog {

    private JTextField edtCodigo = new JTextField();
    private JTextField edtDescricao = new JTextField();
    private JTextField edtCodBarras = new JTextField();
    private JTextField edtUnidade = new JTextField();

    private JTextField edtQuantidade = new JTextField("1.00");
    private JTextField edtValorCompra = new JTextField("0.00");
    private JTextField edtDesconto = new JTextField("0.00");
    private JTextField edtMargemCusto = new JTextField("0.00");
    private JTextField edtValorCusto = new JTextField("0.00");
    private JTextField edtMargemLucro = new JTextField("0.00");
    private JTextField edtValorVenda = new JTextField("0.00");

    // Impostos
    private JTextField edtCst = new JTextField();
    private JTextField edtIcms = new JTextField("0.00");
    private JTextField edtPis = new JTextField("0.00");
    private JTextField edtCofins = new JTextField("0.00");
    private JTextField edtIpi = new JTextField("0.00");

    // Totais
    private JTextField edtTotalCompras = new JTextField("0.00");
    private JTextField edtTotalVendas = new JTextField("0.00");

    private JButton btnInserir = new JButton("Inserir (F12)");
    private JButton btnCancelar = new JButton("Cancelar (ESC)");

    private boolean confirmado = false;
    private CompraItem compraItem;
    private Mercadoria mercadoria;

    public FormCompraEscolherItem(Dialog parent, CompraItem compraItem) {
        super(parent, "Item - Adicionar / Editar", true);
        this.compraItem = (compraItem != null) ? compraItem : new CompraItem();
        this.mercadoria = this.compraItem.getMercadoria();
        initComponents();
        configurarAtalhos();
        carregarDados();
    }

    public FormCompraEscolherItem(Dialog parent, CompraItem compraItem, Mercadoria mercadoria) {
        super(parent, "Item - Adicionar / Editar", true);
        this.compraItem = (compraItem != null) ? compraItem : new CompraItem();
        this.mercadoria = mercadoria;
        this.compraItem.setMercadoria(mercadoria);
        initComponents();
        configurarAtalhos();
        carregarDados();
    }

    private void initComponents() {
        setSize(780, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Dados do Produto
        JPanel pnlProd = new JPanel(new GridLayout(2, 4, 6, 6));
        pnlProd.setBorder(BorderFactory.createTitledBorder("Identificação da Mercadoria"));
        pnlProd.add(new JLabel("Código:"));
        edtCodigo.setEditable(false);
        pnlProd.add(edtCodigo);
        pnlProd.add(new JLabel("Cód. Barras:"));
        edtCodBarras.setEditable(false);
        pnlProd.add(edtCodBarras);

        pnlProd.add(new JLabel("Descrição:"));
        edtDescricao.setEditable(false);
        pnlProd.add(edtDescricao);
        pnlProd.add(new JLabel("Unidade:"));
        edtUnidade.setEditable(false);
        pnlProd.add(edtUnidade);

        // Preços e Margens
        JPanel pnlPrecos = new JPanel(new GridLayout(4, 4, 6, 6));
        pnlPrecos.setBorder(BorderFactory.createTitledBorder("Preços e Margens"));

        pnlPrecos.add(new JLabel("Quantidade:")); pnlPrecos.add(edtQuantidade);
        pnlPrecos.add(new JLabel("Valor Compra R$:")); pnlPrecos.add(edtValorCompra);
        pnlPrecos.add(new JLabel("Desconto (%):")); pnlPrecos.add(edtDesconto);
        pnlPrecos.add(new JLabel("Margem Custo (%):")); pnlPrecos.add(edtMargemCusto);
        pnlPrecos.add(new JLabel("Valor Custo R$:")); pnlPrecos.add(edtValorCusto);
        pnlPrecos.add(new JLabel("Margem Lucro (%):")); pnlPrecos.add(edtMargemLucro);
        pnlPrecos.add(new JLabel("Valor Venda R$:")); pnlPrecos.add(edtValorVenda);
        pnlPrecos.add(new JLabel("")); pnlPrecos.add(new JLabel(""));

        // Impostos
        JPanel pnlImpostos = new JPanel(new GridLayout(2, 6, 6, 6));
        pnlImpostos.setBorder(BorderFactory.createTitledBorder("Impostos do Item"));
        pnlImpostos.add(new JLabel("CST:")); pnlImpostos.add(edtCst);
        pnlImpostos.add(new JLabel("ICMS R$:")); pnlImpostos.add(edtIcms);
        pnlImpostos.add(new JLabel("IPI R$:")); pnlImpostos.add(edtIpi);
        pnlImpostos.add(new JLabel("PIS R$:")); pnlImpostos.add(edtPis);
        pnlImpostos.add(new JLabel("COFINS R$:")); pnlImpostos.add(edtCofins);

        // Totais
        JPanel pnlTotais = new JPanel(new GridLayout(1, 4, 6, 6));
        pnlTotais.setBorder(BorderFactory.createTitledBorder("Totais do Item"));
        edtTotalCompras.setEditable(false);
        edtTotalVendas.setEditable(false);
        pnlTotais.add(new JLabel("Total Compra R$:")); pnlTotais.add(edtTotalCompras);
        pnlTotais.add(new JLabel("Total Venda R$:")); pnlTotais.add(edtTotalVendas);

        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.add(pnlProd);
        pnlCentro.add(pnlPrecos);
        pnlCentro.add(pnlImpostos);
        pnlCentro.add(pnlTotais);
        add(pnlCentro, BorderLayout.CENTER);

        // Ações
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnInserir.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        pnlBotoes.add(btnInserir);
        pnlBotoes.add(btnCancelar);
        add(pnlBotoes, BorderLayout.SOUTH);

        configurarListenersCalculos();
    }

    private void configurarListenersCalculos() {
        FocusAdapter recalculador = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                recalcularValores();
            }
        };

        edtQuantidade.addFocusListener(recalculador);
        edtValorCompra.addFocusListener(recalculador);
        edtDesconto.addFocusListener(recalculador);
        edtMargemCusto.addFocusListener(recalculador);
        edtMargemLucro.addFocusListener(recalculador);
    }

    private void recalcularValores() {
        try {
            double qtd = Double.parseDouble(edtQuantidade.getText().replace(",", "."));
            double vCompra = Double.parseDouble(edtValorCompra.getText().replace(",", "."));
            double desc = Double.parseDouble(edtDesconto.getText().replace(",", "."));
            double mCusto = Double.parseDouble(edtMargemCusto.getText().replace(",", "."));
            double mLucro = Double.parseDouble(edtMargemLucro.getText().replace(",", "."));

            double vCusto = vCompra * (1.0 + (mCusto / 100.0));
            double vVenda = vCusto * (1.0 + (mLucro / 100.0));
            double totCompra = (qtd * vCompra) - ((qtd * vCompra * desc) / 100.0);
            double totVenda = qtd * vVenda;

            edtValorCusto.setText(String.format("%.2f", vCusto));
            edtValorVenda.setText(String.format("%.2f", vVenda));
            edtTotalCompras.setText(String.format("%.2f", totCompra));
            edtTotalVendas.setText(String.format("%.2f", totVenda));
        } catch (Exception ignored) {}
    }

    private void carregarDados() {
        if (mercadoria != null) {
            edtCodigo.setText(mercadoria.getId() != null ? mercadoria.getId().toString() : "");
            edtDescricao.setText(mercadoria.getNome() != null ? mercadoria.getNome() : "");
            edtCodBarras.setText(mercadoria.getCodBarra() != null ? mercadoria.getCodBarra() : "");
            edtUnidade.setText(mercadoria.getUnidade() != null ? mercadoria.getUnidade().getSigla() : "");
            
            // Preencher valores da mercadoria se o item ainda não tiver valores definidos
            if (compraItem.getValorCompra() == 0) {
                if (mercadoria.getValCompra() != null && mercadoria.getValCompra().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    edtValorCompra.setText(mercadoria.getValCompra().toString());
                }
            }
            if (mercadoria.getMargemCusto() != null) {
                edtMargemCusto.setText(mercadoria.getMargemCusto().toString());
            }
            if (compraItem.getValorCusto() == 0 && mercadoria.getValCusto() != null) {
                edtValorCusto.setText(mercadoria.getValCusto().toString());
            }
            if (mercadoria.getMargemLucro() != null) {
                edtMargemLucro.setText(mercadoria.getMargemLucro().toString());
            }
            if (compraItem.getValorVenda() == 0 && mercadoria.getValVenda() != null) {
                edtValorVenda.setText(mercadoria.getValVenda().toString());
            }
        }

        // Sobrescrever com valores do item se já existirem
        if (compraItem.getQuantidade() > 0) {
            edtQuantidade.setText(String.format("%.2f", compraItem.getQuantidade()));
        }
        if (compraItem.getValorCompra() > 0) {
            edtValorCompra.setText(String.format("%.2f", compraItem.getValorCompra()));
        }
        if (compraItem.getDesconto() > 0) {
            edtDesconto.setText(String.format("%.2f", compraItem.getDesconto()));
        }
        if (compraItem.getValorCusto() > 0) {
            edtValorCusto.setText(String.format("%.2f", compraItem.getValorCusto()));
        }
        if (compraItem.getValorVenda() > 0) {
            edtValorVenda.setText(String.format("%.2f", compraItem.getValorVenda()));
        }
        if (compraItem.getCst() != null && !compraItem.getCst().isEmpty()) {
            edtCst.setText(compraItem.getCst());
        }
        if (compraItem.getValorIcms() > 0) {
            edtIcms.setText(String.format("%.2f", compraItem.getValorIcms()));
        }
        if (compraItem.getValorPis() > 0) {
            edtPis.setText(String.format("%.2f", compraItem.getValorPis()));
        }
        if (compraItem.getValorCofins() > 0) {
            edtCofins.setText(String.format("%.2f", compraItem.getValorCofins()));
        }
        if (compraItem.getValorIpi() > 0) {
            edtIpi.setText(String.format("%.2f", compraItem.getValorIpi()));
        }

        recalcularValores();
    }

    private void salvar() {
        try {
            double qtd = Double.parseDouble(edtQuantidade.getText().replace(",", "."));
            double vCompra = Double.parseDouble(edtValorCompra.getText().replace(",", "."));

            if (qtd <= 0 || vCompra <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e Valor de Compra devem ser maiores que zero.");
                return;
            }

            compraItem.setQuantidade(qtd);
            compraItem.setValorCompra(vCompra);
            compraItem.setDesconto(Double.parseDouble(edtDesconto.getText().replace(",", ".")));
            compraItem.setValorCusto(Double.parseDouble(edtValorCusto.getText().replace(",", ".")));
            compraItem.setValorVenda(Double.parseDouble(edtValorVenda.getText().replace(",", ".")));
            compraItem.setValorTotal(Double.parseDouble(edtTotalCompras.getText().replace(",", ".")));

            compraItem.setCst(edtCst.getText().trim());
            compraItem.setValorIcms(Double.parseDouble(edtIcms.getText().replace(",", ".")));
            compraItem.setValorPis(Double.parseDouble(edtPis.getText().replace(",", ".")));
            compraItem.setValorCofins(Double.parseDouble(edtCofins.getText().replace(",", ".")));
            compraItem.setValorIpi(Double.parseDouble(edtIpi.getText().replace(",", ".")));

            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato numérico inválido: " + ex.getMessage());
        }
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "salvar");
        root.getActionMap().put("salvar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { salvar(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        root.getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }

    public boolean isConfirmado() { return confirmado; }
    public CompraItem getCompraItem() { return compraItem; }
}
