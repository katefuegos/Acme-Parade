
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
import domain.Brotherhood;
import domain.Floaat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FloaatServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private FloaatService		floaatService;
	private BrotherhoodService	brotherhoodService;


	// Tests

	@Test
	public void testFindOne() {

		System.out.println("========== testFindOne() ==========");

		try {

			final int idBusqueda = super.getEntityId("float1");
			final Floaat floaat = this.floaatService.findOne(idBusqueda);

			Assert.notNull(floaat);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");

		try {

			final Collection<Floaat> floaats = new ArrayList<>(this.floaatService.findAll());

			Assert.notEmpty(floaats);

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

			final int idBusqueda = super.getEntityId("float1");
			final Floaat floaat = this.floaatService.findOne(idBusqueda);

			floaat.setDescription("cambiado");

			Assert.notNull(floaat);

			final Floaat saved = this.floaatService.save(floaat);

			final Collection<Floaat> floaats = new ArrayList<>(this.floaatService.findAll());

			Assert.isTrue(floaats.contains(saved));

			Assert.isTrue(saved.getDescription() == "cambiado");

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
			final Floaat floaat = this.floaatService.create();
			final int idBusqueda = super.getEntityId("brotherhood1");
			final Brotherhood bro = this.brotherhoodService.findOne(idBusqueda);
			floaat.setBrotherhood(bro);

			floaat.setDescription("Description");

			floaat.setPictures("www.google.es");

			floaat.setTitle("Float A");

			Assert.notNull(floaat);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Exito!");

		}

	}

}
