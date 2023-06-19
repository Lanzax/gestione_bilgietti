package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import DAO.BiglietteriaDAO;
import DAO.LuogoDAO;
import DAO.MezzoDAO;
import DAO.TitoloViaggioDAO;
import DAO.TrattaDAO;
import DAO.UtenteDAO;
import DAO.ViaggioDAO;
import model.Abbonamento;
import model.Biglietteria;
import model.Biglietto;
import model.Distributore;
import model.Luogo;
import model.Mezzo;
import model.Rivenditore;
import model.Tratta;
import model.Utente;
import model.Viaggio;
import utils.DurataAbb;
import utils.JpaUtil;
import utils.TipoMezzo;
import utils.TipoNegozio;

public class MainProject {

	public static void main(String[] args) {
		JpaUtil.getEntityManagerFactory().createEntityManager();

		UtenteDAO DAO_utente = new UtenteDAO();
		TitoloViaggioDAO DAO_titolo = new TitoloViaggioDAO();
		BiglietteriaDAO DAO_biglietteria = new BiglietteriaDAO();
		LuogoDAO DAO_luogo = new LuogoDAO();
		MezzoDAO DAO_mezzo = new MezzoDAO();
		TrattaDAO DAO_tratta = new TrattaDAO();
		ViaggioDAO DAO_viaggio = new ViaggioDAO();

//		riempiDB(DAO_utente, DAO_titolo, DAO_biglietteria, DAO_luogo, DAO_mezzo, DAO_tratta);

		DAO_titolo.getTitoliFromDate(LocalDate.of(2023, 5, 23), LocalDate.of(2023, 5, 25));

		List<Mezzo> mezzi = DAO_mezzo.getAllMezzi();
		List<Biglietto> biglietti = DAO_titolo.getAllBiglietti();
		List<Abbonamento> abbonamenti = DAO_titolo.getAllAbbonamenti();
		List<Luogo> luoghi = DAO_luogo.getAllLuoghi();
		List<Tratta> tratte = DAO_tratta.getAllTratte();
		List<Viaggio> viaggi = DAO_viaggio.getAllViaggi();

		mezzi.get(0).validaBiglietto(biglietti.get(2));
		mezzi.get(1).validaBiglietto(biglietti.get(3), LocalDate.of(2023, 4, 12));
		mezzi.get(0).validaBiglietto(biglietti.get(1));
		mezzi.get(1).validaBiglietto(biglietti.get(5), LocalDate.of(2023, 1, 28));
		mezzi.get(1).validaAbbonamento(abbonamenti.get(6));

		mezzi = DAO_mezzo.getAllMezzi();

		DAO_titolo.getTitoliFromMezzo(1);
		DAO_titolo.getTitoliVidimatiPeriodo(LocalDate.of(2023, 1, 1), LocalDate.now());
		DAO_utente.getTessereScadute();

		Tratta t1 = new Tratta(luoghi.get(0), luoghi.get(1), 73.5);
		Tratta t2 = new Tratta(luoghi.get(1), luoghi.get(0), 80.5);

		DAO_tratta.save(t1);
		DAO_tratta.save(t2);

		Mezzo m1 = mezzi.get(0);

		m1.percorriTratta(t2, LocalTime.now(), LocalTime.now().plusMinutes(t2.getTempoStimato()));
		m1.percorriTratta(t2, LocalTime.now(), LocalTime.now().plusMinutes(t2.getTempoStimato()));
		m1.percorriTratta(t2, LocalTime.now(), LocalTime.now().plusMinutes(t2.getTempoStimato()));

		DAO_mezzo.viaggiPercorsiSuTratta(m1.getId(), t2.getNumeroTratta());

		menuNavigazione(DAO_utente, DAO_titolo, DAO_biglietteria, DAO_luogo, DAO_mezzo, DAO_tratta);
	}

