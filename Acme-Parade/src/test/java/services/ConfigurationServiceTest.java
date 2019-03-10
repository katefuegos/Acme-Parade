
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ConfigurationService	configurationService;


	// Tests

	@Test
	public void testFindOne() {

		System.out.println("========== testFindOne() ==========");

		try {

			final Configuration configuration = this.configurationService.findOne();

			Assert.notNull(configuration);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");

		try {

			final Collection<Configuration> configurations = new ArrayList<>(this.configurationService.findAll());

			Assert.notEmpty(configurations);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("==========");

			System.out.println(e);

			System.out.println("==========");

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testSave() {

		System.out.println("========== testSave() ==========");

		try {

			final Configuration configuration = this.configurationService.findOne();

			Assert.notNull(configuration);

			configuration.setCountryCode(22);

			final Configuration saved = this.configurationService.save(configuration);

			final Collection<Configuration> configurations = new ArrayList<>(this.configurationService.findAll());

			Assert.isTrue(configurations.contains(saved));

			Assert.isTrue(saved.getCountryCode() == 22);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

		this.unauthenticate();

	}

	@Test
	public void testCreate() {

		System.out.println("========== testCreate() ==========");

		try {

			final Configuration configuration = this.configurationService.create();

			configuration.setBanner("prueba");

			configuration.setCountryCode(25);

			Assert.notNull(configuration);

			System.out.println("°Exito!");
		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

}
