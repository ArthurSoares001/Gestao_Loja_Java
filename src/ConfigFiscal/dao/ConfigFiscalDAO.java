package ConfigFiscal.dao;

import ConfigFiscal.model.ConfigFiscal;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigFiscalDAO {

    public void incluir(ConfigFiscal conf) throws Exception {
        conf.validar();
        String sql = "INSERT INTO config_fiscal (nome, cst, cst_fora, aliq_icms, reducao_base, reducao_base_fora, " +
                "cst_ipi, aliq_ipi, cst_pis, aliq_pis, cst_cofins, aliq_cofins, mensagem, ativo, " +
                "usar_reforma_trib, class_trib, cst_ibs, aliq_ibs, aliq_ibs_uf, aliq_ibs_municipio, " +
                "diferimento_ibs, cst_cbs, aliq_cbs, diferimento_cbs, cst_is, aliq_is, aliq_is_especifica, " +
                "reducao_is, perc_compra_governo, ente_publico, utiliza_split_payment, perc_split_payment, " +
                "utiliza_cashback, perc_cashback, operacao_zfm, operacao_alc, operacao_exportacao, " +
                "operacao_importacao, cod_mun_fg_ibs, versao_layout_reforma, tipo_tributacao, " +
                "aliq_ibs_cred_presumido, aliq_cbs_cred_presumido) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(ps, conf);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    conf.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(ConfigFiscal conf) throws Exception {
        conf.validar();
        String sql = "UPDATE config_fiscal SET nome = ?, cst = ?, cst_fora = ?, aliq_icms = ?, reducao_base = ?, " +
                "reducao_base_fora = ?, cst_ipi = ?, aliq_ipi = ?, cst_pis = ?, aliq_pis = ?, cst_cofins = ?, " +
                "aliq_cofins = ?, mensagem = ?, ativo = ?, usar_reforma_trib = ?, class_trib = ?, cst_ibs = ?, " +
                "aliq_ibs = ?, aliq_ibs_uf = ?, aliq_ibs_municipio = ?, diferimento_ibs = ?, cst_cbs = ?, " +
                "aliq_cbs = ?, diferimento_cbs = ?, cst_is = ?, aliq_is = ?, aliq_is_especifica = ?, " +
                "reducao_is = ?, perc_compra_governo = ?, ente_publico = ?, utiliza_split_payment = ?, " +
                "perc_split_payment = ?, utiliza_cashback = ?, perc_cashback = ?, operacao_zfm = ?, " +
                "operacao_alc = ?, operacao_exportacao = ?, operacao_importacao = ?, cod_mun_fg_ibs = ?, " +
                "versao_layout_reforma = ?, tipo_tributacao = ?, aliq_ibs_cred_presumido = ?, " +
                "aliq_cbs_cred_presumido = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherParametros(ps, conf);
            ps.setInt(44, conf.getId());
            ps.executeUpdate();
        }
    }

    private void preencherParametros(PreparedStatement ps, ConfigFiscal c) throws SQLException {
        ps.setString(1, c.getNome());
        ps.setString(2, c.getCst());
        ps.setString(3, c.getCstFora());
        ps.setBigDecimal(4, c.getAliqIcms());
        ps.setBigDecimal(5, c.getReducaoBase());
        ps.setBigDecimal(6, c.getReducaoBaseFora());
        ps.setString(7, c.getCstIpi());
        ps.setBigDecimal(8, c.getAliqIpi());
        ps.setString(9, c.getCstPis());
        ps.setBigDecimal(10, c.getAliqPis());
        ps.setString(11, c.getCstCofins());
        ps.setBigDecimal(12, c.getAliqCofins());
        ps.setString(13, c.getMensagem());
        ps.setBoolean(14, c.isAtivo());
        ps.setBoolean(15, c.isUsarReformaTrib());
        ps.setString(16, c.getClassTrib());
        ps.setString(17, c.getCstIbs());
        ps.setBigDecimal(18, c.getAliqIbs());
        ps.setBigDecimal(19, c.getAliqIbsUf());
        ps.setBigDecimal(20, c.getAliqIbsMunicipio());
        ps.setBigDecimal(21, c.getDiferimentoIbs());
        ps.setString(22, c.getCstCbs());
        ps.setBigDecimal(23, c.getAliqCbs());
        ps.setBigDecimal(24, c.getDiferimentoCbs());
        ps.setString(25, c.getCstIs());
        ps.setBigDecimal(26, c.getAliqIs());
        ps.setBigDecimal(27, c.getAliqIsEspecifica());
        ps.setBigDecimal(28, c.getReducaoIs());
        ps.setBigDecimal(29, c.getPercCompraGoverno());
        ps.setString(30, c.getEntePublico());
        ps.setBoolean(31, c.isUtilizaSplitPayment());
        ps.setBigDecimal(32, c.getPercSplitPayment());
        ps.setBoolean(33, c.isUtilizaCashback());
        ps.setBigDecimal(34, c.getPercCashback());
        ps.setBoolean(35, c.isOperacaoZfm());
        ps.setBoolean(36, c.isOperacaoAlc());
        ps.setBoolean(37, c.isOperacaoExportacao());
        ps.setBoolean(38, c.isOperacaoImportacao());
        ps.setString(39, c.getCodMunFgIbs());
        ps.setString(40, c.getVersaoLayoutReforma());
        ps.setObject(41, c.getTipoTributacao(), Types.NUMERIC);
        ps.setBigDecimal(42, c.getAliqIbsCredPresumido());
        ps.setBigDecimal(43, c.getAliqCbsCredPresumido());
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM config_fiscal WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public ConfigFiscal encontrar(int id) throws Exception {
        String sql = "SELECT * FROM config_fiscal WHERE id = ?";

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

    public List<ConfigFiscal> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<ConfigFiscal> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM config_fiscal WHERE 1=1 ");

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND nome ILIKE ?"); break;
                case 1: sql.append(" AND id = ?"); break;
                case 2: sql.append(" AND cst ILIKE ?"); break;
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

    private ConfigFiscal mapearResultSet(ResultSet rs) throws SQLException {
        ConfigFiscal c = new ConfigFiscal();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCst(rs.getString("cst"));
        c.setCstFora(rs.getString("cst_fora"));
        c.setAliqIcms(rs.getBigDecimal("aliq_icms"));
        c.setReducaoBase(rs.getBigDecimal("reducao_base"));
        c.setReducaoBaseFora(rs.getBigDecimal("reducao_base_fora"));
        c.setCstIpi(rs.getString("cst_ipi"));
        c.setAliqIpi(rs.getBigDecimal("aliq_ipi"));
        c.setCstPis(rs.getString("cst_pis"));
        c.setAliqPis(rs.getBigDecimal("aliq_pis"));
        c.setCstCofins(rs.getString("cst_cofins"));
        c.setAliqCofins(rs.getBigDecimal("aliq_cofins"));
        c.setMensagem(rs.getString("mensagem"));
        c.setAtivo(rs.getBoolean("ativo"));

        c.setUsarReformaTrib(rs.getBoolean("usar_reforma_trib"));
        c.setClassTrib(rs.getString("class_trib"));
        c.setCstIbs(rs.getString("cst_ibs"));
        c.setAliqIbs(rs.getBigDecimal("aliq_ibs"));
        c.setAliqIbsUf(rs.getBigDecimal("aliq_ibs_uf"));
        c.setAliqIbsMunicipio(rs.getBigDecimal("aliq_ibs_municipio"));
        c.setDiferimentoIbs(rs.getBigDecimal("diferimento_ibs"));
        c.setCstCbs(rs.getString("cst_cbs"));
        c.setAliqCbs(rs.getBigDecimal("aliq_cbs"));
        c.setDiferimentoCbs(rs.getBigDecimal("diferimento_cbs"));
        c.setCstIs(rs.getString("cst_is"));
        c.setAliqIs(rs.getBigDecimal("aliq_is"));
        c.setAliqIsEspecifica(rs.getBigDecimal("aliq_is_especifica"));
        c.setReducaoIs(rs.getBigDecimal("reducao_is"));
        c.setPercCompraGoverno(rs.getBigDecimal("perc_compra_governo"));
        c.setEntePublico(rs.getString("ente_publico"));
        c.setUtilizaSplitPayment(rs.getBoolean("utiliza_split_payment"));
        c.setPercSplitPayment(rs.getBigDecimal("perc_split_payment"));
        c.setUtilizaCashback(rs.getBoolean("utiliza_cashback"));
        c.setPercCashback(rs.getBigDecimal("perc_cashback"));
        c.setOperacaoZfm(rs.getBoolean("operacao_zfm"));
        c.setOperacaoAlc(rs.getBoolean("operacao_alc"));
        c.setOperacaoExportacao(rs.getBoolean("operacao_exportacao"));
        c.setOperacaoImportacao(rs.getBoolean("operacao_importacao"));
        c.setCodMunFgIbs(rs.getString("cod_mun_fg_ibs"));
        c.setVersaoLayoutReforma(rs.getString("versao_layout_reforma"));
        c.setTipoTributacao(rs.getObject("tipo_tributacao") != null ? rs.getInt("tipo_tributacao") : 0);
        c.setAliqIbsCredPresumido(rs.getBigDecimal("aliq_ibs_cred_presumido"));
        c.setAliqCbsCredPresumido(rs.getBigDecimal("aliq_cbs_cred_presumido"));

        return c;
    }
}
