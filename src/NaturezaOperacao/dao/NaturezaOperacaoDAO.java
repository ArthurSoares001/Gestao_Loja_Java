package NaturezaOperacao.dao;

import NaturezaOperacao.model.NaturezaOperacao;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NaturezaOperacaoDAO {

    public void incluir(NaturezaOperacao natureza) throws Exception {
        natureza.validar();
        String sql = "INSERT INTO natureza_operacao (nome, fantasia, operacao, cfop, cfop_fora, tipo, ativo, finalidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, natureza.getNome());
            ps.setString(2, natureza.getFantasia());
            ps.setString(3, natureza.getOperacao());
            ps.setString(4, natureza.getCfop());
            ps.setString(5, natureza.getCfopFora());
            ps.setString(6, natureza.getTipo());
            ps.setBoolean(7, natureza.isAtivo());
            ps.setString(8, natureza.getFinalidade());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    natureza.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(NaturezaOperacao natureza) throws Exception {
        natureza.validar();
        String sql = "UPDATE natureza_operacao SET nome = ?, fantasia = ?, operacao = ?, cfop = ?, " +
                "cfop_fora = ?, tipo = ?, ativo = ?, finalidade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, natureza.getNome());
            ps.setString(2, natureza.getFantasia());
            ps.setString(3, natureza.getOperacao());
            ps.setString(4, natureza.getCfop());
            ps.setString(5, natureza.getCfopFora());
            ps.setString(6, natureza.getTipo());
            ps.setBoolean(7, natureza.isAtivo());
            ps.setString(8, natureza.getFinalidade());
            ps.setInt(9, natureza.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM natureza_operacao WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public NaturezaOperacao encontrar(int id) throws Exception {
        String sql = "SELECT id, nome, fantasia, operacao, cfop, cfop_fora, tipo, ativo, finalidade " +
                "FROM natureza_operacao WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NaturezaOperacao n = new NaturezaOperacao();
                    n.setId(rs.getInt("id"));
                    n.setNome(rs.getString("nome"));
                    n.setFantasia(rs.getString("fantasia"));
                    n.setOperacao(rs.getString("operacao"));
                    n.setCfop(rs.getString("cfop"));
                    n.setCfopFora(rs.getString("cfop_fora"));
                    n.setTipo(rs.getString("tipo"));
                    n.setAtivo(rs.getBoolean("ativo"));
                    n.setFinalidade(rs.getString("finalidade"));
                    return n;
                }
            }
        }
        return null;
    }

    public List<NaturezaOperacao> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<NaturezaOperacao> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, nome, fantasia, operacao, cfop, cfop_fora, tipo, ativo, finalidade " +
                        "FROM natureza_operacao WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND (cfop ILIKE ? OR cfop_fora ILIKE ?)"); break;
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
                } else if (tipoFiltro == 2) {
                    ps.setString(1, "%" + filtro.trim() + "%");
                    ps.setString(2, "%" + filtro.trim() + "%");
                } else {
                    ps.setString(1, "%" + filtro.trim() + "%");
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NaturezaOperacao n = new NaturezaOperacao();
                    n.setId(rs.getInt("id"));
                    n.setNome(rs.getString("nome"));
                    n.setFantasia(rs.getString("fantasia"));
                    n.setOperacao(rs.getString("operacao"));
                    n.setCfop(rs.getString("cfop"));
                    n.setCfopFora(rs.getString("cfop_fora"));
                    n.setTipo(rs.getString("tipo"));
                    n.setAtivo(rs.getBoolean("ativo"));
                    n.setFinalidade(rs.getString("finalidade"));
                    lista.add(n);
                }
            }
        }
        return lista;
    }
}
