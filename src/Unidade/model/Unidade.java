package Unidade.model;

public class Unidade {
    private Integer id;
    private String nome;
    private String sigla;
    private boolean pesagem = false;
    private boolean ativo = true;

    public Unidade() {
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public boolean isPesagem() {
        return pesagem;
    }

    public void setPesagem(boolean pesagem) {
        this.pesagem = pesagem;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
        if (sigla == null || sigla.trim().isEmpty()) {
            throw new Exception("Campo Sigla é Obrigatório!");
        }
        if (sigla.trim().length() > 10) {
            throw new Exception("A Sigla deve conter no máximo 10 caracteres!");
        }
    }

    public void ocopy(Unidade source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.sigla = source.getSigla();
            this.pesagem = source.isPesagem();
            this.ativo = source.isAtivo();
        }
    }

    @Override
    public Unidade clone() {
        Unidade clone = new Unidade();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return sigla != null ? sigla + " - " + (nome != null ? nome : "") : (nome != null ? nome : "");
    }
}
