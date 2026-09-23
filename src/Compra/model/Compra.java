package Compra.model;

import CompraItem.model.CompraItem;
import CompraParcela.model.CompraParcela;
import NaturezaOperacao.model.NaturezaOperacao;
import Participante.model.Participante;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Integer id;
    private LocalDateTime digitado;
    private String digitadoPor;
    private Integer itens;
    private Double qtdTotal = 0.0;
    private Double valTotal = 0.0;
    private Double desconto = 0.0;
    private Double acrescimo = 0.0;
    private Double troco = 0.0;
    private Double frete = 0.0;
    private LocalDateTime impresso;
    private String impressoPor;
    private LocalDateTime separado;
    private String separadoPor;
    private LocalDateTime faturado;
    private String faturadoPor;
    private LocalDateTime cancelado;
    private String canceladoPor;
    private String canceladoMotivo;
    private LocalDateTime clonado;
    private String clonadoPor;
    private Integer clonadoId;

    private LocalDateTime acertado;
    private String acertadoPor;
    private String acertadoMotivo;

    private Double baseIcms = 0.0;
    private Double baseIcmsSt = 0.0;
    private Double valorIcms = 0.0;
    private Double valorIcmsSt = 0.0;
    private Double valorPis = 0.0;
    private Double valorCofins = 0.0;
    private Double valorIpi = 0.0;

    private Integer dfeAmbiente;
    private Integer dfeSerie;
    private Integer dfeNumero;
    private String dfeXml;
    private Integer dfeModelo;
    private String dfeChave;
    private String dfeStatus;
    private String dfeVersao;
    private String dfeProtoco;
    private String dfeAutorizacao;
    private LocalDateTime dfeDhAutorizacao;

    private Integer dfeNumeroDevolucao;
    private String dfeDevolucaoXml;
    private String dfeReferenciaChave;

    private NaturezaOperacao naturezaOperacao;
    private Participante fornecedor;

    private List<CompraItem> lstCompraItem;
    private List<CompraParcela> lstCompraParcela;
    private List<Object> lstMercadoria; // Substitua por List<Mercadoria> se aplicável

    public Double fvalorComDesc = 0.0;
    public Double descontoTotal = 0.0;

    public Compra() {
        this.naturezaOperacao = new NaturezaOperacao();
        this.fornecedor = new Participante();
        this.lstCompraItem = new ArrayList<>();
        this.lstCompraParcela = new ArrayList<>();
        this.lstMercadoria = new ArrayList<>();
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDigitado() { return digitado; }
    public void setDigitado(LocalDateTime digitado) { this.digitado = digitado; }

    public String getDigitadoPor() { return digitadoPor; }
    public void setDigitadoPor(String digitadoPor) { this.digitadoPor = digitadoPor; }

    public Integer getItens() { return itens; }
    public void setItens(Integer itens) { this.itens = itens; }

    public Double getQtdTotal() { return qtdTotal != null ? qtdTotal : 0.0; }
    public void setQtdTotal(Double qtdTotal) { this.qtdTotal = qtdTotal; }

    public Double getValTotal() { return valTotal != null ? valTotal : 0.0; }
    public void setValTotal(Double valTotal) { this.valTotal = valTotal; }

    public Double getDesconto() { return desconto != null ? desconto : 0.0; }
    public void setDesconto(Double desconto) { this.desconto = desconto; }

    public Double getAcrescimo() { return acrescimo != null ? acrescimo : 0.0; }
    public void setAcrescimo(Double acrescimo) { this.acrescimo = acrescimo; }

    public Double getTroco() { return troco != null ? troco : 0.0; }
    public void setTroco(Double troco) { this.troco = troco; }

    public Double getFrete() { return frete != null ? frete : 0.0; }
    public void setFrete(Double frete) { this.frete = frete; }

    public LocalDateTime getImpresso() { return impresso; }
    public void setImpresso(LocalDateTime impresso) { this.impresso = impresso; }

    public String getImpressoPor() { return impressoPor; }
    public void setImpressoPor(String impressoPor) { this.impressoPor = impressoPor; }

    public LocalDateTime getSeparado() { return separado; }
    public void setSeparado(LocalDateTime separado) { this.separado = separado; }

    public String getSeparadoPor() { return separadoPor; }
    public void setSeparadoPor(String separadoPor) { this.separadoPor = separadoPor; }

    public LocalDateTime getFaturado() { return faturado; }
    public void setFaturado(LocalDateTime faturado) { this.faturado = faturado; }

    public String getFaturadoPor() { return faturadoPor; }
    public void setFaturadoPor(String faturadoPor) { this.faturadoPor = faturadoPor; }

    public LocalDateTime getCancelado() { return cancelado; }
    public void setCancelado(LocalDateTime cancelado) { this.cancelado = cancelado; }

    public String getCanceladoPor() { return canceladoPor; }
    public void setCanceladoPor(String canceladoPor) { this.canceladoPor = canceladoPor; }

    public String getCanceladoMotivo() { return canceladoMotivo; }
    public void setCanceladoMotivo(String canceladoMotivo) { this.canceladoMotivo = canceladoMotivo; }

    public LocalDateTime getClonado() { return clonado; }
    public void setClonado(LocalDateTime clonado) { this.clonado = clonado; }

    public String getClonadoPor() { return clonadoPor; }
    public void setClonadoPor(String clonadoPor) { this.clonadoPor = clonadoPor; }

    public Integer getClonadoId() { return clonadoId; }
    public void setClonadoId(Integer clonadoId) { this.clonadoId = clonadoId; }

    public LocalDateTime getAcertado() { return acertado; }
    public void setAcertado(LocalDateTime acertado) { this.acertado = acertado; }

    public String getAcertadoPor() { return acertadoPor; }
    public void setAcertadoPor(String acertadoPor) { this.acertadoPor = acertadoPor; }

    public String getAcertadoMotivo() { return acertadoMotivo; }
    public void setAcertadoMotivo(String acertadoMotivo) { this.acertadoMotivo = acertadoMotivo; }

    public Double getBaseIcms() { return baseIcms != null ? baseIcms : 0.0; }
    public void setBaseIcms(Double baseIcms) { this.baseIcms = baseIcms; }

    public Double getBaseIcmsSt() { return baseIcmsSt != null ? baseIcmsSt : 0.0; }
    public void setBaseIcmsSt(Double baseIcmsSt) { this.baseIcmsSt = baseIcmsSt; }

    public Double getValorIcms() { return valorIcms != null ? valorIcms : 0.0; }
    public void setValorIcms(Double valorIcms) { this.valorIcms = valorIcms; }

    public Double getValorIcmsSt() { return valorIcmsSt != null ? valorIcmsSt : 0.0; }
    public void setValorIcmsSt(Double valorIcmsSt) { this.valorIcmsSt = valorIcmsSt; }

    public Double getValorPis() { return valorPis != null ? valorPis : 0.0; }
    public void setValorPis(Double valorPis) { this.valorPis = valorPis; }

    public Double getValorCofins() { return valorCofins != null ? valorCofins : 0.0; }
    public void setValorCofins(Double valorCofins) { this.valorCofins = valorCofins; }

    public Double getValorIpi() { return valorIpi != null ? valorIpi : 0.0; }
    public void setValorIpi(Double valorIpi) { this.valorIpi = valorIpi; }

    public Integer getDfeAmbiente() { return dfeAmbiente; }
    public void setDfeAmbiente(Integer dfeAmbiente) { this.dfeAmbiente = dfeAmbiente; }

    public Integer getDfeSerie() { return dfeSerie; }
    public void setDfeSerie(Integer dfeSerie) { this.dfeSerie = dfeSerie; }

    public Integer getDfeNumero() { return dfeNumero; }
    public void setDfeNumero(Integer dfeNumero) { this.dfeNumero = dfeNumero; }

    public String getDfeXml() { return dfeXml; }
    public void setDfeXml(String dfeXml) { this.dfeXml = dfeXml; }

    public Integer getDfeModelo() { return dfeModelo; }
    public void setDfeModelo(Integer dfeModelo) { this.dfeModelo = dfeModelo; }

    public String getDfeChave() { return dfeChave; }
    public void setDfeChave(String dfeChave) { this.dfeChave = dfeChave; }

    public String getDfeStatus() { return dfeStatus; }
    public void setDfeStatus(String dfeStatus) { this.dfeStatus = dfeStatus; }

    public String getDfeVersao() { return dfeVersao; }
    public void setDfeVersao(String dfeVersao) { this.dfeVersao = dfeVersao; }

    public String getDfeProtoco() { return dfeProtoco; }
    public void setDfeProtoco(String dfeProtoco) { this.dfeProtoco = dfeProtoco; }

    public String getDfeAutorizacao() { return dfeAutorizacao; }
    public void setDfeAutorizacao(String dfeAutorizacao) { this.dfeAutorizacao = dfeAutorizacao; }

    public LocalDateTime getDfeDhAutorizacao() { return dfeDhAutorizacao; }
    public void setDfeDhAutorizacao(LocalDateTime dfeDhAutorizacao) { this.dfeDhAutorizacao = dfeDhAutorizacao; }

    public Integer getDfeNumeroDevolucao() { return dfeNumeroDevolucao; }
    public void setDfeNumeroDevolucao(Integer dfeNumeroDevolucao) { this.dfeNumeroDevolucao = dfeNumeroDevolucao; }

    public String getDfeDevolucaoXml() { return dfeDevolucaoXml; }
    public void setDfeDevolucaoXml(String dfeDevolucaoXml) { this.dfeDevolucaoXml = dfeDevolucaoXml; }

    public String getDfeReferenciaChave() { return dfeReferenciaChave; }
    public void setDfeReferenciaChave(String dfeReferenciaChave) { this.dfeReferenciaChave = dfeReferenciaChave; }

    public NaturezaOperacao getNaturezaOperacao() { return naturezaOperacao; }
    public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
        this.naturezaOperacao = (naturezaOperacao != null) ? naturezaOperacao : new NaturezaOperacao();
    }

    public Participante getFornecedor() { return fornecedor; }
    public void setFornecedor(Participante fornecedor) {
        this.fornecedor = (fornecedor != null) ? fornecedor : new Participante();
    }

    public List<CompraItem> getLstCompraItem() { return lstCompraItem; }
    public void setLstCompraItem(List<CompraItem> lstCompraItem) {
        this.lstCompraItem = (lstCompraItem != null) ? lstCompraItem : new ArrayList<>();
    }

    public List<CompraParcela> getLstCompraParcela() { return lstCompraParcela; }
    public void setLstCompraParcela(List<CompraParcela> lstCompraParcela) {
        this.lstCompraParcela = (lstCompraParcela != null) ? lstCompraParcela : new ArrayList<>();
    }

    public List<Object> getLstMercadoria() { return lstMercadoria; }
    public void setLstMercadoria(List<Object> lstMercadoria) {
        this.lstMercadoria = (lstMercadoria != null) ? lstMercadoria : new ArrayList<>();
    }

    public Double getDescontoTotal() { return descontoTotal != null ? descontoTotal : 0.0; }
    public void setDescontoTotal(Double descontoTotal) { this.descontoTotal = descontoTotal; }

    // Regras de Negócio e Métodos do Delphi
    public String getStatus() {
        String status = "DIGITADO";
        if (acertadoPor != null && !acertadoPor.trim().isEmpty()) {
            status = "ACERTADO";
        }
        if (canceladoPor != null && !canceladoPor.trim().isEmpty()) {
            status = "CANCELADO";
        } else if (faturadoPor != null && !faturadoPor.trim().isEmpty()) {
            status = "FATURADO";
        }
        return status;
    }

    public boolean temItens() {
        return lstCompraItem != null && !lstCompraItem.isEmpty();
    }

    public Double calcularDescontoTotal() {
        double total = 0.0;
        if (lstCompraItem != null) {
            for (CompraItem item : lstCompraItem) {
                total += (item.getDesconto() != null ? item.getDesconto() : 0.0);
            }
        }
        return total;
    }

    public void totalizarImpostos() {
        setItens(lstCompraItem != null ? lstCompraItem.size() : 0);
        setQtdTotal(0.0);
        setValTotal(0.0);
        setValorIcms(0.0);
        setValorIpi(0.0);
        setValorCofins(0.0);
        setValorPis(0.0);

        if (lstCompraItem != null) {
            for (CompraItem item : lstCompraItem) {
                double qtd = item.getQuantidade() != null ? item.getQuantidade() : 0.0;
                double valUnit = item.getValorCompra() != null ? item.getValorCompra() : 0.0;
                double fator = (qtd > 0) ? 1.0 : 0.0;

                setValTotal(getValTotal() + (qtd * valUnit));
                setQtdTotal(getQtdTotal() + qtd);

                setValorIcms(getValorIcms() + ((item.getValorIcms() != null ? item.getValorIcms() : 0.0) * fator));
                setValorIpi(getValorIpi() + ((item.getValorIpi() != null ? item.getValorIpi() : 0.0) * fator));
                setValorCofins(getValorCofins() + ((item.getValorCofins() != null ? item.getValorCofins() : 0.0) * fator));
                setValorPis(getValorPis() + ((item.getValorPis() != null ? item.getValorPis() : 0.0) * fator));
            }
        }

        if (getValTotal() != 0.0) {
            setDesconto((calcularDescontoTotal() / getValTotal()) * 100.0);
        }

        valorComDescontoTotal();
    }

    public Double valorComDescontoTotal() {
        fvalorComDesc = getValTotal() - ((getValTotal() * getDesconto()) / 100.0);
        return fvalorComDesc;
    }

    public Double totalCompraParcela() {
        double total = 0.0;
        if (lstCompraParcela != null) {
            for (CompraParcela parcela : lstCompraParcela) {
                total += (parcela.getValor() != null ? parcela.getValor() : 0.0);
            }
        }
        return total;
    }

    public Double getValorRestante() {
        return valorComDescontoTotal() - totalCompraParcela();
    }

    public Double totalQtdDevolvida() {
        double total = 0.0;
        if (lstCompraItem != null) {
            for (CompraItem item : lstCompraItem) {
                total += (item.getQtdDevolvida() != null ? item.getQtdDevolvida() : 0.0);
            }
        }
        return total;
    }

    public boolean validar() throws Exception {
        if (fornecedor == null || fornecedor.getId() == null || fornecedor.getId() <= 0) {
            throw new Exception("Campo Fornecedor é Obrigatório!");
        }
        if (lstCompraItem == null || lstCompraItem.isEmpty()) {
            throw new Exception("A Compra deve ter pelo menos um produto!");
        }
        if (lstCompraParcela == null || lstCompraParcela.isEmpty()) {
            throw new Exception("A Compra deve ter pelo menos uma parcela!");
        }
        return true;
    }

    public void ocopy(Compra source) {
        if (source != null) {
            this.id = source.getId();
            this.digitado = source.getDigitado();
            this.digitadoPor = source.getDigitadoPor();
            this.itens = source.getItens();
            this.qtdTotal = source.getQtdTotal();
            this.valTotal = source.getValTotal();
            this.desconto = source.getDesconto();
            this.acrescimo = source.getAcrescimo();
            this.troco = source.getTroco();
            this.frete = source.getFrete();
            this.impresso = source.getImpresso();
            this.impressoPor = source.getImpressoPor();
            this.separado = source.getSeparado();
            this.separadoPor = source.getSeparadoPor();
            this.faturado = source.getFaturado();
            this.faturadoPor = source.getFaturadoPor();
            this.cancelado = source.getCancelado();
            this.canceladoPor = source.getCanceladoPor();
            this.canceladoMotivo = source.getCanceladoMotivo();
            this.clonado = source.getClonado();
            this.clonadoPor = source.getClonadoPor();
            this.clonadoId = source.getClonadoId();
            this.acertado = source.getAcertado();
            this.acertadoPor = source.getAcertadoPor();
            this.acertadoMotivo = source.getAcertadoMotivo();
            this.baseIcms = source.getBaseIcms();
            this.baseIcmsSt = source.getBaseIcmsSt();
            this.valorIcms = source.getValorIcms();
            this.valorIcmsSt = source.getValorIcmsSt();
            this.valorPis = source.getValorPis();
            this.valorCofins = source.getValorCofins();
            this.valorIpi = source.getValorIpi();
            this.dfeAmbiente = source.getDfeAmbiente();
            this.dfeSerie = source.getDfeSerie();
            this.dfeNumero = source.getDfeNumero();
            this.dfeXml = source.getDfeXml();
            this.dfeModelo = source.getDfeModelo();
            this.dfeChave = source.getDfeChave();
            this.dfeStatus = source.getDfeStatus();
            this.dfeVersao = source.getDfeVersao();
            this.dfeProtoco = source.getDfeProtoco();
            this.dfeAutorizacao = source.getDfeAutorizacao();
            this.dfeDhAutorizacao = source.getDfeDhAutorizacao();
            this.dfeNumeroDevolucao = source.getDfeNumeroDevolucao();
            this.dfeDevolucaoXml = source.getDfeDevolucaoXml();
            this.dfeReferenciaChave = source.getDfeReferenciaChave();

            if (source.getNaturezaOperacao() != null) {
                this.naturezaOperacao.ocopy(source.getNaturezaOperacao());
            }
            if (source.getFornecedor() != null) {
                this.fornecedor.ocopy(source.getFornecedor());
            }

            this.lstCompraItem.clear();
            if (source.getLstCompraItem() != null) {
                for (CompraItem item : source.getLstCompraItem()) {
                    this.lstCompraItem.add(item.clone());
                }
            }

            this.lstCompraParcela.clear();
            if (source.getLstCompraParcela() != null) {
                for (CompraParcela p : source.getLstCompraParcela()) {
                    this.lstCompraParcela.add(p.clone());
                }
            }
        }
    }

    public Compra clone() {
        Compra c = new Compra();
        c.ocopy(this);
        return c;
    }

    @Override
    public String toString() {
        return id != null ? String.valueOf(id) : "";
    }
}