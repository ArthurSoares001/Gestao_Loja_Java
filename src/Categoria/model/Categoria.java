package Categoria.model;

public class Categoria {
    private Integer id;
    private String nome;
    private boolean enviarWeb = true;
    private boolean participante = false;
    private boolean mercadoria = false;
    private boolean conta = false;
    private boolean ativo = true;

    public Categoria() {
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

    public boolean isEnviarWeb() {
        return enviarWeb;
    }

    public void setEnviarWeb(boolean enviarWeb) {
        this.enviarWeb = enviarWeb;
    }

    public boolean isParticipante() {
        return participante;
    }

    public void setParticipante(boolean participante) {
        this.participante = participante;
    }

    public boolean isMercadoria() {
        return mercadoria;
    }

    public void setMercadoria(boolean mercadoria) {
        this.mercadoria = mercadoria;
    }

    public boolean isConta() {
        return conta;
    }

    public void setConta(boolean conta) {
        this.conta = conta;
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
    }

    public void ocopy(Categoria source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.enviarWeb = source.isEnviarWeb();
            this.participante = source.isParticipante();
            this.mercadoria = source.isMercadoria();
            this.conta = source.isConta();
            this.ativo = source.isAtivo();
        }
    }

    @Override
    public Categoria clone() {
        Categoria clone = new Categoria();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
