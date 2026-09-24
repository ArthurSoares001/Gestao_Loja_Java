package CompraAcerto.dao;

import Compra.model.Compra;
import CompraAcerto.model.CompraAcerto;
import CompraAcerto.model.CompraAcertoItem;
import CompraItem.model.CompraItem;
import ConfigFiscal.model.ConfigFiscal;
import Mercadoria.model.Mercadoria;
import Unidade.model.Unidade;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraAcertoDAO {

    public void incluir(CompraAcerto acerto) throws Exception {
        acerto.validar();
        String sql = "INSERT INTO compra_acerto " +
                "(id_compra, numero_acerto, acertado_motivo, gerar_credito, valor_credito, valor_total_acerto, " +
                " dfe_numero_devolucao, dfe_devolucao_xml, dfe_referencia_chave) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, acerto.getCompra().getId());
                    ps.setInt(2, acerto.getNumeroAcerto());
                    ps.setString(3, acerto.getAcertadoMotivo());
                    ps.setBoolean(4, acerto.getGerarCredito());
                    ps.setDouble(5, acerto.getValorCredito());
                    ps.setDouble(6, acerto.getValorTotalAcerto());

                    if (acerto.getDfeNumeroDevolucao() != null && acerto.getDfeNumeroDevolucao() > 0) {
                        ps.setInt(7, acerto.getDfeNumeroDevolucao());
                    } else {
                        ps.setNull(7, Types.INTEGER);
                    }

                    ps.setString(8, acerto.getDfeDevolucaoXml());
                    ps.setString(9, acerto.getDfeReferenciaChave());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            acerto.setId(rs.getInt(1));
                        }
                    }
                }

                armazenarItens(conn, acerto);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Esse Registro não pode ser Incluído: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void alterar(CompraAcerto acerto) throws Exception {
        acerto.validar();
        String sql = "UPDATE compra_acerto SET " +
                "  id_compra = ?, numero_acerto = ?, acertado_motivo = ?, gerar_credito = ?, " +
                "  valor_credito = ?, valor_total_acerto = ?, dfe_numero_devolucao = ?, " +
                "  dfe_devolucao_xml = ?, dfe_referencia_chave = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, acerto.getCompra().getId());
                    ps.setInt(2, acerto.getNumeroAcerto());
                    ps.setString(3, acerto.getAcertadoMotivo());
                    ps.setBoolean(4, acerto.getGerarCredito());
                    ps.setDouble(5, acerto.getValorCredito());
                    ps.setDouble(6, acerto.getValorTotalAcerto());

                    if (acerto.getDfeNumeroDevolucao() != null && acerto.getDfeNumeroDevolucao() > 0) {
                        ps.setInt(7, acerto.getDfeNumeroDevolucao());
                    } else {
                        ps.setNull(7, Types.INTEGER);
                    }

                    ps.setString(8, acerto.getDfeDevolucaoXml());
                    ps.setString(9, acerto.getDfeReferenciaChave());
                    ps.setInt(10, acerto.getId());
                    ps.executeUpdate();
                }

                armazenarItens(conn, acerto);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Esse Registro não pode ser Atualizado: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void armazenarItens(Connection conn, CompraAcerto acerto) throws SQLException {
        // Deleta itens anteriores
        String sqlDelete = "DELETE FROM compra_acerto_item WHERE id_compra_acerto = ?";
        try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
            psDel.setInt(1, acerto.getId());
            psDel.executeUpdate();
        }

        // Insere a nova lista de itens
        String sqlInsert = "INSERT INTO compra_acerto_item " +
                "(id_compra_acerto, id_compra_item, qtd_devolvida, valor_unitario, valor_total, devolucao_motivo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
            for (CompraAcertoItem item : acerto.getLstAcertoItem()) {
                psIns.setInt(1, acerto.getId());
                psIns.setInt(2, item.getCompraItem().getId());
                psIns.setDouble(3, item.getQuantidade());
                psIns.setDouble(4, item.getValorUnitario());
                psIns.setDouble(5, item.getValorTotal());
                psIns.setString(6, item.getMotivo());
                psIns.executeUpdate();
            }
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM compra_acerto WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public CompraAcerto encontrar(int id) throws Exception {
        String sql = "SELECT id, id_compra, numero_acerto, acertado, acertado_por, acertado_motivo, " +
                "       gerar_credito, valor_credito, valor_total_acerto, " +
                "       dfe_numero_devolucao, dfe_devolucao_xml, dfe_referencia_chave " +
                "FROM compra_acerto WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CompraAcerto acerto = mapearResultSet(rs);
                    recuperarItens(conn, acerto.getId(), acerto);
                    return acerto;
                }
            }
        }
        return null;
    }

    public List<CompraAcerto> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<CompraAcerto> lista = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT id, id_compra, numero_acerto, acertado, acertado_por, acertado_motivo, " +
                        "       gerar_credito, valor_credito, valor_total_acerto, " +
                        "       dfe_numero_devolucao, dfe_devolucao_xml, dfe_referencia_chave " +
                        "FROM compra_acerto WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND id_compra = ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND acertado_motivo ILIKE ?"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY id DESC");
        } else {
            sql.append(" ORDER BY acertado DESC");
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                if (temFiltro) {
                    if (tipoFiltro == 0 || tipoFiltro == 1) {
                        ps.setInt(1, Integer.parseInt(filtro.trim()));
                    } else {
                        ps.setString(1, "%" + filtro.trim() + "%");
                    }
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ids.add(rs.getInt("id"));
                    }
                }
            }

            // Carrega cada acerto completo com seus itens
            for (Integer id : ids) {
                CompraAcerto acerto = encontrar(id);
                if (acerto != null) {
                    lista.add(acerto);
                }
            }
        }
        return lista;
    }

    public List<CompraAcerto> recuperarPorCompra(int idCompra) throws Exception {
        return recuperarTodos(String.valueOf(idCompra), 0, 0);
    }

    public int obterProximoNumeroAcerto(int idCompra) {
        String sql = "SELECT COALESCE(MAX(numero_acerto), 0) + 1 AS num FROM compra_acerto WHERE id_compra = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("num");
                }
            }
        } catch (Exception ignored) {}
        return 1;
    }

    public CompraAcerto obterUltimoAcerto(int idCompra) throws Exception {
        String sql = "SELECT * FROM compra_acerto WHERE id_compra = ? ORDER BY numero_acerto DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CompraAcerto acerto = mapearResultSet(rs);
                    recuperarItens(conn, acerto.getId(), acerto);
                    return acerto;
                }
            }
        }
        return null;
    }

    public void marcar(int idAcerto) throws Exception {
        if (idAcerto <= 0) return;

        String sql = "UPDATE compra_acerto SET " +
                "  acertado = CURRENT_TIMESTAMP, " +
                "  acertado_por = CURRENT_USER " +
                "WHERE id = ? AND acertado IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAcerto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao marcar compra_acerto: " + e.getMessage());
        }
    }

    public void marcarCompraAcerto(CompraAcerto acerto, int idCompra) throws Exception {
        if (idCompra <= 0) return;

        String sqlItem = "UPDATE compra_item SET qtd_devolvida = COALESCE(qtd_devolvida, 0) + ? " +
                "WHERE id_compra = ? AND id = ?";

        String sqlAcerto = "UPDATE compra_acerto SET " +
                "  acertado = CURRENT_TIMESTAMP, " +
                "  acertado_por = CURRENT_USER " +
                "WHERE id = ? AND acertado IS NULL";

        String sqlCompra = "UPDATE compra SET " +
                "  acertado = CURRENT_TIMESTAMP, " +
                "  acertado_por = CURRENT_USER " +
                "WHERE id = ? AND acertado IS NULL";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Atualizar itens da compra
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    for (CompraAcertoItem item : acerto.getLstAcertoItem()) {
                        psItem.setDouble(1, item.getQuantidade());
                        psItem.setInt(2, idCompra);
                        psItem.setInt(3, item.getCompraItem().getId());
                        psItem.executeUpdate();
                    }
                }

                // 2. Atualizar compra_acerto
                try (PreparedStatement psAcerto = conn.prepareStatement(sqlAcerto)) {
                    psAcerto.setInt(1, acerto.getId());
                    psAcerto.executeUpdate();
                }

                // 3. Atualizar compra
                try (PreparedStatement psCompra = conn.prepareStatement(sqlCompra)) {
                    psCompra.setInt(1, idCompra);
                    psCompra.executeUpdate();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Erro ao processar acerto da compra: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void atualizarDfeDevolucao(int acertoId, int dfeNumero, String dfeXml, String dfeChave) throws Exception {
        String sql = "UPDATE compra_acerto SET " +
                "  dfe_numero_devolucao = ?, " +
                "  dfe_devolucao_xml = ?, " +
                "  dfe_referencia_chave = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dfeNumero);
            ps.setString(2, dfeXml);
            ps.setString(3, dfeChave);
            ps.setInt(4, acertoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar DFe da compra_acerto: " + e.getMessage());
        }
    }

    private void recuperarItens(Connection conn, int idCompraAcerto, CompraAcerto acerto) throws SQLException {
        acerto.getLstAcertoItem().clear();

        String sql = "SELECT cai.id, cai.id_compra_acerto, cai.id_compra_item, " +
                "       cai.qtd_devolvida, cai.valor_unitario, cai.valor_total, cai.devolucao_motivo, " +
                "       ci.id_produto, ci.valor_compra, ci.valor_venda, ci.quantidade, " +
                "       ci.desconto, ci.acrescimo, ci.valor_total as ci_valor_total, " +
                "       ci.valor_custo, ci.qtd_devolvida as ci_qtd_devolvida, " +
                "       ci.cst, ci.cfop, ci.aliq_icms, ci.base_icms, ci.valor_icms, " +
                "       ci.reducao_base, ci.aliq_icms_st, ci.base_icms_st, ci.valor_icms_st, " +
                "       ci.cst_pis, ci.aliq_pis, ci.base_pis, ci.valor_pis, " +
                "       ci.cst_cofins, ci.aliq_cofins, ci.base_cofins, ci.valor_cofins, " +
                "       ci.cst_ipi, ci.aliq_ipi, ci.base_ipi, ci.valor_ipi, " +
                "       m.id as m_id, m.nome as m_nome, m.cod_barra, m.ncm, m.cest, " +
                "       m.id_config_fiscal, " +
                "       u.id as u_id, u.sigla, " +
                "       cf.cst as cf_cst, cf.cst_fora as cf_cst_fora, " +
                "       cf.aliq_icms as cf_aliq_icms, cf.reducao_base as cf_reducao_base " +
                "FROM compra_acerto_item cai " +
                "INNER JOIN compra_item ci ON ci.id = cai.id_compra_item " +
                "INNER JOIN mercadoria m ON m.id = ci.id_produto " +
                "INNER JOIN unidade u ON u.id = m.id_unidade " +
                "LEFT JOIN config_fiscal cf ON cf.id = m.id_config_fiscal " +
                "WHERE cai.id_compra_acerto = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompraAcerto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompraAcertoItem item = new CompraAcertoItem();
                    CompraItem compraItem = new CompraItem();

                    compraItem.setId(rs.getInt("id_compra_item"));
                    compraItem.setIdMercadoria(rs.getInt("id_produto"));
                    compraItem.setValorCompra(rs.getDouble("valor_compra"));
                    compraItem.setValorVenda(rs.getDouble("valor_venda"));
                    compraItem.setQuantidade(rs.getDouble("quantidade"));
                    compraItem.setDesconto(rs.getDouble("desconto"));
                    compraItem.setValorCusto(rs.getDouble("valor_custo"));
                    compraItem.setQtdDevolvida(rs.getInt("ci_qtd_devolvida"));
                    compraItem.setCst(rs.getString("cst"));
                    compraItem.setCfop(rs.getString("cfop"));
                    compraItem.setAliqIcms(rs.getDouble("aliq_icms"));
                    compraItem.setBaseIcms(rs.getDouble("base_icms"));
                    compraItem.setValorIcms(rs.getDouble("valor_icms"));
                    compraItem.setReducaoBase(rs.getDouble("reducao_base"));
                    compraItem.setAliqIcmsSt(rs.getDouble("aliq_icms_st"));
                    compraItem.setBaseIcmsSt(rs.getDouble("base_icms_st"));
                    compraItem.setValorIcmsSt(rs.getDouble("valor_icms_st"));
                    compraItem.setCstPis(rs.getString("cst_pis"));
                    compraItem.setAliqPis(rs.getDouble("aliq_pis"));
                    compraItem.setBasePis(rs.getDouble("base_pis"));
                    compraItem.setValorPis(rs.getDouble("valor_pis"));
                    compraItem.setCstCofins(rs.getString("cst_cofins"));
                    compraItem.setAliqCofins(rs.getDouble("aliq_cofins"));
                    compraItem.setBaseCofins(rs.getDouble("base_cofins"));
                    compraItem.setValorCofins(rs.getDouble("valor_cofins"));
                    compraItem.setCstIpi(rs.getString("cst_ipi"));
                    compraItem.setAliqIpi(rs.getDouble("aliq_ipi"));
                    compraItem.setBaseIpi(rs.getDouble("base_ipi"));
                    compraItem.setValorIpi(rs.getDouble("valor_ipi"));

                    Mercadoria m = new Mercadoria();
                    m.setId(rs.getInt("m_id"));
                    m.setNome(rs.getString("m_nome"));
                    m.setCodBarra(rs.getString("cod_barra"));
                    m.setNcm(rs.getString("ncm"));
                    m.setCest(rs.getString("cest"));

                    Unidade u = new Unidade();
                    u.setId(rs.getInt("u_id"));
                    u.setSigla(rs.getString("sigla"));
                    m.setUnidade(u);

                    ConfigFiscal cf = new ConfigFiscal();
                    cf.setCst(rs.getString("cf_cst"));
                    cf.setCstFora(rs.getString("cf_cst_fora"));
                    cf.setAliqIcms(rs.getBigDecimal("cf_aliq_icms"));
                    cf.setReducaoBase(rs.getBigDecimal("cf_reducao_base"));
                    m.setConfigFiscal(cf);

                    compraItem.setMercadoria(m);

                    item.setId(rs.getInt("id"));
                    item.setIdCompraAcerto(rs.getInt("id_compra_acerto"));
                    item.setCompraItem(compraItem);
                    item.setQuantidade(rs.getDouble("qtd_devolvida"));
                    item.setValorUnitario(rs.getDouble("valor_unitario"));
                    item.setValorTotal(rs.getDouble("valor_total"));
                    item.setMotivo(rs.getString("devolucao_motivo"));

                    acerto.getLstAcertoItem().add(item);
                }
            }
        }
    }

    private CompraAcerto mapearResultSet(ResultSet rs) throws SQLException {
        CompraAcerto acerto = new CompraAcerto();
        acerto.setId(rs.getInt("id"));

        Compra c = new Compra();
        c.setId(rs.getInt("id_compra"));
        acerto.setCompra(c);

        acerto.setNumeroAcerto(rs.getInt("numero_acerto"));
        Timestamp tsAcertado = rs.getTimestamp("acertado");
        if (tsAcertado != null) {
            acerto.setAcertado(tsAcertado.toLocalDateTime());
        }

        acerto.setAcertadoPor(rs.getString("acertado_por"));
        acerto.setAcertadoMotivo(rs.getString("acertado_motivo"));
        acerto.setGerarCredito(rs.getBoolean("gerar_credito"));
        acerto.setValorCredito(rs.getDouble("valor_credito"));
        acerto.setValorTotalAcerto(rs.getDouble("valor_total_acerto"));

        int dfeNum = rs.getInt("dfe_numero_devolucao");
        if (!rs.wasNull()) {
            acerto.setDfeNumeroDevolucao(dfeNum);
        }

        acerto.setDfeDevolucaoXml(rs.getString("dfe_devolucao_xml"));
        acerto.setDfeReferenciaChave(rs.getString("dfe_referencia_chave"));

        return acerto;
    }
}
