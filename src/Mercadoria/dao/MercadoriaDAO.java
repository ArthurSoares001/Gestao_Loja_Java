package Mercadoria.dao;

import ConfigFiscal.model.ConfigFiscal;
import Mercadoria.model.Mercadoria;
import Unidade.model.Unidade;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MercadoriaDAO {

    public void incluir(Mercadoria m) throws Exception {
        m.validar();
        String sql = "INSERT INTO mercadoria (nome, id_unidade, val_compra, margem_custo, val_custo, " +
                "margem_lucro, val_venda, margem_minimo, val_minimo, peso_bruto, peso_liquido, " +
                "val_pauta, cod_barra, referencia, tamanho, altura, largura, comprimento, comissao, " +
                "tipo, id_config_fiscal, ativo, quantidade, ncm, cest, usar_prescricao, cprod) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(ps, m);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                }
            }
        }
    }

    public void alterar(Mercadoria m) throws Exception {
        m.validar();
        String sql = "UPDATE mercadoria SET nome = ?, id_unidade = ?, val_compra = ?, margem_custo = ?, " +
                "val_custo = ?, margem_lucro = ?, val_venda = ?, margem_minimo = ?, val_minimo = ?, " +
                "peso_bruto = ?, peso_liquido = ?, val_pauta = ?, cod_barra = ?, referencia = ?, " +
                "tamanho = ?, altura = ?, largura = ?, comprimento = ?, comissao = ?, tipo = ?, " +
                "id_config_fiscal = ?, ativo = ?, quantidade = ?, ncm = ?, cest = ?, usar_prescricao = ?, " +
                "cprod = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherParametros(ps, m);
            ps.setInt(28, m.getId());
            ps.executeUpdate();
        }
    }

    private void preencherParametros(PreparedStatement ps, Mercadoria m) throws SQLException {
        ps.setString(1, m.getNome());
        ps.setInt(2, m.getUnidade().getId());
        ps.setBigDecimal(3, m.getValCompra());
        ps.setBigDecimal(4, m.getMargemCusto());
        ps.setBigDecimal(5, m.getValCusto());
        ps.setBigDecimal(6, m.getMargemLucro());
        ps.setBigDecimal(7, m.getValVenda());
        ps.setBigDecimal(8, m.getMargemMinimo());
        ps.setBigDecimal(9, m.getValMinimo());
        ps.setBigDecimal(10, m.getPesoBruto());
        ps.setBigDecimal(11, m.getPesoLiquido());
        ps.setBigDecimal(12, m.getValPauta());
        ps.setString(13, m.getCodBarra());
        ps.setString(14, m.getReferencia());
        ps.setString(15, m.getTamanho());
        ps.setBigDecimal(16, m.getAltura());
        ps.setBigDecimal(17, m.getLargura());
        ps.setBigDecimal(18, m.getComprimento());
        ps.setBigDecimal(19, m.getComissao());
        ps.setString(20, m.getTipo());
        ps.setInt(21, m.getConfigFiscal().getId());
        ps.setBoolean(22, m.isAtivo());
        ps.setObject(23, m.getQuantidade(), Types.INTEGER);
        ps.setString(24, m.getNcm());
        ps.setString(25, m.getCest());
        ps.setBoolean(26, m.isUsarPrescricao());
        ps.setString(27, m.getCprod());
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM mercadoria WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Esse Registro não pode ser Excluído: " + e.getMessage());
        }
    }

    public Mercadoria encontrar(int id) throws Exception {
        String sql = "SELECT m.*, u.nome AS unid_nome, u.sigla AS unid_sigla, cf.nome AS conf_nome " +
                "FROM mercadoria m " +
                "INNER JOIN unidade u ON m.id_unidade = u.id " +
                "INNER JOIN config_fiscal cf ON m.id_config_fiscal = cf.id " +
                "WHERE m.id = ?";

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

    public List<Mercadoria> recuperarTodos(String filtro, int tipoFiltro, int ordenarPor) throws Exception {
        List<Mercadoria> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT m.*, u.nome AS unid_nome, u.sigla AS unid_sigla, cf.nome AS conf_nome " +
                        "FROM mercadoria m " +
                        "INNER JOIN unidade u ON m.id_unidade = u.id " +
                        "INNER JOIN config_fiscal cf ON m.id_config_fiscal = cf.id WHERE 1=1 "
        );

        boolean temFiltro = (filtro != null && !filtro.trim().isEmpty());
        if (temFiltro) {
            switch (tipoFiltro) {
                case 0: sql.append(" AND m.nome ILIKE ?"); break;
                case 1: sql.append(" AND m.id = ?"); break;
                case 2: sql.append(" AND (m.cod_barra ILIKE ? OR m.referencia ILIKE ?)"); break;
            }
        }

        if (ordenarPor == 0) {
            sql.append(" ORDER BY m.id DESC");
        } else {
            sql.append(" ORDER BY m.nome");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (temFiltro) {
                if (tipoFiltro == 1) {
                    ps.setInt(1, Integer.parseInt(filtro.trim()));
                } else if (tipoFiltro == 2) {
                    ps.setString(1, "%" + filtro.trim() + "%");
                    ps.setString(2, "%" + filtro.trim() + "%");
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

    public List<Unidade> recuperarUnidades() {
        List<Unidade> lista = new ArrayList<>();
        String sql = "SELECT id, nome, sigla FROM unidade WHERE ativo = true ORDER BY sigla";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Unidade u = new Unidade();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setSigla(rs.getString("sigla"));
                lista.add(u);
            }
        } catch (Exception ignored) {}
        return lista;
    }

    public List<ConfigFiscal> recuperarConfiguracoesFiscais() {
        List<ConfigFiscal> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM config_fiscal WHERE ativo = true ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConfigFiscal cf = new ConfigFiscal();
                cf.setId(rs.getInt("id"));
                cf.setNome(rs.getString("nome"));
                lista.add(cf);
            }
        } catch (Exception ignored) {}
        return lista;
    }

    private Mercadoria mapearResultSet(ResultSet rs) throws SQLException {
        Mercadoria m = new Mercadoria();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));

        Unidade u = new Unidade();
        u.setId(rs.getInt("id_unidade"));
        u.setNome(rs.getString("unid_nome"));
        u.setSigla(rs.getString("unid_sigla"));
        m.setUnidade(u);

        m.setValCompra(rs.getBigDecimal("val_compra"));
        m.setMargemCusto(rs.getBigDecimal("margem_custo"));
        m.setValCusto(rs.getBigDecimal("val_custo"));
        m.setMargemLucro(rs.getBigDecimal("margem_lucro"));
        m.setValVenda(rs.getBigDecimal("val_venda"));
        m.setMargemMinimo(rs.getBigDecimal("margem_minimo"));
        m.setValMinimo(rs.getBigDecimal("val_minimo"));
        m.setPesoBruto(rs.getBigDecimal("peso_bruto"));
        m.setPesoLiquido(rs.getBigDecimal("peso_liquido"));
        m.setValPauta(rs.getBigDecimal("val_pauta"));
        m.setCodBarra(rs.getString("cod_barra"));
        m.setReferencia(rs.getString("referencia"));
        m.setTamanho(rs.getString("tamanho"));
        m.setAltura(rs.getBigDecimal("altura"));
        m.setLargura(rs.getBigDecimal("largura"));
        m.setComprimento(rs.getBigDecimal("comprimento"));
        m.setComissao(rs.getBigDecimal("comissao"));
        m.setTipo(rs.getString("tipo"));

        ConfigFiscal cf = new ConfigFiscal();
        cf.setId(rs.getInt("id_config_fiscal"));
        cf.setNome(rs.getString("conf_nome"));
        m.setConfigFiscal(cf);

        m.setAtivo(rs.getBoolean("ativo"));
        m.setQuantidade(rs.getObject("quantidade") != null ? rs.getInt("quantidade") : 0);
        m.setNcm(rs.getString("ncm"));
        m.setCest(rs.getString("cest"));
        m.setUsarPrescricao(rs.getBoolean("usar_prescricao"));
        m.setCprod(rs.getString("cprod"));

        return m;
    }
}
