package NaturezaOperacao.model;

public class NaturezaOperacao {
    private Integer id;
    private String nome;
    private String fantasia;
    private String operacao = "P";
    private String cfop;
    private String cfopFora;
    private String tipo = "S";
    private boolean ativo = true;
    private String finalidade = "1";

    public NaturezaOperacao() {
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

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getCfopFora() {
        return cfopFora;
    }

    public void setCfopFora(String cfopFora) {
        this.cfopFora = cfopFora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
    }

    public void ocopy(NaturezaOperacao source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.fantasia = source.getFantasia();
            this.operacao = source.getOperacao();
            this.cfop = source.getCfop();
            this.cfopFora = source.getCfopFora();
            this.tipo = source.getTipo();
            this.ativo = source.isAtivo();
            this.finalidade = source.getFinalidade();
        }
    }

    @Override
    public NaturezaOperacao clone() {
        NaturezaOperacao clone = new NaturezaOperacao();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
