package InterfaceDAO;

import java.util.List;

import model.Mezzo;

public interface IMezziDAO {
	
	public void save(Mezzo m);

	public void update(Mezzo m);

	public void delete(Mezzo m);

	public Mezzo getById(Integer id);

	public List<Mezzo> getAllMezzi();

	public List<Mezzo> getAllAutobus();

	public List<Mezzo> getAllTram();

	public Object viaggiPercorsiSuTratta(Integer mezzoId, Integer trattaId);
}
