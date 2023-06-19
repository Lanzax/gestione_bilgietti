package model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "titoli_viaggio")
@DiscriminatorColumn(name = "tipo_biglietto", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name = "titolo_emesso_date", query = "SELECT t.biglietteriaEmissione.id, COUNT(t) FROM TitoloViaggio t WHERE t.dataEmissione BETWEEN :data1 AND :data2 GROUP BY t.biglietteriaEmissione.id")
@NamedQuery(name = "titolo_vidimato_su", query = "SELECT t  FROM TitoloViaggio t WHERE t.utilizzatoSu.id = :id")
@NamedQuery(name = "titolo_vidimato_date", query = "SELECT t.utilizzatoSu.id, COUNT(t) FROM TitoloViaggio t WHERE t.dataVidimazione BETWEEN :data1 AND :data2 GROUP BY t.utilizzatoSu.id")
public abstract class TitoloViaggio {

	@Id
	@SequenceGenerator(name = "codice_biglietto", sequenceName = "codice_biglietto", allocationSize = 1, initialValue = 1000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codice_biglietto")
	private Integer codice;

	@Column(name = "data_emissione", nullable = false)
	private LocalDate dataEmissione;

	@ManyToOne(fetch = FetchType.EAGER)
	private Biglietteria biglietteriaEmissione;

	public TitoloViaggio() {
		super();
	}

	public TitoloViaggio(Biglietteria luogoEmissione) {
		super();
		this.dataEmissione = LocalDate.now();
		this.biglietteriaEmissione = luogoEmissione;
	}

	public TitoloViaggio(LocalDate dataEmissione, Biglietteria luogoEmissione) {
		super();
		this.dataEmissione = dataEmissione;
		this.biglietteriaEmissione = luogoEmissione;
	}

	public Integer getCodice() {
		return codice;
	}

	public LocalDate getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(LocalDate dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public Biglietteria getLuogoEmissione() {
		return biglietteriaEmissione;
	}

	public void setLuogoEmissione(Biglietteria luogoEmissione) {
		this.biglietteriaEmissione = luogoEmissione;
	}

	@Override
	public String toString() {
		return "TitoloViaggio [codice=" + codice + ", dataEmissione=" + dataEmissione + ", luogoEmissione="
				+ biglietteriaEmissione + "]";
	}

}