	public static void riempiDB(UtenteDAO DAO_utente, TitoloViaggioDAO DAO_titoloViaggio,
			BiglietteriaDAO DAO_biglietteria, LuogoDAO DAO_luogo, MezzoDAO DAO_mezzo, TrattaDAO DAO_tratta) {

		List<Luogo> luoghi = new ArrayList<>();

		luoghi.add(new Luogo("TO", "Torino", "Torino Porta Nuova"));
		luoghi.add(new Luogo("MI", "Milano", "Milano Centrale"));
		luoghi.add(new Luogo("RM", "Roma", "Roma Termini"));
		luoghi.add(new Luogo("NA", "Napoli", "Napoli Centrale"));
		luoghi.add(new Luogo("FI", "Firenze", "Firenze Santa Maria Novella"));

		for (Luogo luogo : luoghi) {
			DAO_luogo.save(luogo);
		}

		List<Utente> utenti = new ArrayList<>();
		utenti.add(new Utente("Emanuele", "Rossi", LocalDate.of(1997, 1, 27)));
		utenti.add(new Utente("Sara", "Bianchi", LocalDate.of(1997, 9, 23)));
		utenti.add(new Utente("Alessio", "Viola", LocalDate.of(1989, 4, 27)));
		utenti.add(new Utente(LocalDate.of(2023, 2, 28), "Marco", "Verdi", LocalDate.of(1978, 12, 10)));
		utenti.add(new Utente(LocalDate.of(2023, 1, 12), "Luigi", "Gialli", LocalDate.of(1983, 8, 1)));
		utenti.add(new Utente(LocalDate.of(2023, 3, 25), "Flavio", "Blu", LocalDate.of(1987, 7, 8)));
		utenti.add(new Utente(LocalDate.of(2022, 9, 5), "Giulia", "Russo", LocalDate.of(1982, 6, 15)));
		utenti.add(new Utente(LocalDate.of(2022, 4, 30), "Federica", "Arancio", LocalDate.of(1983, 2, 14)));
		utenti.add(new Utente(LocalDate.of(2021, 12, 6), "Giovanni", "Neri", LocalDate.of(1975, 4, 5)));
		utenti.add(new Utente(LocalDate.of(2020, 4, 18), "Alessia", "Marroni", LocalDate.of(1981, 10, 30)));
		for (Utente utente : utenti) {
			DAO_utente.save(utente);
		}

		List<Biglietteria> biglietterie = new ArrayList<>();

		List<Rivenditore> rivenditori = new ArrayList<>();

		rivenditori.add(new Rivenditore(TipoNegozio.BAR, luoghi.get(0)));
		rivenditori.add(new Rivenditore(TipoNegozio.EDICOLA, luoghi.get(0)));
		rivenditori.add(new Rivenditore(TipoNegozio.TABACCHI, luoghi.get(1)));
		rivenditori.add(new Rivenditore(TipoNegozio.BAR, luoghi.get(1)));
		rivenditori.add(new Rivenditore(TipoNegozio.EDICOLA, luoghi.get(2)));
		rivenditori.add(new Rivenditore(TipoNegozio.TABACCHI, luoghi.get(2)));
		rivenditori.add(new Rivenditore(TipoNegozio.BAR, luoghi.get(3)));
		rivenditori.add(new Rivenditore(TipoNegozio.EDICOLA, luoghi.get(3)));
		rivenditori.add(new Rivenditore(TipoNegozio.TABACCHI, luoghi.get(4)));
		rivenditori.add(new Rivenditore(TipoNegozio.BAR, luoghi.get(4)));

		for (Rivenditore rivenditore : rivenditori) {
			DAO_biglietteria.save(rivenditore);
			biglietterie.add(rivenditore);
		}

		List<Distributore> distributori = new ArrayList<>();

		distributori.add(new Distributore(false, luoghi.get(0)));
		distributori.add(new Distributore(luoghi.get(0)));
		distributori.add(new Distributore(luoghi.get(1)));
		distributori.add(new Distributore(luoghi.get(1)));
		distributori.add(new Distributore(false, luoghi.get(2)));
		distributori.add(new Distributore(luoghi.get(2)));
		distributori.add(new Distributore(luoghi.get(3)));
		distributori.add(new Distributore(luoghi.get(3)));
		distributori.add(new Distributore(luoghi.get(4)));
		distributori.add(new Distributore(false, luoghi.get(4)));

		for (Distributore distributore : distributori) {
			DAO_biglietteria.save(distributore);
			biglietterie.add(distributore);
		}

		List<Biglietto> biglietti = new ArrayList<>();

		biglietti.add(new Biglietto(LocalDate.of(2023, 5, 1), biglietterie.get(0)));
		biglietti.add(new Biglietto(LocalDate.of(2023, 5, 2), biglietterie.get(1)));
		biglietti.add(new Biglietto(LocalDate.of(2023, 5, 3), biglietterie.get(2)));
		biglietti.add(new Biglietto(LocalDate.of(2023, 5, 4), biglietterie.get(3)));
		biglietti.add(new Biglietto(LocalDate.of(2023, 5, 5), biglietterie.get(4)));
		biglietterie.get(6).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
		biglietterie.get(6).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
		biglietterie.get(7).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
		biglietterie.get(9).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
		biglietterie.get(12).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
		biglietterie.get(16).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);

