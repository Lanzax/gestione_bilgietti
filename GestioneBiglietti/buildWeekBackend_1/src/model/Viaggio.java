package model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "viaggi")
public class Viaggio {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Mezzo mezzo;

	@ManyToOne(fetch = FetchType.EAGER)
	private Tratta tratta;

	@Column(name = "orario_partenza")
	private LocalTime orarioPartenza;

	@Column(name = "orario_arrivo")
	private LocalTime orarioArrivo;

	@Column(name = "tempo_effettivo_min")
	private Long tempoEffettivo;

	public Viaggio() {
		super();
	}

	public Viaggio(Mezzo mezzo, Tratta tratta, LocalTime orarioPartenza, LocalTime orarioArrivo) {
		super();
		this.mezzo = mezzo;
		this.tratta = tratta;
		this.orarioPartenza = orarioPartenza;
		this.orarioArrivo = orarioArrivo;
		this.tempoEffettivo = orarioPartenza.until(orarioArrivo, ChronoUnit.MINUTES);
	}

	public Integer getId() {
		return id;
	}

	public Mezzo getMezzo() {
		return mezzo;
	}

	public Tratta getTratta() {
		return tratta;
	}

	public LocalTime getOrarioPartenza() {
		return orarioPartenza;
	}

	public void setOrarioPartenza(LocalTime orarioPartenza) {
		this.orarioPartenza = orarioPartenza;
	}

	public Long getTempoEffettivo() {
		return tempoEffettivo;
	}
	
	public LocalTime getOrarioArrivo() {
		return orarioArrivo;
	}

	public void setOrarioArrivo(LocalTime orarioArrivo) {
		this.orarioArrivo = orarioArrivo;
		this.tempoEffettivo = (long) orarioArrivo.compareTo(orarioPartenza);
	}

	@Override
	public String toString() {
		return "Viaggio [mezzo=" + mezzo + ", orarioPartenza=" + orarioPartenza + ", orarioArrivo=" + orarioArrivo
				+ ", tempoEffettivo=" + tempoEffettivo + "]";
	}

}
