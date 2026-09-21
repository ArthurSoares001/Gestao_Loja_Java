package bandeiratef.view;

import bandeiratef.dao.BandeiraTefDAO;
import bandeiratef.model.BandeiraTef;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormBandeiraTefControle extends JFrame {

    private BandeiraTefDAO dao;
    private List<BandeiraTef> listaRegistros;

    private DefaultTableModel modelGrid;
    private JTable stgDados;

    private JTextField edtDescricao;
    private JComboBox<String> cmbTipo;
    private JComboBox<String> rgOrdenar;
    private JLabel lblRegistro;

    public FormBandeiraTefControle(Frame parent) {
        super("BandeiraTef - Controle");
        this.dao = new BandeiraTefDAO();
        this.listaRegistros = new ArrayList<>();

        setSize(780, 460);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        configurarAtalhos();
        atualizarGrid();
    }

    public FormBandeiraTefControle() {
        this(null);
    }

    private void initUI() {
        // ------------------ Topo: Barra de Busca (fraCabecalhoPequenoControle) ------------------
        JPanel pnlTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        pnlTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        edtDescricao = new JTextField(18);
        cmbTipo = new JComboBox<>(new String[]{"Nome", "Id", "CNPJ"});
        rgOrdenar = new JComboBox<>(new String[]{"Id Desc", "Id Asc", "Nome"});

        JButton btnProcurar = new JButton("Procurar (F3)");
        JButton btnNovo = new JButton("Novo (F2)");

        pnlTopo.add(new JLabel("Descrição:"));
        pnlTopo.add(edtDescricao);
        pnlTopo.add(cmbTipo);
        pnlTopo.add(new JLabel("Ordem:"));
        pnlTopo.add(rgOrdenar);
        pnlTopo.add(btnProcurar);
        pnlTopo.add(btnNovo);

        add(pnlTopo, BorderLayout.NORTH);

        // ------------------ Centro: Tabela (stgDados) ------------------
        modelGrid = new DefaultTableModel(new Object[]{"Ativo", "Id", "Nome", "CNPJ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        stgDados = new JTable(modelGrid);
        stgDados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stgDados.setRowHeight(22);

        // Renderizador idêntico ao Delphi: linha zebra e destaque para inativo (cells[0] = 'N')
        stgDados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String ativo = (String) table.getValueAt(row, 0);

                if (!isSelected) {
                    if ("N".equals(ativo)) {
                        c.setBackground(new Color(255, 205, 205)); // Vermelho suave / inativo
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(245, 248, 255) : Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }

                // Alinhamentos: Ativo e ID centralizados; Nome e CNPJ à esquerda
                if (col == 0 || col == 1) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });

        // Duplo clique para editar
        stgDados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chamarFormBandeiraTef(false);
                }
            }
        });

        add(new JScrollPane(stgDados), BorderLayout.CENTER);

        // ------------------ Rodapé: Ações (fraRodaPeControle) ------------------
        JPanel pnlRodape = new JPanel(new BorderLayout());
        pnlRodape.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnClonar = new JButton("Clonar");

        pnlAcoes.add(btnEditar);
        pnlAcoes.add(btnExcluir);
        pnlAcoes.add(btnClonar);

        lblRegistro = new JLabel("Registros: 0");

        pnlRodape.add(pnlAcoes, BorderLayout.WEST);
        pnlRodape.add(lblRegistro, BorderLayout.EAST);

        add(pnlRodape, BorderLayout.SOUTH);

        // Eventos
        btnProcurar.addActionListener(e -> atualizarGrid());
        btnNovo.addActionListener(e -> chamarFormBandeiraTef(true));
        btnEditar.addActionListener(e -> chamarFormBandeiraTef(false));
        btnExcluir.addActionListener(e -> excluir());
        btnClonar.addActionListener(e -> clonar());
    }

    private void configurarAtalhos() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "novo");
        root.getActionMap().put("novo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { chamarFormBandeiraTef(true); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "procurar");
        root.getActionMap().put("procurar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { atualizarGrid(); }
        });

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "fechar");
        root.getActionMap().put("fechar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }

    private void atualizarGrid() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            String filtro = edtDescricao.getText();
            int tipo = cmbTipo.getSelectedIndex();
            int ordem = rgOrdenar.getSelectedIndex();

            listaRegistros = dao.recuperarTodos(filtro, tipo, ordem);
            modelGrid.setRowCount(0);

            for (BandeiraTef b : listaRegistros) {
                modelGrid.addRow(new Object[]{
                        b.isAtivo() ? "S" : "N",
                        b.getId(),
                        b.getNome(),
                        formatarCNPJ(b.getCnpj())
                });
            }

            lblRegistro.setText("Registros: " + listaRegistros.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao recuperar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void chamarFormBandeiraTef(boolean novo) {
        BandeiraTef registro = null;
        if (!novo) {
            int row = stgDados.getSelectedRow();
            if (row < 0) return;

            try {
                // Busca o objeto completo com as taxas no banco
                int id = listaRegistros.get(row).getId();
                registro = dao.encontrar(id);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados completos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        FormBandeiraTef form = new FormBandeiraTef(this, registro, dao);
        form.setVisible(true);

        if (form.isConfirmado()) {
            atualizarGrid();
        }
    }

    private void excluir() {
        int row = stgDados.getSelectedRow();
        if (row < 0) return;

        if (JOptionPane.showConfirmDialog(this, "Confirma a exclusão deste registro?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                int id = listaRegistros.get(row).getId();
                dao.excluir(id);
                atualizarGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clonar() {
        int row = stgDados.getSelectedRow();
        if (row < 0) return;

        if (JOptionPane.showConfirmDialog(this, "Deseja clonar este registro?", "Clonar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                int id = listaRegistros.get(row).getId();
                BandeiraTef original = dao.encontrar(id);
                if (original != null) {
                    BandeiraTef clone = original.clone();
                    clone.setId(null);
                    dao.incluir(clone);
                    atualizarGrid();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao clonar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String formatarCNPJ(String cnpj) {
        if (cnpj == null) return "";
        String digitos = cnpj.replaceAll("[^0-9]", "");
        if (digitos.length() == 14) {
            return digitos.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return cnpj;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormBandeiraTefControle().setVisible(true));
    }
}
