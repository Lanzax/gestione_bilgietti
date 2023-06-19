package model;

import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "utenti")
@NamedQuery(name = "check_tessera", query = "SELECT u FROM Utente u WHERE u.scadenzaTessera < NOW()")
public class Utente {

	@Id
	@SequenceGenerator(name = "numero_tessera", sequenceName = "numero_tessera", allocationSize = 1, initialValue = 5000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "numero_tessera")
	private Integer tessera;

	@Column(name = "emissione_tessera", nullable = false)
	private LocalDate emissioneTessera;

	@Column(name = "rinnovo_tessera")
	private LocalDate rinnovoTessera = null;

	@Column(name = "scadenza_tessera", nullable = false)
	private LocalDate scadenzaTessera;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String cognome;

	@Column(nullable = false)
	private LocalDate dataNascita;


	@OneToMany(mappedBy = "titolare", fetch = FetchType.EAGER)
	private List<Abbonamento> abbonamentiAcquistati = new ArrayList<Abbonamento>();

	public Utente() {
		super();
	}

	public Utente(String nome, String cognome, LocalDate dataNascita) {
		super();
		this.emissioneTessera = LocalDate.now();
		this.scadenzaTessera = rinnovoTessera != null ? this.rinnovoTessera.plusYears(1)
				: this.emissioneTessera.plusYears(1);
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
	}

	public Utente(LocalDate emissioneTessera, String nome, String cognome, LocalDate dataNascita) {
		super();
		this.emissioneTessera = emissioneTessera;
		this.scadenzaTessera = this.emissioneTessera.plusYears(1);
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
	}

	public Integer getTessera() {
		return tessera;
	}

	public LocalDate getEmissioneTessera() {
		return emissioneTessera;
	}

	public void setEmissioneTessera(LocalDate emissioneTessera) {
		this.emissioneTessera = emissioneTessera;
	}

	public LocalDate getRinnovoTessera() {
		return rinnovoTessera;
	}

	public void setRinnovoTessera(LocalDate rinnovoTessera) {
		this.rinnovoTessera = rinnovoTessera;
		this.scadenzaTessera = rinnovoTessera.plusYears(1);
	}

	public LocalDate getScadenzaTessera() {
		return scadenzaTessera;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public List<Abbonamento> getAbbonamentiAcquistati() {
		return abbonamentiAcquistati;
	}
	public boolean isValido() {
		boolean validita = (this.scadenzaTessera.compareTo(LocalDate.now()) > 0) ? true : false;
		return validita;
	}

	@Override
	public String toString() {
		return "Utente [tessera=" + tessera + ", emissioneTessera=" + emissioneTessera + ", rinnovoTessera="
				+ rinnovoTessera + ", scadenzaTessera=" + scadenzaTessera + ", nome=" + nome + ", cognome=" + cognome
				+ ", dataNascita=" + dataNascita + ", titoliAcquistati=" + abbonamentiAcquistati.size() + "]";
	}

}
