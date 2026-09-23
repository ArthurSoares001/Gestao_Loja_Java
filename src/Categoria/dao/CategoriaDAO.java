package Categoria.dao;

import Categoria.model.Categoria;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void incluir(Categoria categoria) throws Exception {
        categoria.validar();
        String sql = "INSERT INTO categoria (nome, enviar_web, is_participante, is_mercadoria, is_conta, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNome());
            ps.setBoolean(2, categoria.isEnviarWeb());
            ps.setBoolean(3, categoria.isParticipante());
            ps.setBoolean(4, categoria.isMercadoria());
            ps.setBoolean(5, categoria.isConta());
            ps.setBoolean(6, categoria.isAtivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Categoria categoria) throws Exception {
        categoria.validar();
        String sql = "UPDATE categoria SET nome = ?, enviar_web = ?, is_participante = ?, is_mercadoria = ?, " +
                "is_conta = ?, ativo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNome());
            ps.setBoolean(2, categoria.isEnviarWeb());
            ps.setBoolean(3, categoria.isParticipante());
            ps.setBoolean(4, categoria.isMercadoria());
            ps.setBoolean(5, categoria.isConta());
            ps.setBoolean(6, categoria.isAtivo());
            ps.setInt(7, categoria.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Categoria encontrar(int id) throws Exception {
        String sql = "SELECT id, nome, enviar_web, is_participante, is_mercadoria, is_conta, ativo " +
                "FROM categoria WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(rs.getInt("id"));
                    cat.setNome(rs.getString("nome"));
                    cat.setEnviarWeb(rs.getBoolean("enviar_web"));
                    cat.setParticipante(rs.getBoolean("is_participante"));
                    cat.setMercadoria(rs.getBoolean("is_mercadoria"));
                    cat.setConta(rs.getBoolean("is_conta"));
                    cat.setAtivo(rs.getBoolean("ativo"));
                    return cat;
                }
            }
        }
        return null;
    }

    public List<Categoria> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Categoria> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, nome, enviar_web, is_participante, is_mercadoria, is_conta, ativo " +
                        "FROM categoria WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
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
                if (tipoFiltro == 0) {
                    ps.setString(1, filtro + "%");
                } else {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(rs.getInt("id"));
                    cat.setNome(rs.getString("nome"));
                    cat.setEnviarWeb(rs.getBoolean("enviar_web"));
                    cat.setParticipante(rs.getBoolean("is_participante"));
                    cat.setMercadoria(rs.getBoolean("is_mercadoria"));
                    cat.setConta(rs.getBoolean("is_conta"));
                    cat.setAtivo(rs.getBoolean("ativo"));
                    lista.add(cat);
                }
            }
        }
        return lista;
    }
}
