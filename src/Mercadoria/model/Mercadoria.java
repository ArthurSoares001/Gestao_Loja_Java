package Mercadoria.model;

import ConfigFiscal.model.ConfigFiscal;
import Unidade.model.Unidade;

import java.math.BigDecimal;

public class Mercadoria {
    private Integer id;
    private String nome;
    private Unidade unidade;
    private BigDecimal valCompra = BigDecimal.ZERO;
    private BigDecimal margemCusto = BigDecimal.ZERO;
    private BigDecimal valCusto = BigDecimal.ZERO;
    private BigDecimal margemLucro = BigDecimal.ZERO;
    private BigDecimal valVenda = BigDecimal.ZERO;
    private BigDecimal margemMinimo = BigDecimal.ZERO;
    private BigDecimal valMinimo = BigDecimal.ZERO;
    private BigDecimal pesoBruto = BigDecimal.ZERO;
    private BigDecimal pesoLiquido = BigDecimal.ZERO;
    private BigDecimal valPauta;
    private String codBarra;
    private String referencia;
    private String tamanho;
    private BigDecimal altura;
    private BigDecimal largura;
    private BigDecimal comprimento;
    private BigDecimal comissao;
    private String tipo = "PRO";
    private ConfigFiscal configFiscal;
    private boolean ativo = true;
    private Integer quantidade = 0;
    private String ncm;
    private String cest;
    private boolean usarPrescricao = false;
    private String cprod;

    public Mercadoria() {
        this.unidade = new Unidade();
        this.configFiscal = new ConfigFiscal();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) {
        this.unidade = (unidade != null) ? unidade : new Unidade();
    }

    public BigDecimal getValCompra() { return valCompra; }
    public void setValCompra(BigDecimal valCompra) { this.valCompra = valCompra; }

    public BigDecimal getMargemCusto() { return margemCusto; }
    public void setMargemCusto(BigDecimal margemCusto) { this.margemCusto = margemCusto; }

    public BigDecimal getValCusto() { return valCusto; }
    public void setValCusto(BigDecimal valCusto) { this.valCusto = valCusto; }

    public BigDecimal getMargemLucro() { return margemLucro; }
    public void setMargemLucro(BigDecimal margemLucro) { this.margemLucro = margemLucro; }

    public BigDecimal getValVenda() { return valVenda; }
    public void setValVenda(BigDecimal valVenda) { this.valVenda = valVenda; }

    public BigDecimal getMargemMinimo() { return margemMinimo; }
    public void setMargemMinimo(BigDecimal margemMinimo) { this.margemMinimo = margemMinimo; }

    public BigDecimal getValMinimo() { return valMinimo; }
    public void setValMinimo(BigDecimal valMinimo) { this.valMinimo = valMinimo; }

    public BigDecimal getPesoBruto() { return pesoBruto; }
    public void setPesoBruto(BigDecimal pesoBruto) { this.pesoBruto = pesoBruto; }

    public BigDecimal getPesoLiquido() { return pesoLiquido; }
    public void setPesoLiquido(BigDecimal pesoLiquido) { this.pesoLiquido = pesoLiquido; }

    public BigDecimal getValPauta() { return valPauta; }
    public void setValPauta(BigDecimal valPauta) { this.valPauta = valPauta; }

    public String getCodBarra() { return codBarra; }
    public void setCodBarra(String codBarra) { this.codBarra = codBarra; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }

    public BigDecimal getAltura() { return altura; }
    public void setAltura(BigDecimal altura) { this.altura = altura; }

    public BigDecimal getLargura() { return largura; }
    public void setLargura(BigDecimal largura) { this.largura = largura; }

    public BigDecimal getComprimento() { return comprimento; }
    public void setComprimento(BigDecimal comprimento) { this.comprimento = comprimento; }

    public BigDecimal getComissao() { return comissao; }
    public void setComissao(BigDecimal comissao) { this.comissao = comissao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = (tipo != null) ? tipo.toUpperCase().trim() : "PRO"; }

    public ConfigFiscal getConfigFiscal() { return configFiscal; }
    public void setConfigFiscal(ConfigFiscal configFiscal) {
        this.configFiscal = (configFiscal != null) ? configFiscal : new ConfigFiscal();
    }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }

    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }

    public boolean isUsarPrescricao() { return usarPrescricao; }
    public void setUsarPrescricao(boolean usarPrescricao) { this.usarPrescricao = usarPrescricao; }

    public String getCprod() { return cprod; }
    public void setCprod(String cprod) { this.cprod = cprod; }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
        if (unidade == null || unidade.getId() == null || unidade.getId() <= 0) {
            throw new Exception("Selecione uma Unidade de Medida válida!");
        }
        if (configFiscal == null || configFiscal.getId() == null || configFiscal.getId() <= 0) {
            throw new Exception("Selecione uma Configuração Fiscal válida!");
        }
        if (valCompra == null || valVenda == null || valCusto == null || valMinimo == null) {
            throw new Exception("Os campos de valores (Compra, Custo, Venda e Mínimo) são obrigatórios!");
        }
        if (pesoBruto == null || pesoLiquido == null) {
            throw new Exception("Os pesos bruto e líquido são obrigatórios!");
        }
    }

    public void ocopy(Mercadoria source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            if (source.getUnidade() != null) {
                Unidade u = new Unidade();
                u.setId(source.getUnidade().getId());
                u.setNome(source.getUnidade().getNome());
                u.setSigla(source.getUnidade().getSigla());
                this.unidade = u;
            }
            this.valCompra = source.getValCompra();
            this.margemCusto = source.getMargemCusto();
            this.valCusto = source.getValCusto();
            this.margemLucro = source.getMargemLucro();
            this.valVenda = source.getValVenda();
            this.margemMinimo = source.getMargemMinimo();
            this.valMinimo = source.getValMinimo();
            this.pesoBruto = source.getPesoBruto();
            this.pesoLiquido = source.getPesoLiquido();
            this.valPauta = source.getValPauta();
            this.codBarra = source.getCodBarra();
            this.referencia = source.getReferencia();
            this.tamanho = source.getTamanho();
            this.altura = source.getAltura();
            this.largura = source.getLargura();
            this.comprimento = source.getComprimento();
            this.comissao = source.getComissao();
            this.tipo = source.getTipo();
            if (source.getConfigFiscal() != null) {
                ConfigFiscal cf = new ConfigFiscal();
                cf.setId(source.getConfigFiscal().getId());
                cf.setNome(source.getConfigFiscal().getNome());
                this.configFiscal = cf;
            }
            this.ativo = source.isAtivo();
            this.quantidade = source.getQuantidade();
            this.ncm = source.getNcm();
            this.cest = source.getCest();
            this.usarPrescricao = source.isUsarPrescricao();
            this.cprod = source.getCprod();
        }
    }

    @Override
    public Mercadoria clone() {
        Mercadoria clone = new Mercadoria();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}