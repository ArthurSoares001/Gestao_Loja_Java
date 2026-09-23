package Cfop.dao;

import Cfop.model.Cfop;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CfopDAO {

    public void incluir(Cfop cfop) throws Exception {
        cfop.validar();
        String sql = "INSERT INTO cfop (codigo, descricao, descricao_completa, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cfop.getCodigo());
            ps.setString(2, cfop.getDescricao());
            ps.setString(3, cfop.getDescricaoCompleta());
            ps.setString(4, cfop.getTipo());
            ps.executeUpdate();
        }
    }

    public void alterar(Cfop cfop) throws Exception {
        cfop.validar();
        String sql = "UPDATE cfop SET descricao = ?, descricao_completa = ?, tipo = ? WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cfop.getDescricao());
            ps.setString(2, cfop.getDescricaoCompleta());
            ps.setString(3, cfop.getTipo());
            ps.setString(4, cfop.getCodigo());
            ps.executeUpdate();
        }
    }

    public void excluir(String codigo) throws Exception {
        String sql = "DELETE FROM cfop WHERE codigo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Cfop encontrar(String codigo) throws Exception {
        String sql = "SELECT codigo, descricao, descricao_completa, tipo FROM cfop WHERE codigo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cfop c = new Cfop();
                    c.setCodigo(rs.getString("codigo"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setDescricaoCompleta(rs.getString("descricao_completa"));
                    c.setTipo(rs.getString("tipo"));
                    return c;
                }
            }
        }
        return null;
    }

    public List<Cfop> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Cfop> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT codigo, descricao, descricao_completa, tipo FROM cfop WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND codigo ILIKE ?"); break;
                case 1: sql.append(" AND descricao ILIKE ?"); break;
                case 2: sql.append(" AND tipo = ?"); break;
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
                if (tipoFiltro == 2) {
                    ps.setString(1, filtro.trim().toUpperCase());
                } else {
                    ps.setString(1, "%" + filtro.trim() + "%");
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cfop c = new Cfop();
                    c.setCodigo(rs.getString("codigo"));
                    c.setDescricao(rs.getString("descricao"));
                    c.setDescricaoCompleta(rs.getString("descricao_completa"));
                    c.setTipo(rs.getString("tipo"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }
}
