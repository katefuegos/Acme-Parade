package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Path extends DomainEntity {

	// ATRIBUTOS

	// Relationships ---------------------------------------------------------

	private Parade parade;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Parade getParade() {
		return parade;
	}

	public void setParade(Parade parade) {
		this.parade = parade;
	}

}
