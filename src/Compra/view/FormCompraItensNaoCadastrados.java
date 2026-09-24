package Compra.view;

import Compra.model.Compra;
import Mercadoria.dao.MercadoriaDAO;
import Mercadoria.model.Mercadoria;
import Mercadoria.view.FormMercadoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormCompraItensNaoCadastrados extends JDialog {

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar = new JButton("Cadastrar Item Selecionado");
    private JButton btnFechar = new JButton("Concluir / Fechar");
    private JLabel lblRegistros = new JLabel("Registros: 0");

    private Compra compra;
    private MercadoriaDAO mercadoriaDAO = new MercadoriaDAO();

    public FormCompraItensNaoCadastrados(Dialog parent, Compra compra) {
        super(parent, "Itens do XML Não Cadastrados", true);
        this.compra = compra;
        initComponents();
        atualizarGrid();
    }

    private void initComponents() {
        setSize(780, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTopo.add(new JLabel("Dê um duplo clique no item ou selecione-o e clique em cadastrar:"));
        add(pnlTopo, BorderLayout.NORTH);

        String[] colunas = {"Nome (XML)", "Cód. Barras", "Cód. Próprio (cProd)", "Qtd. XML", "Valor Unit."};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) cadastrarItemSelecionado();
            }
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnlSul.add(lblRegistros);
        btnCadastrar.addActionListener(e -> cadastrarItemSelecionado());
        btnFechar.addActionListener(e -> dispose());
        pnlSul.add(btnCadastrar);
        pnlSul.add(btnFechar);
        add(pnlSul, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    private void atualizarGrid() {
        tableModel.setRowCount(0);
        List<Object> pendentes = compra.getLstMercadoria();
        if (pendentes != null) {
            for (Object obj : pendentes) {
                if (obj instanceof Mercadoria) {
                    Mercadoria m = (Mercadoria) obj;
                    tableModel.addRow(new Object[]{
                            m.getNome(),
                            m.getCodBarra() != null ? m.getCodBarra() : "",
                            m.getCprod() != null ? m.getCprod() : "",
                            m.getQuantidade(),
                            String.format("%.2f", m.getValCompra())
                    });
                }
            }
            lblRegistros.setText("Pendentes: " + pendentes.size());
        }
    }

    private void cadastrarItemSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item pendente da lista.");
            return;
        }

        Mercadoria pendente = (Mercadoria) compra.getLstMercadoria().get(row);
        FormMercadoria form = new FormMercadoria((Frame) getOwner(), pendente.clone());
        form.setVisible(true);

        if (form.isConfirmado()) {
            try {
                mercadoriaDAO.incluir(form.getMercadoria());
                compra.getLstMercadoria().remove(row);
                atualizarGrid();
                JOptionPane.showMessageDialog(this, "Mercadoria cadastrada com sucesso!");

                if (compra.getLstMercadoria().isEmpty()) {
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar mercadoria: " + ex.getMessage());
            }
        }
    }
}
