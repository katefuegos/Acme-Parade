/*
 * RegistrationTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChapterManageParadeTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ParadeService	paradeService;


	@Autowired
	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {
		this.authenticate("chapter1");
		final domain.Parade paradeCreated = this.paradeService.create();

		final Object testingData[][] = {

			/*
			 * a) Functional requirements - 2.2 - Manage Parade by chapter
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 89.2%
			 * d) analysis of data coverage.
			 */
			{
				"parade1", "chapter1", null
			},

			/*
			 * a) Functional requirements - 2.2 - Manage Parade by chapter
			 * b) Negative tests - Business rule: It can not be modified by another chapter.
			 * c) analysis of sentence coverage: 89.2%
			 * d) analysis of data coverage.
			 */
			{
				"parade1", "chapter2", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 2.2 - Manage Parade by chapter
			 * b) Negative tests - Business rule: It can not be modified by another chapter.
			 * c) analysis of sentence coverage: 89.2%
			 * d) analysis of data coverage.
			 */
			{
				"parade1", "brotherhood2", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final String nameParade, final String username, final Class<?> expected) {
		Class<?> caught;
		final int paradeId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			domain.Parade parade;

			paradeId = super.getEntityId(nameParade);
			parade = this.paradeService.findOne(paradeId);

			parade.setDraftMode(false);
			parade.setStatus("ACCEPTED");

			this.paradeService.changeStatus(parade);

			super.unauthenticate();
			this.paradeService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
