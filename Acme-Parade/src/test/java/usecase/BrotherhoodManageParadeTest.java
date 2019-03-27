/*
 * RegistrationTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package usecase;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ParadeService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodManageParadeTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ParadeService	paradeService;


	@Autowired
	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {
		this.authenticate("brotherhood1");
		final domain.Parade paradeCreated = this.paradeService.create();

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - 10.2 Manage Parade - Create a Parade
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, paradeCreated, "brotherhood1", "new title", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage Parade - Create a Parade
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, paradeCreated, "brotherhood2", "new title", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 10.2 Manage Parade - Edit a Parade
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"parade1", null, "brotherhood1", "new title", null
			},

			/*
			 * a) Functional requirements - 10.2 Manage Parade - Edit a Parade
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"parade1", null, "brotherhood2", "new title", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0], (domain.Parade) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final String nameParade, final domain.Parade newParade, final String username, final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int paradeId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			domain.Parade parade;
			if (newParade == null) {
				paradeId = super.getEntityId(nameParade);
				parade = this.paradeService.findOne(paradeId);
			} else
				parade = newParade;

			parade.setTitle(newTitle);
			parade.setDescription("description1");
			parade.setMoment(new Date(new Date().getTime() + 315360000000L));

			this.paradeService.save(parade);

			super.unauthenticate();
			this.paradeService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
