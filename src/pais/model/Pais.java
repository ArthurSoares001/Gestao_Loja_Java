package pais.model;

public class Pais {
    private Integer id;
    private Integer cPais;
    private String nome;
    private boolean ativo = true;

    public Pais() {
    }

    public Pais(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCPais() {
        return cPais;
    }

    public void setCPais(Integer cPais) {
        this.cPais = cPais;
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

    // Regra de validação equivalente ao TPais.validar
    public boolean validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo nome é obrigatório!");
        }
        return true;
    }

    // Equivalente ao TPais.ocopy
    public void ocopy(Pais source) {
        if (source != null) {
            this.id = source.getId();
            this.cPais = source.getCPais();
            this.nome = source.getNome();
            this.ativo = source.isAtivo();
        }
    }

    @Override
    public Pais clone() {
        Pais clone = new Pais();
        clone.ocopy(this);
        return clone;
    }

    // Equivalente ao TPais.toString: getNome + '-' + IntToStr(getId)
    @Override
    public String toString() {
        return (nome != null ? nome : "") + (id != null ? " - " + id : "");
    }
}
