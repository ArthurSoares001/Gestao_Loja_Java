package Participante.dao;


import Participante.model.Participante;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteDAO {

    public void incluir(Participante participante) throws Exception {
        participante.validar();
        String sql = "INSERT INTO participante (nome, fantasia, cpf_cnpj, rg_ie, email, telefone1, telefone2, " +
                "telefone3, observacao, estrelas, login, senha, comissao, tipo, id_tabela_preco, " +
                "enviar_web, autoriza_lgpd, ativo, data_nascimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(ps, participante);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    participante.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Participante participante) throws Exception {
        participante.validar();
        String sql = "UPDATE participante SET nome = ?, fantasia = ?, cpf_cnpj = ?, rg_ie = ?, email = ?, " +
                "telefone1 = ?, telefone2 = ?, telefone3 = ?, observacao = ?, estrelas = ?, login = ?, " +
                "senha = ?, comissao = ?, tipo = ?, id_tabela_preco = ?, enviar_web = ?, autoriza_lgpd = ?, " +
                "ativo = ?, data_nascimento = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherParametros(ps, participante);
            ps.setInt(20, participante.getId());
            ps.executeUpdate();
        }
    }

    private void preencherParametros(PreparedStatement ps, Participante p) throws SQLException {
        ps.setString(1, p.getNome());
        ps.setString(2, p.getFantasia());
        ps.setString(3, p.getCpfCnpj());
        ps.setString(4, p.getRgIe());
        ps.setString(5, p.getEmail());
        ps.setString(6, p.getTelefone1());
        ps.setString(7, p.getTelefone2());
        ps.setString(8, p.getTelefone3());
        ps.setString(9, p.getObservacao());
        ps.setObject(10, p.getEstrelas(), Types.INTEGER);
        ps.setString(11, p.getLogin());
        ps.setString(12, p.getSenha());
        ps.setBigDecimal(13, p.getComissao());
        ps.setString(14, p.getTipo());
        ps.setObject(15, p.getIdTabelaPreco(), Types.INTEGER);
        ps.setBoolean(16, p.isEnviarWeb());
        ps.setBoolean(17, p.isAutorizaLgpd());
        ps.setBoolean(18, p.isAtivo());
        ps.setDate(19, p.getDataNascimento() != null ? Date.valueOf(p.getDataNascimento()) : null);
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM participante WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Participante encontrar(int id) throws Exception {
        String sql = "SELECT * FROM participante WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Participante> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Participante> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM participante WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND cpf_cnpj ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY id DESC");
        } else {
            sql.append(" ORDER BY nome");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                if (tipoFiltro == 1) {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                } else {
                    ps.setString(1, "%" + filtro.trim() + "%");
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSet(rs));
                }
            }
        }
        return lista;
    }

    private Participante mapearResultSet(ResultSet rs) throws SQLException {
        Participante p = new Participante();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setFantasia(rs.getString("fantasia"));
        p.setCpfCnpj(rs.getString("cpf_cnpj"));
        p.setRgIe(rs.getString("rg_ie"));
        p.setEmail(rs.getString("email"));
        p.setTelefone1(rs.getString("telefone1"));
        p.setTelefone2(rs.getString("telefone2"));
        p.setTelefone3(rs.getString("telefone3"));
        p.setObservacao(rs.getString("observacao"));
        p.setEstrelas(rs.getObject("estrelas") != null ? rs.getInt("estrelas") : null);
        p.setLogin(rs.getString("login"));
        p.setSenha(rs.getString("senha"));
        p.setComissao(rs.getBigDecimal("comissao"));
        p.setTipo(rs.getString("tipo"));
        p.setIdTabelaPreco(rs.getObject("id_tabela_preco") != null ? rs.getInt("id_tabela_preco") : null);
        p.setEnviarWeb(rs.getBoolean("enviar_web"));
        p.setAutorizaLgpd(rs.getBoolean("autoriza_lgpd"));
        p.setAtivo(rs.getBoolean("ativo"));
        Date dt = rs.getDate("data_nascimento");
        if (dt != null) {
            p.setDataNascimento(dt.toLocalDate());
        }
        return p;
    }
}
