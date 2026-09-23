package ConfigFiscal.model;


import java.math.BigDecimal;

public class ConfigFiscal {
    private Integer id;
    private String nome;

    // ICMS
    private String cst;
    private String cstFora;
    private BigDecimal aliqIcms;
    private BigDecimal reducaoBase;
    private BigDecimal reducaoBaseFora;

    // IPI, PIS e COFINS
    private String cstIpi;
    private BigDecimal aliqIpi;
    private String cstPis;
    private BigDecimal aliqPis;
    private String cstCofins;
    private BigDecimal aliqCofins;

    private String mensagem;
    private boolean ativo = true;

    // Reforma Tributária
    private boolean usarReformaTrib = false;
    private String classTrib;
    private String cstIbs;
    private BigDecimal aliqIbs;
    private BigDecimal aliqIbsUf;
    private BigDecimal aliqIbsMunicipio;
    private BigDecimal diferimentoIbs;

    private String cstCbs;
    private BigDecimal aliqCbs;
    private BigDecimal diferimentoCbs;

    private String cstIs;
    private BigDecimal aliqIs;
    private BigDecimal aliqIsEspecifica;
    private BigDecimal reducaoIs;

    private BigDecimal percCompraGoverno;
    private String entePublico;
    private boolean utilizaSplitPayment = false;
    private BigDecimal percSplitPayment;
    private boolean utilizaCashback = false;
    private BigDecimal percCashback;

    // Operações Especiais
    private boolean operacaoZfm = false;
    private boolean operacaoAlc = false;
    private boolean operacaoExportacao = false;
    private boolean operacaoImportacao = false;
    private String codMunFgIbs;
    private String versaoLayoutReforma;
    private Integer tipoTributacao = 0;
    private BigDecimal aliqIbsCredPresumido;
    private BigDecimal aliqCbsCredPresumido;

    public ConfigFiscal() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCst() { return cst; }
    public void setCst(String cst) { this.cst = cst; }

    public String getCstFora() { return cstFora; }
    public void setCstFora(String cstFora) { this.cstFora = cstFora; }

    public BigDecimal getAliqIcms() { return aliqIcms; }
    public void setAliqIcms(BigDecimal aliqIcms) { this.aliqIcms = aliqIcms; }

    public BigDecimal getReducaoBase() { return reducaoBase; }
    public void setReducaoBase(BigDecimal reducaoBase) { this.reducaoBase = reducaoBase; }

    public BigDecimal getReducaoBaseFora() { return reducaoBaseFora; }
    public void setReducaoBaseFora(BigDecimal reducaoBaseFora) { this.reducaoBaseFora = reducaoBaseFora; }

    public String getCstIpi() { return cstIpi; }
    public void setCstIpi(String cstIpi) { this.cstIpi = cstIpi; }

