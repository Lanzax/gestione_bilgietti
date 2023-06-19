package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("distributori")
public class Distributore extends Biglietteria {

	@Column
	private boolean inServizio;

	public Distributore() {
		super();
		this.inServizio = true;
	}

	public Distributore(Luogo luogo) {
		super(luogo);
		this.inServizio = true;
	}

	public Distributore(boolean inServizio, Luogo luogo) {
		super(luogo);
		this.inServizio = inServizio;
	}

	public boolean isInServizio() {
		return inServizio;
	}

	public void setInServizio(boolean inServizio) {
		this.inServizio = inServizio;
	}

	@Override
	public String toString() {
		return "Distributore [ " + super.toString() + "inServizio=" + inServizio + "]";
	}

}
