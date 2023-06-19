package model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DAO.BiglietteriaDAO;
import DAO.TitoloViaggioDAO;
import utils.DurataAbb;

@Entity
@Table(name = "biglietterie")
@DiscriminatorColumn(name = "tipo_biglietteria", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Biglietteria {
	private static Logger log = LoggerFactory.getLogger(Biglietteria.class);
	@Id
	@SequenceGenerator(name = "id_biglietteria", sequenceName = "id_biglietteria", allocationSize = 1, initialValue = 3000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_biglietteria")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Luogo luogo;

	@OneToMany(mappedBy = "biglietteriaEmissione", fetch = FetchType.EAGER)
	private List<TitoloViaggio> titoliEmessi = new ArrayList<TitoloViaggio>();

	public Biglietteria() {
		super();
	}

	public Biglietteria(Luogo luogo) {
		super();
		this.luogo = luogo;
	}

	public Integer getId() {
		return id;
	}

	public Luogo getLuogo() {
		return luogo;
	}

	public void setLuogo(Luogo luogo) {
		this.luogo = luogo;
	}

	public List<TitoloViaggio> getTitoliEmessi() {
		return titoliEmessi;
	}

	public void setTitoliEmessi(List<TitoloViaggio> titoliEmessi) {
		this.titoliEmessi = titoliEmessi;
	}

	@Override
	public String toString() {
		return "Biglietteria [id=" + id + ", luogo=" + luogo + ", titoliEmessi=" + titoliEmessi.size() + "]";
	}

	public Biglietto emettiBiglietto(TitoloViaggioDAO titolo_DAO, BiglietteriaDAO bigl_DAO) {
		if (this.id != null) {
			Biglietteria biglietteriaEmissione = bigl_DAO.getById(this.id);
			if (!(biglietteriaEmissione instanceof Rivenditore)) {

				if (biglietteriaEmissione instanceof Distributore
						& !((Distributore) biglietteriaEmissione).isInServizio()) {
					log.error("Distributore fuori servizio!!");
				} else {
					TitoloViaggio t = new Biglietto(biglietteriaEmissione);
					titolo_DAO.save(t);
					return  ((Biglietto)t);
				}
			} else {
				TitoloViaggio t = new Biglietto(biglietteriaEmissione);
				titolo_DAO.save(t);
				return  ((Biglietto)t);
			}
		} else {
			log.error("Nessuna Biglietteria trovata!!");
		}
		return  null;
	}

	public Abbonamento emettiAbbonamento(TitoloViaggioDAO titolo_DAO, BiglietteriaDAO bigl_DAO, DurataAbb durata, Utente u) {
		if (this.id != null) {
			if (u.getTessera() != null) {

				Biglietteria biglietteriaEmissione = bigl_DAO.getById(this.id);
				if (!(biglietteriaEmissione instanceof Rivenditore)) {

					if (biglietteriaEmissione instanceof Distributore
							& !((Distributore) biglietteriaEmissione).isInServizio()) {
						log.error("Distributore fuori servizio!!");
					} else {
						TitoloViaggio t = new Abbonamento(durata, biglietteriaEmissione, u);
						titolo_DAO.save(t);
						return ((Abbonamento)t);
					}

				} else {

					TitoloViaggio t = new Abbonamento(durata, biglietteriaEmissione, u);
					titolo_DAO.save(t);
					return ((Abbonamento)t);
				}
			} else {
				log.error("Utente non registrato!!");
			}

		} else {
			log.error("Nessuna Biglietteria trovata!!");
		}
		return null;
	}

}
