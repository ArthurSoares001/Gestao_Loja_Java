package Bandeiratef.dao;

import Bandeiratef.model.BandeiraTef;
import Bandeiratef.model.BandeiraTefTaxa;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BandeiraTefDAO {

    public void incluir(BandeiraTef bandeira) throws Exception {
        bandeira.validar();
        String sql = "INSERT INTO bandeira_tef (nome, cnpj, ativo, enumerar) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, bandeira.getNome());
                    ps.setString(2, bandeira.getCnpj());
                    ps.setBoolean(3, bandeira.isAtivo());
                    ps.setString(4, bandeira.getEnumerar());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            bandeira.setId(rs.getInt(1));
                        }
                    }
                }

                salvarTaxas(conn, bandeira);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void alterar(BandeiraTef bandeira) throws Exception {
        bandeira.validar();
        String sql = "UPDATE bandeira_tef SET nome = ?, cnpj = ?, ativo = ?, enumerar = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, bandeira.getNome());
                    ps.setString(2, bandeira.getCnpj());
                    ps.setBoolean(3, bandeira.isAtivo());
                    ps.setString(4, bandeira.getEnumerar());
                    ps.setInt(5, bandeira.getId());
                    ps.executeUpdate();
                }

                salvarTaxas(conn, bandeira);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void salvarTaxas(Connection conn, BandeiraTef bandeira) throws Exception {
        // Remove as taxas antigas para recadastrar a lista atualizada
        String sqlDelete = "DELETE FROM bandeira_tef_taxa WHERE id_bandeira_tef = ?";
        try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
            psDel.setInt(1, bandeira.getId());
            psDel.executeUpdate();
        }

        if (bandeira.getLstTaxas() != null && !bandeira.getLstTaxas().isEmpty()) {
            String sqlInsert = "INSERT INTO bandeira_tef_taxa " +
                    "(id_bandeira_tef, tp_integrado, parcela_min, parcela_max, taxa_percentual, prazo_repasse_dias, ativo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement psIns = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                for (BandeiraTefTaxa taxa : bandeira.getLstTaxas()) {
                    taxa.validar();
                    psIns.setInt(1, bandeira.getId());
                    psIns.setInt(2, taxa.getTpIntegrado());
                    psIns.setInt(3, taxa.getParcelaMin());
                    psIns.setInt(4, taxa.getParcelaMax());
                    psIns.setDouble(5, taxa.getTaxaPercentual());
                    psIns.setInt(6, taxa.getPrazoRepasseDias());
                    psIns.setBoolean(7, taxa.isAtivo());
                    psIns.executeUpdate();

                    try (ResultSet rs = psIns.getGeneratedKeys()) {
                        if (rs.next()) {
                            taxa.setId(rs.getInt(1));
                        }
                    }
                }
            }
        }
    }

    public void excluir(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Remove as taxas associadas primeiro
                try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM bandeira_tef_taxa WHERE id_bandeira_tef = ?")) {
                    ps1.setInt(1, id);
                    ps1.executeUpdate();
                }

                // Remove a bandeira
                try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM bandeira_tef WHERE id = ?")) {
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new Exception("Este registro não pode ser excluído! " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public BandeiraTef encontrar(int id) throws Exception {
        String sql = "SELECT b.id, b.nome, b.cnpj, b.ativo, b.enumerar, " +
                "t.id AS t_id, t.tp_integrado AS t_tp_integrado, t.parcela_min AS t_parcela_min, " +
                "t.parcela_max AS t_parcela_max, t.taxa_percentual AS t_taxa_percentual, " +
                "t.prazo_repasse_dias AS t_prazo_repasse_dias, t.ativo AS t_ativo " +
                "FROM bandeira_tef b " +
                "LEFT JOIN bandeira_tef_taxa t ON t.id_bandeira_tef = b.id " +
                "WHERE b.id = ? ORDER BY t.tp_integrado, t.parcela_min";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                BandeiraTef bandeira = null;
                while (rs.next()) {
                    if (bandeira == null) {
                        bandeira = new BandeiraTef();
                        bandeira.setId(rs.getInt("id"));
                        bandeira.setNome(rs.getString("nome"));
                        bandeira.setCnpj(rs.getString("cnpj"));
                        bandeira.setAtivo(rs.getBoolean("ativo"));
                        bandeira.setEnumerar(rs.getString("enumerar"));
                    }

                    int tId = rs.getInt("t_id");
                    if (!rs.wasNull()) {
                        BandeiraTefTaxa taxa = new BandeiraTefTaxa();
                        taxa.setId(tId);
                        taxa.setIdBandeiraTef(bandeira.getId());
                        taxa.setTpIntegrado(rs.getInt("t_tp_integrado"));
                        taxa.setParcelaMin(rs.getInt("t_parcela_min"));
                        taxa.setParcelaMax(rs.getInt("t_parcela_max"));
                        taxa.setTaxaPercentual(rs.getDouble("t_taxa_percentual"));
                        taxa.setPrazoRepasseDias(rs.getInt("t_prazo_repasse_dias"));
                        taxa.setAtivo(rs.getBoolean("t_ativo"));
                        bandeira.getLstTaxas().add(taxa);
                    }
                }
                return bandeira;
            }
        }
    }

    public List<BandeiraTef> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<BandeiraTef> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nome, cnpj, ativo, enumerar FROM bandeira_tef WHERE 1=1");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND cnpj ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY id DESC");
        } else if (ordenarPor == 1) {
            sql.append(" ORDER BY id ASC");
        } else {
            sql.append(" ORDER BY nome ASC");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                if (tipoFiltro == 0 || tipoFiltro == 2) {
                    ps.setString(1, "%" + filtro.trim() + "%");
                } else {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BandeiraTef b = new BandeiraTef();
                    b.setId(rs.getInt("id"));
                    b.setNome(rs.getString("nome"));
                    b.setCnpj(rs.getString("cnpj"));
                    b.setAtivo(rs.getBoolean("ativo"));
                    b.setEnumerar(rs.getString("enumerar"));
                    lista.add(b);
                }
            }
        }
        return lista;
    }

    public void clone(BandeiraTef original) throws Exception {
        if (original != null && original.getId() != null) {
            BandeiraTef clone = encontrar(original.getId());
            if (clone != null) {
                clone.setId(null);
                incluir(clone);
            }
        }
    }
}
