package Bandeiratef.model;


import java.util.ArrayList;
import java.util.List;

public class BandeiraTef {
    private Integer id;
    private String nome;
    private String cnpj;
    private boolean ativo = true;
    private String enumerar;
    private List<BandeiraTefTaxa> lstTaxas;

    public BandeiraTef() {
        this.ativo = true;
        this.lstTaxas = new ArrayList<>();
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getEnumerar() {
        return enumerar;
    }

    public void setEnumerar(String enumerar) {
        this.enumerar = enumerar;
    }

    public List<BandeiraTefTaxa> getLstTaxas() {
        return lstTaxas;
    }

    public void setLstTaxas(List<BandeiraTefTaxa> lstTaxas) {
        this.lstTaxas = (lstTaxas != null) ? lstTaxas : new ArrayList<>();
    }

    // Validação equivalente a TBandeiraTef.validar
    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
    }

    public void ocopy(BandeiraTef source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.cnpj = source.getCnpj();
            this.ativo = source.isAtivo();
            this.enumerar = source.getEnumerar();

            this.lstTaxas = new ArrayList<>();
            if (source.getLstTaxas() != null) {
                for (BandeiraTefTaxa taxa : source.getLstTaxas()) {
                    this.lstTaxas.add(taxa.clone());
                }
            }
        }
    }

    @Override
    public BandeiraTef clone() {
        BandeiraTef clone = new BandeiraTef();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return (nome != null) ? nome : "";
    }
}
