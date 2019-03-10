
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
import domain.Finder;
import domain.Procession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	//Service----------------------------------------------------------
	@Autowired
	private FinderService	finderService;


	//Test-------------------------------------------------------------

	@Test
	public void testCreate() {
		System.out.println("========== testCreate() ==========");
		this.authenticate("member1");

		try {
			final Finder finder = this.finderService.create();

			Assert.notNull(finder, "Finder creada no puede ser nula");

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	@Test
	public void testFindOne() {
		System.out.println("========== testFindOne() ==========");

		final int finderId = this.getEntityId("finder1");

		try {
			final Finder finder = this.finderService.findOne(finderId);
			Assert.notNull(finder);

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}
	@Test
	public void testFindAll() {
		System.out.println("========== testFindAll() ==========");

		try {
			final Collection<Finder> finderes = new ArrayList<>(this.finderService.findAll());
			Assert.notEmpty(finderes);

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

			final Finder finder = this.finderService.findFinderByMemberId(this.getEntityId("member1"));
			Assert.notNull(finder);
			final Collection<Procession> processions = new ArrayList<>();
			finder.setKeyword("new Finder");
			finder.setProcessions(processions);

			final Finder saved = this.finderService.save(finder);

			final Collection<Finder> finders = new ArrayList<>(this.finderService.findAll());
			Assert.isTrue(finders.contains(saved));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

	@Test
	public void testClear() {
		System.out.println("========== testClear() ==========");

		try {

			final Finder finder = this.finderService.findOne(this.getEntityId("finder2"));
			Assert.notNull(finder.getProcessions(), "Procession must be not null");
			Assert.notNull(finder);
			this.finderService.clear(finder);
			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

		this.unauthenticate();

	}

}
