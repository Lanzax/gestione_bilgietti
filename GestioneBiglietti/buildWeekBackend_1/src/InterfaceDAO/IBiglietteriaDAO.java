package InterfaceDAO;

import java.util.List; 

import model.Biglietteria;

public interface IBiglietteriaDAO {
	
	public void save(Biglietteria bi);

	public void update(Biglietteria b);

	public void delete(Biglietteria bi);

	public Biglietteria getById(Integer id);

	public List<Biglietteria> getAllBiglietterie();
	
}
