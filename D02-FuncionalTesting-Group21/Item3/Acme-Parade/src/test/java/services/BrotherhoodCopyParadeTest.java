package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodCopyParadeTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ParadeService paradeService;

	// Tests ------------------------------------------------------------------
	@Test
	public void driverManage() {
		final int paradeId = super.getEntityId("parade1");

		final Object testingData[][] = {
		/*
		 * a) Functional requirements - 3.2 Make a copy of one of their parades.
		 * b) Positive test c) analysis of sentence coverage: 89.6% d) analysis
		 * of data coverage - se copia parade1 siendo brotherhood1.
		 */
		{ paradeId, "brotherhood1", null },
		/*
		 * a) Functional requirements - 3.2 Make a copy of one of their parades.
		 * b) Negative test - Business rule: It can not be copied if it's not
		 * from the brotherhood. c) analysis of sentence coverage: 89.6% d)
		 * analysis of data coverage se intenta copiar parade1 siendo
		 * brotherhood2.
		 */
		{ paradeId, "brotherhood2", java.lang.IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((int) testingData[i][0],
						(String) testingData[i][1],
						(Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final int paradeId, final String username,
			final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			Parade parade = paradeService.findOne(paradeId);
			this.paradeService.copy(parade);

			super.unauthenticate();
			this.paradeService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
