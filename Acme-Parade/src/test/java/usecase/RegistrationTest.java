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
import utilities.AbstractTest;
import forms.ActorForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegistrationTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	accountService;

	@Autowired
	private AreaService			areaService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverBrotherhood() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;
		// Brotherhood
		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB1 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", "http://www.picture.com");

		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB2 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, "http://www.picture.com");

		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB3 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", null);

		final Object testingData[][] = {
			/*
			 * a) Functional requirements 8.1 - Register as a brotherhood
			 * b) Positive tests
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormB1, null, "area1", null
			},
			/*
			 * a) Functional requirements 8.1 - Register as a member
			 * b) Negative tests - Business rule: Attribute title must not be null
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormB2, null, "area1", javax.validation.ConstraintViolationException.class
			},

			/*
			 * a) Functional requirements 8.1 - Register as a member
			 * b) Negative tests - Business rule: Attribute picture must not be null
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */{
				actorFormB3, null, "area1", javax.validation.ConstraintViolationException.class
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

	@Test
	public void driverMember() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;

		//Member
		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM1 = this.constructActor(userAccount, "MEMBER", "Jotaro", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM2 = this.constructActor(userAccount, "MEMBER", null, "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM3 = this.constructActor(userAccount, "MEMBER", "Jotaro", "Joestar", "Joestar", "photo", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		final Object testingData[][] = {

			/*
			 * a) Functional requirements 8.1 - Register as a member
			 * b) Positive tests
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormM1, null, null, null
			},
			/*
			 * a) Functional requirements 8.1 - Register as a member
			 * b) Negative tests - Business rule: Attribute name must not be null
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormM2, null, null, javax.validation.ConstraintViolationException.class
			},

			/*
			 * a) Functional requirements 8.1 - Register as a member
			 * b) Negative tests - Business rule: Attribute photo must be a url
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormM3, null, null, javax.validation.ConstraintViolationException.class
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

	@Test
	public void driverAdministrator() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;

		//Administrator
		userAccount = this.accountService.create("Jonathan", encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA1 = this.constructActor(userAccount, "ADMIN", "Jonathan", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jonathan", encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA2 = this.constructActor(userAccount, "ADMIN", "Jonathan", "Joestar", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jonathan", encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA3 = this.constructActor(userAccount, "ADMIN", "Jonathan", "Joestar", "Joestar", "photo", "jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123", null, null);

		final Object testingData[][] = {

			/*
			 * a) Functional requirements 12.1 - Create a user accounts for a new administrator
			 * b) Positive tests
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormA1, "admin", null, null
			},
			/*
			 * a) Functional requirements 12.1 - Create a user accounts for a new administrator
			 * b) Negative tests - Business rule: You must be authenticate as a administrator.
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */

			{
				actorFormA2, null, null, IllegalArgumentException.class
			},
			/*
			 * a) Functional requirements 12.1 - Create a user accounts for a new administrator
			 * b) Negative tests - Business rule: Attribute photo must be a url
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormA3, "admin", null, javax.validation.ConstraintViolationException.class
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

	@Test
	public void driverChapter() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;

		//Chapter
		userAccount = this.accountService.create("Josuke", encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC1 = this.constructActor(userAccount, "CHAPTER", "Dio", "Josuke", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", null);

		userAccount = this.accountService.create("Josuke", encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC2 = this.constructActor(userAccount, "CHAPTER", null, "Josuke", "Joestar", "http://www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Josuke", encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC3 = this.constructActor(userAccount, "CHAPTER", "Dio", "Josuke", "Joestar", "http://photo.com", "jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123", null, null);

		final Object testingData[][] = {

			/*
			 * a) Functional requirements 7.1 - Register as a chapter
			 * b) Positive case
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormC1, null, "area4", null
			},
			/*
			 * a) Functional requirements 7.1 - Register as a chapter
			 * b) Case of negative tests - Business rule: Name of actor must not be null.
			 * c) analysis of sentence coverage;
			 * d) analysis of data coverage.
			 */
			{
				actorFormC2, null, "area4", javax.validation.ConstraintViolationException.class
			},
			/*
			 * a) Functional requirements 7.1 - Register as a chapter
			 * b) Case of negative tests - Business rule: Only a chapter can manage an specific area.
			 * c) analysis of sentence coverage;
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
