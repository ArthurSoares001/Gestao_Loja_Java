package Estado.view;

import Estado.dao.EstadoDAO;
import Estado.model.Estado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormEstadoControle extends JDialog {

    private JTextField edtDescricao = new JTextField(15);
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Nome", "ID", "Sigla"});
    private JTable tabela;
    private DefaultTableModel tableModel;

    private List<Estado> listaEstados = new ArrayList<>();
    private EstadoDAO dao = new EstadoDAO();

    public FormEstadoControle(Frame parent) {
        super(parent, "Estado - Controle", true);
        initComponents();
        atualizarGrid();
    }

    private void initComponents() {
        setSize(720, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        configurarAtalhos();

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

        String[] colunas = {"Ativo", "ID", "Estado", "Sigla", "País"};
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
            listaEstados = dao.recuperarTodos(edtDescricao.getText(), cmbTipo.getSelectedIndex(), 1);

            for (Estado e : listaEstados) {
                tableModel.addRow(new Object[]{
                        e.isAtivo() ? "S" : "N",
                        e.getId(),
                        e.getNome(),
                        e.getSigla(),
                        e.getPais().getNome()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }

    private void novo() {
        FormEstado form = new FormEstado((Frame) getParent(), new Estado());
        form.setVisible(true);
        if (form.isConfirmado()) {
            try {
                dao.incluir(form.getEstado());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void editar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um estado na lista.");
            return;
        }

        Estado selecionado = listaEstados.get(row);
        FormEstado form = new FormEstado((Frame) getParent(), selecionado.clone());
        form.setVisible(true);
        if (form.isConfirmado()) {
            try {
                dao.alterar(form.getEstado());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um estado para excluir.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Confirma a exclusão deste registro?", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                dao.excluir(listaEstados.get(row).getId());
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void clonar() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        Estado clone = listaEstados.get(row).clone();
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
