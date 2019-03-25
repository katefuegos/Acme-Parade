
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
import domain.History;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	// Sut

	@Autowired
	private HistoryService	historyService;


	// Tests

	//CASOS VALIDOS AUTENTICADOS

	@Test
	public void testFindOne() {
		System.out.println("========== testFindOne() ==========");
		try {
			this.authenticate("brotherhood1");
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			Assert.notNull(history);
			System.out.println("°Exito!");
		} catch (final Exception e) {
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
	}

	@Test
	public void testFindAll() {

		System.out.println("========== testFindAll() ==========");
		try {
			this.authenticate("brotherhood1");
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.notEmpty(historys);
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
			this.authenticate("brotherhood1");
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			history.setTitle("cambiado");
			Assert.notNull(history);
			final History saved = this.historyService.save(history);
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.isTrue(historys.contains(saved));
			Assert.isTrue(saved.getTitle() == "cambiado");
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
			this.authenticate("brotherhood1");
			final History history = this.historyService.create();
			history.setTitle("Name");
			history.setPhotos("www.google.es");
			history.setDescription("description prueba");
			Assert.notNull(history);
			System.out.println("°Exito!");

		} catch (final Exception e) {
			System.out.println("°Exito!");
		}
	}

	//------------------TEST SIN AUNTENTICAR-------------------------

	@Test
	public void testFindOneU() {
		System.out.println("========== testFindOne() ==========");
		try {
			this.unauthenticate();
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			Assert.notNull(history);
			System.out.println("°Exito!");
		} catch (final Exception e) {
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
	}

	@Test
	public void testFindAllU() {

		System.out.println("========== testFindAll() ==========");
		try {
			this.unauthenticate();
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.notEmpty(historys);
			System.out.println("°Exito!");
		} catch (final Exception e) {
			System.out.println("==========");
			System.out.println(e);
			System.out.println("==========");
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
	}

	@Test
	public void testSaveU() {
		System.out.println("========== testSave() ==========");
		try {
			this.unauthenticate();
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			history.setTitle("cambiado");
			Assert.notNull(history);
			final History saved = this.historyService.save(history);
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.isTrue(historys.contains(saved));
			Assert.isTrue(saved.getTitle() == "cambiado");
			System.out.println("°Exito!");

		} catch (final Exception e) {
			System.out.println("°Fallo," + e.getMessage() + "!");
		}

	}

	@Test
	public void testCreateU() {
		System.out.println("========== testCreate() ==========");
		try {
			this.unauthenticate();
			final History history = this.historyService.create();
			history.setTitle("Name");
			history.setPhotos("www.google.es");
			history.setDescription("description prueba");
			Assert.notNull(history);
			System.out.println("°Exito!");

		} catch (final Exception e) {
			System.out.println("°Exito!");
		}
	}

	//--------------TEST CON OTRO ROL---------------

	@Test
	public void testFindOneA() {
		System.out.println("========== testFindOne() ==========");
		try {
			this.authenticate("administrator1");
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			Assert.notNull(history);
			System.out.println("°Exito! No se puede con admin");
		} catch (final Exception e) {
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
	}

	@Test
	public void testFindAllA() {

		System.out.println("========== testFindAll() ==========");
		try {
			this.authenticate("administrator1");
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.notEmpty(historys);
			System.out.println("°Exito! No se puede con admin");
		} catch (final Exception e) {
			System.out.println("==========");
			System.out.println(e);
			System.out.println("==========");
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
	}

	@Test
	public void testSaveA() {
		System.out.println("========== testSave() ==========");
		try {
			this.authenticate("administrator1");
			final int idBusqueda = super.getEntityId("history1");
			final History history = this.historyService.findOne(idBusqueda);
			history.setTitle("cambiado");
			Assert.notNull(history);
			final History saved = this.historyService.save(history);
			final Collection<History> historys = new ArrayList<>(this.historyService.findAll());
			Assert.isTrue(historys.contains(saved));
			Assert.isTrue(saved.getTitle() == "cambiado");
			System.out.println("°Exito! No se puede con admin");

		} catch (final Exception e) {
			System.out.println("°Fallo," + e.getMessage() + "!");
		}
		this.unauthenticate();
	}

	@Test
	public void testCreateA() {
		System.out.println("========== testCreate() ==========");
		try {
			this.authenticate("administrator1");
			final History history = this.historyService.create();
			history.setTitle("Name");
			history.setPhotos("www.google.es");
			history.setDescription("description prueba");
			Assert.notNull(history);
			System.out.println("°Exito! No se puede con admin");

		} catch (final Exception e) {
			System.out.println("°Exito!");
		}
	}
}
