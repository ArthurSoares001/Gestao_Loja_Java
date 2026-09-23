package Estado.dao;

import Estado.model.Estado;
import Pais.model.Pais;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {

    public Estado encontrar(int id) throws Exception {
        String sql = "SELECT id, cuf, nome, ativo, sigla, id_pais FROM estado WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estado e = new Estado();
                    e.setId(rs.getInt("id"));
                    e.setCuf(rs.getInt("cuf"));
                    e.setNome(rs.getString("nome"));
                    e.setAtivo(rs.getBoolean("ativo"));
                    e.setSigla(rs.getString("sigla"));
                    e.getPais().setId(rs.getInt("id_pais"));
                    return e;
                }
            }
        }
        return null;
    }

    public void incluir(Estado estado) throws Exception {
        estado.validar();
        String sql = "INSERT INTO estado (cuf, nome, ativo, sigla, id_pais) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, estado.getCuf(), Types.INTEGER);
            ps.setString(2, estado.getNome());
            ps.setBoolean(3, estado.isAtivo());
            ps.setString(4, estado.getSigla());
            ps.setInt(5, estado.getPais().getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    estado.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Estado estado) throws Exception {
        estado.validar();
        String sql = "UPDATE estado SET cuf = ?, nome = ?, ativo = ?, sigla = ?, id_pais = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, estado.getCuf(), Types.INTEGER);
            ps.setString(2, estado.getNome());
            ps.setBoolean(3, estado.isAtivo());
            ps.setString(4, estado.getSigla());
            ps.setInt(5, estado.getPais().getId());
            ps.setInt(6, estado.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Alterado: " + e.getMessage());
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM estado WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído (possui vínculos com Cidades): " + e.getMessage());
        }
    }

    public List<Estado> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Estado> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT e.id, e.cuf, e.nome, e.ativo, e.sigla, e.id_pais, p.nome as pais_nome FROM estado e LEFT JOIN pais p ON e.id_pais = p.id WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND e.nome ILIKE ?"); break;
                case 1: sql.append(" AND e.id = ?"); break;
                case 2: sql.append(" AND e.sigla ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY e.id DESC");
        } else {
            sql.append(" ORDER BY e.nome");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                if (tipoFiltro == 0 || tipoFiltro == 2) {
                    ps.setString(1, filtro + "%");
                } else if (tipoFiltro == 1) {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estado e = new Estado();
                    e.setId(rs.getInt("id"));
                    e.setCuf(rs.getInt("cuf"));
                    e.setNome(rs.getString("nome"));
                    e.setAtivo(rs.getBoolean("ativo"));
                    e.setSigla(rs.getString("sigla"));
                    e.getPais().setId(rs.getInt("id_pais"));
                    e.getPais().setNome(rs.getString("pais_nome"));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    public List<Pais> recuperarPaises() throws Exception {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT id, cpais, nome, ativo FROM pais WHERE ativo = true ORDER BY nome";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pais p = new Pais();
                p.setId(rs.getInt("id"));
                p.setCPais(rs.getInt("cpais"));
                p.setNome(rs.getString("nome"));
                p.setAtivo(rs.getBoolean("ativo"));
                lista.add(p);
            }
        }
        return lista;
    }
}
