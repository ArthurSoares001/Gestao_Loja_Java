package Compra.view;

import Compra.model.Compra;
import CompraAcerto.model.CompraAcerto;
import CompraAcerto.model.CompraAcertoItem;
import CompraItem.model.CompraItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class FormAcertoCompra extends JDialog {

    private JTextField edtCompraId = new JTextField();
    private JTextField edtFornecedor = new JTextField();
    private JTextField edtDataFaturado = new JTextField();

    private JTable tabela;
    private DefaultTableModel tableModel;

    private JLabel lblQtdComprada = new JLabel("Qtd. Compra: 0");
    private JLabel lblQtdJaDevolvida = new JLabel("Qtd. Já Devolvida: 0");
    private JLabel lblValorAcerto = new JLabel("Valor NESTE Acerto: R$ 0,00");

    private JButton btnDevolverTudo = new JButton("Devolver Todas as Quantidades");
    private JButton btnConfirmar = new JButton("Confirmar Devolução (F12)");
    private JButton btnCancelar = new JButton("Cancelar (ESC)");

    private boolean confirmado = false;
    private Compra compra;
    private CompraAcerto compraAcerto;

    public FormAcertoCompra(Window parent, Compra compra) {
        super((Frame) parent, "Devolução / Acerto de Compra", true);
        this.compra = compra;
        this.compraAcerto = new CompraAcerto();
        this.compraAcerto.setCompra(compra);
        this.compraAcerto.setNumeroAcerto(1);
        this.compraAcerto.setAcertado(LocalDateTime.now());
        this.compraAcerto.setAcertadoPor("ADMIN");

        initComponents();
        carregarCabecalho();
        atualizarGrid();
    }

    private void initComponents() {
        setSize(920, 520);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(5, 5));

        // Cabeçalho
        JPanel pnlCab = new JPanel(new GridLayout(1, 6, 6, 6));
        pnlCab.setBorder(BorderFactory.createTitledBorder("Informações da Compra"));
        edtCompraId.setEditable(false);
        edtFornecedor.setEditable(false);
        edtDataFaturado.setEditable(false);

        pnlCab.add(new JLabel("Pedido Compra:")); pnlCab.add(edtCompraId);
        pnlCab.add(new JLabel("Fornecedor:")); pnlCab.add(edtFornecedor);
        pnlCab.add(new JLabel("Faturado em:")); pnlCab.add(edtDataFaturado);
        add(pnlCab, BorderLayout.NORTH);

        // Grade de Itens
        String[] colunas = {"Item", "Descrição", "Qtd. Comprada", "Já Devolvida", "Disponível", "Neste Acerto", "Vl. Unitário", "Total"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editarDevolucaoItem();
            }
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Rodapé de Ações e Totais
        JPanel pnlSul = new JPanel(new BorderLayout());
        JPanel pnlTotais = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        pnlTotais.add(lblQtdComprada);
        pnlTotais.add(lblQtdJaDevolvida);
        lblValorAcerto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblValorAcerto.setForeground(new Color(180, 0, 0));
        pnlTotais.add(lblValorAcerto);
        pnlSul.add(pnlTotais, BorderLayout.NORTH);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        btnDevolverTudo.addActionListener(e -> devolverTudo());
        btnConfirmar.addActionListener(e -> confirmar());
        btnCancelar.addActionListener(e -> dispose());
        pnlBotoes.add(btnDevolverTudo);
        pnlBotoes.add(btnConfirmar);
        pnlBotoes.add(btnCancelar);
        pnlSul.add(pnlBotoes, BorderLayout.SOUTH);

        add(pnlSul, BorderLayout.SOUTH);
    }

    private void carregarCabecalho() {
        if (compra != null) {
            edtCompraId.setText(compra.getId() != null ? compra.getId().toString() : "");
            edtFornecedor.setText(compra.getFornecedor() != null ? compra.getFornecedor().getNome() : "");
            edtDataFaturado.setText(compra.getFaturado() != null ? compra.getFaturado().toString() : "");
        }
    }

    private void atualizarGrid() {
        tableModel.setRowCount(0);
        if (compra.getLstCompraItem() != null) {
            for (CompraItem item : compra.getLstCompraItem()) {
                double qtdComprada = item.getQuantidade();
                double qtdDevolvida = item.getQtdDevolvida() != null ? item.getQtdDevolvida() : 0.0;
                double disponivel = Math.max(0.0, qtdComprada - qtdDevolvida);
                double nesteAcerto = obterQtdNesteAcerto(item);

                tableModel.addRow(new Object[]{
                        item.getId(),
                        item.getMercadoria() != null ? item.getMercadoria().getNome() : "",
                        String.format("%.2f", qtdComprada),
                        String.format("%.2f", qtdDevolvida),
                        String.format("%.2f", disponivel),
                        String.format("%.2f", nesteAcerto),
                        String.format("%.2f", item.getValorCompra()),
                        String.format("%.2f", nesteAcerto * item.getValorCompra())
                });
            }
        }
        calcularTotais();
    }

    private double obterQtdNesteAcerto(CompraItem ci) {
        for (CompraAcertoItem cai : compraAcerto.getLstAcertoItem()) {
            if (cai.getCompraItem() != null && cai.getCompraItem().getId().equals(ci.getId())) {
                return cai.getQuantidade();
            }
        }
        return 0.0;
    }

    private void editarDevolucaoItem() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        CompraItem item = compra.getLstCompraItem().get(row);
        double disponivel = Math.max(0.0, item.getQuantidade() - (item.getQtdDevolvida() != null ? item.getQtdDevolvida() : 0.0));

        if (disponivel <= 0) {
            JOptionPane.showMessageDialog(this, "Este item já foi totalmente devolvido.");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Quantidade a devolver (máx: " + disponivel + "):", disponivel);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double qtd = Double.parseDouble(input.replace(",", "."));
                if (qtd < 0 || qtd > disponivel) {
                    JOptionPane.showMessageDialog(this, "Quantidade fora do limite permitido.");
                    return;
                }

                CompraAcertoItem acItem = null;
                for (CompraAcertoItem cai : compraAcerto.getLstAcertoItem()) {
                    if (cai.getCompraItem().getId().equals(item.getId())) {
                        acItem = cai;
                        break;
                    }
                }

                if (qtd > 0) {
                    if (acItem == null) {
                        acItem = new CompraAcertoItem();
                        acItem.setCompraItem(item);
                        acItem.setValorUnitario(item.getValorCompra());
                        compraAcerto.getLstAcertoItem().add(acItem);
                    }
                    acItem.setQuantidade(qtd);
                } else if (acItem != null) {
                    compraAcerto.getLstAcertoItem().remove(acItem);
                }

                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Valor numérico inválido.");
            }
        }
    }

    private void devolverTudo() {
        compraAcerto.getLstAcertoItem().clear();
        for (CompraItem item : compra.getLstCompraItem()) {
            double disponivel = Math.max(0.0, item.getQuantidade() - (item.getQtdDevolvida() != null ? item.getQtdDevolvida() : 0.0));
            if (disponivel > 0) {
                CompraAcertoItem cai = new CompraAcertoItem();
                cai.setCompraItem(item);
                cai.setQuantidade(disponivel);
                cai.setValorUnitario(item.getValorCompra());
                compraAcerto.getLstAcertoItem().add(cai);
            }
        }
        atualizarGrid();
    }

    private void calcularTotais() {
        double totalAcerto = compraAcerto.calcularValorTotal();
        lblValorAcerto.setText(String.format("Valor NESTE Acerto: R$ %.2f", totalAcerto));
        lblQtdComprada.setText(String.format("Qtd. Compra: %.2f", compra.getQtdTotal()));
        lblQtdJaDevolvida.setText(String.format("Qtd. Já Devolvida: %.2f", compra.totalQtdDevolvida()));
    }

    private void confirmar() {
        if (!compraAcerto.temItens()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um item para devolução.");
            return;
        }

        String motivo = JOptionPane.showInputDialog(this, "Informe o motivo do acerto/devolução:");
        if (motivo == null || motivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O motivo é obrigatório.");
            return;
        }

        try {
            compraAcerto.setAcertadoMotivo(motivo.trim());
            compraAcerto.validar();
            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de validação: " + ex.getMessage());
        }
    }

    public boolean isConfirmado() { return confirmado; }
    public CompraAcerto getCompraAcerto() { return compraAcerto; }
}
