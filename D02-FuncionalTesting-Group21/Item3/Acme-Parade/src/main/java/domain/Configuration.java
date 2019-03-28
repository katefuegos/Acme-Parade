
package domain;

import java.util.Collection;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private int								countryCode;
	private Map<String, String>				welcomeMessage;
	private String							banner;
	private Map<String, Collection<String>>	spamWords;
	private int								finderCacheTime;
	private int								finderMaxResults;
	private Map<String, Collection<String>>	negativeWords;
	private Map<String, Collection<String>>	positiveWords;
	private Collection<String>				priorities;
	private String							systemName;


	//private Map<String, Collection<String>>	positions;

	//	@NotEmpty
	//	@ElementCollection(targetClass = org.hibernate.mapping.Collection.class)
	//	public Map<String, Collection<String>> getPositions() {
	//		return this.positions;
	//	}
	//
	//	public void setPositions(final Map<String, Collection<String>> positions) {
	//		this.positions = positions;
	//	}

	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getPriorities() {
		return this.priorities;
	}

	public void setPriorities(final Collection<String> priorities) {
		this.priorities = priorities;
	}

	public void setFinderMaxResults(final Integer finderMaxResults) {
		this.finderMaxResults = finderMaxResults;
	}

	@Range(min = 0, max = 999)
	public int getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final int countryCode) {
		this.countryCode = countryCode;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
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

	@NotEmpty
	@ElementCollection
	public Map<String, String> getWelcomeMessage() {
		return this.welcomeMessage;
	}

	public void setWelcomeMessage(final Map<String, String> welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	@NotEmpty
	@ElementCollection(targetClass = org.hibernate.mapping.Collection.class)
	public Map<String, Collection<String>> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Map<String, Collection<String>> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@NotEmpty
	@ElementCollection(targetClass = org.hibernate.mapping.Collection.class)
	public Map<String, Collection<String>> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Map<String, Collection<String>> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@NotEmpty
	@ElementCollection(targetClass = org.hibernate.mapping.Collection.class)
	public Map<String, Collection<String>> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Map<String, Collection<String>> spamWords) {
		this.spamWords = spamWords;
	}

	// Relationships ---------------------------------------------------------
}
