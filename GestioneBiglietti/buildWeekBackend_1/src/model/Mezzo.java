package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import DAO.MezzoDAO;
import DAO.TitoloViaggioDAO;
import DAO.TrattaDAO;
import DAO.ViaggioDAO;
import utils.Status;
import utils.TipoMezzo;

@Entity
@Table(name = "mezzi")
@NamedQuery(name = "numero_viaggi_tratta", query = "SELECT COUNT(v), AVG(v.tempoEffettivo) FROM Viaggio v WHERE v.mezzo.id = :id AND v.tratta.numeroTratta = :nTratta")
public class Mezzo {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer id;

	private Integer capienza;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_mezzo")
	private TipoMezzo tipoMezzo;

	@Enumerated(EnumType.STRING)
	private Status stato;
	
	@Column(name = "velocita", nullable = false)
	private Integer velocita;

	@Column(name = "inizio_servizio")
	private LocalDate inizioServizio;

	@Column(name = "fine_servizio")
	private LocalDate fineServizio;

	@OneToMany(mappedBy = "utilizzatoSu")
	private List<Biglietto> vidimati = new ArrayList<Biglietto>();

	@OneToMany(mappedBy = "mezzo")
	private List<Viaggio> viaggiPercorsi = new ArrayList<Viaggio>();

	public Mezzo() {
		super();
	}

	public Mezzo(TipoMezzo tipoMezzo, LocalDate inizioServizio) {
		super();
		this.inizioServizio = inizioServizio;
		this.tipoMezzo = tipoMezzo;
		this.stato = Status.IN_SERVIZIO;
		this.capienza = tipoMezzo.equals(TipoMezzo.TRAM) ? 4 : 6;
		this.velocita = tipoMezzo.equals(TipoMezzo.TRAM) ? 60 : 40;
	}

	public Mezzo(TipoMezzo tipoMezzo, LocalDate inizioServizio, LocalDate fineServizio) {
		super();
		this.stato = (fineServizio.compareTo(LocalDate.now()) > 0) ? Status.IN_SERVIZIO : Status.IN_MANUTENZIONE;
		this.inizioServizio = inizioServizio;
		this.fineServizio = fineServizio;
		this.tipoMezzo = tipoMezzo;
		this.capienza = tipoMezzo.equals(TipoMezzo.TRAM) ? 4 : 6;
		this.velocita = tipoMezzo.equals(TipoMezzo.TRAM) ? 60 : 40;
	}

	public Integer getId() {
		return id;
	}

	public Integer getCapienza() {
		return capienza;
	}

	public TipoMezzo getTipoMezzo() {
		return tipoMezzo;
	}
	
	public Status getStato() {
		Status statoAttuale = (this.fineServizio != null)
				? ((this.fineServizio.compareTo(LocalDate.now()) > 0) ? Status.IN_SERVIZIO : Status.IN_MANUTENZIONE)
				: Status.IN_SERVIZIO;
		return statoAttuale;
	}
	
	public Integer getVelocita() {
		return velocita;
	}

	public LocalDate getInizioServizio() {
		return inizioServizio;
	}

	public void setInizioServizio(LocalDate inizioServizio) {
		this.inizioServizio = inizioServizio;
	}

	public LocalDate getFineServizio() {
		return fineServizio;
	}

	public void setFineServizio(LocalDate fineServizio) {
		this.fineServizio = fineServizio;
	}

	public List<Biglietto> getVidimati() {
		return vidimati;
	}

	public List<Viaggio> getViaggiPercorsi() {
		return viaggiPercorsi;
	}

	public void validaBiglietto(Biglietto b) {
		if (this.id != null) {
			if (b.getCodice() != null) {
				if (!b.isVidimato()) {
					MezzoDAO DAO_mezzo = new MezzoDAO();
					Mezzo mezzo = DAO_mezzo.getById(this.id);
					TitoloViaggioDAO DAO_titolo = new TitoloViaggioDAO();
					Biglietto biglietto = (Biglietto) DAO_titolo.getByCodice(b.getCodice());

					biglietto.setUtilizzatoSu(mezzo);
					System.out.println("Questo biglietto è stato vidimato");
					DAO_titolo.update(biglietto);
					DAO_mezzo.update(mezzo);
				} else {
					System.out.println("Questo biglietto è stato già utilizzato");
				}
			} else {
				System.out.println("Biglietto non riconosciuto");
			}
		}
	}

	public void validaBiglietto(Biglietto b, LocalDate dataVid) {
		if (this.id != null) {
			if (b.getCodice() != null) {
				if (!b.isVidimato()) {
					MezzoDAO DAO_mezzo = new MezzoDAO();
					Mezzo mezzo = DAO_mezzo.getById(this.id);
					TitoloViaggioDAO DAO_titolo = new TitoloViaggioDAO();
					Biglietto biglietto = (Biglietto) DAO_titolo.getByCodice(b.getCodice());

					biglietto.setUtilizzatoSu(mezzo, dataVid);

					System.out.println("Questo biglietto è stato vidimato");
					DAO_titolo.update(biglietto);
					DAO_mezzo.update(mezzo);
				} else {
					System.out.println("Questo biglietto è stato già utilizzato");
				}
			} else {
				System.out.println("Biglietto non riconosciuto");
			}
		}
	}

	public void validaAbbonamento(Abbonamento a) {
		if (this.id != null) {
			if (a.getCodice() != null) {
				if (a.getDataScadenza().compareTo(LocalDate.now()) > 0) {
					MezzoDAO DAO_mezzo = new MezzoDAO();
					Mezzo mezzo = DAO_mezzo.getById(this.id);
					TitoloViaggioDAO DAO_titolo = new TitoloViaggioDAO();
					Abbonamento abbonamento = (Abbonamento) DAO_titolo.getByCodice(a.getCodice());
					System.out.println("Questo Abbonamento è ancora valido");
				} else {
					System.out.println("Questo Abbonamento è scaduto");
				}
			} else {
				System.out.println("Abbonamento non riconosciuto");
			}
		}
	}

	public void percorriTratta(Tratta t, LocalTime orarioPartenza, LocalTime orarioArrivo) {
		if (this.id != null && this.stato != Status.IN_MANUTENZIONE) {
			if (t.getNumeroTratta() != null) {
				MezzoDAO DAO_mezzo = new MezzoDAO();
				TrattaDAO DAO_tratta = new TrattaDAO();
				Mezzo mezzo = DAO_mezzo.getById(this.id);
				Tratta tratta = DAO_tratta.getById(t.getNumeroTratta());

				Viaggio nuovoViaggio = new Viaggio(this, tratta, orarioPartenza, orarioArrivo);

				new ViaggioDAO().save(nuovoViaggio);
				DAO_mezzo.update(mezzo);
				DAO_tratta.update(tratta);

				System.out.println("Viaggio creato!");
			} else {
				System.out.println("Questa tratta non esiste");
			}
		} else {
			System.out.println("Il mezzo non è in condizione di viaggiare");
		}
	};

	@Override
	public String toString() {
		return "Mezzo [id=" + id + ", capienza=" + capienza + ", stato=" + stato + ", tipoMezzo=" + tipoMezzo
				+ ", inizioServizio=" + inizioServizio + ", fineServizio=" + fineServizio + ", vidimati="
				+ vidimati.size() + "]";
	}

}
