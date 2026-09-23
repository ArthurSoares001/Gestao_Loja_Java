package CompraParcela.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CompraParcela {

    private Integer id;
    private Double valor = 0.0;
    private LocalDate vencimento;
    private LocalDateTime digitado;
    private String sku;
    private LocalDateTime autorizacao;
    private Integer idCompra;
    private Object conta; // Entidade Conta/Participante
    private String bandeira;
    private String cnpjOperadora;
    private Integer tpIntegrado;
    private Object bandeiraTEF; // Entidade BandeiraTef
    private Integer parcelas = 0;
    private Integer adquirente = 3;

    public CompraParcela() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Double getValor() { return valor != null ? valor : 0.0; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }

    public LocalDateTime getDigitado() { return digitado; }
    public void setDigitado(LocalDateTime digitado) { this.digitado = digitado; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public LocalDateTime getAutorizacao() { return autorizacao; }
    public void setAutorizacao(LocalDateTime autorizacao) { this.autorizacao = autorizacao; }

    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }

    public Object getConta() { return conta; }
    public void setConta(Object conta) { this.conta = conta; }

    public String getBandeira() { return bandeira; }
    public void setBandeira(String bandeira) { this.bandeira = bandeira; }

    public String getCnpjOperadora() { return cnpjOperadora; }
    public void setCnpjOperadora(String cnpjOperadora) { this.cnpjOperadora = cnpjOperadora; }

    public Integer getTpIntegrado() { return tpIntegrado; }
    public void setTpIntegrado(Integer tpIntegrado) { this.tpIntegrado = tpIntegrado; }

    public Object getBandeiraTEF() { return bandeiraTEF; }
    public void setBandeiraTEF(Object bandeiraTEF) { this.bandeiraTEF = bandeiraTEF; }

    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

    public Integer getAdquirente() { return adquirente; }
    public void setAdquirente(Integer adquirente) { this.adquirente = adquirente; }

    public void ocopy(CompraParcela source) {
        if (source != null) {
            this.id = source.getId();
            this.valor = source.getValor();
            this.digitado = source.getDigitado();
            this.vencimento = source.getVencimento();
            this.sku = source.getSku();
            this.autorizacao = source.getAutorizacao();
            this.idCompra = source.getIdCompra();
            this.conta = source.getConta();
            this.bandeira = source.getBandeira();
            this.cnpjOperadora = source.getCnpjOperadora();
            this.tpIntegrado = source.getTpIntegrado();
            this.bandeiraTEF = source.getBandeiraTEF();
            this.parcelas = source.getParcelas();
            this.adquirente = source.getAdquirente();
        }
    }

    public CompraParcela clone() {
        CompraParcela clone = new CompraParcela();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return id != null ? String.valueOf(id) : "";
    }
}
