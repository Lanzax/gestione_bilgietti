package model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import utils.DurataAbb;

@Entity
@DiscriminatorValue("abbonamenti")
public class Abbonamento extends TitoloViaggio {

	@Enumerated(EnumType.STRING)
	private DurataAbb durata;

	@ManyToOne(fetch = FetchType.EAGER)
	private Utente titolare;

	@Column(name = "data_scadenza")
	private LocalDate dataScadenza = null;

	public Abbonamento() {
		super();
	}

	public Abbonamento(Utente titolare) {
		super();
		this.titolare = titolare;
	}

	public Abbonamento(DurataAbb durata, Biglietteria luogoEmissione, Utente titolare) {
		super(luogoEmissione);
		this.titolare = titolare;
		this.durata = durata;
		this.dataScadenza = durata.equals(DurataAbb.SETTIMANALE) ? LocalDate.now().plusDays(7)
				: LocalDate.now().plusMonths(1);
	}

	public Abbonamento(LocalDate dataEmissione, DurataAbb durata, Biglietteria luogoEmissione, Utente titolare) {
		super(dataEmissione, luogoEmissione);
		this.titolare = titolare;
		this.durata = durata;
		this.dataScadenza = durata.equals(DurataAbb.SETTIMANALE) ? LocalDate.now().plusDays(7)
				: LocalDate.now().plusMonths(1);
	}

	public Utente getTitolare() {
		return titolare;
	}

	public void setTitolare(Utente titolare) {
		this.titolare = titolare;
	}

	public LocalDate getDataScadenza() {
		return dataScadenza;
	}

	@Override
	public String toString() {
		return "Abbonamento [ " + super.toString() + "titolare=" + titolare + ", dataScadenza=" + dataScadenza + "]";
	}

	public boolean isValido() {
		boolean validita = (this.dataScadenza.compareTo(LocalDate.now()) > 0) ? true : false;
		return validita;
	}
}
