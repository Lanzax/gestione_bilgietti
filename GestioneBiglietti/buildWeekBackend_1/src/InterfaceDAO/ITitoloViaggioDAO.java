package InterfaceDAO;

import java.time.LocalDate; 
import java.util.List;
import java.util.Map;

import model.Abbonamento;
import model.Biglietto;
import model.TitoloViaggio;

public interface ITitoloViaggioDAO {
	
	public void save(TitoloViaggio ti);

	public void update(TitoloViaggio ti);

	public void delete(TitoloViaggio ti);

	public TitoloViaggio getByCodice(Integer codice);

	public List<TitoloViaggio> getAllTitoli();

	public List<Biglietto> getAllBiglietti();

	public List<Abbonamento> getAllAbbonamenti();

	public Map<Integer, Long> getTitoliFromDate(LocalDate data1, LocalDate data2);

	public List<Biglietto> getTitoliFromMezzo(Integer id);

	public Map<Integer, Long> getTitoliVidimatiPeriodo(LocalDate data1, LocalDate data2);
}
