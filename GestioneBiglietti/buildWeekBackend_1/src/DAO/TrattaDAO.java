package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import InterfaceDAO.ITrattaDAO;
import model.Tratta;
import utils.JpaUtil;

public class TrattaDAO implements ITrattaDAO {
	private static Logger log = LoggerFactory.getLogger(TrattaDAO.class);

	@Override
	public void save(Tratta m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(m);
			em.getTransaction().commit();
			log.info("Tratta salvata correttamente");
		} catch (Exception ex) {
			em.getTransaction().rollback();
			log.error(ex.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Tratta m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(m);
			em.getTransaction().commit();
			log.info("Tratta aggiornata corretamente");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella modifica della Tratta");
		} finally {
			em.close();
		}

	}

	public void delete(Tratta m) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(m);
			em.getTransaction().commit();
			log.info("Tratta rimossa correttamente dal database");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella rimozione della Tratta");
		} finally {
			em.close();
		}
	}

	@Override
	public Tratta getById(Integer id) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Tratta u = em.find(Tratta.class, id);
			return u;
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nel recupero della Tratta");
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<Tratta> getAllTratte() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT t FROM Tratta t");
			return q.getResultList();
		} finally {
			em.close();
		}
	}

}
