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

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AreaService;
import services.ChapterService;
import utilities.AbstractTest;
import forms.ActorForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SelfAssignedAreaChapterTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	accountService;

	@Autowired
	private AreaService			areaService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverChangeArea() {

		final Object testingData[][] = {
			/*
			 * a) Functional requirements - Self-assigned area
			 * b) Positive tests
			 * c) analysis of sentence coverage: 85.9%
			 * d) analysis of data coverage.
			 */
			{
				"chapter3", "area1", null
			},
			/*
			 * a) Functional requirements - Self-assigned area
			 * b) Negative tests - Business rule: it cannot be changed.
			 * c) analysis of sentence coverage: 85.9%
			 * d) analysis of data coverage.
			 */
			{
				"chapter4", "area1", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateChangeArea((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateChangeArea(final String nameChapter, final String nameArea, final Class<?> expected) {
		Class<?> caught;
		final int areaId;
		final int chapterId;

		caught = null;
		try {
			if (nameChapter != null)
				super.authenticate(nameChapter);
			else
				super.unauthenticate();

			areaId = super.getEntityId(nameArea);
			chapterId = this.getEntityId(nameChapter);

			final domain.Area area = this.areaService.findOne(areaId);
			final domain.Chapter chapter = this.chapterService.findOne(chapterId);

			area.setChapter(chapter);

			this.areaService.save(area);

			super.unauthenticate();
			this.areaService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverSelfAssignedArea() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;

		//Chapter
		userAccount = this.accountService.create("Josuke", encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC1 = this.constructActor(userAccount, "CHAPTER", "Dio", "Josuke", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", null);

		userAccount = this.accountService.create("Josuke", encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC3 = this.constructActor(userAccount, "CHAPTER", "Dio", "Josuke", "Joestar", "http://photo.com", "jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123", null, null);

		final Object testingData[][] = {

			/*
			 * a) Functional requirements 2.1 Self-assigned area
			 * b) Positive case
			 * c) analysis of sentence coverage: 92%
			 * d) analysis of data coverage.
			 */
			{
				actorFormC1, null, "area4", null
			},
			/*
			 * a) Functional requirements 2.1 self-assigned area
			 * b) Case of negative tests - Business rule: Only a chapter can manage an specific area.
			 * c) analysis of sentence coverage: 92%
			 * d) analysis of data coverage.
			 */
			{
				actorFormC3, null, "area1", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	// Ancillary methods ------------------------------------------------------

	protected void template(final ActorForm actor, final String username, final String area, final Class<?> expected) {
		Class<?> caught;
		final int areaId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (area != null)
				areaId = super.getEntityId(area);
			else
				areaId = 0;
			actor.setArea(this.areaService.findOne(areaId));

			this.actorService.update(actor);
			super.unauthenticate();
			this.actorService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	private ActorForm constructActor(final UserAccount userAccount, final String auth, final String name, final String middleName, final String surname, final String photo, final String email, final String phone, final String address, final String title,
		final String pictures) {
		final ActorForm result = new ActorForm();

		final int id = 0;
		final int version = 0;

		result.setId(id);
		result.setVersion(version);
		result.setUserAccount(userAccount);
		result.setAuth(auth);

		result.setName(name);
		result.setMiddleName(middleName);
		result.setSurname(surname);
		result.setPhoto(photo);
		result.setEmail(email);
		result.setPhone(phone);
		result.setAddress(address);
		result.setTitle(title);
		result.setPictures(pictures);

		return result;
	}

}
