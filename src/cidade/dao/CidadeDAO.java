package cidade.dao;

import cidade.model.Cidade;
import estado.model.Estado;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {

    public void incluir(Cidade cidade) throws Exception {
        cidade.validar();
        String sql = "INSERT INTO cidade (nome, cep, codigo_ibge, ativo, id_estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cidade.getNome());
            ps.setObject(2, cidade.getCep(), Types.INTEGER);
            ps.setObject(3, cidade.getCodigoIBGE(), Types.INTEGER);
            ps.setBoolean(4, cidade.isAtivo());
            ps.setInt(5, cidade.getEstado().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cidade.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Cidade cidade) throws Exception {
        cidade.validar();
        String sql = "UPDATE cidade SET nome = ?, cep = ?, codigo_ibge = ?, ativo = ?, id_estado = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cidade.getNome());
            ps.setObject(2, cidade.getCep(), Types.INTEGER);
            ps.setObject(3, cidade.getCodigoIBGE(), Types.INTEGER);
            ps.setBoolean(4, cidade.isAtivo());
            ps.setInt(5, cidade.getEstado().getId());
            ps.setInt(6, cidade.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM cidade WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Cidade encontrar(int id) throws Exception {
        String sql = "SELECT c.id, c.nome, c.cep, c.codigo_ibge, c.ativo, c.id_estado, e.nome AS estado, e.sigla " +
                "FROM cidade c INNER JOIN estado e ON c.id_estado = e.id WHERE c.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cidade c = new Cidade();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setCep(rs.getInt("cep"));
                    c.setCodigoIBGE(rs.getInt("codigo_ibge"));
                    c.setAtivo(rs.getBoolean("ativo"));
                    Estado est = new Estado();
                    est.setId(rs.getInt("id_estado"));
                    est.setNome(rs.getString("estado"));
                    est.setSigla(rs.getString("sigla"));
                    c.setEstado(est);
                    return c;
                }
            }
        }
        return null;
    }

    public List<Cidade> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Cidade> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.id, c.nome, c.cep, c.codigo_ibge, c.ativo, c.id_estado, e.nome AS estado, e.sigla " +
                        "FROM cidade c INNER JOIN estado e ON c.id_estado = e.id WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND c.nome ILIKE ?"); break;
                case 1: sql.append(" AND c.id = ?"); break;
                case 2: sql.append(" AND c.codigo_ibge = ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY c.id DESC");
        } else {
            sql.append(" ORDER BY c.nome");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                if (tipoFiltro == 0) {
                    ps.setString(1, filtro + "%");
                } else {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cidade c = new Cidade();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setCep(rs.getInt("cep"));
                    c.setCodigoIBGE(rs.getInt("codigo_ibge"));
                    c.setAtivo(rs.getBoolean("ativo"));
                    Estado est = new Estado();
                    est.setId(rs.getInt("id_estado"));
                    est.setNome(rs.getString("estado"));
                    est.setSigla(rs.getString("sigla"));
                    c.setEstado(est);
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public List<Estado> recuperarEstados() {
        // Retorna a lista para o ComboBox
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT id, nome, sigla FROM estado ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Estado est = new Estado();
                est.setId(rs.getInt("id"));
                est.setNome(rs.getString("nome"));
                est.setSigla(rs.getString("sigla"));
                estados.add(est);
            }
        } catch (Exception ignored) {}
        return estados;
    }
}