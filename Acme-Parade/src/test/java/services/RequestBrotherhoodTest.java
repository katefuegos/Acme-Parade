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
public class RequestBrotherhoodTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private RequestService	requestService;


	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - Manage request brotherhood b)
			 * Positive tests c) analysis of sentence coverage: 89.9% d)
			 * analysis of data coverage.
			 * The request1 is being modified with the following data: status=rejected, reasonReject="reasonReject"
			 * The actor in charge is: brotherhood2
			 */

			{
				"request2", "brotherhood2", "reasonReject", null
			},
			/*
			 * a) Functional requirements - Manage request brotherhood
			 * b) Negative tests - Business rule: An actor can not edit finder that belongs to another actor.
			 * c) analysis of sentence coverage: 89.9%
			 * d) analysis of data coverage.
			 * The request1 is being modified with the following data: status=rejected,reasonReject="reasonReject"
			 * The actor in charge is: brotherhood2
			 */
			{
				"request2", "brotherhood3", "reasonReject", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - Manage request brotherhood
			 * b) Negative tests - Business rule: Reason reject must not be null
			 * c) analysis of sentence coverage: 89.9%
			 * d) analysis of data coverage.
			 * The request1 is being modified with the following data: status=rejected, reasonReject= null
			 * The actor in charge is: brotherhood2
			 */
			{
				"request2", "brotherhood2", null, NullPointerException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateReject((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateReject(final String nameRequest, final String brotherhood, final String reasonReject, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (brotherhood != null)
				super.authenticate(brotherhood);
			else
				super.unauthenticate();
			final domain.Request request = this.requestService.findOne((this.getEntityId(nameRequest)));

			request.setReasonReject(reasonReject);

			this.requestService.reject(request);

			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
