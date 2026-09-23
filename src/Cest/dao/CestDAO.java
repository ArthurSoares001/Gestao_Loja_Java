package Cest.dao;

import Cest.model.Cest;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CestDAO {

    public void incluir(Cest cest) throws Exception {
        cest.validar();
        String sql = "INSERT INTO cest (codigo, ncm, descricao) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cest.getCodigo());
            ps.setString(2, cest.getNcm());
            ps.setString(3, cest.getDescricao());
            ps.executeUpdate();
        }
    }

    public void alterar(Cest cest) throws Exception {
        cest.validar();
        String sql = "UPDATE cest SET ncm = ?, descricao = ? WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cest.getNcm());
            ps.setString(2, cest.getDescricao());
            ps.setString(3, cest.getCodigo());
            ps.executeUpdate();
        }
    }

    public void excluir(String codigo) throws Exception {
        String sql = "DELETE FROM cest WHERE codigo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Cest encontrar(String codigo) throws Exception {
        String sql = "SELECT codigo, ncm, descricao FROM cest WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cest c = new Cest();
                    c.setCodigo(rs.getString("codigo"));
                    c.setNcm(rs.getString("ncm"));
                    c.setDescricao(rs.getString("descricao"));
                    return c;
                }
            }
        }
        return null;
    }

    public List<Cest> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Cest> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT codigo, ncm, descricao FROM cest WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND codigo ILIKE ?"); break;
                case 1: sql.append(" AND descricao ILIKE ?"); break;
                case 2: sql.append(" AND ncm ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY codigo");
        } else {
            sql.append(" ORDER BY descricao");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                ps.setString(1, "%" + filtro.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cest c = new Cest();
                    c.setCodigo(rs.getString("codigo"));
                    c.setNcm(rs.getString("ncm"));
                    c.setDescricao(rs.getString("descricao"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }
}
