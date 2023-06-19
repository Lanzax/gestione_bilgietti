package DAO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import InterfaceDAO.ITitoloViaggioDAO;
import model.Abbonamento;
import model.Biglietto;
import model.Mezzo;
import model.TitoloViaggio;
import utils.JpaUtil;

public class TitoloViaggioDAO implements ITitoloViaggioDAO {
	private static Logger log = LoggerFactory.getLogger(TitoloViaggioDAO.class);

	@Override
	public void save(TitoloViaggio t) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
			log.info("TitoloViaggio salvato correttamente");
		} catch (Exception ex) {
			em.getTransaction().rollback();
			log.error(ex.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public void update(TitoloViaggio ti) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(ti);
			em.getTransaction().commit();
			log.info("TitoloViaggio Modificato correttamente dal database");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella Modifica del TitoloViaggio");
		} finally {
			em.close();
		}

	}

	@Override
	public void delete(TitoloViaggio ti) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(ti);
			em.getTransaction().commit();
			log.info("TitoloViaggio rimosso correttamente dal database");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella rimozione del TitoloViaggio");
		} finally {
			em.close();
		}

	}

	@Override
	public TitoloViaggio getByCodice(Integer codice) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
//				em.getTransaction().begin();
			TitoloViaggio u = em.find(TitoloViaggio.class, codice);
//				em.getTransaction().commit();
			return u;
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nel recupero del TitoloViaggio");
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<TitoloViaggio> getAllTitoli() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT u FROM TitoloViaggio u");
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Map<Integer, Long> getTitoliFromDate(LocalDate data1, LocalDate data2) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createNamedQuery("titolo_emesso_date");
			q.setParameter("data1", data1);
			q.setParameter("data2", data2);
			List<Object[]> resultList = q.getResultList();

			Map<Integer, Long> result = new HashMap<>();

			for (Object[] row : resultList) {
				Integer biglietteriaId = (Integer) row[0];
				Long numeroBiglietti = (Long) row[1];
				result.put(biglietteriaId, numeroBiglietti);
			}

			Long totaleBiglietti = 0L;

			System.out.println("Conteggio titoli emessi tra il " + data1 + " ed il " + data2);
			for (Map.Entry<Integer, Long> entry : result.entrySet()) {
				Integer biglietteriaId = entry.getKey();
				Long numeroBiglietti = entry.getValue();
				totaleBiglietti += numeroBiglietti;
				System.out.println(
						"ID Biglietteria: " + biglietteriaId + ", Numero titoli di viaggio: " + numeroBiglietti);
			}
			System.out.println("Totale: " + totaleBiglietti);

			return result;
		} finally {
			em.close();
		}

	}

	@Override
	public List<Biglietto> getAllBiglietti() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {

			Query q = em.createQuery("SELECT b FROM TitoloViaggio b WHERE TYPE(b)=Biglietto");

			return q.getResultList();

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<Abbonamento> getAllAbbonamenti() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {

			Query q = em.createQuery("SELECT b FROM TitoloViaggio b WHERE TYPE(b)=Abbonamento");

			return q.getResultList();

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<Biglietto> getTitoliFromMezzo(Integer id) {
		List<Biglietto> resultList = null;
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createNamedQuery("titolo_vidimato_su");
			q.setParameter("id", id);

			resultList = q.getResultList();
			if (resultList.size() != 0) {
				Mezzo mezzoUsato = resultList.get(0).getUtilizzatoSu();
				System.out.println("Lista di biglietti usati sul: " + mezzoUsato.getTipoMezzo() + " ("
						+ mezzoUsato.getId() + ") ");
				resultList.forEach(t -> System.out.println(" - Biglietto : " + t.getCodice() + " Emesso il : "
						+ t.getDataEmissione() + "utilizzato il : " + t.getDataVidimazione()));

			} else {
				System.out.println("Nessun Biglietto trovato");
			}

			return resultList;
		} finally {
			em.close();
		}
	}

	@Override
	public Map<Integer, Long> getTitoliVidimatiPeriodo(LocalDate data1, LocalDate data2) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createNamedQuery("titolo_vidimato_date");
			q.setParameter("data1", data1);
			q.setParameter("data2", data2);
			List<Object[]> resultList = q.getResultList();

			Map<Integer, Long> result = new HashMap<>();

			for (Object[] row : resultList) {
				Integer mezzoId = (Integer) row[0];
				Long numeroBiglietti = (Long) row[1];
				result.put(mezzoId, numeroBiglietti);
			}

			Long totaleBiglietti = 0L;

			System.out.println("Conteggio titoli emessi tra il " + data1 + " ed il " + data2);
			for (Map.Entry<Integer, Long> entry : result.entrySet()) {
				Integer mezzoId = entry.getKey();
				Long numeroBiglietti = entry.getValue();
				totaleBiglietti += numeroBiglietti;
				System.out.println("ID mezzo di trasporto: " + mezzoId + ", Numero titoli di viaggio vidimati: "
						+ numeroBiglietti);
			}
			System.out.println("Totale: " + totaleBiglietti);

			return result;
		} finally {
			em.close();
		}
	}

}