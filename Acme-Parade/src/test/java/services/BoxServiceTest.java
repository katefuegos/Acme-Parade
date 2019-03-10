
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

import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BoxServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


	//Tests
	@Test
	public void testCreate() {
		System.out.println("========== testCreate() ==========");
		this.authenticate("member1");
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		try {
			final Box box = this.boxService.create();

			Assert.notNull(box, "Box creada no puede ser nula");
			Assert.isTrue(actor.equals(box.getActor()), "El actor asignado a cada carpeta debe ser igual al que esta logueado en el sistema");

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	@Test
	public void testFindOne() {
		System.out.println("========== testFindOne() ==========");

		final int boxId = this.getEntityId("box1");

		try {
			final Box box = this.boxService.findOne(boxId);
			Assert.notNull(box);

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}
	@Test
	public void testFindAll() {
		System.out.println("========== testFindAll() ==========");

		try {
			final Collection<Box> boxes = new ArrayList<>(this.boxService.findAll());
			Assert.notEmpty(boxes);

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}

	@Test
	public void testSave() {
		System.out.println("========== testSave() ==========");
		this.authenticate("member1");

		try {

			final Box box = this.boxService.create();
			Assert.notNull(box);
			final int boxId = this.getEntityId("box1");
			final Box box1 = this.boxService.findOne(boxId);

			box.setName("new Box");
			box.setRootbox(box1);

			final Box saved = this.boxService.save(box);

			final Collection<Box> boxes = new ArrayList<>(this.boxService.findAll());
			Assert.isTrue(boxes.contains(saved));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	@Test
	public void testDelete() {
		System.out.println("========== testDelete() ==========");
		this.authenticate("handyWorker1");

		try {
			// Se añade las system Box a actor
			//boxService.addSystemBox(actor);

			final Box box = this.boxService.findOne(this.getEntityId("box1"));

			Assert.notNull(box);

			this.boxService.delete(box);
			final Collection<Box> boxes = this.boxService.findAll();

			Assert.isTrue(!boxes.contains(box));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}
}
