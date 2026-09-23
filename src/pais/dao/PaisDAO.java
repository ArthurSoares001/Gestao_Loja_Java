package Pais.dao;

import Pais.model.Pais;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaisDAO {

    public void incluir(Pais pais) throws Exception {
        pais.validar();
        String sql = "INSERT INTO pais (cpais, nome, ativo) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, pais.getCPais(), Types.INTEGER);
            ps.setString(2, pais.getNome());
            ps.setBoolean(3, pais.isAtivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pais.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Pais pais) throws Exception {
        pais.validar();
        String sql = "UPDATE pais SET cpais = ?, nome = ?, ativo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, pais.getCPais(), Types.INTEGER);
            ps.setString(2, pais.getNome());
            ps.setBoolean(3, pais.isAtivo());
            ps.setInt(4, pais.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Alterado: " + e.getMessage());
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM pais WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído (possui vínculos com Estados): " + e.getMessage());
        }
    }

    public Pais encontrar(int id) throws Exception {
        String sql = "SELECT id, cpais, nome, ativo FROM pais WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pais p = new Pais();
                    p.setId(rs.getInt("id"));
                    p.setCPais(rs.getInt("cpais"));
                    p.setNome(rs.getString("nome"));
                    p.setAtivo(rs.getBoolean("ativo"));
                    return p;
                }
            }
        }
        return null;
    }

    public List<Pais> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Pais> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, cpais, nome, ativo FROM pais WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND cpais = ?"); break;
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
                } else if (tipoFiltro == 1 || tipoFiltro == 2) {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pais p = new Pais();
                    p.setId(rs.getInt("id"));
                    p.setCPais(rs.getInt("cpais"));
                    p.setNome(rs.getString("nome"));
                    p.setAtivo(rs.getBoolean("ativo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
