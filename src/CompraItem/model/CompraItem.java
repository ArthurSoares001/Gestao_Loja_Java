package CompraItem.model;

import Mercadoria.model.Mercadoria;

public class CompraItem {

    private Integer id;
    private Integer idCompra;
    private Integer idMercadoria;
    private Double valorCompra = 0.0;
    private Double valorVenda = 0.0;
    private Double quantidade = 0.0;
    private Double valorTotal = 0.0;
    private Double desconto = 0.0;
    private Double acrescimo = 0.0;
    private Double valorTabela = 0.0;
    private Double valorCusto = 0.0;
    private Integer qtdDevolvida = 0;
    private String devolucaoMotivo;
    private String cst;
    private String cfop;
    private Double aliqIcms = 0.0;
    private Double baseIcms = 0.0;
    private Double valorIcms = 0.0;
    private Double reducaoBase = 0.0;
    private Double aliqIcmsSt = 0.0;
    private Double baseIcmsSt = 0.0;
    private Double valorIcmsSt = 0.0;
    private String cstPis;
    private Double aliqPis = 0.0;
    private Double basePis = 0.0;
    private Double valorPis = 0.0;
    private String cstCofins;
    private Double aliqCofins = 0.0;
    private Double baseCofins = 0.0;
    private Double valorCofins = 0.0;
    private String cstIpi;
    private Double aliqIpi = 0.0;
    private Double baseIpi = 0.0;
    private Double valorIpi = 0.0;
    private Double aliqIss = 0.0;
    private Double baseIss = 0.0;
    private Double valorIss = 0.0;
    private Mercadoria mercadoria;
    private String imagemPadrao;

    public CompraItem() {
        this.mercadoria = new Mercadoria();
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }

    public Integer getIdMercadoria() { return idMercadoria; }
    public void setIdMercadoria(Integer idMercadoria) { this.idMercadoria = idMercadoria; }

    public Double getValorCompra() { return valorCompra != null ? valorCompra : 0.0; }
    public void setValorCompra(Double valorCompra) { this.valorCompra = valorCompra; }

    public Double getValorVenda() { return valorVenda != null ? valorVenda : 0.0; }
    public void setValorVenda(Double valorVenda) { this.valorVenda = valorVenda; }

    public Double getQuantidade() { return quantidade != null ? quantidade : 0.0; }
    public void setQuantidade(Double quantidade) { this.quantidade = quantidade; }

    public Double getValorTotal() { return valorTotal != null ? valorTotal : 0.0; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public Double getDesconto() { return desconto != null ? desconto : 0.0; }
    public void setDesconto(Double desconto) { this.desconto = desconto; }

    public Double getAcrescimo() { return acrescimo != null ? acrescimo : 0.0; }
    public void setAcrescimo(Double acrescimo) { this.acrescimo = acrescimo; }

    public Double getValorTabela() { return valorTabela != null ? valorTabela : 0.0; }
    public void setValorTabela(Double valorTabela) { this.valorTabela = valorTabela; }

    public Double getValorCusto() { return valorCusto != null ? valorCusto : 0.0; }
    public void setValorCusto(Double valorCusto) { this.valorCusto = valorCusto; }

    public Integer getQtdDevolvida() { return qtdDevolvida != null ? qtdDevolvida : 0; }
    public void setQtdDevolvida(Integer qtdDevolvida) { this.qtdDevolvida = qtdDevolvida; }

    public String getDevolucaoMotivo() { return devolucaoMotivo; }
    public void setDevolucaoMotivo(String devolucaoMotivo) { this.devolucaoMotivo = devolucaoMotivo; }

    public String getCst() { return cst; }
    public void setCst(String cst) { this.cst = cst; }

    public String getCfop() { return cfop; }
    public void setCfop(String cfop) { this.cfop = cfop; }

    public Double getAliqIcms() { return aliqIcms != null ? aliqIcms : 0.0; }
    public void setAliqIcms(Double aliqIcms) { this.aliqIcms = aliqIcms; }

    public Double getBaseIcms() { return baseIcms != null ? baseIcms : 0.0; }
    public void setBaseIcms(Double baseIcms) { this.baseIcms = baseIcms; }

    public Double getValorIcms() { return valorIcms != null ? valorIcms : 0.0; }
    public void setValorIcms(Double valorIcms) { this.valorIcms = valorIcms; }

    public Double getReducaoBase() { return reducaoBase != null ? reducaoBase : 0.0; }
    public void setReducaoBase(Double reducaoBase) { this.reducaoBase = reducaoBase; }

    public Double getAliqIcmsSt() { return aliqIcmsSt != null ? aliqIcmsSt : 0.0; }
    public void setAliqIcmsSt(Double aliqIcmsSt) { this.aliqIcmsSt = aliqIcmsSt; }

    public Double getBaseIcmsSt() { return baseIcmsSt != null ? baseIcmsSt : 0.0; }
    public void setBaseIcmsSt(Double baseIcmsSt) { this.baseIcmsSt = baseIcmsSt; }

    public Double getValorIcmsSt() { return valorIcmsSt != null ? valorIcmsSt : 0.0; }
    public void setValorIcmsSt(Double valorIcmsSt) { this.valorIcmsSt = valorIcmsSt; }

    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }

