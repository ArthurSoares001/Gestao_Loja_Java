package PlanoPagamento.model;


import NaturezaOperacao.model.NaturezaOperacao;
import java.math.BigDecimal;

public class PlanoPagamento {
    private Integer id;
    private String nome;
    private boolean imutavel = false;
    private BigDecimal desconto;
    private BigDecimal descontoMaximo;
    private Integer maxParcela;
    private BigDecimal taxaServico;
    private boolean enviarWeb = true;
    private boolean ativo = true;
    private NaturezaOperacao naturezaOperacao;

    public PlanoPagamento() {
        this.naturezaOperacao = new NaturezaOperacao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isImutavel() {
        return imutavel;
    }

    public void setImutavel(boolean imutavel) {
        this.imutavel = imutavel;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getDescontoMaximo() {
        return descontoMaximo;
    }

    public void setDescontoMaximo(BigDecimal descontoMaximo) {
        this.descontoMaximo = descontoMaximo;
    }

    public Integer getMaxParcela() {
        return maxParcela;
    }

    public void setMaxParcela(Integer maxParcela) {
        this.maxParcela = maxParcela;
    }

    public BigDecimal getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(BigDecimal taxaServico) {
        this.taxaServico = taxaServico;
    }

    public boolean isEnviarWeb() {
        return enviarWeb;
    }

    public void setEnviarWeb(boolean enviarWeb) {
        this.enviarWeb = enviarWeb;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public NaturezaOperacao getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
        this.naturezaOperacao = (naturezaOperacao != null) ? naturezaOperacao : new NaturezaOperacao();
    }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
    }

    public void ocopy(PlanoPagamento source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.imutavel = source.isImutavel();
            this.desconto = source.getDesconto();
            this.descontoMaximo = source.getDescontoMaximo();
            this.maxParcela = source.getMaxParcela();
            this.taxaServico = source.getTaxaServico();
            this.enviarWeb = source.isEnviarWeb();
            this.ativo = source.isAtivo();
            if (source.getNaturezaOperacao() != null) {
                NaturezaOperacao nat = new NaturezaOperacao();
                nat.setId(source.getNaturezaOperacao().getId());
                nat.setNome(source.getNaturezaOperacao().getNome());
                nat.setFantasia(source.getNaturezaOperacao().getFantasia());
                this.naturezaOperacao = nat;
            }
        }
    }

    @Override
    public PlanoPagamento clone() {
        PlanoPagamento clone = new PlanoPagamento();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
