package InterfaceDAO;

import java.util.List; 

import model.Tratta;

public interface ITrattaDAO {
	
	public void save(Tratta m);

	public void update(Tratta m);

	public void delete(Tratta m);
	
	public Tratta getById(Integer id);

	public List<Tratta> getAllTratte();

}
