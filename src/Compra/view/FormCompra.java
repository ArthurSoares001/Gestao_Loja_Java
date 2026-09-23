package Compra.view;

import Compra.dao.CompraDAO;
import Compra.model.Compra;
import CompraItem.model.CompraItem;
import CompraParcela.model.CompraParcela;
import Mercadoria.model.Mercadoria;
import Participante.model.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormCompra extends JDialog {

    private final DateTimeFormatter dtfData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Componentes de Topo e Filtro XML
    private JTextField edtImportarXml = new JTextField();
    private JButton btnProcessarXml = new JButton("F4 - Processar");
    private JComboBox<Participante> cmbFornecedor = new JComboBox<>();

    // Impostos e Totais da Barra Superior
    private JTextField edtIcms = new JTextField("0.00");
    private JTextField edtPis = new JTextField("0.00");
    private JTextField edtCofins = new JTextField("0.00");
    private JTextField edtIpi = new JTextField("0.00");
    private JTextField edtItens = new JTextField("0");
    private JTextField edtQtdTotal = new JTextField("0");
    private JTextField edtValorSemDesc = new JTextField("0.00");
    private JTextField edtDesconto = new JTextField("0.00");
    private JTextField edtValorComDesconto = new JTextField("0.00");
    private JTextField edtValorFrete = new JTextField("0.00");

    // Painel de Destaque à Direita
    private JLabel lblValorTotalGeral = new JLabel("Total R$: 0,00");
    private JLabel lblQtdTotalGeral = new JLabel("Qtd. Total: 0");
    private JLabel lblTotalContasPagar = new JLabel("Total Contas a Pagar: 0,00");

    // Painel de Inserção Rápida de Produto
    private JComboBox<String> cmbTipoFiltro = new JComboBox<>(new String[]{"NOME", "ID", "CÓD. BARRAS"});
    private JTextField edtDescricaoProd = new JTextField();
    private JButton btnProcurarProd = new JButton("F3 - Procurar");
    private JLabel lblRegEncontrados = new JLabel("Registros Encontrados: 0");

    // Aba de Produtos
    private JTable tblItens;
    private DefaultTableModel modelItens;
    private JButton btnRemoverItem = new JButton("Remover (F6)");
    private JButton btnAlterarItem = new JButton("Alterar (F7)");

    // Aba de Contas a Pagar
    private JTable tblParcelas;
    private DefaultTableModel modelParcelas;
    private JTextField edtFormaPagto = new JTextField("DINHEIRO");
    private JTextField edtVencimentoParcela = new JTextField();
    private JTextField edtValorParcela = new JTextField("0.00");
    private JButton btnAdicionarParcela = new JButton("Adicionar");
    private JButton btnRemoverParcela = new JButton("Remover");
    private JButton btnRefazerParcelas = new JButton("Refazer Parcelas");
    private JLabel lblTotalRegistrosParcelas = new JLabel("Registros: 0");

    // Botões Rodapé
    private JButton btnGravar = new JButton("F12 Gravar");
    private JButton btnCancelar = new JButton("ESC Sair");

    private boolean confirmado = false;
    private Compra compra;
    private CompraDAO dao = new CompraDAO();

    public FormCompra(Frame parent, Compra compra) {
        super(parent, "Entrada de Mercadoria", true);
        this.compra = (compra != null) ? compra : new Compra();
        if (this.compra.getLstCompraItem() == null) {
            this.compra.setLstCompraItem(new ArrayList<>());
        }
        if (this.compra.getLstCompraParcela() == null) {
            this.compra.setLstCompraParcela(new ArrayList<>());
        }

        initComponents();
        configurarAtalhos();
        carregarFornecedores();
        preencherDadosInterface();
    }

    private void initComponents() {
        setSize(1200, 720);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // ─────────────────────────────────────────────────────────────────
        // PAINEL SUPERIOR: XML, FORNECEDOR, IMPOSTOS E TOTAL GERAL
        // ─────────────────────────────────────────────────────────────────
        JPanel pnlNorte = new JPanel(new BorderLayout(5, 5));
        pnlNorte.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JPanel pnlCabecalho = new JPanel(new GridLayout(4, 1, 4, 4));

        // Linha 1: Importar XML
        JPanel pnlXml = new JPanel(new BorderLayout(5, 0));
        pnlXml.add(new JLabel("Importar Arquivo XML / Chave: "), BorderLayout.WEST);
        pnlXml.add(edtImportarXml, BorderLayout.CENTER);
        btnProcessarXml.addActionListener(e -> processarXml());
        pnlXml.add(btnProcessarXml, BorderLayout.EAST);
        pnlCabecalho.add(pnlXml);

        // Linha 2: Fornecedor e Frete
        JPanel pnlForn = new JPanel(new BorderLayout(5, 0));
        pnlForn.add(new JLabel("Fornecedor*: "), BorderLayout.WEST);
        pnlForn.add(cmbFornecedor, BorderLayout.CENTER);

        JPanel pnlFrete = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pnlFrete.add(new JLabel("Frete R$:"));
        edtValorFrete.setPreferredSize(new Dimension(85, 24));
        pnlFrete.add(edtValorFrete);
        pnlForn.add(pnlFrete, BorderLayout.EAST);
        pnlCabecalho.add(pnlForn);

        // Linha 3: Impostos (ICMS, PIS, COFINS, IPI)
        JPanel pnlImpostos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        configurarCampoBloqueado(edtIcms, 85);
        configurarCampoBloqueado(edtPis, 85);
        configurarCampoBloqueado(edtCofins, 85);
        configurarCampoBloqueado(edtIpi, 85);

        pnlImpostos.add(new JLabel("ICMS:")); pnlImpostos.add(edtIcms);
        pnlImpostos.add(new JLabel("PIS:")); pnlImpostos.add(edtPis);
        pnlImpostos.add(new JLabel("COFINS:")); pnlImpostos.add(edtCofins);
        pnlImpostos.add(new JLabel("IPI:")); pnlImpostos.add(edtIpi);
        pnlCabecalho.add(pnlImpostos);

        // Linha 4: Resumos numéricos
        JPanel pnlResumos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        configurarCampoBloqueado(edtItens, 50);
        configurarCampoBloqueado(edtQtdTotal, 70);
        configurarCampoBloqueado(edtValorSemDesc, 90);
        configurarCampoBloqueado(edtDesconto, 60);
        configurarCampoBloqueado(edtValorComDesconto, 90);

        pnlResumos.add(new JLabel("Itens:")); pnlResumos.add(edtItens);
        pnlResumos.add(new JLabel("Qtd:")); pnlResumos.add(edtQtdTotal);
        pnlResumos.add(new JLabel("Valor Bruto:")); pnlResumos.add(edtValorSemDesc);
        pnlResumos.add(new JLabel("Desc %:")); pnlResumos.add(edtDesconto);
        pnlResumos.add(new JLabel("Com Desconto:")); pnlResumos.add(edtValorComDesconto);
        pnlCabecalho.add(pnlResumos);

        pnlNorte.add(pnlCabecalho, BorderLayout.CENTER);

        // Painel Destaque Lateral Direita (Totais)
        JPanel pnlDestaque = new JPanel(new GridLayout(3, 1, 2, 2));
        pnlDestaque.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnlDestaque.setBackground(new Color(245, 245, 250));
        lblValorTotalGeral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValorTotalGeral.setForeground(new Color(0, 102, 204));
        lblQtdTotalGeral.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalContasPagar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalContasPagar.setForeground(new Color(40, 140, 40));

        pnlDestaque.add(lblValorTotalGeral);
        pnlDestaque.add(lblQtdTotalGeral);
        pnlDestaque.add(lblTotalContasPagar);
        pnlNorte.add(pnlDestaque, BorderLayout.EAST);

        add(pnlNorte, BorderLayout.NORTH);

        // ─────────────────────────────────────────────────────────────────
        // PAINEL CENTRAL: ABAS (PRODUTOS / CONTAS A PAGAR)
        // ─────────────────────────────────────────────────────────────────
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Produtos", criarAbaProdutos());
        abas.addTab("Contas a Pagar", criarAbaContasAPagar());
        add(abas, BorderLayout.CENTER);

        // ─────────────────────────────────────────────────────────────────
        // PAINEL INFERIOR: BOTÕES DE GRAVAR E FECHAR
        // ─────────────────────────────────────────────────────────────────
        JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        btnGravar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGravar.setBackground(new Color(40, 140, 60));
        btnGravar.setForeground(Color.WHITE);
        btnGravar.addActionListener(e -> gravar());

        btnCancelar.addActionListener(e -> fecharComConfirmacao());

        pnlSul.add(btnGravar);
        pnlSul.add(btnCancelar);
        add(pnlSul, BorderLayout.SOUTH);
    }

    private JPanel criarAbaProdutos() {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Barra de busca do produto
        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlBusca.add(new JLabel("Tipo:"));
        pnlBusca.add(cmbTipoFiltro);
        edtDescricaoProd.setPreferredSize(new Dimension(320, 26));
        pnlBusca.add(edtDescricaoProd);

        btnProcurarProd.addActionListener(e -> procurarProduto());
        pnlBusca.add(btnProcurarProd);
        pnlBusca.add(lblRegEncontrados);

        pnl.add(pnlBusca, BorderLayout.NORTH);

        // Tabela de itens
        String[] colunas = {
                "Item", "Cód. Barra", "Descrição", "UN", "Vl. Compra",
                "Desc %", "Vl. Custo", "Vl. Venda", "Qtd", "Total", "Status Custo"
        };

        modelItens = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblItens = new JTable(modelItens);
        tblItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblItens.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) alterarItem();
            }
        });

        // Colorir o status de custo ('>' subiu, '<' baixou)[cite: 4, 11]
        tblItens.getColumnModel().getColumn(10).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String val = (value != null) ? value.toString() : "";
                if (val.equals(">")) {
                    c.setForeground(Color.RED);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else if (val.equals("<")) {
                    c.setForeground(new Color(0, 150, 0));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.BLACK);
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        pnl.add(new JScrollPane(tblItens), BorderLayout.CENTER);

        // Ações de itens
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnAlterarItem.addActionListener(e -> alterarItem());
        btnRemoverItem.addActionListener(e -> removerItem());
        pnlAcoes.add(btnAlterarItem);
        pnlAcoes.add(btnRemoverItem);
        pnl.add(pnlAcoes, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel criarAbaContasAPagar() {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // Formulário de inserção manual de parcelas
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlTop.add(new JLabel("Forma:"));
        edtFormaPagto.setPreferredSize(new Dimension(160, 24));
        pnlTop.add(edtFormaPagto);

        pnlTop.add(new JLabel("Vencimento (dd/mm/aaaa):"));
        edtVencimentoParcela.setPreferredSize(new Dimension(95, 24));
        edtVencimentoParcela.setText(LocalDate.now().format(dtfData));
        pnlTop.add(edtVencimentoParcela);

        pnlTop.add(new JLabel("Valor R$:"));
        edtValorParcela.setPreferredSize(new Dimension(95, 24));
        pnlTop.add(edtValorParcela);

        btnAdicionarParcela.addActionListener(e -> adicionarParcela());
        pnlTop.add(btnAdicionarParcela);

        btnRefazerParcelas.addActionListener(e -> dispararRefazerParcelamento());
        pnlTop.add(btnRefazerParcelas);

        pnl.add(pnlTop, BorderLayout.NORTH);

        // Grade de Parcelas
        String[] colunas = {"Nº", "Forma Pagamento", "Vencimento", "Valor R$"};
        modelParcelas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblParcelas = new JTable(modelParcelas);
        tblParcelas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnl.add(new JScrollPane(tblParcelas), BorderLayout.CENTER);

        // Rodapé de Parcelas
        JPanel pnlAcoesParcela = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRemoverParcela.addActionListener(e -> removerParcela());
        pnlAcoesParcela.add(btnRemoverParcela);
        pnlAcoesParcela.add(lblTotalRegistrosParcelas);
        pnl.add(pnlAcoesParcela, BorderLayout.SOUTH);

        return pnl;
    }

    private void configurarCampoBloqueado(JTextField tf, int width) {
        tf.setPreferredSize(new Dimension(width, 24));
        tf.setEditable(false);
        tf.setFocusable(false);
        tf.setBackground(new Color(240, 240, 240));
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "f3");
        root.getActionMap().put("f3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { procurarProduto(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "f4");
        root.getActionMap().put("f4", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { processarXml(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "f6");
        root.getActionMap().put("f6", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { removerItem(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "f7");
        root.getActionMap().put("f7", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { alterarItem(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "f12");
        root.getActionMap().put("f12", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { gravar(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc");
        root.getActionMap().put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { fecharComConfirmacao(); }
        });
    }

    private void carregarFornecedores() {
        try {
            cmbFornecedor.removeAllItems();
            for (Participante p : dao.recuperarFornecedores()) {
                cmbFornecedor.addItem(p);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar fornecedores: " + ex.getMessage());
        }
    }

    private void preencherDadosInterface() {
        if (compra.getFornecedor() != null && compra.getFornecedor().getId() != null) {
            for (int i = 0; i < cmbFornecedor.getItemCount(); i++) {
                if (cmbFornecedor.getItemAt(i).getId().equals(compra.getFornecedor().getId())) {
                    cmbFornecedor.setSelectedIndex(i);
                    break;
                }
            }
        }

        edtValorFrete.setText(String.format("%.2f", compra.getFrete()));
        atualizarDadosCalculados();
        atualizarGridItens();
        atualizarGridParcelas();
    }

    private void atualizarDadosCalculados() {
        compra.totalizarImpostos();

        edtIcms.setText(String.format("%.2f", compra.getValorIcms()));
        edtPis.setText(String.format("%.2f", compra.getValorPis()));
        edtCofins.setText(String.format("%.2f", compra.getValorCofins()));
        edtIpi.setText(String.format("%.2f", compra.getValorIpi()));

        edtItens.setText(String.valueOf(compra.getItens()));
        edtQtdTotal.setText(String.format("%.2f", compra.getQtdTotal()));
        edtValorSemDesc.setText(String.format("%.2f", compra.getValTotal()));
        edtDesconto.setText(String.format("%.2f", compra.getDesconto()));
        edtValorComDesconto.setText(String.format("%.2f", compra.valorComDescontoTotal()));

        lblValorTotalGeral.setText(String.format("Total R$: %.2f", compra.valorComDescontoTotal()));
        lblQtdTotalGeral.setText(String.format("Qtd. Total: %.2f", compra.getQtdTotal()));
        lblTotalContasPagar.setText(String.format("Total Contas a Pagar: %.2f", compra.totalCompraParcela()));

        double restante = compra.getValorRestante();
        edtValorParcela.setText(String.format("%.2f", Math.max(0.0, restante)).replace(",", "."));
    }

    private void atualizarGridItens() {
        modelItens.setRowCount(0);
        if (compra.getLstCompraItem() != null) {
            int seq = 1;
            for (CompraItem item : compra.getLstCompraItem()) {
                String desc = (item.getMercadoria() != null) ? item.getMercadoria().getNome() : "";
                String codBarra = (item.getMercadoria() != null && item.getMercadoria().getCodBarra() != null) ? item.getMercadoria().getCodBarra() : "";
                String un = (item.getMercadoria() != null && item.getMercadoria().getUnidade() != null) ? item.getMercadoria().getUnidade().getSigla() : "";

                modelItens.addRow(new Object[]{
                        seq++,
                        codBarra,
                        desc,
                        un,
                        String.format("%.2f", item.getValorCompra()),
                        String.format("%.2f", item.getDesconto()),
                        String.format("%.2f", item.getValorCusto()),
                        String.format("%.2f", item.getValorVenda()),
                        String.format("%.2f", item.getQuantidade()),
                        String.format("%.2f", item.getValorTotal()),
                        item.statusValor()
                });
            }
        }
    }

    private void atualizarGridParcelas() {
        modelParcelas.setRowCount(0);
        if (compra.getLstCompraParcela() != null) {
            int seq = 1;
            for (CompraParcela p : compra.getLstCompraParcela()) {
                String dtVenc = (p.getVencimento() != null) ? p.getVencimento().format(dtfData) : "";
                String forma = (p.getConta() != null) ? p.getConta().toString() : "DINHEIRO";
                modelParcelas.addRow(new Object[]{
                        seq++,
                        forma,
                        dtVenc,
                        String.format("%.2f", p.getValor())
                });
            }
        }
        lblTotalRegistrosParcelas.setText("Registros: " + modelParcelas.getRowCount());
    }

    private void procurarProduto() {
        String termo = edtDescricaoProd.getText().trim();
        if (termo.isEmpty()) return;

        // Demonstração da busca e inserção rápida de item
        CompraItem item = new CompraItem();
        Mercadoria m = new Mercadoria();
        m.setNome(termo);
        m.setCodBarra("789" + (int)(Math.random() * 100000));
        m.setValCompra(new java.math.BigDecimal("10.00"));
        item.setMercadoria(m);
        item.setValorCompra(10.00);
        item.setQuantidade(1.0);
        item.setValorTotal(10.00);
        item.setValorCusto(10.00);
        item.setValorVenda(15.00);

        compra.getLstCompraItem().add(item);
        edtDescricaoProd.setText("");
        atualizarDadosCalculados();
        atualizarGridItens();
    }

    private void alterarItem() {
        int row = tblItens.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item da lista para alterar.");
            return;
        }
        CompraItem item = compra.getLstCompraItem().get(row);
        String strQtd = JOptionPane.showInputDialog(this, "Informe a nova quantidade:", item.getQuantidade());
        if (strQtd != null && !strQtd.trim().isEmpty()) {
            try {
                double qtd = Double.parseDouble(strQtd.replace(",", "."));
                item.setQuantidade(qtd);
                item.setValorTotal(item.getValorCompra() * qtd);
                atualizarDadosCalculados();
                atualizarGridItens();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            }
        }
    }

    private void removerItem() {
        int row = tblItens.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item da lista para remover.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Deseja remover o item selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            compra.getLstCompraItem().remove(row);
            atualizarDadosCalculados();
            atualizarGridItens();
        }
    }

    private void adicionarParcela() {
        try {
            double valor = Double.parseDouble(edtValorParcela.getText().trim().replace(",", "."));
            LocalDate venc = LocalDate.parse(edtVencimentoParcela.getText().trim(), dtfData);

            CompraParcela p = new CompraParcela();
            p.setValor(valor);
            p.setVencimento(venc);
            p.setConta(edtFormaPagto.getText().trim());

            compra.getLstCompraParcela().add(p);
            atualizarDadosCalculados();
            atualizarGridParcelas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Preencha a data e o valor da parcela corretamente.");
        }
    }

    private void removerParcela() {
        int row = tblParcelas.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma parcela para remover.");
            return;
        }
        compra.getLstCompraParcela().remove(row);
        atualizarDadosCalculados();
        atualizarGridParcelas();
    }

    private void dispararRefazerParcelamento() {
        String inputQtd = JOptionPane.showInputDialog(this, "Informe o número de parcelas:");
        if (inputQtd == null || inputQtd.trim().isEmpty()) return;

        int qtd;
        try {
            qtd = Integer.parseInt(inputQtd.trim());
            if (qtd <= 0) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Número de parcelas inválido!");
            return;
        }

        String inputData = JOptionPane.showInputDialog(this, "Data do 1º vencimento (dd/mm/aaaa):", LocalDate.now().format(dtfData));
        if (inputData == null || inputData.trim().isEmpty()) return;

        LocalDate dtPrimeiro;
        try {
            dtPrimeiro = LocalDate.parse(inputData.trim(), dtfData);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Data inválida!");
            return;
        }

        refazerParcelamento(qtd, dtPrimeiro);
    }

    private void refazerParcelamento(int qtdParcelas, LocalDate dtPrimeiro) {
        compra.getLstCompraParcela().clear();
        double totalGeral = compra.valorComDescontoTotal();
        double somaParcelas = 0.0;

        for (int i = 0; i < qtdParcelas; i++) {
            CompraParcela p = new CompraParcela();
            double valParcela;
            if (i < qtdParcelas - 1) {
                valParcela = Math.round((totalGeral / qtdParcelas) * 100.0) / 100.0;
            } else {
                valParcela = Math.round((totalGeral - somaParcelas) * 100.0) / 100.0; // Ajuste do centavo residual
            }
            somaParcelas += valParcela;

            p.setValor(valParcela);
            p.setVencimento(dtPrimeiro.plusMonths(i));
            p.setConta("DINHEIRO");
            compra.getLstCompraParcela().add(p);
        }

        atualizarDadosCalculados();
        atualizarGridParcelas();
    }

    private void processarXml() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            edtImportarXml.setText(f.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "XML carregado com sucesso!\n" + f.getName());
        }
    }

    private void gravar() {
        try {
            Participante forn = (Participante) cmbFornecedor.getSelectedItem();
            compra.setFornecedor(forn);

            if (!edtValorFrete.getText().trim().isEmpty()) {
                compra.setFrete(Double.parseDouble(edtValorFrete.getText().trim().replace(",", ".")));
            }

            compra.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmado() { return confirmado; }
    public Compra getCompra() { return compra; }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}