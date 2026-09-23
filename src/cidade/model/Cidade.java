package Cidade.model;

import Estado.model.Estado;

public class Cidade {
    private Integer id;
    private String nome;
    private boolean ativo = true;
    private Integer cep;
    private Integer codigoIBGE;
    private Estado estado;

    public Cidade() {
        this.estado = new Estado();
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Integer getCodigoIBGE() {
        return codigoIBGE;
    }

    public void setCodigoIBGE(Integer codigoIBGE) {
        this.codigoIBGE = codigoIBGE;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = (estado != null) ? estado : new Estado();
    }

    // Regra de validação equivalente ao TCidade.validar
    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
        if (codigoIBGE == null || codigoIBGE <= 0) {
            throw new Exception("Campo Código IBGE é Obrigatório!");
        }
        if (estado == null || estado.getId() == null || estado.getId() <= 0) {
            throw new Exception("Campo Estado é Obrigatório!");
        }
    }

    public void ocopy(Cidade source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.ativo = source.isAtivo();
            this.cep = source.getCep();
            this.codigoIBGE = source.getCodigoIBGE();
            Estado est = new Estado();
            est.setId(source.getEstado().getId());
            est.setNome(source.getEstado().getNome());
            est.setSigla(source.getEstado().getSigla());
            this.estado = est;
        }
    }

    @Override
    public Cidade clone() {
        Cidade clone = new Cidade();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        String sigla = (estado != null && estado.getSigla() != null) ? estado.getSigla() : "";
        return nome + " - " + sigla;
    }
}
