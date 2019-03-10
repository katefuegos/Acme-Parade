
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "actor","id")})
public class SocialProfile extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private String	nick;
	private String	nameSocialNetwork;
	private String	linkSocialNetwork;


	@NotBlank
	public String getNick() {
		return this.nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	@NotBlank
	public String getNameSocialNetwork() {
		return this.nameSocialNetwork;
	}

	public void setNameSocialNetwork(final String nameSocialNetwork) {
		this.nameSocialNetwork = nameSocialNetwork;
	}

	@NotBlank
	@URL
	public String getLinkSocialNetwork() {
		return this.linkSocialNetwork;
	}

	public void setLinkSocialNetwork(final String linkSocialNetwork) {
		this.linkSocialNetwork = linkSocialNetwork;
	}


	// Relationships ---------------------------------------------------------
	private Actor	actor;


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
