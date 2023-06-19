package InterfaceDAO;

import java.util.List; 

import model.Viaggio;

public interface IViaggioDAO {
	
	public void save(Viaggio v);

	public void update(Viaggio v);

	public void delete(Viaggio v);

	public Viaggio getById(Integer id);

	public List<Viaggio> getAllViaggi();
}
