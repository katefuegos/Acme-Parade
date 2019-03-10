
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(name = "ID1Box", columnList = "name,actor")

})
public class Box extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private String	name;
	private boolean	isSystem;


	//private Boolean	isTrash;

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public boolean getIsSystem() {
		return this.isSystem;
	}

	public void setIsSystem(final boolean isSystem) {
		this.isSystem = isSystem;
	}


	// Relationships ---------------------------------------------------------
	private Collection<Box>	subboxes;
	private Box				rootbox;
	private Actor			actor;


	@NotNull
	@Valid
	@OneToMany
	public Collection<Box> getSubboxes() {
		return this.subboxes;
	}

	public void setSubboxes(final Collection<Box> subboxes) {
		this.subboxes = subboxes;
	}

	@Valid
	@ManyToOne(optional = true)
	public Box getRootbox() {
		return this.rootbox;
	}

	public void setRootbox(final Box rootbox) {
		this.rootbox = rootbox;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}
}
