package bandeiratef.view;

import bandeiratef.dao.BandeiraTefDAO;
import bandeiratef.model.BandeiraTef;
import bandeiratef.model.BandeiraTefTaxa;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class FormBandeiraTef extends JDialog {

    private BandeiraTef objeto;
    private BandeiraTefDAO dao;
    private boolean confirmado = false;

    // Componentes - Aba Dados
    private JTextField edtNome;
    private JFormattedTextField edtCnpj;
    private JCheckBox chkAtivo;

    // Componentes - Aba Taxas
    private JComboBox<String> cmbModalidade;
    private JComboBox<String> cmbAdquirente;
    private JTextField edtParcelaMin;
    private JTextField edtParcelaMax;
    private JTextField edtTaxa;
    private JTextField edtPrazo;
    private DefaultTableModel modelTaxas;
    private JTable tblTaxas;
    private JButton btnAddTaxa;
    private JButton btnRemTaxa;

    public FormBandeiraTef(Frame parent, BandeiraTef objeto, BandeiraTefDAO dao) {
        super(parent, "Bandeira TEF - Cadastro", true);
        this.dao = dao;
        this.objeto = (objeto != null) ? objeto.clone() : new BandeiraTef();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initUI();
        configurarAtalhos();
        carregarDados();
    }

    private void initUI() {
        JTabbedPane pageControl = new JTabbedPane();

        // ------------------ Aba 1: Dados ------------------
        JPanel tabDados = new JPanel(null);

        JLabel lblNome = new JLabel("Nome*");
        lblNome.setBounds(20, 20, 100, 20);
        tabDados.add(lblNome);

        edtNome = new JTextField();
        edtNome.setBounds(20, 42, 420, 28);
        tabDados.add(edtNome);

        JLabel lblCnpj = new JLabel("CNPJ*");
        lblCnpj.setBounds(20, 80, 100, 20);
        tabDados.add(lblCnpj);

        try {
            MaskFormatter maskCnpj = new MaskFormatter("##.###.###/####-##");
            maskCnpj.setPlaceholderCharacter('_');
            edtCnpj = new JFormattedTextField(maskCnpj);
        } catch (Exception e) {
            edtCnpj = new JFormattedTextField();
        }
        edtCnpj.setBounds(20, 102, 180, 28);
        tabDados.add(edtCnpj);

        chkAtivo = new JCheckBox("Ativo", true);
        chkAtivo.setBounds(350, 102, 100, 28);
        tabDados.add(chkAtivo);

        pageControl.addTab("Dados", tabDados);

        // ------------------ Aba 2: Taxas ------------------
        JPanel tabTaxas = new JPanel(new BorderLayout(5, 5));
        tabTaxas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlCampos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));

        cmbModalidade = new JComboBox<>(new String[]{
                "1 - Debito", "2 - Credito a Vista", "3 - Parc. Lojista", "4 - Parc. Emissor"
        });
        cmbAdquirente = new JComboBox<>(new String[]{
                "1 - Cielo", "2 - Rede", "3 - Getnet", "4 - Stone"
        });
        edtParcelaMin = new JTextField("1", 3);
        edtParcelaMax = new JTextField("1", 3);
        edtTaxa = new JTextField("0.00", 5);
        edtPrazo = new JTextField("2", 3);

        btnAddTaxa = new JButton("+");
        btnRemTaxa = new JButton("-");

        pnlCampos.add(new JLabel("Modalidade:"));
        pnlCampos.add(cmbModalidade);
        pnlCampos.add(new JLabel("De:"));
        pnlCampos.add(edtParcelaMin);
        pnlCampos.add(new JLabel("Até:"));
        pnlCampos.add(edtParcelaMax);
        pnlCampos.add(new JLabel("Taxa %:"));
        pnlCampos.add(edtTaxa);
        pnlCampos.add(new JLabel("Prazo:"));
        pnlCampos.add(edtPrazo);
        pnlCampos.add(new JLabel("Adquirente:"));
        pnlCampos.add(cmbAdquirente);
        pnlCampos.add(btnAddTaxa);
        pnlCampos.add(btnRemTaxa);

        tabTaxas.add(pnlCampos, BorderLayout.NORTH);

        modelTaxas = new DefaultTableModel(new Object[]{"Modalidade", "Faixa", "Taxa %", "Prazo (dias)", "Adquirente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblTaxas = new JTable(modelTaxas);
        tblTaxas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblTaxas.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblTaxas.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        tabTaxas.add(new JScrollPane(tblTaxas), BorderLayout.CENTER);
        pageControl.addTab("Taxas por Modalidade", tabTaxas);

        add(pageControl, BorderLayout.CENTER);

        // ------------------ Rodapé: Salvar / Cancelar ------------------
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar (F12)");
        JButton btnCancelar = new JButton("Cancelar (ESC)");
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnCancelar);
        add(pnlBotoes, BorderLayout.SOUTH);

        // Ações
        btnAddTaxa.addActionListener(e -> adicionarTaxa());
        btnRemTaxa.addActionListener(e -> removerTaxa());
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> fecharComConfirmacao());
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
            public void actionPerformed(ActionEvent e) { fecharComConfirmacao(); }
        });
    }

    private void carregarDados() {
        if (objeto.getId() != null) {
            edtNome.setText(objeto.getNome());
            edtCnpj.setText(objeto.getCnpj());
            chkAtivo.setSelected(objeto.isAtivo());
        }
        atualizarGridTaxas();
    }

    private void atualizarGridTaxas() {
        modelTaxas.setRowCount(0);
        for (BandeiraTefTaxa taxa : objeto.getLstTaxas()) {
            modelTaxas.addRow(new Object[]{
                    taxa.getDescricaoModalidade(),
                    taxa.getFaixaDescricao(),
                    String.format(Locale.US, "%.4f %%", taxa.getTaxaPercentual()),
                    taxa.getPrazoRepasseDias() + " dias",
                    taxa.getDescricaoAdquirente()
            });
        }
    }

    private void adicionarTaxa() {
        try {
            int pMin = Integer.parseInt(edtParcelaMin.getText().trim());
            int pMax = Integer.parseInt(edtParcelaMax.getText().trim());
            double taxaVal = Double.parseDouble(edtTaxa.getText().trim().replace(",", "."));
            int prazo = Integer.parseInt(edtPrazo.getText().trim());

            BandeiraTefTaxa novaTaxa = new BandeiraTefTaxa();
            novaTaxa.setIdBandeiraTef(objeto.getId());
            novaTaxa.setTpIntegrado(cmbModalidade.getSelectedIndex() + 1);
            novaTaxa.setParcelaMin(pMin);
            novaTaxa.setParcelaMax(pMax);
            novaTaxa.setTaxaPercentual(taxaVal);
            novaTaxa.setPrazoRepasseDias(prazo);
            novaTaxa.setAdquirente(cmbAdquirente.getSelectedIndex() + 1);
            novaTaxa.setAtivo(true);

            novaTaxa.validar();
            objeto.getLstTaxas().add(novaTaxa);
            atualizarGridTaxas();

            edtTaxa.setText("0.00");
            edtParcelaMin.setText("1");
            edtParcelaMax.setText("1");
            edtPrazo.setText("2");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerTaxa() {
        int idx = tblTaxas.getSelectedRow();
        if (idx < 0) return;

        if (JOptionPane.showConfirmDialog(this, "Deseja remover a taxa selecionada?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            objeto.getLstTaxas().remove(idx);
            atualizarGridTaxas();
        }
    }

    private void salvar() {
        try {
            objeto.setNome(edtNome.getText().trim());
            objeto.setCnpj(edtCnpj.getText().replaceAll("[^0-9]", ""));
            objeto.setAtivo(chkAtivo.isSelected());

            objeto.validar();

            if (objeto.getId() == null) {
                dao.incluir(objeto);
            } else {
                dao.alterar(objeto);
            }

            confirmado = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fecharComConfirmacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
