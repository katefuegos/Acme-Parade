
package forms;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class ConfigurationForm {

	//Attributes------------------------------------------------------------------

	private int					id;

	private int					countryCode;

	private int					finderCacheTime;

	private int					finderMaxResults;

	private String				bannerr;

	private String				welcomeMessageES;

	private String				welcomeMessageEN;

	private Collection<String>	spamWordsES;

	private Collection<String>	spamWordsEN;

	private Collection<String>	negativeWordsES;

	private Collection<String>	negativeWordsEN;

	private Collection<String>	positiveWordsES;

	private Collection<String>	positiveWordsEN;
	
	private String 				systemName;


	//	private Collection<String>	positionEN;
	//
	//	private Collection<String>	positionES;

	//Constructor------------------------------------------------------------------

	public ConfigurationForm() {

		super();

	}

	//Getter and Setters------------------------------------------------------------

	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	public int getId() {

		return this.id;

	}

	public void setId(final int id) {

		this.id = id;

	}

	@Range(min = 0, max = 999)
	public int getCountryCode() {

		return this.countryCode;

	}

	public void setCountryCode(final int countryCode) {

		this.countryCode = countryCode;

	}

	@Range(min = 1, max = 24)
	public int getFinderCacheTime() {

		return this.finderCacheTime;

	}

	public void setFinderCacheTime(final int finderCacheTime) {

		this.finderCacheTime = finderCacheTime;

	}

	@Range(min = 0, max = 100)
	public int getFinderMaxResults() {

		return this.finderMaxResults;

	}

	public void setFinderMaxResults(final int finderMaxResults) {

		this.finderMaxResults = finderMaxResults;

	}

	@NotBlank
	@URL
	public String getBannerr() {

		return this.bannerr;

	}

	public void setBannerr(final String bannerr) {

		this.bannerr = bannerr;

	}

	@NotBlank
	public String getWelcomeMessageES() {

		return this.welcomeMessageES;

	}

	public void setWelcomeMessageES(final String welcomeMessageES) {

		this.welcomeMessageES = welcomeMessageES;

	}

	@NotBlank
	public String getWelcomeMessageEN() {

		return this.welcomeMessageEN;

	}

	public void setWelcomeMessageEN(final String welcomeMessageEN) {

		this.welcomeMessageEN = welcomeMessageEN;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getSpamWordsES() {

		return this.spamWordsES;

	}

	public void setSpamWordsES(final Collection<String> spamWordsES) {

		this.spamWordsES = spamWordsES;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getSpamWordsEN() {

		return this.spamWordsEN;

	}

	public void setSpamWordsEN(final Collection<String> spamWordsEN) {

		this.spamWordsEN = spamWordsEN;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getNegativeWordsES() {

		return this.negativeWordsES;

	}

	public void setNegativeWordsES(final Collection<String> negativeWordsES) {

		this.negativeWordsES = negativeWordsES;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getNegativeWordsEN() {

		return this.negativeWordsEN;

	}

	public void setNegativeWordsEN(final Collection<String> negativeWordsEN) {

		this.negativeWordsEN = negativeWordsEN;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getPositiveWordsES() {

		return this.positiveWordsES;

	}

	public void setPositiveWordsES(final Collection<String> positiveWordsES) {

		this.positiveWordsES = positiveWordsES;

	}

	@NotEmpty
	@ElementCollection
	public Collection<String> getPositiveWordsEN() {

		return this.positiveWordsEN;

	}

	public void setPositiveWordsEN(final Collection<String> positiveWordsEN) {

		this.positiveWordsEN = positiveWordsEN;

	}

	//	@NotEmpty
	//	@ElementCollection
	//	public Collection<String> getPositionES() {
	//
	//		return this.positionES;
	//
	//	}
	//
	//	public void setPositionES(final Collection<String> positionES) {
	//
	//		this.positionES = positionES;
	//
	//	}
	//
	//	@NotEmpty
	//	@ElementCollection
	//	public Collection<String> getPositionEN() {
	//
	//		return this.positionEN;
	//
	//	}
	//
	//	public void setPositionEN(final Collection<String> positionEN) {
	//
	//		this.positionEN = positionEN;
	//
	//	}

}
