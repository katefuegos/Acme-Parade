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

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Enrolment;
import domain.Member;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EnrolTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private MemberService		memberService;
	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private PositionService		positionService;


	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {
		this.authenticate("brotherhood2");
		final domain.Enrolment enrolmentCreated = this.enrolmentService.create();
		final Date moment = new Date(System.currentTimeMillis() - 1000);
		final Object testingData[][] = {
			/*
			 * a) Functional requirements - Manage enrolments brotherhood
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 * The new enrolment is being modified with the following data: momentDropOut=null,accepted=true,
			 * momentEnrol= current Time, position=position1, Brotherhood = brotherhood2, member=member1
			 * The actor in charge is: brotherhood2
			 */

			{
				enrolmentCreated, "brotherhood2", "member1", true, moment, "position1", null
			},
			/*
			 * a) Functional requirements - Manage enrolments brotherhood
			 * b) Negative tests - Business rule: When a member is enrolled, a position must be selected by the brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 * The new enrolment is being modified with the following data: momentDropOut=null,accepted=true,
			 * momentEnrol= current Time, position=null, Brotherhood = brotherhood2, member=member1
			 * The actor in charge is: brotherhood2
			 */
			{
				enrolmentCreated, "brotherhood2", "member1", true, moment, null, IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateEnrol((domain.Enrolment) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	// Ancillary methods ------------------------------------------------------

	protected void templateEnrol(final domain.Enrolment enrolment, final String username, final String nambeMember, final Boolean accepted, final Date momentEnrol, final String position, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			enrolment.setMomentDropOut(null);
			enrolment.setAccepted(true);
			enrolment.setMomentEnrol(momentEnrol);
			enrolment.setPosition(this.positionService.findOne(this.getEntityId(position)));
			enrolment.setBrotherhood(this.brotherhoodService.findOne(this.getEntityId(username)));

			final Enrolment enrolment2 = this.enrolmentService.save(enrolment);

			final Member member = this.memberService.findOne(this.getEntityId("member1"));
			member.getEnrolments().add(enrolment2);
			this.memberService.save(member);
			super.unauthenticate();
			this.enrolmentService.flush();
			this.memberService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	//	@Test
	//	public void driverDropOut() {
	//
	//
	//		final domain.Enrolment enrolmentCreated = this.enrolmentService.create();
	//		final Date moment = new Date(System.currentTimeMillis() - 1000);
	//		final Object testingData[][] = {
	//			/*
	//			 * a) Functional requirements - Manage enrolments brotherhood
	//			 * b) Positive tests
	//			 * c) analysis of sentence coverage: 92.3%
	//			 * d) analysis of data coverage.
	//			 */
	//			{
	//				enrolmentCreated, "brotherhood2", "member1", true, moment, "position1", null
	//			},
	//			/*
	//			 * a) Functional requirements - Manage enrolments brotherhood
	//			 * b) Negative tests - Business rule: When a member is enrolled, a position must be selected by the brotherhood.
	//			 * c) analysis of sentence coverage: 92.3%
	//			 * d) analysis of data coverage.
	//			 */
	//			{
	//				enrolmentCreated, "brotherhood2", "member1", true, moment, null, IllegalArgumentException.class
	//			},
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			try {
	//				super.startTransaction();
	//				this.templateEnrol((domain.Enrolment) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Boolean) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	//			} catch (final Throwable oops) {
	//				throw new RuntimeException(oops);
	//			} finally {
	//				super.rollbackTransaction();
	//			}
	//	}
	//
	//	// Ancillary methods ------------------------------------------------------
	//
	//	protected void templateDropOut(String nameEnrolment, final String username, final String nambeMember, final Class<?> expected) {
	//		Class<?> caught;
	//
	//		caught = null;
	//		try {
	//			if (username != null)
	//				super.authenticate(username);
	//			else
	//				super.unauthenticate();
	//
	//			domain.Enrolment enrolment = getEntityId(nameEnrolment);
	//			this.enrolmentService.delete(enrolment);
	//
	//		
	//
	//			final Member member = this.memberService.findOne(this.getEntityId("member1"));
	//			member.getEnrolments().add(enrolment);
	//			this.memberService.save(member);
	//			super.unauthenticate();
	//			this.enrolmentService.flush();
	//			this.memberService.flush();
	//
	//			super.flushTransaction();
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		super.checkExceptions(expected, caught);
	//	}

}
