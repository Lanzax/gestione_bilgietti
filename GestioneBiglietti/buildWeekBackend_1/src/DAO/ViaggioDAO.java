package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import InterfaceDAO.IViaggioDAO;
import model.Viaggio;
import utils.JpaUtil;

public class ViaggioDAO implements IViaggioDAO {
	private static Logger log = LoggerFactory.getLogger(TrattaDAO.class);

	@Override
	public void save(Viaggio v) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(v);
			em.getTransaction().commit();
			log.info("Viaggio salvato correttamente");
		} catch (Exception ex) {
			em.getTransaction().rollback();
			log.error(ex.getMessage());
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Viaggio v) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(v);
			em.getTransaction().commit();
			log.info("Viaggio aggiornato corretamente");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella modifica del viaggio");
		} finally {
			em.close();
		}

	}

	public void delete(Viaggio v) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(v);
			em.getTransaction().commit();
			log.info("Viaggio rimosso correttamente dal database");
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nella rimozione del viaggio");
		} finally {
			em.close();
		}
	}

	@Override
	public Viaggio getById(Integer id) {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Viaggio v = em.find(Viaggio.class, id);
			return v;
		} catch (Exception e) {
			em.getTransaction().rollback();
			log.error("Errore nel recupero del Viaggio");
		} finally {
			em.close();
		}
		return null;
	}

	@Override
	public List<Viaggio> getAllViaggi() {
		EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT v FROM Viaggio v");
			return q.getResultList();
		} finally {
			em.close();
		}
	}

}
