
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	// Repository-------------------------------------------------------------------------

	@Autowired
	private ConfigurationRepository	configurationRepository;


	// Services---------------------------------------------------------------------------

	// Constructor------------------------------------------------------------------------

	public ConfigurationService() {

		super();

	}

	// Simple

	// CRUD------------------------------------------------------------------------

	public Configuration create() {

		final Configuration configuration = new Configuration();
		final Map<String, Collection<String>> spamWords = new HashMap<>();
		final Map<String, Collection<String>> negativeWords = new HashMap<>();
		final Map<String, Collection<String>> positiveWords = new HashMap<>();
		final Map<String, String> welcomeMessage = new HashMap<>();
		//final Map<String, Collection<String>> positions = new HashMap<>();
		final Collection<String> priorities = new ArrayList<String>();

		configuration.setFinderMaxResults(21);
		configuration.setCountryCode(34);
		configuration.setWelcomeMessage(welcomeMessage);
		configuration.setBanner("");
		configuration.setSpamWords(spamWords);
		configuration.setFinderCacheTime(60);
		configuration.setFinderMaxResults(10);
		configuration.setNegativeWords(negativeWords);
		configuration.setPositiveWords(positiveWords);
		configuration.setPriorities(priorities);
		//configuration.setPositions(positions);

		return configuration;
	}

	public Collection<Configuration> findAll() {

		Collection<Configuration> configurations;
		configurations = this.configurationRepository.findAll();
		Assert.notNull(configurations);
		return configurations;

	}

	public Configuration findOne() {
		return new ArrayList<>(this.findAll()).get(0);
	}

	public Configuration save(final Configuration configuration) {
		final Configuration saved = this.configurationRepository.save(configuration);
		return saved;
	}

	public void delete(final Configuration entity) {
		this.configurationRepository.delete(entity);

	}

	// Other

	// Methods---------------------------------------------------------------------------

	public Collection<String> internacionalizcionListas(final Map<String, Collection<String>> words) {

		final String laungage = LocaleContextHolder.getLocale().getLanguage();
		final Collection<String> res = words.get(laungage.toUpperCase());
		return res;

	}

	public String internacionalizcion(final Map<String, String> words) {
		final String laungage = LocaleContextHolder.getLocale().getLanguage();
		final String res = words.get(laungage.toUpperCase());
		return res;
	}

	public double calculate(final double price) {
		double res = 0.0;
		final int varTax = this.findOne().getFinderMaxResults();
		res = price + (price * varTax / 100);
		return res;

	}

	public Configuration findDefault() {
		return this.configurationRepository.findAll().get(0);
	}

}
