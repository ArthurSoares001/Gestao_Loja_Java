package Cfop.model;

public class Cfop {
    private String codigo;
    private String descricao;
    private String descricaoCompleta;
    private String tipo = "S"; // Padrão 'S' (Saída), aceita 'E' (Entrada) ou 'S'

    public Cfop() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoCompleta() {
        return descricaoCompleta;
    }

    public void setDescricaoCompleta(String descricaoCompleta) {
        this.descricaoCompleta = descricaoCompleta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = (tipo != null) ? tipo.toUpperCase().trim() : "S";
    }

    public void validar() throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("Campo Código CFOP é Obrigatório!");
        }
        if (codigo.trim().length() != 4) {
            throw new Exception("O Código CFOP deve conter exatamente 4 caracteres!");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new Exception("Campo Descrição é Obrigatório!");
        }
        if (tipo == null || (!tipo.equals("E") && !tipo.equals("S"))) {
            throw new Exception("O Tipo do CFOP deve ser 'E' (Entrada) ou 'S' (Saída)!");
        }
    }

    public void ocopy(Cfop source) {
        if (source != null) {
            this.codigo = source.getCodigo();
            this.descricao = source.getDescricao();
            this.descricaoCompleta = source.getDescricaoCompleta();
            this.tipo = source.getTipo();
        }
    }

    @Override
    public Cfop clone() {
        Cfop clone = new Cfop();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return codigo != null ? codigo + " - " + (descricao != null ? descricao : "") : "";
    }
}
