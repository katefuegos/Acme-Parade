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
public class RequestMemberTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private RequestService	requestService;

	@Autowired
	private ParadeService	paradeService;


	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - Manage request member b)
			 * Positive tests c) analysis of sentence coverage: 85.9% d)
			 * analysis of data coverage.
			 * The request1 is being modified with the following data: create a new request for parade2
			 * The actor in charge is: member1
			 */

			{
				"parade1", "member2", null
			},
			/*
			 * a) Functional requirements - Manage request member
			 * b) Negative tests - Business rule: It can not be requested several times.
			 * c) analysis of sentence coverage: 85.9%
			 * d) analysis of data coverage.
			 * The request1 is being modified with the following data: create a new request for parade2
			 * The actor in charge is: member2
			 */
			{
				"parade1", "member1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateReject((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateReject(final String nameParade, final String member, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (member != null)
				super.authenticate(member);
			else
				super.unauthenticate();

			final domain.Request request = this.requestService.create();
			request.setParade(this.paradeService.findOne(this.getEntityId(nameParade)));

			this.requestService.application(request);

			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
