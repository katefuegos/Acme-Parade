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
public class FinderTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private MemberService			memberService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private ConfigurationService	configurationService;


	// Tests ------------------------------------------------------------------
	@Test
	public void driverFinder() {

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - member must be able to manage his or her finder
			 * b) Positive tests
			 * c) analysis of sentence coverage: 93.9%
			 * d) analysis of data coverage.
			 * The finder1 is being modified with the following data: keyword="",nameArea="area1"
			 * The actor in charge is: member1
			 */

			{
				"member1", "finder1", "", "area1", false, null
			},
			/*
			 * a) Functional requirements - member must be able to manage his or her finder
			 * b) Negative tests - Business rule: An actor can not edit finder that belongs to another actor.
			 * c) analysis of sentence coverage: 93.9%
			 * d) analysis of data coverage.
			 * The finder1 is being modified with the following data: keyword="",nameArea="area1"
			 * The actor in charge is: member2
			 */
			{
				"member2", "finder1", "", "area1", false, IllegalArgumentException.class
			},
			/*
			 * a) Functional requirements - member must be able to clean their finder
			 * b) Positive tests
			 * c) analysis of sentence coverage: 93.9%
			 * d) analysis of data coverage.
			 * The finder1 is being modified with the following data: keyword="",nameArea="area1"
			 * The actor in charge is: member2
			 */
			{
				"member1", "finder1", "", "area1", true, null
			},
			/*
			 * a) Functional requirements - member must be able to clean their finder
			 * b) Negative tests - Business rule: An actor can not edit finder that belongs to another actor.
			 * c) analysis of sentence coverage: 93.9%
			 * d) analysis of data coverage.
			 * The finder1 is being modified with the following data: keyword="",nameArea="area1"
			 * The actor in charge is: member2
			 */
			{
				"member2", "finder1", "", "area1", true, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (boolean) testingData[i][4], (Class<?>) testingData[i][5]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final String nameFinder, final String keyword, final String nameArea, final boolean clear, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.Finder finder = this.finderService.findOne(this.getEntityId(nameFinder));

			finder.setKeyword(keyword);
			finder.setNameArea(this.areaService.findOne(this.getEntityId(nameArea)).getName());

			if (clear)
				this.finderService.clear(finder);
			else
				this.finderService.save(finder);

			super.unauthenticate();
			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
