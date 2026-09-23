package Cidade.view;

import Cidade.dao.CidadeDAO;
import Cidade.model.Cidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormCidadeControle extends JDialog {

    private JTextField edtDescricao = new JTextField(15);
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Nome", "ID", "IBGE"});
    private JTable tabela;
    private DefaultTableModel tableModel;

    private List<Cidade> listaCidades = new ArrayList<>();
    private CidadeDAO dao = new CidadeDAO();

    public FormCidadeControle(Frame parent) {
        super(parent, "Cidade - Controle", true);
        initComponents();
        atualizarGrid();
    }

    private void initComponents() {
        setSize(750, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        configurarAtalhos();

        // Cabeçalho de Filtros
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFiltros.add(new JLabel("Filtrar por:"));
        pnlFiltros.add(cmbTipo);
        pnlFiltros.add(edtDescricao);

        JButton btnProcurar = new JButton("Procurar (F3)");
        btnProcurar.addActionListener(e -> atualizarGrid());
        pnlFiltros.add(btnProcurar);

        JButton btnNovo = new JButton("Novo (F2)");
        btnNovo.addActionListener(e -> novo());
        pnlFiltros.add(btnNovo);

        add(pnlFiltros, BorderLayout.NORTH);

        // Grid de Dados (stgDados)
        String[] colunas = {"Ativo", "ID", "Estado", "Nome", "CEP", "IBGE"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editar();
                }
            }
        });
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Rodapé de Ações
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnClonar = new JButton("Clonar");

        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnClonar.addActionListener(e -> clonar());

        pnlAcoes.add(btnEditar);
        pnlAcoes.add(btnExcluir);
        pnlAcoes.add(btnClonar);

        add(pnlAcoes, BorderLayout.SOUTH);
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0), "novo");
        root.getActionMap().put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) { novo(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0), "procurar");
        root.getActionMap().put("procurar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) { atualizarGrid(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        root.getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }

    private void atualizarGrid() {
        try {
            tableModel.setRowCount(0);
            listaCidades = dao.recuperarTodos(edtDescricao.getText(), cmbTipo.getSelectedIndex(), 1);

            for (Cidade c : listaCidades) {
                tableModel.addRow(new Object[]{
                        c.isAtivo() ? "S" : "N",
                        c.getId(),
                        c.getEstado().getSigla(),
                        c.getNome(),
                        c.getCep(),
                        c.getCodigoIBGE()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }

    private void novo() {
        FormCidade form = new FormCidade((Frame) getParent(), new Cidade());
        form.setVisible(true);
        if (form.isConfirmado()) {
            try {
                dao.incluir(form.getCidade());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void editar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma cidade na lista.");
            return;
        }

        Cidade selecionada = listaCidades.get(row);
        FormCidade form = new FormCidade((Frame) getParent(), selecionada.clone());
        form.setVisible(true);
        if (form.isConfirmado()) {
            try {
                dao.alterar(form.getCidade());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma cidade para excluir.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Confirma a exclusão deste registro?", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.excluir(listaCidades.get(row).getId());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void clonar() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        Cidade clone = listaCidades.get(row).clone();
        clone.setId(null);
        clone.setNome(clone.getNome() + " (Cópia)");

        try {
            dao.incluir(clone);
            atualizarGrid();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