		for (Biglietto biglietto : biglietti) {
			DAO_titoloViaggio.save(biglietto);
		}

		List<Abbonamento> abbonamenti = new ArrayList<>();

		abbonamenti.add(
				new Abbonamento(LocalDate.of(2023, 5, 1), DurataAbb.SETTIMANALE, biglietterie.get(0), utenti.get(0)));
		abbonamenti
				.add(new Abbonamento(LocalDate.of(2023, 4, 2), DurataAbb.MENSILE, biglietterie.get(0), utenti.get(1)));
		abbonamenti.add(
				new Abbonamento(LocalDate.of(2023, 4, 3), DurataAbb.SETTIMANALE, biglietterie.get(4), utenti.get(2)));
		abbonamenti
				.add(new Abbonamento(LocalDate.of(2023, 5, 8), DurataAbb.MENSILE, biglietterie.get(5), utenti.get(3)));
		abbonamenti.add(
				new Abbonamento(LocalDate.of(2023, 5, 5), DurataAbb.SETTIMANALE, biglietterie.get(11), utenti.get(4)));
		abbonamenti.add(
				new Abbonamento(LocalDate.of(2023, 5, 6), DurataAbb.SETTIMANALE, biglietterie.get(13), utenti.get(5)));
		abbonamenti
				.add(new Abbonamento(LocalDate.of(2023, 5, 7), DurataAbb.MENSILE, biglietterie.get(15), utenti.get(6)));
		abbonamenti.add(
				new Abbonamento(LocalDate.of(2023, 5, 8), DurataAbb.SETTIMANALE, biglietterie.get(18), utenti.get(7)));
		biglietterie.get(0).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.SETTIMANALE,
				utenti.get(0));
		biglietterie.get(15).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.MENSILE, utenti.get(1));
		biglietterie.get(15).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.SETTIMANALE,
				utenti.get(4));
		biglietterie.get(12).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.MENSILE, utenti.get(5));
		biglietterie.get(11).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.SETTIMANALE,
				utenti.get(7));
		biglietterie.get(9).emettiAbbonamento(DAO_titoloViaggio, DAO_biglietteria, DurataAbb.SETTIMANALE,
				utenti.get(8));

		for (Abbonamento abbonamento : abbonamenti) {
			DAO_titoloViaggio.save(abbonamento);
		}

		List<Mezzo> mezzi = new ArrayList<>();

		mezzi.add(new Mezzo(TipoMezzo.AUTOBUS, LocalDate.of(2023, 2, 10)));
		mezzi.add(new Mezzo(TipoMezzo.TRAM, LocalDate.of(1996, 12, 26)));
		mezzi.add(new Mezzo(TipoMezzo.AUTOBUS, LocalDate.of(2000, 5, 16), LocalDate.of(2023, 5, 24)));

		for (Mezzo mezzo : mezzi) {
			DAO_mezzo.save(mezzo);
		}

	}

	public static Scanner sc = new Scanner(System.in);

	public static void menuNavigazione(UtenteDAO DAO_utente, TitoloViaggioDAO DAO_titoloViaggio,
			BiglietteriaDAO DAO_biglietteria, LuogoDAO DAO_luogo, MezzoDAO DAO_mezzo, TrattaDAO DAO_tratta) {
		System.out.println("Benvenuto nella biglietteria ATAC");
		Luogo stazionePartenza = menuStazione(DAO_luogo);
		System.out.println("Hai scelto di partire dalla stazione :" + stazionePartenza);
		System.out.println("Hai una tessera?");

		boolean risp = sc.nextBoolean();
		if (risp) {
			Utente utente = menuUtente(DAO_utente);
			if (utente != null) {
				menuAbbonamento(utente, DAO_luogo, stazionePartenza, DAO_biglietteria, DAO_titoloViaggio);
			} else {
				menuBiglietto(DAO_luogo, stazionePartenza, DAO_biglietteria, DAO_titoloViaggio);
			}
		} else {
			menuBiglietto(DAO_luogo, stazionePartenza, DAO_biglietteria, DAO_titoloViaggio);
		}

//		Integer stazione=0;
//		System.out.println("Sei in possesso della tessera? true/false");
//		boolean risp = sc.nextBoolean();
//		//sc.close();
//		if (risp) {
//			System.out.println("Inserisci il numero della tessera");
//			Integer tessera = sc.nextInt();
//			//sc.close();
//		    Utente u = DAO_utente.getByTessera(tessera);
//		    if (u!= null) {
//		    	if(u.isValido()) {
//		    		
//		    		List<Abbonamento> lista = u.getAbbonamentiAcquistati();
//		    		lista.stream().filter(a->a.isValido()).collect(Collectors.toList()).forEach(a-> System.out.println("Abbonamento valido: n. "+ a.getCodice() + " data scadenza - " + a.getDataScadenza()));
//		    		stazione = lista.stream().filter(a->a.isValido()).collect(Collectors.toList()).get(0).getLuogoEmissione().getId();
//		    	}else {
//		    		System.out.println("Vuoi rinnovare la tua tessera??true/false");
//		    		risp = sc.nextBoolean();
//		    		if (risp) {
//		    			u.setRinnovoTessera(LocalDate.now());
//		    		}
//		    	}
//		    }
//		    else {
//		    	System.out.println("Utente non trovato!");
//		    }
//		}
//		else {
//			System.out.println("Per iniziare compra un biglietto!");
//			List<Luogo> luoghi = DAO_luogo.getAllLuoghi();
//			Luogo luogo = luoghi.get(0);
//			do {
//			System.out.println("Seleziona la stazione di partenza: ");
//			luoghi.forEach(l-> System.out.println(l.getId() + " - " + l.getNome()));
//			stazione = sc.nextInt();
//			//sc.close();
//			luogo= DAO_luogo.getById(stazione);
//			if (stazione<1 | stazione>luoghi.size()) {
//					System.out.println("Scelta non valida!");
//				}
//				else {
//					System.out.println("Stazione scelta: " + luogo.getNome() + " (" + luogo.getCitta()+ ")");
//				}
//			}
//			while (stazione<1 & stazione>luoghi.size());
//			System.out.println("Seleziona la biglietteria: ");
//			DAO_luogo.trovaBiglietteria(stazione).forEach(b-> System.out.println(b.getId() + " : " + b.getLuogo().getNome()));
//			Integer b = sc.nextInt();
//			DAO_biglietteria.getById(b).emettiBiglietto(DAO_titoloViaggio, DAO_biglietteria);
//			}
//		System.out.println(DAO_tratta.getById(stazione));

	}

	// cerca stazione
	// Identifica utente -> se abbonamento valido -> tratta oppure ->se abbonamento
	// scaduto -> rinnova o ->compra bligietto
	// trovi distributore
	// abbonamento
	// biglietto

	public static Luogo menuStazione(LuogoDAO DAO_luogo) {
		List<Luogo> luoghi = DAO_luogo.getAllLuoghi();
		luoghi.forEach(l -> System.out.println(l.getId() + " " + l.getNome()));
		System.out.println("inserisci un id della stazione da dove vuoi partire");
		Integer risp = sc.nextInt();
		Luogo cercato = DAO_luogo.getById(risp);
		return cercato;
	}

	public static Utente menuUtente(UtenteDAO DAO_utente) {
		System.out.println("Inserisci il numero della tessera");
		Utente utente = DAO_utente.getByTessera(sc.nextInt());

		if (utente != null) {
			if (utente.isValido()) {
				return utente;
			} else {
				System.out.println("La tua tessera è scaduta");
				System.out.println("Vuoi Rinnovarla? true/false");
				boolean risp = sc.nextBoolean();
				if (risp) {
					utente.setRinnovoTessera(LocalDate.now());
					DAO_utente.update(utente);
					System.out.println("Tessera Rinnovata");
					return utente;

				} else {
					System.out.println("Rivolgiti ad una Biglietteria ");
					return null;
				}
			}
		} else {
			System.out.println("Utente non trovato!");
		}

		return null;
	}

	public static Biglietto menuBiglietto(LuogoDAO DAO_luogo, Luogo stazione, BiglietteriaDAO DAO_bigl,
			TitoloViaggioDAO DAO_tit) {
		DAO_luogo.trovaBiglietteria(stazione.getId())
				.forEach(b -> System.out.println(b.getId() + " : " + b.getLuogo().getNome()));
		Integer risp = sc.nextInt();
		Biglietteria scelta = DAO_bigl.getById(risp);
		if (scelta != null) {
			Biglietto bigl = scelta.emettiBiglietto(DAO_tit, DAO_bigl);
			System.out.println("Hai un biglietto, scegli il tuo viaggio");
			return bigl;
		} else {
			System.out.println("Biglietteria non presente");
		}
		return null;
	}

	public static Abbonamento menuAbbonamento(Utente utente, LuogoDAO DAO_luogo, Luogo stazione,
			BiglietteriaDAO DAO_bigl, TitoloViaggioDAO DAO_tit) {
		List<Abbonamento> listAbbUtente = utente.getAbbonamentiAcquistati();
		if (listAbbUtente.size() > 0) {

			List<Abbonamento> listAbbValidi = listAbbUtente.stream().filter(a -> a.isValido())
					.collect(Collectors.toList());
			if (listAbbValidi.size() > 0) {
				System.out.println("Hai un abbonamento valido, scegli il tuo viaggio");
				return listAbbValidi.get(0);

			}
		} else {
			System.out.println("Non hai abbonamenti Validi");
			System.out.println("Vuoi farne uno nuovo??true/false");
			boolean risp = sc.nextBoolean();
			if (risp) {
				DAO_luogo.trovaBiglietteria(stazione.getId())
						.forEach(b -> System.out.println(b.getId() + " : " + b.getLuogo().getNome()));
				Integer risp2 = sc.nextInt();
				Biglietteria scelta = DAO_bigl.getById(risp2);
				if (scelta != null) {
					System.out.println("Scegli durata abbonamento.1->settimale 2->mensile");
					Integer durata = sc.nextInt();
					DurataAbb durataScelta;
					do {

						
						switch (durata) {
						case 1:
							durataScelta = DurataAbb.SETTIMANALE;
							break;
						case 2:
							durataScelta = DurataAbb.MENSILE;
							break;
						default:
							System.out.println("non hai scelto nessuna durata");
						}
					} while (durata < 1 | durata > 2);
					Abbonamento abb = 	scelta.emettiAbbonamento(DAO_tit,DAO_bigl,durata,utente);
					System.out.println("Hai un abbonamento, scegli il tuo viaggio");
					return abb;
				} else {
					System.out.println("Biglietteria non presente");
				}
			}

		}

		return listAbbUtente;
	}

}
