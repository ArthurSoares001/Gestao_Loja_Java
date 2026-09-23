package Unidade.dao;

import Unidade.model.Unidade;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAO {

    public void incluir(Unidade unidade) throws Exception {
        unidade.validar();
        String sql = "INSERT INTO unidade (nome, sigla, pesagem, ativo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getSigla().toUpperCase().trim());
            ps.setBoolean(3, unidade.isPesagem());
            ps.setBoolean(4, unidade.isAtivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    unidade.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Unidade unidade) throws Exception {
        unidade.validar();
        String sql = "UPDATE unidade SET nome = ?, sigla = ?, pesagem = ?, ativo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getSigla().toUpperCase().trim());
            ps.setBoolean(3, unidade.isPesagem());
            ps.setBoolean(4, unidade.isAtivo());
            ps.setInt(5, unidade.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM unidade WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Unidade encontrar(int id) throws Exception {
        String sql = "SELECT id, nome, sigla, pesagem, ativo FROM unidade WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Unidade u = new Unidade();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setSigla(rs.getString("sigla"));
                    u.setPesagem(rs.getBoolean("pesagem"));
                    u.setAtivo(rs.getBoolean("ativo"));
                    return u;
                }
            }
        }
        return null;
    }

    public List<Unidade> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Unidade> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nome, sigla, pesagem, ativo FROM unidade WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND sigla ILIKE ?"); break;
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
                    Unidade u = new Unidade();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setSigla(rs.getString("sigla"));
                    u.setPesagem(rs.getBoolean("pesagem"));
                    u.setAtivo(rs.getBoolean("ativo"));
                    lista.add(u);
                }
            }
        }
        return lista;
    }
}
