package Compra.view;

import Mercadoria.dao.MercadoriaDAO;
import Mercadoria.model.Mercadoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormCompraEscolherMaisItens extends JDialog {

    private JTextField edtDescricao = new JTextField(20);
    private JComboBox<String> cmbTipoFiltro = new JComboBox<>(new String[]{"NOME", "ID", "CÓD. BARRAS"});
    private JButton btnProcurar = new JButton("Procurar (F3)");
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JLabel lblRegEncontrados = new JLabel("Registros encontrados: 0");

    private JButton btnConfirmar = new JButton("Confirmar (F12)");
    private JButton btnCancelar = new JButton("Cancelar (ESC)");

    private Mercadoria mercadoriaSelecionada;
    private List<Mercadoria> lista = new ArrayList<>();
    private MercadoriaDAO mercadoriaDAO = new MercadoriaDAO();

    public FormCompraEscolherMaisItens(Dialog parent, List<Mercadoria> itensIniciais) {
        super(parent, "Escolher Mercadoria", true);
        if (itensIniciais != null) {
            this.lista = itensIniciais;
        }
        initComponents();
        configurarAtalhos();
        atualizarGrid();
    }

    private void initComponents() {
        setSize(780, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel pnlTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        pnlTopo.add(new JLabel("Filtrar por:"));
        pnlTopo.add(cmbTipoFiltro);
        pnlTopo.add(edtDescricao);
        btnProcurar.addActionListener(e -> buscar());
        pnlTopo.add(btnProcurar);
        pnlTopo.add(lblRegEncontrados);
        add(pnlTopo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Cód. Barra", "Nome", "UN", "Preço Venda R$", "Estoque"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) confirmar();
            }
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirmar.addActionListener(e -> confirmar());
        btnCancelar.addActionListener(e -> dispose());
        pnlSul.add(btnConfirmar);
        pnlSul.add(btnCancelar);
        add(pnlSul, BorderLayout.SOUTH);
    }

    private void buscar() {
        try {
            lista = mercadoriaDAO.recuperarTodos(edtDescricao.getText().trim(), cmbTipoFiltro.getSelectedIndex(), 1);
            atualizarGrid();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro na consulta: " + ex.getMessage());
        }
    }

    private void atualizarGrid() {
        tableModel.setRowCount(0);
        for (Mercadoria m : lista) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getCodBarra() != null ? m.getCodBarra() : "",
                    m.getNome(),
                    m.getUnidade() != null ? m.getUnidade().getSigla() : "",
                    String.format("%.2f", m.getValVenda()),
                    m.getQuantidade()
            });
        }
        lblRegEncontrados.setText("Registros encontrados: " + lista.size());
    }

    private void confirmar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma mercadoria.");
            return;
        }
        mercadoriaSelecionada = lista.get(row);
        dispose();
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "f3");
        root.getActionMap().put("f3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { buscar(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "f12");
        root.getActionMap().put("f12", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { confirmar(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc");
        root.getActionMap().put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }

    public Mercadoria getMercadoriaSelecionada() {
        return mercadoriaSelecionada;
    }
}
