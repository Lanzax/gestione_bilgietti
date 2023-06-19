package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import InterfaceDAO.IMezziDAO;
import model.Mezzo;
import utils.JpaUtil;

public class MezzoDAO implements IMezziDAO {
	static Logger log = LoggerFactory.getLogger(MezzoDAO.class);

	@Override
	public void save(Mezzo m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(m);
			em.getTransaction().commit();
			log.info("mezzo aggiunto nel DB: " + m.getId() + " : " + m.getTipoMezzo());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Mezzo m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(m);
			em.getTransaction().commit();
			log.info("mezzo Modificato nel DB: " + m.getId() + " : " + m.getTipoMezzo() + " Stato: " + m.getStato());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(Mezzo m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(m);
			em.getTransaction().commit();
			log.info("mezzo Eliminato nel DB: " + m.getId() + " : " + m.getTipoMezzo());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public Mezzo getById(Integer id) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Mezzo m = em.find(Mezzo.class, id);
			log.info("mezzo Trovato nel DB");
			return m;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();

		}
		return null;
	}

	@Override
	public List<Mezzo> getAllMezzi() {

		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {

			Query q = em.createQuery("SELECT m FROM Mezzo m");

			return q.getResultList();

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}

		return null;
	}

	@Override
	public List<Mezzo> getAllAutobus() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT m FROM Mezzo m WHERE m.tipomezzo=m.TipoMezzo.AUTOBUS");
			return q.getResultList();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<Mezzo> getAllTram() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT m FROM Mezzo m WHERE m.tipomezzo = m.TipoMezzo.TRAM");
			return q.getResultList();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public Object viaggiPercorsiSuTratta(Integer mezzoId, Integer trattaId) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createNamedQuery("numero_viaggi_tratta");
			q.setParameter("id", mezzoId);
			q.setParameter("nTratta", trattaId);

			Object[] result = (Object[]) q.getSingleResult();

			Long numeroViaggi = (Long) result[0];
			Double tempoMedio = (Double) result[1];

			if (tempoMedio != null) {
				double tempoMedioValue = tempoMedio.doubleValue();
				log.info("Il mezzo con id: " + mezzoId + " ha eseguito " + numeroViaggi + " viaggi, con tempo medio di "
						+ tempoMedioValue + " minuti, sulla tratta numero " + trattaId);
			} else {
				log.info("Il mezzo selezionato non ha mai effettuato viaggi su questa tratta.");
			}
			return result;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			em.close();
		}
		return null;
	}
}
