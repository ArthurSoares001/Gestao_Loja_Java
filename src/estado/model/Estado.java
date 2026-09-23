package Estado.model;

import Pais.model.Pais;

public class Estado {
    private Integer id;
    private Integer cuf;
    private String nome;
    private boolean ativo = true;
    private String sigla;
    private Pais pais;

    public Estado() {
        this.pais = new Pais();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCuf() {
        return cuf;
    }

    public void setCuf(Integer cuf) {
        this.cuf = cuf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = (pais != null) ? pais : new Pais();
    }

    // Regras de validação idênticas a TEstado.validar
    public boolean validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
        if (sigla == null || sigla.trim().isEmpty()) {
            throw new Exception("Campo Sigla é Obrigatório!");
        }
        if (pais == null || pais.getId() == null || pais.getId() <= 0) {
            throw new Exception("Campo Pais é Obrigatório!");
        }
        return true;
    }

    public void ocopy(Estado source) {
        if (source != null) {
            this.id = source.getId();
            this.cuf = source.getCuf();
            this.nome = source.getNome();
            this.ativo = source.isAtivo();
            this.sigla = source.getSigla();
            this.pais = new Pais(source.getPais().getId(), source.getPais().getNome());
        }
    }

    @Override
    public Estado clone() {
        Estado clone = new Estado();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return sigla != null ? sigla : "";
    }
}
