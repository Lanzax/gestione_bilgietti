package InterfaceDAO;

import java.util.List;

import model.Utente;

public interface IUtenteDAO {

	public void save(Utente u);

	public void update(Utente u);

	public void delete(Utente u);

	public Utente getByTessera(Integer tessera);

	public List<Utente> getAllUsers();

	public List<Utente> getTessereScadute();
}
