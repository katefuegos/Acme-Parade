package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Area;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class AreaServiceTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private AreaService areaService;

	// Tests ------------------------------------------------------------------

	@Test
	public void driverArea() {

		final int areaId = super.getEntityId("area6");

		final Object testingData[][] = {
		/*
		 * a) Functional requirements - 22.1 Manage Area - Create b) Positive
		 * tests c) analysis of sentence coverage: XXXXX% d) analysis of data
		 * coverage - se crea un area con name="name" siendo admin.
		 */
		{ 0, 0, "admin", null },

		/*
		 * a) Functional requirements - 22.1 Manage Area - Create b) Negative
		 * tests - Business rule: It can not be created by a no admin user. c)
		 * analysis of sentence coverage: XXXXX% d) analysis of data coverage -
		 * se intenta crear un area siendo brotherhood1.
		 */
		{ 0, 0, "brotherhood1", IllegalArgumentException.class },

		/*
		 * a) Functional requirements - 22.1 Manage Area - Edit b) Positive
		 * tests c) analysis of sentence coverage: XXXXX% d) analysis of data
		 * coverage - se edita un area cambiando name="edited" siendo admin.
		 */
		{ areaId, 0, "admin", null },

		/*
		 * // * a) Functional requirements - 22.1 Manage Area - Edit b) Negative
		 * // * tests - Business rule: It can not be created by a no admin user.
		 * c) // * analysis of sentence coverage: XXXXX% d) analysis of data
		 * coverage - // * se intenta editar un area siendo brotherhood1. //
		 */
		{ areaId, 0, "brotherhood1", IllegalArgumentException.class },

		/*
		 * a) Functional requirements - 22.1 Manage Area - Delete b) Positive
		 * tests c) analysis of sentence coverage: XXXXX% d) analysis of data
		 * coverage - se elimina area1 siendo admin.
		 */
		{ areaId, areaId, "admin", null },

		/*
		 * a) Functional requirements - 22.1 Manage Area - Create b) Negative
		 * tests - Business rule: It can not be created by a no admin user. c)
		 * analysis of sentence coverage: XXXXX% d) analysis of data coverage -
		 * se intenta eliminar area1 siendo brotherhood1.
		 */
		{ areaId, areaId, "brotherhood1", IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateArea((int) testingData[i][0],
						(int) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// ----MiscellaneousRecord----

	protected void templateArea(final int areaId, final int areaToDeleteId,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (areaId == 0) {
				Area a = areaService.create();
				a.setName("name");
				areaService.save(a);
			} else if (areaToDeleteId == 0) {
				Area a = areaService.findOne(areaId);
				a.setName("edited");
				areaService.save(a);
			} else {
				Area a = areaService.findOne(areaToDeleteId);
				areaService.delete(a);
			}

			super.unauthenticate();
			this.areaService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
