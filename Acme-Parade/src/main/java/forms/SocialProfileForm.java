package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class SocialProfileForm {

	// Attributes------------------------------------------------------------------

	private int id;
	private String nick;
	private String nameSocialNetwork;
	private String linkSocialNetwork;

	// Constructor------------------------------------------------------------------

	public SocialProfileForm() {
		super();
	}

	// Getter and
	// Setters------------------------------------------------------------

	@NotNull
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
}
