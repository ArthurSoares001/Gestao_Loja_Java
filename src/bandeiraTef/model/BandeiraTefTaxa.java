package bandeiratef.model;

import java.util.Locale;

public class BandeiraTefTaxa {
    // Constantes de Modalidade
    public static final int TP_DEBITO = 1;
    public static final int TP_CREDITO_VISTA = 2;
    public static final int TP_CREDITO_PARC_LOJISTA = 3;
    public static final int TP_CREDITO_PARC_EMISSOR = 4;

    private static final String[] MODALIDADE_LABEL = {
            "Debito",
            "Credito a Vista",
            "Credito Parc. Lojista",
            "Credito Parc. Emissor"
    };

    // Constantes de Adquirente
    public static final int TP_CIELO = 1;
    public static final int TP_REDE = 2;
    public static final int TP_GETNET = 3;
    public static final int TP_STONE = 4;

    private static final String[] ADQUIRENTE_LABEL = {
            "Cielo",
            "Rede",
            "Getnet",
            "Stone"
    };

    private Integer id;
    private Integer idBandeiraTef;
    private Integer tpIntegrado; // 1-4
    private Integer parcelaMin;
    private Integer parcelaMax;
    private Double taxaPercentual = 0.0;
    private Integer prazoRepasseDias;
    private boolean ativo = true;
    private Integer adquirente; // 1-4

    public BandeiraTefTaxa() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdBandeiraTef() {
        return idBandeiraTef;
    }

    public void setIdBandeiraTef(Integer idBandeiraTef) {
        this.idBandeiraTef = idBandeiraTef;
    }

    public Integer getTpIntegrado() {
        return tpIntegrado;
    }

    public void setTpIntegrado(Integer tpIntegrado) {
        this.tpIntegrado = tpIntegrado;
    }

    public Integer getParcelaMin() {
        return parcelaMin;
    }

    public void setParcelaMin(Integer parcelaMin) {
        this.parcelaMin = parcelaMin;
    }

    public Integer getParcelaMax() {
        return parcelaMax;
    }

    public void setParcelaMax(Integer parcelaMax) {
        this.parcelaMax = parcelaMax;
    }

    public Double getTaxaPercentual() {
        return taxaPercentual;
    }

    public void setTaxaPercentual(Double taxaPercentual) {
        this.taxaPercentual = taxaPercentual;
    }

    public Integer getPrazoRepasseDias() {
        return prazoRepasseDias;
    }

    public void setPrazoRepasseDias(Integer prazoRepasseDias) {
        this.prazoRepasseDias = prazoRepasseDias;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getAdquirente() {
        return adquirente;
    }

    public void setAdquirente(Integer adquirente) {
        this.adquirente = adquirente;
    }

    public String getDescricaoModalidade() {
        if (tpIntegrado != null && tpIntegrado >= 1 && tpIntegrado <= 4) {
            return MODALIDADE_LABEL[tpIntegrado - 1];
        }
        return "Outro";
    }

    public String getDescricaoAdquirente() {
        if (adquirente != null && adquirente >= 1 && adquirente <= 4) {
            return ADQUIRENTE_LABEL[adquirente - 1];
        }
        return "Outro";
    }

    public String getFaixaDescricao() {
        int min = (parcelaMin != null) ? parcelaMin : 0;
        int max = (parcelaMax != null) ? parcelaMax : 0;
        if (min == max) {
            return min + "x";
        }
        return min + "-" + max + "x";
    }

    // Validação equivalente a TBandeiraTefTaxa.validar
    public void validar() throws Exception {
        if (tpIntegrado == null || tpIntegrado < 1) {
            throw new Exception("Informe a modalidade!");
        }
        if (parcelaMin == null || parcelaMin < 1) {
            throw new Exception("Parcela minima deve ser >= 1!");
        }
        if (parcelaMax == null || parcelaMax < parcelaMin) {
            throw new Exception("Parcela maxima deve ser >= parcela minima!");
        }
        if (taxaPercentual == null || taxaPercentual < 0) {
            throw new Exception("Taxa nao pode ser negativa!");
        }
        if (prazoRepasseDias == null || prazoRepasseDias < 0) {
            throw new Exception("Prazo de repasse nao pode ser negativo!");
        }
        if (adquirente == null || adquirente < 1) {
            throw new Exception("Informe a adquirente!");
        }
    }

    public void ocopy(BandeiraTefTaxa source) {
        if (source != null) {
            this.id = source.getId();
            this.idBandeiraTef = source.getIdBandeiraTef();
            this.tpIntegrado = source.getTpIntegrado();
            this.parcelaMin = source.getParcelaMin();
            this.parcelaMax = source.getParcelaMax();
            this.taxaPercentual = source.getTaxaPercentual();
            this.prazoRepasseDias = source.getPrazoRepasseDias();
            this.ativo = source.isAtivo();
            this.adquirente = source.getAdquirente();
        }
    }

    @Override
    public BandeiraTefTaxa clone() {
        BandeiraTefTaxa clone = new BandeiraTefTaxa();
        clone.ocopy(this);
        return clone;
    }

    @Override
    public String toString() {
        double taxa = (taxaPercentual != null) ? taxaPercentual : 0.0;
        return getDescricaoModalidade() + " " + getFaixaDescricao() + " (" + String.format(Locale.US, "%.2f", taxa) + "%)";
    }
}
