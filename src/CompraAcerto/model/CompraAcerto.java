package CompraAcerto.model;

import Compra.model.Compra;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraAcerto {

    private Integer id;
    private Compra compra;
    private Integer numeroAcerto;
    private LocalDateTime acertado;
    private String acertadoPor;
    private String acertadoMotivo;
    private Boolean gerarCredito = false;
    private Double valorCredito = 0.0;
    private Double valorTotalAcerto = 0.0;
    private Integer dfeNumeroDevolucao;
    private String dfeDevolucaoXml;
    private String dfeReferenciaChave;
    private List<CompraAcertoItem> lstAcertoItem;

    public CompraAcerto() {
        this.compra = new Compra();
        this.lstAcertoItem = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = (compra != null) ? compra : new Compra();
    }

    public Integer getNumeroAcerto() {
        return numeroAcerto;
    }

    public void setNumeroAcerto(Integer numeroAcerto) {
        this.numeroAcerto = numeroAcerto;
    }

    public LocalDateTime getAcertado() {
        return acertado;
    }

    public void setAcertado(LocalDateTime acertado) {
        this.acertado = acertado;
    }

    public String getAcertadoPor() {
        return acertadoPor;
    }

    public void setAcertadoPor(String acertadoPor) {
        this.acertadoPor = acertadoPor;
    }

    public String getAcertadoMotivo() {
        return acertadoMotivo;
    }

    public void setAcertadoMotivo(String acertadoMotivo) {
        this.acertadoMotivo = acertadoMotivo;
    }

    public Boolean getGerarCredito() {
        return gerarCredito != null ? gerarCredito : false;
    }

    public void setGerarCredito(Boolean gerarCredito) {
        this.gerarCredito = gerarCredito;
    }

    public Double getValorCredito() {
        return valorCredito != null ? valorCredito : 0.0;
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public Double getValorTotalAcerto() {
        return valorTotalAcerto != null ? valorTotalAcerto : 0.0;
    }

    public void setValorTotalAcerto(Double valorTotalAcerto) {
        this.valorTotalAcerto = valorTotalAcerto;
    }

    public Integer getDfeNumeroDevolucao() {
        return dfeNumeroDevolucao;
    }

    public void setDfeNumeroDevolucao(Integer dfeNumeroDevolucao) {
        this.dfeNumeroDevolucao = dfeNumeroDevolucao;
    }

    public String getDfeDevolucaoXml() {
        return dfeDevolucaoXml;
    }

    public void setDfeDevolucaoXml(String dfeDevolucaoXml) {
        this.dfeDevolucaoXml = dfeDevolucaoXml;
    }

    public String getDfeReferenciaChave() {
        return dfeReferenciaChave;
    }

    public void setDfeReferenciaChave(String dfeReferenciaChave) {
        this.dfeReferenciaChave = dfeReferenciaChave;
    }

    public List<CompraAcertoItem> getLstAcertoItem() {
        return lstAcertoItem;
    }

    public void setLstAcertoItem(List<CompraAcertoItem> lstAcertoItem) {
        this.lstAcertoItem = (lstAcertoItem != null) ? lstAcertoItem : new ArrayList<>();
    }

    public boolean temItens() {
        return lstAcertoItem != null && !lstAcertoItem.isEmpty();
    }

    public double calcularValorTotal() {
        double total = 0.0;
        if (lstAcertoItem != null) {
            for (CompraAcertoItem item : lstAcertoItem) {
                total += (item.getValorTotal() != null ? item.getValorTotal() : 0.0);
            }
        }
        this.valorTotalAcerto = total;
        return total;
    }

    public void validar() throws Exception {
        if (compra == null || compra.getId() == null || compra.getId() <= 0) {
            throw new Exception("Campo Compra é Obrigatório!");
        }
        if (numeroAcerto == null || numeroAcerto <= 0) {
            throw new Exception("Número do acerto inválido!");
        }
        if (acertadoMotivo == null || acertadoMotivo.trim().isEmpty()) {
            throw new Exception("Motivo do acerto é obrigatório!");
        }
        if (!temItens()) {
            throw new Exception("É necessário informar ao menos um item no acerto!");
        }
        if (valorTotalAcerto == null || valorTotalAcerto <= 0.0) {
            throw new Exception("Valor total do acerto deve ser maior que zero!");
        }
    }

    public void ocopy(CompraAcerto source) {
        if (source != null) {
            this.id = source.getId();
            if (source.getCompra() != null) {
                this.compra = source.getCompra().clone();
            }
            this.numeroAcerto = source.getNumeroAcerto();
            this.acertado = source.getAcertado();
            this.acertadoPor = source.getAcertadoPor();
            this.acertadoMotivo = source.getAcertadoMotivo();
            this.gerarCredito = source.getGerarCredito();
            this.valorCredito = source.getValorCredito();
            this.valorTotalAcerto = source.getValorTotalAcerto();
            this.dfeNumeroDevolucao = source.getDfeNumeroDevolucao();
            this.dfeDevolucaoXml = source.getDfeDevolucaoXml();
            this.dfeReferenciaChave = source.getDfeReferenciaChave();

            this.lstAcertoItem = new ArrayList<>();
            if (source.getLstAcertoItem() != null) {
                for (CompraAcertoItem item : source.getLstAcertoItem()) {
                    this.lstAcertoItem.add(item.clone());
                }
            }
        }
    }

    public CompraAcerto clone() {
        CompraAcerto clone = new CompraAcerto();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        int idCompra = (compra != null && compra.getId() != null) ? compra.getId() : 0;
        return String.format("Acerto #%d - Compra #%d - Valor: %.2f",
                numeroAcerto != null ? numeroAcerto : 0,
                idCompra,
                valorTotalAcerto != null ? valorTotalAcerto : 0.0);
    }
}
