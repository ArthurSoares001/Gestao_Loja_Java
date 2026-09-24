package Compra.dao;


import Compra.model.Compra;
import NaturezaOperacao.model.NaturezaOperacao;
import Participante.model.Participante;
import Util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public void incluir(Compra compra) throws Exception {
        compra.validar();
        String sql = "INSERT INTO compra (id_fornecedor, itens, qtd_total, val_total, desconto, acrescimo, troco, frete, " +
                "impresso, impresso_por, separado, separado_por, faturado, faturado_por, clonado, clonado_por, " +
                "clonado_id, id_natureza_operacao, base_icms, valor_icms, base_icms_st, valor_icms_st, " +
                "valor_pis, valor_cofins, valor_ipi, dfe_ambiente, dfe_serie, dfe_numero, dfe_xml, dfe_modelo, " +
                "dfe_chave, dfe_status, dfe_versao, dfe_protocolo, dfe_autorizacao, dfe_dh_autorizacao, " +
                "cancelado, cancelado_por, acertado, acertado_por, acertado_motivo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(ps, compra);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    compra.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Compra compra) throws Exception {
        compra.validar();
        String sql = "UPDATE compra SET id_fornecedor = ?, itens = ?, qtd_total = ?, val_total = ?, desconto = ?, " +
                "acrescimo = ?, troco = ?, frete = ?, impresso = ?, impresso_por = ?, separado = ?, " +
                "separado_por = ?, faturado = ?, faturado_por = ?, clonado = ?, clonado_por = ?, " +
                "clonado_id = ?, id_natureza_operacao = ?, base_icms = ?, valor_icms = ?, base_icms_st = ?, " +
                "valor_icms_st = ?, valor_pis = ?, valor_cofins = ?, valor_ipi = ?, dfe_ambiente = ?, " +
                "dfe_serie = ?, dfe_numero = ?, dfe_xml = ?, dfe_modelo = ?, dfe_chave = ?, dfe_status = ?, " +
                "dfe_versao = ?, dfe_protocolo = ?, dfe_autorizacao = ?, dfe_dh_autorizacao = ?, cancelado = ?, " +
                "cancelado_por = ?, acertado = ?, acertado_por = ?, acertado_motivo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherParametros(ps, compra);
            ps.setInt(42, compra.getId());
            ps.executeUpdate();
        }
    }

    private void preencherParametros(PreparedStatement ps, Compra c) throws SQLException {
        ps.setInt(1, c.getFornecedor().getId());
        ps.setObject(2, c.getItens(), Types.INTEGER);
        ps.setBigDecimal(3, c.getQtdTotal() != null ? BigDecimal.valueOf(c.getQtdTotal()) : null);
        ps.setBigDecimal(4, c.getValTotal() != null ? BigDecimal.valueOf(c.getValTotal()) : null);
        ps.setBigDecimal(5, c.getDesconto() != null ? BigDecimal.valueOf(c.getDesconto()) : null);
        ps.setBigDecimal(6, c.getAcrescimo() != null ? BigDecimal.valueOf(c.getAcrescimo()) : null);
        ps.setBigDecimal(7, c.getTroco() != null ? BigDecimal.valueOf(c.getTroco()) : null);
        ps.setBigDecimal(8, c.getFrete() != null ? BigDecimal.valueOf(c.getFrete()) : null);

        ps.setTimestamp(9, c.getImpresso() != null ? Timestamp.valueOf(c.getImpresso()) : null);
        ps.setString(10, c.getImpressoPor());
        ps.setTimestamp(11, c.getSeparado() != null ? Timestamp.valueOf(c.getSeparado()) : null);
        ps.setString(12, c.getSeparadoPor());
        ps.setTimestamp(13, c.getFaturado() != null ? Timestamp.valueOf(c.getFaturado()) : null);
        ps.setString(14, c.getFaturadoPor());
        ps.setTimestamp(15, c.getClonado() != null ? Timestamp.valueOf(c.getClonado()) : null);
        ps.setString(16, c.getClonadoPor());
        ps.setObject(17, c.getClonadoId(), Types.INTEGER);

        if (c.getNaturezaOperacao() != null && c.getNaturezaOperacao().getId() != null && c.getNaturezaOperacao().getId() > 0) {
            ps.setInt(18, c.getNaturezaOperacao().getId());
        } else {
            ps.setNull(18, Types.INTEGER);
        }

        ps.setBigDecimal(19, c.getBaseIcms() != null ? BigDecimal.valueOf(c.getBaseIcms()) : null);
        ps.setBigDecimal(20, c.getValorIcms() != null ? BigDecimal.valueOf(c.getValorIcms()) : null);
        ps.setBigDecimal(21, c.getBaseIcmsSt() != null ? BigDecimal.valueOf(c.getBaseIcmsSt()) : null);
        ps.setBigDecimal(22, c.getValorIcmsSt() != null ? BigDecimal.valueOf(c.getValorIcmsSt()) : null);
        ps.setBigDecimal(23, c.getValorPis() != null ? BigDecimal.valueOf(c.getValorPis()) : null);
        ps.setBigDecimal(24, c.getValorCofins() != null ? BigDecimal.valueOf(c.getValorCofins()) : null);
        ps.setBigDecimal(25, c.getValorIpi() != null ? BigDecimal.valueOf(c.getValorIpi()) : null);

        ps.setObject(26, c.getDfeAmbiente(), Types.INTEGER);
        ps.setObject(27, c.getDfeSerie(), Types.INTEGER);
        ps.setObject(28, c.getDfeNumero(), Types.INTEGER);
        ps.setString(29, c.getDfeXml());
        ps.setObject(30, c.getDfeModelo(), Types.INTEGER);
        ps.setString(31, c.getDfeChave());
        ps.setString(32, c.getDfeStatus());
        ps.setString(33, c.getDfeVersao());
        ps.setString(34, c.getDfeProtoco());
        ps.setString(35, c.getDfeAutorizacao());
        ps.setTimestamp(36, c.getDfeDhAutorizacao() != null ? Timestamp.valueOf(c.getDfeDhAutorizacao()) : null);

        ps.setTimestamp(37, c.getCancelado() != null ? Timestamp.valueOf(c.getCancelado()) : null);
        ps.setString(38, c.getCanceladoPor());
        ps.setTimestamp(39, c.getAcertado() != null ? Timestamp.valueOf(c.getAcertado()) : null);
        ps.setString(40, c.getAcertadoPor());
        ps.setString(41, c.getAcertadoMotivo());
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM compra WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Compra encontrar(int id) throws Exception {
        String sql = "SELECT c.*, p.nome AS forn_nome, p.cpf_cnpj AS forn_cpf, n.nome AS nat_nome " +
                "FROM compra c " +
                "INNER JOIN participante p ON c.id_fornecedor = p.id " +
                "LEFT JOIN natureza_operacao n ON c.id_natureza_operacao = n.id " +
                "WHERE c.id = ?";

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

    public List<Compra> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Compra> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, p.nome AS forn_nome, p.cpf_cnpj AS forn_cpf, n.nome AS nat_nome " +
                        "FROM compra c " +
                        "INNER JOIN participante p ON c.id_fornecedor = p.id " +
                        "LEFT JOIN natureza_operacao n ON c.id_natureza_operacao = n.id WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND p.nome ILIKE ?"); break;
                case 1: sql.append(" AND c.id = ?"); break;
                case 2: sql.append(" AND c.dfe_chave ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY c.id DESC");
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

    public List<Participante> recuperarFornecedores() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cpf_cnpj FROM participante WHERE ativo = true ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Participante p = new Participante();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCpfCnpj(rs.getString("cpf_cnpj"));
                lista.add(p);
            }
        } catch (Exception ignored) {}
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

    private Compra mapearResultSet(ResultSet rs) throws SQLException {
        Compra c = new Compra();
        c.setId(rs.getInt("id"));

        Participante p = new Participante();
        p.setId(rs.getInt("id_fornecedor"));
        p.setNome(rs.getString("forn_nome"));
        p.setCpfCnpj(rs.getString("forn_cpf"));
        c.setFornecedor(p);

        c.setItens(rs.getObject("itens") != null ? rs.getInt("itens") : null);
        BigDecimal bdQtdTotal = rs.getBigDecimal("qtd_total");
        c.setQtdTotal(bdQtdTotal != null ? bdQtdTotal.doubleValue() : null);
        BigDecimal bdValTotal = rs.getBigDecimal("val_total");
        c.setValTotal(bdValTotal != null ? bdValTotal.doubleValue() : null);
        BigDecimal bdDesconto = rs.getBigDecimal("desconto");
        c.setDesconto(bdDesconto != null ? bdDesconto.doubleValue() : null);
        BigDecimal bdAcrescimo = rs.getBigDecimal("acrescimo");
        c.setAcrescimo(bdAcrescimo != null ? bdAcrescimo.doubleValue() : null);
        BigDecimal bdTroco = rs.getBigDecimal("troco");
        c.setTroco(bdTroco != null ? bdTroco.doubleValue() : null);
        BigDecimal bdFrete = rs.getBigDecimal("frete");
        c.setFrete(bdFrete != null ? bdFrete.doubleValue() : null);

        Timestamp tsImp = rs.getTimestamp("impresso");
        if (tsImp != null) c.setImpresso(tsImp.toLocalDateTime());
        c.setImpressoPor(rs.getString("impresso_por"));

        Timestamp tsFat = rs.getTimestamp("faturado");
        if (tsFat != null) c.setFaturado(tsFat.toLocalDateTime());
        c.setFaturadoPor(rs.getString("faturado_por"));

        int idNat = rs.getInt("id_natureza_operacao");
        if (!rs.wasNull()) {
            NaturezaOperacao nat = new NaturezaOperacao();
            nat.setId(idNat);
            nat.setNome(rs.getString("nat_nome"));
            c.setNaturezaOperacao(nat);
        }

        c.setDfeChave(rs.getString("dfe_chave"));
        c.setDfeNumero(rs.getObject("dfe_numero") != null ? rs.getInt("dfe_numero") : null);
        c.setDfeSerie(rs.getObject("dfe_serie") != null ? rs.getInt("dfe_serie") : null);

        Timestamp tsCanc = rs.getTimestamp("cancelado");
        if (tsCanc != null) c.setCancelado(tsCanc.toLocalDateTime());
        c.setCanceladoPor(rs.getString("cancelado_por"));

        return c;
    }
}
