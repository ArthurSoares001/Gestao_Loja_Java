package CompraAcerto.model;

import CompraItem.model.CompraItem;

public class CompraAcertoItem {

    private Integer id;
    private Integer idCompraAcerto;
    private Integer idMercadoria;
    private CompraItem compraItem;
    private Double quantidade = 0.0;
    private Double valorUnitario = 0.0;
    private Double valorTotal = 0.0;
    private String motivo;

    public CompraAcertoItem() {
        this.compraItem = new CompraItem();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCompraAcerto() { return idCompraAcerto; }
    public void setIdCompraAcerto(Integer idCompraAcerto) { this.idCompraAcerto = idCompraAcerto; }

    public Integer getIdMercadoria() { return idMercadoria; }
    public void setIdMercadoria(Integer idMercadoria) { this.idMercadoria = idMercadoria; }

    public CompraItem getCompraItem() { return compraItem; }
    public void setCompraItem(CompraItem compraItem) {
        this.compraItem = (compraItem != null) ? compraItem : new CompraItem();
    }

    public Double getQuantidade() { return quantidade != null ? quantidade : 0.0; }
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = (this.quantidade != null ? this.quantidade : 0.0) * (this.valorUnitario != null ? this.valorUnitario : 0.0);
    }

    public Double getValorUnitario() { return valorUnitario != null ? valorUnitario : 0.0; }
    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
        this.valorTotal = (this.quantidade != null ? this.quantidade : 0.0) * (this.valorUnitario != null ? this.valorUnitario : 0.0);
    }

    public Double getValorTotal() { return valorTotal != null ? valorTotal : 0.0; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public void ocopy(CompraAcertoItem source) {
        if (source != null) {
            this.id = source.getId();
            this.idCompraAcerto = source.getIdCompraAcerto();
            this.idMercadoria = source.getIdMercadoria();
            if (source.getCompraItem() != null) {
                this.compraItem.ocopy(source.getCompraItem());
            }
            this.quantidade = source.getQuantidade();
            this.valorUnitario = source.getValorUnitario();
            this.valorTotal = source.getValorTotal();
            this.motivo = source.getMotivo();
        }
    }

    public CompraAcertoItem clone() {
        CompraAcertoItem clone = new CompraAcertoItem();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return id != null ? String.valueOf(id) : "";
    }
}
