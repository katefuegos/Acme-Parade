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
public class DropOutMemberTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;


	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - member must be able to drop out from a brotherhood to which he or she belongs.
			 * b)Positive tests
			 * c) analysis of sentence coverage: 85.9%
			 * d) analysis of data coverage. The enrolment1 is being
			 * modified with the following data:
			 * momentDropOut=current time,accepted=false, momentEnrol= null,
			 * position=null
			 * The actor in charge is: member3
			 */

			{
				"enrolment2", "member1", null
			},
			/*
			 * a) Functional requirements - member must be able to drop out from a brotherhood to which he or she belongs.
			 * b)Positive tests
			 * c) analysis of sentence coverage: 85.9%
			 * d) analysis of data coverage. The enrolment1 is being
			 * modified with the following data:
			 * momentDropOut=current time,accepted=false, momentEnrol= null,
			 * position=null
			 * The actor in charge is: member2
			 */
			{
				"enrolment2", "member2", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEnrol((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateEnrol(final String nameEnrolment, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.Enrolment enrolment = this.enrolmentService.findOne(this.getEntityId(nameEnrolment));

			this.enrolmentService.dropout(enrolment);

			super.unauthenticate();
			this.enrolmentService.flush();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
