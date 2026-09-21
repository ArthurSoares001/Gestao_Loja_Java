import bandeiratef.view.FormBandeiraTefControle;
import cidade.view.FormCidadeControle;
import estado.view.FormEstadoControle;
import pais.view.FormPaisControle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FormPrincipal extends JFrame {

    public FormPrincipal() {
        initComponents();
    }

    private void initComponents() {
        // Configurações da Janela Principal
        setTitle("Sistemas - Gestao_Loja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // wsMaximized no Delphi
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Menu Superior (MainMenu)
        setJMenuBar(createMainMenu());

        // 2. Painel Lateral Esquerdo (ScrollBox com Botões)
        add(createSidebarPanel(), BorderLayout.WEST);

        // 3. Painel Central (Fundo com logo/imagem)
        add(createCenterPanel(), BorderLayout.CENTER);

        // 4. Painel Rodapé (fraAtualizador1)
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JMenuBar createMainMenu() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menu Arquivo ---
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.setMnemonic('A');
        JMenuItem itemContabilidade = new JMenuItem("Contabilidade...");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> fecharAplicacao());

        menuArquivo.add(itemContabilidade);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        // --- Menu Cadastros ---
        JMenu menuCadastros = new JMenu("Cadastros");
        menuCadastros.setMnemonic('C');

        JMenu menuLocalizacao = new JMenu("Localização");
        JMenuItem itemPais = new JMenuItem("País...");
        itemPais.addActionListener(e -> new FormPaisControle(this).setVisible(true));
        JMenuItem itemEstado = new JMenuItem("Estado...");
        itemEstado.addActionListener(e -> new FormEstadoControle(this).setVisible(true));
        JMenuItem itemCidade = new JMenuItem("Cidade...");
        itemCidade.addActionListener(e -> new FormCidadeControle(this).setVisible(true));
        menuLocalizacao.add(itemPais);
        menuLocalizacao.addSeparator();
        menuLocalizacao.add(itemEstado);
        menuLocalizacao.addSeparator();
        menuLocalizacao.add(itemCidade);

        menuCadastros.add(menuLocalizacao);
        menuCadastros.addSeparator();

        JMenuItem itemBandeiraTef = new JMenuItem("Bandeira TEF...");
        itemBandeiraTef.addActionListener(e -> new FormBandeiraTefControle(this).setVisible(true));
        JMenuItem itemCaracteristica = new JMenuItem("Característica...");
        itemCaracteristica.addActionListener(e -> abrirModulo("Características"));
        JMenuItem itemConta = new JMenuItem("Conta...");
        itemConta.addActionListener(e -> abrirModulo("Contas"));
        JMenuItem itemCategoria = new JMenuItem("Categoria...");
        itemCategoria.addActionListener(e -> abrirModulo("Categorias"));
        JMenuItem itemEmpresa = new JMenuItem("Empresa...");
        itemEmpresa.addActionListener(e -> abrirModulo("Empresas"));
        JMenuItem itemNaturezaOp = new JMenuItem("Natureza Operação...");
        itemNaturezaOp.addActionListener(e -> abrirModulo("Natureza de Operação"));
        JMenuItem itemNcm = new JMenuItem("NCM...");
        itemNcm.addActionListener(e -> abrirModulo("NCM"));
        JMenuItem itemPlanoPgto = new JMenuItem("Plano de Pagamento...");
        itemPlanoPgto.addActionListener(e -> abrirModulo("Plano de Pagamento"));
        JMenuItem itemTabelaPreco = new JMenuItem("Tabela Preço...");
        itemTabelaPreco.addActionListener(e -> abrirModulo("Tabela de Preço"));
        JMenuItem itemUnidade = new JMenuItem("Unidade...");
        itemUnidade.addActionListener(e -> abrirModulo("Unidade de Medida"));
        JMenuItem itemUsuario = new JMenuItem("Usuário...");
        itemUsuario.addActionListener(e -> abrirModulo("Usuários"));
        JMenuItem itemParticipanteCredito = new JMenuItem("Participante Crédito...");
        itemParticipanteCredito.addActionListener(e -> abrirModulo("Crédito de Participantes"));
        JMenuItem itemCashback = new JMenuItem("CashBack Regra");
        itemCashback.addActionListener(e -> abrirModulo("Regras de Cashback"));
        JMenuItem itemPromocao = new JMenuItem("Promoção");
        itemPromocao.addActionListener(e -> abrirModulo("Promoções"));

        menuCadastros.add(itemBandeiraTef);
        menuCadastros.add(itemCaracteristica);
        menuCadastros.add(itemConta);
        menuCadastros.addSeparator();
        menuCadastros.add(itemCategoria);
        menuCadastros.addSeparator();
        menuCadastros.add(itemEmpresa);
        menuCadastros.addSeparator();
        menuCadastros.add(itemNaturezaOp);
        menuCadastros.add(itemNcm);
        menuCadastros.add(itemPlanoPgto);
        menuCadastros.add(itemTabelaPreco);
        menuCadastros.add(itemUnidade);
        menuCadastros.addSeparator();
        menuCadastros.add(itemUsuario);
        menuCadastros.addSeparator();
        menuCadastros.add(itemParticipanteCredito);
        menuCadastros.addSeparator();
        menuCadastros.add(itemCashback);
        menuCadastros.add(itemPromocao);

        // --- Menu DF-e ---
        JMenu menuDfe = new JMenu("DF-e");
        menuDfe.setMnemonic('D');
        JMenuItem itemDfeInut = new JMenuItem("DF-e Inutilização...");
        itemDfeInut.addActionListener(e -> abrirModulo("Inutilização DF-e"));
        JMenuItem itemDfeCc = new JMenuItem("DF-e Carta de correção...");
        itemDfeCc.addActionListener(e -> abrirModulo("Carta de Correção"));
        JMenuItem itemDfeExpXml = new JMenuItem("DF-e Exportar XML...");
        itemDfeExpXml.addActionListener(e -> abrirModulo("Exportar XML"));
        JMenuItem itemContingencias = new JMenuItem("Ver contingências...");
        itemContingencias.addActionListener(e -> abrirModulo("Contingências"));
        JMenuItem itemDevolverDfe = new JMenuItem("Devolver ou Cancelar pelo arquivo...");
        itemDevolverDfe.addActionListener(e -> abrirModulo("Devolver/Cancelar DF-e"));
        JMenuItem itemSpedFiscal = new JMenuItem("Gerar SPED Fiscal...");
        itemSpedFiscal.addActionListener(e -> abrirModulo("SPED Fiscal"));

        menuDfe.add(itemDfeInut);
        menuDfe.addSeparator();
        menuDfe.add(itemDfeCc);
        menuDfe.addSeparator();
        menuDfe.add(itemDfeExpXml);
        menuDfe.addSeparator();
        menuDfe.add(itemContingencias);
        menuDfe.addSeparator();
        menuDfe.add(itemDevolverDfe);
        menuDfe.addSeparator();
        menuDfe.add(itemSpedFiscal);

        // --- Menu Boleto ---
        JMenu menuBoleto = new JMenu("Boleto");
        JMenuItem itemBoletoCtrl = new JMenuItem("Controle...");
        itemBoletoCtrl.addActionListener(e -> abrirModulo("Controle de Boletos"));
        menuBoleto.add(itemBoletoCtrl);

        // --- Menu Inventário ---
        JMenu menuInventario = new JMenu("Inventário");
        JMenuItem itemRequisicao = new JMenuItem("Requisição (entrada/saída)...");
        itemRequisicao.addActionListener(e -> abrirModulo("Requisições"));
        JMenuItem itemAlteracaoMassa = new JMenuItem("Alteração em Massa...");
        itemAlteracaoMassa.addActionListener(e -> abrirModulo("Alteração em Massa"));
        JMenuItem itemAuditoria = new JMenuItem("Auditoria de mercadoria...");
        itemAuditoria.addActionListener(e -> abrirModulo("Auditoria"));
        JMenuItem itemInventarioContabil = new JMenuItem("Gerar Inventário Contábil...");
        itemInventarioContabil.addActionListener(e -> abrirModulo("Inventário Contábil"));

        menuInventario.add(itemRequisicao);
        menuInventario.addSeparator();
        menuInventario.add(itemAlteracaoMassa);
        menuInventario.addSeparator();
        menuInventario.add(itemAuditoria);
        menuInventario.addSeparator();
        menuInventario.add(itemInventarioContabil);

        // --- Menu Prescrição ---
        JMenu menuPrescricao = new JMenu("Prescrição");
        JMenuItem itemPrescricaoCtrl = new JMenuItem("Controle...");
        itemPrescricaoCtrl.addActionListener(e -> abrirModulo("Controle de Prescrição"));
        menuPrescricao.add(itemPrescricaoCtrl);

        // --- Menu Conciliação ---
        JMenu menuConciliacao = new JMenu("Conciliação");
        JMenuItem itemConciliacaoManual = new JMenuItem("Conciliação de cartão (Manual)...");
        itemConciliacaoManual.addActionListener(e -> abrirModulo("Conciliação Manual"));
        JMenuItem itemConciliacaoArquivo = new JMenuItem("Conciliação de cartão (Arquivo)...");
        itemConciliacaoArquivo.addActionListener(e -> abrirModulo("Conciliação por Arquivo"));
        menuConciliacao.add(itemConciliacaoManual);
        menuConciliacao.addSeparator();
        menuConciliacao.add(itemConciliacaoArquivo);

        // --- Menu Configurações ---
        JMenu menuConfig = new JMenu("Configurações");
        JMenuItem itemConfigPadrao = new JMenuItem("Config. Padrão");
        itemConfigPadrao.addActionListener(e -> abrirModulo("Configurações Gerais"));
        JMenuItem itemConfigFiscal = new JMenuItem("Config. Fiscal...");
        itemConfigFiscal.addActionListener(e -> abrirModulo("Configurações Fiscais"));
        JMenuItem itemConfigDfe = new JMenuItem("Config. DFe...");
        itemConfigDfe.addActionListener(e -> abrirModulo("Configurações DF-e"));
        JMenuItem itemConfigEtiqueta = new JMenuItem("Config. Etiqueta...");
        itemConfigEtiqueta.addActionListener(e -> abrirModulo("Configurações de Etiquetas"));
        JMenuItem itemConfigBoleto = new JMenuItem("Config. Boleto...");
        itemConfigBoleto.addActionListener(e -> abrirModulo("Configurações de Boletos"));

        menuConfig.add(itemConfigPadrao);
        menuConfig.addSeparator();
        menuConfig.add(itemConfigFiscal);
        menuConfig.addSeparator();
        menuConfig.add(itemConfigDfe);
        menuConfig.addSeparator();
        menuConfig.add(itemConfigEtiqueta);
        menuConfig.addSeparator();
        menuConfig.add(itemConfigBoleto);

        // --- Menu Sistema ---
        JMenu menuSistema = new JMenu("Sistema");
        menuSistema.setMnemonic('S');
        JMenuItem itemDashboard = new JMenuItem("DashBoard...");
        itemDashboard.addActionListener(e -> abrirModulo("Dashboard"));
        JMenuItem itemAtualizador = new JMenuItem("Atualizador...");
        itemAtualizador.addActionListener(e -> abrirModulo("Atualizador"));
        JMenuItem itemAdminTef = new JMenuItem("Admin do TEF...");
        itemAdminTef.addActionListener(e -> abrirModulo("Administração TEF"));

        menuSistema.add(itemDashboard);
        menuSistema.addSeparator();
        menuSistema.add(itemAtualizador);
        menuSistema.addSeparator();
        menuSistema.add(itemAdminTef);

        // Adiciona à barra principal
        menuBar.add(menuArquivo);
        menuBar.add(menuCadastros);
        menuBar.add(menuDfe);
        menuBar.add(menuBoleto);
        menuBar.add(menuInventario);
        menuBar.add(menuPrescricao);
        menuBar.add(menuConciliacao);
        menuBar.add(menuConfig);
        menuBar.add(menuSistema);

        return menuBar;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(245, 245, 247));
        sidebar.setPreferredSize(new Dimension(110, 650));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        // SpeedButtons do Delphi
        sidebar.add(createSpeedButton("Participante", e -> abrirModulo("Participantes")));
        sidebar.add(createSpeedButton("Mercadoria", e -> abrirModulo("Mercadorias")));
        sidebar.add(createSpeedButton("Venda", e -> abrirModulo("Vendas / Pedidos")));
        sidebar.add(createSpeedButton("Compra", e -> abrirModulo("Compras")));
        sidebar.add(createSpeedButton("Financeiro", e -> abrirModulo("Financeiro / Caixa")));
        sidebar.add(createSpeedButton("Atendimento", e -> abrirModulo("Atendimento")));
        sidebar.add(createSpeedButton("Ajuda", e -> JOptionPane.showMessageDialog(this, "Sistema Gestão Loja versão 0.0.1")));
        sidebar.add(createSpeedButton("DashBoard", e -> abrirModulo("DashBoard")));

        sidebar.add(Box.createVerticalGlue()); // Espaçamento flexível empurrando o Sair para o rodapé da barra

        JButton btnSair = createSpeedButton("Sair", e -> fecharAplicacao());
        btnSair.setForeground(new Color(180, 40, 40));
        sidebar.add(btnSair);

        return sidebar;
    }

    private JButton createSpeedButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(110, 60));
        btn.setPreferredSize(new Dimension(110, 60));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);

        JLabel lblLogo = new JLabel("Gestão de lojas" );
        lblLogo.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblLogo.setForeground(new Color(120, 120, 125));
        center.add(lblLogo);

        return center;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setPreferredSize(new Dimension(getWidth(), 35));
        footer.setBackground(new Color(240, 240, 243));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JLabel lblVersao = new JLabel(" Versão do Sistema: 1.0.0.34 | Conexão: PostgreSQL Ativa");
        lblVersao.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblVersao.setBorder(new EmptyBorder(5, 10, 5, 5));

        JButton btnAtualizar = new JButton("Verificar Atualizações");
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnAtualizar.addActionListener(e -> JOptionPane.showMessageDialog(this, "O sistema já está na versão mais recente."));

        footer.add(lblVersao, BorderLayout.WEST);
        footer.add(btnAtualizar, BorderLayout.EAST);

        return footer;
    }

    private void abrirModulo(String modulo) {
        JOptionPane.showMessageDialog(this, "Abrindo módulo: " + modulo, "ICommerce", JOptionPane.INFORMATION_MESSAGE);
    }

    private void fecharAplicacao() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair do sistema?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}