
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
import domain.Area;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AreaServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private AreaService	areaService;


	// Tests

	@Test
	public void testFindOne() {

		System.out.println("========== testFindOne() ==========");

		try {

			final int idBusqueda = super.getEntityId("area1");
			final Area area = this.areaService.findOne(idBusqueda);

			Assert.notNull(area);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Fallo," + e.getMessage() + "!");

		}

	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");

		try {

			final Collection<Area> areas = new ArrayList<>(this.areaService.findAll());

			Assert.notEmpty(areas);

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

			final int idBusqueda = super.getEntityId("area1");
			final Area area = this.areaService.findOne(idBusqueda);

			area.setName("cambiado");

			Assert.notNull(area);

			final Area saved = this.areaService.save(area);

			final Collection<Area> areas = new ArrayList<>(this.areaService.findAll());

			Assert.isTrue(areas.contains(saved));

			Assert.isTrue(saved.getName() == "cambiado");

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
			final Area area = this.areaService.create();
			area.setName("Name");

			area.setPictures("www.google.es");

			Assert.notNull(area);

			System.out.println("°Exito!");

		} catch (final Exception e) {

			System.out.println("°Exito!");

		}

	}

}
