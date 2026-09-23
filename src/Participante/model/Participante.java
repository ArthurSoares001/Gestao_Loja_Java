package Participante.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Participante {
    private Integer id;
    private String nome;
    private String fantasia;
    private String cpfCnpj;
    private String rgIe;
    private String email;
    private String telefone1;
    private String telefone2;
    private String telefone3;
    private String observacao;
    private Integer estrelas;
    private String login;
    private String senha;
    private BigDecimal comissao;
    private String tipo = "CLI"; // Ex: CLI (Cliente), FOR (Fornecedor), FUN (Funcionário)
    private Integer idTabelaPreco;
    private boolean enviarWeb = true;
    private boolean autorizaLgpd = true;
    private boolean ativo = true;
    private LocalDate dataNascimento;

    public Participante() {
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRgIe() {
        return rgIe;
    }

    public void setRgIe(String rgIe) {
        this.rgIe = rgIe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(Integer estrelas) {
        this.estrelas = estrelas;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = (tipo != null) ? tipo.toUpperCase().trim() : "CLI";
    }

    public Integer getIdTabelaPreco() {
        return idTabelaPreco;
    }

    public void setIdTabelaPreco(Integer idTabelaPreco) {
        this.idTabelaPreco = idTabelaPreco;
    }

    public boolean isEnviarWeb() {
        return enviarWeb;
    }

    public void setEnviarWeb(boolean enviarWeb) {
        this.enviarWeb = enviarWeb;
    }

    public boolean isAutorizaLgpd() {
        return autorizaLgpd;
    }

    public void setAutorizaLgpd(boolean autorizaLgpd) {
        this.autorizaLgpd = autorizaLgpd;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void validar() throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Campo Nome é Obrigatório!");
        }
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
            throw new Exception("Campo CPF/CNPJ é Obrigatório!");
        }
    }

    public void ocopy(Participante source) {
        if (source != null) {
            this.id = source.getId();
            this.nome = source.getNome();
            this.fantasia = source.getFantasia();
            this.cpfCnpj = source.getCpfCnpj();
            this.rgIe = source.getRgIe();
            this.email = source.getEmail();
            this.telefone1 = source.getTelefone1();
            this.telefone2 = source.getTelefone2();
            this.telefone3 = source.getTelefone3();
            this.observacao = source.getObservacao();
            this.estrelas = source.getEstrelas();
            this.login = source.getLogin();
            this.senha = source.getSenha();
            this.comissao = source.getComissao();
            this.tipo = source.getTipo();
            this.idTabelaPreco = source.getIdTabelaPreco();
            this.enviarWeb = source.isEnviarWeb();
            this.autorizaLgpd = source.isAutorizaLgpd();
            this.ativo = source.isAtivo();
            this.dataNascimento = source.getDataNascimento();
        }
    }

    @Override
    public Participante clone() {
        Participante clone = new Participante();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
