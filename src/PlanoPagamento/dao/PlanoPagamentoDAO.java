package PlanoPagamento.dao;

import NaturezaOperacao.model.NaturezaOperacao;
import PlanoPagamento.model.PlanoPagamento;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanoPagamentoDAO {

    public void incluir(PlanoPagamento plano) throws Exception {
        plano.validar();
        String sql = "INSERT INTO plano_pagamento (nome, is_imutavel, desconto, desconto_maximo, max_parcela, " +
                "taxa_servico, enviar_web, ativo, id_natureza_operacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, plano.getNome());
            ps.setBoolean(2, plano.isImutavel());
            ps.setBigDecimal(3, plano.getDesconto());
            ps.setBigDecimal(4, plano.getDescontoMaximo());
            ps.setObject(5, plano.getMaxParcela(), Types.INTEGER);
            ps.setBigDecimal(6, plano.getTaxaServico());
            ps.setBoolean(7, plano.isEnviarWeb());
            ps.setBoolean(8, plano.isAtivo());
            if (plano.getNaturezaOperacao() != null && plano.getNaturezaOperacao().getId() != null && plano.getNaturezaOperacao().getId() > 0) {
                ps.setInt(9, plano.getNaturezaOperacao().getId());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    plano.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(PlanoPagamento plano) throws Exception {
        plano.validar();
        String sql = "UPDATE plano_pagamento SET nome = ?, is_imutavel = ?, desconto = ?, desconto_maximo = ?, " +
                "max_parcela = ?, taxa_servico = ?, enviar_web = ?, ativo = ?, id_natureza_operacao = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plano.getNome());
            ps.setBoolean(2, plano.isImutavel());
            ps.setBigDecimal(3, plano.getDesconto());
            ps.setBigDecimal(4, plano.getDescontoMaximo());
            ps.setObject(5, plano.getMaxParcela(), Types.INTEGER);
            ps.setBigDecimal(6, plano.getTaxaServico());
            ps.setBoolean(7, plano.isEnviarWeb());
            ps.setBoolean(8, plano.isAtivo());
            if (plano.getNaturezaOperacao() != null && plano.getNaturezaOperacao().getId() != null && plano.getNaturezaOperacao().getId() > 0) {
                ps.setInt(9, plano.getNaturezaOperacao().getId());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            ps.setInt(10, plano.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM plano_pagamento WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public PlanoPagamento encontrar(int id) throws Exception {
        String sql = "SELECT p.*, n.nome AS nat_nome, n.fantasia AS nat_fantasia " +
                "FROM plano_pagamento p " +
                "LEFT JOIN natureza_operacao n ON p.id_natureza_operacao = n.id " +
                "WHERE p.id = ?";

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

    public List<PlanoPagamento> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<PlanoPagamento> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, n.nome AS nat_nome, n.fantasia AS nat_fantasia " +
                        "FROM plano_pagamento p " +
                        "LEFT JOIN natureza_operacao n ON p.id_natureza_operacao = n.id WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND p.nome ILIKE ?"); break;
                case 1: sql.append(" AND p.id = ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY p.id DESC");
        } else {
            sql.append(" ORDER BY p.nome");
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

    public List<NaturezaOperacao> recuperarNaturezas() {
        List<NaturezaOperacao> lista = new ArrayList<>();
        String sql = "SELECT id, nome, fantasia FROM natureza_operacao WHERE ativo = true ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NaturezaOperacao nat = new NaturezaOperacao();
                nat.setId(rs.getInt("id"));
                nat.setNome(rs.getString("nome"));
                nat.setFantasia(rs.getString("fantasia"));
                lista.add(nat);
            }
        } catch (Exception ignored) {}
        return lista;
    }

    private PlanoPagamento mapearResultSet(ResultSet rs) throws SQLException {
        PlanoPagamento p = new PlanoPagamento();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setImutavel(rs.getBoolean("is_imutavel"));
        p.setDesconto(rs.getBigDecimal("desconto"));
        p.setDescontoMaximo(rs.getBigDecimal("desconto_maximo"));
        p.setMaxParcela(rs.getObject("max_parcela") != null ? rs.getInt("max_parcela") : null);
        p.setTaxaServico(rs.getBigDecimal("taxa_servico"));
        p.setEnviarWeb(rs.getBoolean("enviar_web"));
        p.setAtivo(rs.getBoolean("ativo"));

        int idNat = rs.getInt("id_natureza_operacao");
        if (!rs.wasNull()) {
            NaturezaOperacao nat = new NaturezaOperacao();
            nat.setId(idNat);
            nat.setNome(rs.getString("nat_nome"));
            nat.setFantasia(rs.getString("nat_fantasia"));
            p.setNaturezaOperacao(nat);
        }
        return p;
    }
}