    public Double getAliqPis() { return aliqPis != null ? aliqPis : 0.0; }
    public void setAliqPis(Double aliqPis) { this.aliqPis = aliqPis; }

    public Double getBasePis() { return basePis != null ? basePis : 0.0; }
    public void setBasePis(Double basePis) { this.basePis = basePis; }

    public Double getValorPis() { return valorPis != null ? valorPis : 0.0; }
    public void setValorPis(Double valorPis) { this.valorPis = valorPis; }

    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }

    public Double getAliqCofins() { return aliqCofins != null ? aliqCofins : 0.0; }
    public void setAliqCofins(Double aliqCofins) { this.aliqCofins = aliqCofins; }

    public Double getBaseCofins() { return baseCofins != null ? baseCofins : 0.0; }
    public void setBaseCofins(Double baseCofins) { this.baseCofins = baseCofins; }

    public Double getValorCofins() { return valorCofins != null ? valorCofins : 0.0; }
    public void setValorCofins(Double valorCofins) { this.valorCofins = valorCofins; }

    public String getCstIpi() { return cstIpi; }
    public void setCstIpi(String cstIpi) { this.cstIpi = cstIpi; }

    public Double getAliqIpi() { return aliqIpi != null ? aliqIpi : 0.0; }
    public void setAliqIpi(Double aliqIpi) { this.aliqIpi = aliqIpi; }

    public Double getBaseIpi() { return baseIpi != null ? baseIpi : 0.0; }
    public void setBaseIpi(Double baseIpi) { this.baseIpi = baseIpi; }

    public Double getValorIpi() { return valorIpi != null ? valorIpi : 0.0; }
    public void setValorIpi(Double valorIpi) { this.valorIpi = valorIpi; }

    public Double getAliqIss() { return aliqIss != null ? aliqIss : 0.0; }
    public void setAliqIss(Double aliqIss) { this.aliqIss = aliqIss; }

    public Double getBaseIss() { return baseIss != null ? baseIss : 0.0; }
    public void setBaseIss(Double baseIss) { this.baseIss = baseIss; }

    public Double getValorIss() { return valorIss != null ? valorIss : 0.0; }
    public void setValorIss(Double valorIss) { this.valorIss = valorIss; }

    public Mercadoria getMercadoria() { return mercadoria; }
    public void setMercadoria(Mercadoria mercadoria) {
        this.mercadoria = (mercadoria != null) ? mercadoria : new Mercadoria();
    }

    public String getImagemPadrao() { return imagemPadrao; }
    public void setImagemPadrao(String imagemPadrao) { this.imagemPadrao = imagemPadrao; }

    // Funções de Negócio originais do Delphi[cite: 4]
    public double valorTotalSemDesconto() {
        return getQuantidade() * getValorCompra();
    }

    public double valorDesconto() {
        return (valorTotalSemDesconto() * getDesconto()) / 100.0;
    }

    public double valParaPorcentDesconto(Double pValDesconto) {
        if (getValorTotal() != 0.0 && pValDesconto != null) {
            return (pValDesconto / getValorTotal()) * 100.0;
        }
        return 0.0;
    }

    public double valorTotalVenda() {
        return getValorVenda() * getQuantidade();
    }

    public double porcentoCusto() {
        if (getValorCompra() > 0) {
            return ((getValorCusto() - getValorCompra()) * 100.0) / getValorCompra();
        }
        return 0.0;
    }

    public double porcentoLucro() {
        if (getValorCusto() > 0) {
            return ((getValorVenda() - getValorCusto()) / getValorCusto()) * 100.0;
        }
        return 0.0;
    }

    public double atualizarPorcentagemCusto(double pValorCompra, double pValorCusto) {
        if (pValorCompra > 0) {
            return ((pValorCusto - pValorCompra) * 100.0) / pValorCompra;
        }
        return 0.0;
    }

    public double atualizarPorcentagemLucro(double pValorVenda, double pValorCusto) {
        if (pValorCusto > 0) {
            return ((pValorVenda - pValorCusto) * 100.0) / pValorCusto;
        }
        return 0.0;
    }

    public void atualizarValorCusto(double porcentagemCusto, double pValorCompra) {
        setValorCusto(((pValorCompra * porcentagemCusto) / 100.0) + pValorCompra);
    }

    public double atualizarValorVenda(double lucro, double pValorCusto) {
        return pValorCusto * (1.0 + (lucro / 100.0));
    }

    public double atualizarTotalCompras(double pQuantidade, double pValorCompra, double pDesconto) {
        if (pQuantidade > 0 && pValorCompra > 0) {
            return (pQuantidade * pValorCompra) - (((pQuantidade * pValorCompra) * pDesconto) / 100.0);
        }
        return 0.0;
    }

    public double atualizarTotalVenda(double pQuantidade, double pValorVenda) {
        return pValorVenda * pQuantidade;
    }

    public double valorUnitarioTributo(double pQuantidade, double tributo) {
        if (pQuantidade > 0) {
            return tributo / pQuantidade;
        }
        return 0.0;
    }

    public double calcularTributo(double pQuantidade, double valorUniTributo) {
        return pQuantidade * valorUniTributo;
    }

    public String statusValor() {
        if (mercadoria != null && mercadoria.getValCompra() != null) {
            double valCompraOriginal = mercadoria.getValCompra().doubleValue();
            if (getValorCompra() > valCompraOriginal) return ">";
            if (getValorCompra() < valCompraOriginal) return "<";
        }
        return "=";
    }

    public void ocopy(CompraItem source) {
        if (source != null) {
            this.id = source.getId();
            this.idMercadoria = source.getIdMercadoria();
            this.idCompra = source.getIdCompra();
            this.valorCompra = source.getValorCompra();
            this.valorVenda = source.getValorVenda();
            this.quantidade = source.getQuantidade();
            this.valorTotal = source.getValorTotal();
            this.desconto = source.getDesconto();
            this.acrescimo = source.getAcrescimo();
            this.valorTabela = source.getValorTabela();
            this.valorCusto = source.getValorCusto();
            this.qtdDevolvida = source.getQtdDevolvida();
            this.devolucaoMotivo = source.getDevolucaoMotivo();
            this.cst = source.getCst();
            this.cfop = source.getCfop();
            this.aliqIcms = source.getAliqIcms();
            this.baseIcms = source.getBaseIcms();
            this.valorIcms = source.getValorIcms();
            this.reducaoBase = source.getReducaoBase();
            this.aliqIcmsSt = source.getAliqIcmsSt();
            this.baseIcmsSt = source.getBaseIcmsSt();
            this.valorIcmsSt = source.getValorIcmsSt();
            this.cstPis = source.getCstPis();
            this.aliqPis = source.getAliqPis();
            this.basePis = source.getBasePis();
            this.valorPis = source.getValorPis();
            this.cstCofins = source.getCstCofins();
            this.aliqCofins = source.getAliqCofins();
            this.baseCofins = source.getBaseCofins();
            this.valorCofins = source.getValorCofins();
            this.cstIpi = source.getCstIpi();
            this.aliqIpi = source.getAliqIpi();
            this.baseIpi = source.getBaseIpi();
            this.valorIpi = source.getValorIpi();
            this.aliqIss = source.getAliqIss();
            this.baseIss = source.getBaseIss();
            this.valorIss = source.getValorIss();
            this.imagemPadrao = source.getImagemPadrao();
            if (source.getMercadoria() != null) {
                this.mercadoria.ocopy(source.getMercadoria());
            }
        }
    }

    public CompraItem clone() {
        CompraItem clone = new CompraItem();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return id != null ? String.valueOf(id) : "";
    }
}