    public BigDecimal getAliqIpi() { return aliqIpi; }
    public void setAliqIpi(BigDecimal aliqIpi) { this.aliqIpi = aliqIpi; }

    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }

    public BigDecimal getAliqPis() { return aliqPis; }
    public void setAliqPis(BigDecimal aliqPis) { this.aliqPis = aliqPis; }

    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }

    public BigDecimal getAliqCofins() { return aliqCofins; }
    public void setAliqCofins(BigDecimal aliqCofins) { this.aliqCofins = aliqCofins; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public boolean isUsarReformaTrib() { return usarReformaTrib; }
    public void setUsarReformaTrib(boolean usarReformaTrib) { this.usarReformaTrib = usarReformaTrib; }

    public String getClassTrib() { return classTrib; }
    public void setClassTrib(String classTrib) { this.classTrib = classTrib; }

    public String getCstIbs() { return cstIbs; }
    public void setCstIbs(String cstIbs) { this.cstIbs = cstIbs; }

    public BigDecimal getAliqIbs() { return aliqIbs; }
    public void setAliqIbs(BigDecimal aliqIbs) { this.aliqIbs = aliqIbs; }

    public BigDecimal getAliqIbsUf() { return aliqIbsUf; }
    public void setAliqIbsUf(BigDecimal aliqIbsUf) { this.aliqIbsUf = aliqIbsUf; }

    public BigDecimal getAliqIbsMunicipio() { return aliqIbsMunicipio; }
    public void setAliqIbsMunicipio(BigDecimal aliqIbsMunicipio) { this.aliqIbsMunicipio = aliqIbsMunicipio; }

    public BigDecimal getDiferimentoIbs() { return diferimentoIbs; }
    public void setDiferimentoIbs(BigDecimal diferimentoIbs) { this.diferimentoIbs = diferimentoIbs; }

    public String getCstCbs() { return cstCbs; }
    public void setCstCbs(String cstCbs) { this.cstCbs = cstCbs; }

    public BigDecimal getAliqCbs() { return aliqCbs; }
    public void setAliqCbs(BigDecimal aliqCbs) { this.aliqCbs = aliqCbs; }

    public BigDecimal getDiferimentoCbs() { return diferimentoCbs; }
    public void setDiferimentoCbs(BigDecimal diferimentoCbs) { this.diferimentoCbs = diferimentoCbs; }

    public String getCstIs() { return cstIs; }
    public void setCstIs(String cstIs) { this.cstIs = cstIs; }

    public BigDecimal getAliqIs() { return aliqIs; }
    public void setAliqIs(BigDecimal aliqIs) { this.aliqIs = aliqIs; }

    public BigDecimal getAliqIsEspecifica() { return aliqIsEspecifica; }
    public void setAliqIsEspecifica(BigDecimal aliqIsEspecifica) { this.aliqIsEspecifica = aliqIsEspecifica; }

    public BigDecimal getReducaoIs() { return reducaoIs; }
    public void setReducaoIs(BigDecimal reducaoIs) { this.reducaoIs = reducaoIs; }

    public BigDecimal getPercCompraGoverno() { return percCompraGoverno; }
    public void setPercCompraGoverno(BigDecimal percCompraGoverno) { this.percCompraGoverno = percCompraGoverno; }

    public String getEntePublico() { return entePublico; }
    public void setEntePublico(String entePublico) { this.entePublico = entePublico; }

    public boolean isUtilizaSplitPayment() { return utilizaSplitPayment; }
    public void setUtilizaSplitPayment(boolean utilizaSplitPayment) { this.utilizaSplitPayment = utilizaSplitPayment; }

    public BigDecimal getPercSplitPayment() { return percSplitPayment; }
    public void setPercSplitPayment(BigDecimal percSplitPayment) { this.percSplitPayment = percSplitPayment; }

    public boolean isUtilizaCashback() { return utilizaCashback; }
    public void setUtilizaCashback(boolean utilizaCashback) { this.utilizaCashback = utilizaCashback; }

    public BigDecimal getPercCashback() { return percCashback; }
    public void setPercCashback(BigDecimal percCashback) { this.percCashback = percCashback; }

    public boolean isOperacaoZfm() { return operacaoZfm; }
    public void setOperacaoZfm(boolean operacaoZfm) { this.operacaoZfm = operacaoZfm; }

    public boolean isOperacaoAlc() { return operacaoAlc; }
    public void setOperacaoAlc(boolean operacaoAlc) { this.operacaoAlc = operacaoAlc; }

    public boolean isOperacaoExportacao() { return operacaoExportacao; }
    public void setOperacaoExportacao(boolean operacaoExportacao) { this.operacaoExportacao = operacaoExportacao; }

    public boolean isOperacaoImportacao() { return operacaoImportacao; }
    public void setOperacaoImportacao(boolean operacaoImportacao) { this.operacaoImportacao = operacaoImportacao; }

    public String getCodMunFgIbs() { return codMunFgIbs; }
    public void setCodMunFgIbs(String codMunFgIbs) { this.codMunFgIbs = codMunFgIbs; }

    public String getVersaoLayoutReforma() { return versaoLayoutReforma; }
    public void setVersaoLayoutReforma(String versaoLayoutReforma) { this.versaoLayoutReforma = versaoLayoutReforma; }

    public Integer getTipoTributacao() { return tipoTributacao; }
    public void setTipoTributacao(Integer tipoTributacao) { this.tipoTributacao = tipoTributacao; }

    public BigDecimal getAliqIbsCredPresumido() { return aliqIbsCredPresumido; }
    public void setAliqIbsCredPresumido(BigDecimal aliqIbsCredPresumido) { this.aliqIbsCredPresumido = aliqIbsCredPresumido; }

    public BigDecimal getAliqCbsCredPresumido() { return aliqCbsCredPresumido; }
    public void setAliqCbsCredPresumido(BigDecimal aliqCbsCredPresumido) { this.aliqCbsCredPresumido = aliqCbsCredPresumido; }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome da Configuração Fiscal é Obrigatório!");
        }
    }

    public void ocopy(ConfigFiscal s) {
        if (s != null) {
            this.id = s.getId();
            this.nome = s.getNome();
            this.cst = s.getCst();
            this.cstFora = s.getCstFora();
            this.aliqIcms = s.getAliqIcms();
            this.reducaoBase = s.getReducaoBase();
            this.reducaoBaseFora = s.getReducaoBaseFora();
            this.cstIpi = s.getCstIpi();
            this.aliqIpi = s.getAliqIpi();
            this.cstPis = s.getCstPis();
            this.aliqPis = s.getAliqPis();
            this.cstCofins = s.getCstCofins();
            this.aliqCofins = s.getAliqCofins();
            this.mensagem = s.getMensagem();
            this.ativo = s.isAtivo();
            this.usarReformaTrib = s.isUsarReformaTrib();
            this.classTrib = s.getClassTrib();
            this.cstIbs = s.getCstIbs();
            this.aliqIbs = s.getAliqIbs();
            this.aliqIbsUf = s.getAliqIbsUf();
            this.aliqIbsMunicipio = s.getAliqIbsMunicipio();
            this.diferimentoIbs = s.getDiferimentoIbs();
            this.cstCbs = s.getCstCbs();
            this.aliqCbs = s.getAliqCbs();
            this.diferimentoCbs = s.getDiferimentoCbs();
            this.cstIs = s.getCstIs();
            this.aliqIs = s.getAliqIs();
            this.aliqIsEspecifica = s.getAliqIsEspecifica();
            this.reducaoIs = s.getReducaoIs();
            this.percCompraGoverno = s.getPercCompraGoverno();
            this.entePublico = s.getEntePublico();
            this.utilizaSplitPayment = s.isUtilizaSplitPayment();
            this.percSplitPayment = s.getPercSplitPayment();
            this.utilizaCashback = s.isUtilizaCashback();
            this.percCashback = s.getPercCashback();
            this.operacaoZfm = s.isOperacaoZfm();
            this.operacaoAlc = s.isOperacaoAlc();
            this.operacaoExportacao = s.isOperacaoExportacao();
            this.operacaoImportacao = s.isOperacaoImportacao();
            this.codMunFgIbs = s.getCodMunFgIbs();
            this.versaoLayoutReforma = s.getVersaoLayoutReforma();
            this.tipoTributacao = s.getTipoTributacao();
            this.aliqIbsCredPresumido = s.getAliqIbsCredPresumido();
            this.aliqCbsCredPresumido = s.getAliqCbsCredPresumido();
        }
    }

    @Override
    public ConfigFiscal clone() {
        ConfigFiscal clone = new ConfigFiscal();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
