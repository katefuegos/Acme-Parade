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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Tests

	@Test
	public void testFindOne() {

		System.out.println("========== testFindOne() ==========");

		try {

			final int idBusqueda = super.getEntityId("brotherhood1");
			final Brotherhood brotherhood = this.brotherhoodService
					.findOne(idBusqueda);

			Assert.notNull(brotherhood);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");

		try {

			final Collection<Brotherhood> brotherhoods = new ArrayList<>(
					this.brotherhoodService.findAll());

			Assert.notEmpty(brotherhoods);

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

			final int idBusqueda = super.getEntityId("brotherhood1");
			final Brotherhood brotherhood = this.brotherhoodService
					.findOne(idBusqueda);

			brotherhood.setSurname("cambiado");

			Assert.notNull(brotherhood);

			final Brotherhood saved = this.brotherhoodService.save(brotherhood);

			final Collection<Brotherhood> brotherhoods = new ArrayList<>(
					this.brotherhoodService.findAll());

			Assert.isTrue(brotherhoods.contains(saved));

			Assert.isTrue(saved.getSurname() == "cambiado");

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
			final Brotherhood brotherhood = this.brotherhoodService.create();

			brotherhood.setSurname("Description");

			brotherhood.setPictures("www.google.es");

			brotherhood.setTitle("Float A");

			Assert.notNull(brotherhood);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo!");

		}

	}

}
