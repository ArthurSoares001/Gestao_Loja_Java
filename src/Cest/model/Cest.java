package Cest.model;

public class Cest {
    private String codigo;
    private String ncm;
    private String descricao;

    public Cest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void validar() throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("Campo Código CEST é Obrigatório!");
        }
        if (codigo.trim().length() > 7) {
            throw new Exception("O Código CEST deve ter no máximo 7 caracteres!");
        }
        if (ncm != null && ncm.trim().length() > 8) {
            throw new Exception("O NCM deve ter no máximo 8 caracteres!");
        }
    }

    public void ocopy(Cest source) {
        if (source != null) {
            this.codigo = source.getCodigo();
            this.ncm = source.getNcm();
            this.descricao = source.getDescricao();
        }
    }

    @Override
    public Cest clone() {
        Cest clone = new Cest();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return codigo != null ? codigo + " - " + (descricao != null ? descricao : "") : "";
    }
}
