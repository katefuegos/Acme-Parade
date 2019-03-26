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

	//	@Test
	//	public void driverBrotherhood() {
	//		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
	//		UserAccount userAccount = null;
	//		// Brotherhood
	//		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
	//		final ActorForm actorFormB1 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", "www.picture.com");
	//
	//		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
	//		final ActorForm actorFormB2 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, "www.picture.com");
	//
	//		userAccount = this.accountService.create("Joseph", encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
	//		final ActorForm actorFormB3 = this.constructActor(userAccount, "BROTHERHOOD", "Joseph", "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", "JoJo's Bizarre Adventure", null);
	//
	//		final Object testingData[][] = {
	//			/*
	//			 * Brotherhood
	//			 */
	//			{
	//				actorFormB1, null, "area1", null
	//			}, {
	//				actorFormB2, null, "area1", IllegalArgumentException.class
	//			}, {
	//				actorFormB3, null, "area1", IllegalArgumentException.class
	//			}
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			try {
	//				super.startTransaction();
	//				this.template((ActorForm) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	//			} catch (final Throwable oops) {
	//				throw new RuntimeException(oops);
	//			} finally {
	//				super.rollbackTransaction();
	//			}
	//	}
	//
	//	@Test
	//	public void driverMember() {
	//		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
	//		UserAccount userAccount = null;
	//
	//		//Member
	//		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
	//		final ActorForm actorFormM1 = this.constructActor(userAccount, "MEMBER", "Jotaro", "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);
	//
	//		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
	//		final ActorForm actorFormM2 = this.constructActor(userAccount, "MEMBER", null, "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);
	//
	//		userAccount = this.accountService.create("Jotaro", encoder.encodePassword("jojo12345", null), "MEMBER");
	//		final ActorForm actorFormM3 = this.constructActor(userAccount, "MEMBER", "Jotaro", "Joestar", "photo", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);
	//
	//		final Object testingData[][] = {
	//
	//			/*
	//			 * Member
	//			 */
	//			{
	//				actorFormM1, null, null, null
	//			}, {
	//				actorFormM2, null, null, IllegalArgumentException.class
	//			}, {
	//				actorFormM3, null, null, IllegalArgumentException.class
	//			}
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			try {
	//				super.startTransaction();
	//				this.template((ActorForm) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	//			} catch (final Throwable oops) {
	//				throw new RuntimeException(oops);
	//			} finally {
	//				super.rollbackTransaction();
	//			}
	//	}
	//
	//	@Test
	//	public void driverAdministrator() {
	//		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
	//		UserAccount userAccount = null;
	//
	//		//Administrator
	//		userAccount = this.accountService.create("Dio", encoder.encodePassword("jojo12345", null), "ADMIN");
	//		final ActorForm actorFormA1 = this.constructActor(userAccount, "ADMIN", "Dio", "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);
	//
	//		userAccount = this.accountService.create("Dio", encoder.encodePassword("jojo12345", null), "ADMIN");
	//		final ActorForm actorFormA2 = this.constructActor(userAccount, "ADMIN", null, "Joestar", "www.photo.com", "jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);
	//
	//		userAccount = this.accountService.create("Dio", encoder.encodePassword("jojo12345", null), "ADMIN");
	//		final ActorForm actorFormA3 = this.constructActor(userAccount, "ADMIN", "Dio", "Joestar", "photo", "jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123", null, null);
	//
	//		final Object testingData[][] = {
	//
	//			/*
	//			 * Administrator
	//			 */
	//			{
	//				actorFormA1, "admin", "area1", null
	//			}, {
	//				actorFormA2, null, "area1", IllegalArgumentException.class
	//			}, {
	//				actorFormA3, "admin", "area1", IllegalArgumentException.class
	//			}
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			try {
	//				super.startTransaction();
	//				this.template((ActorForm) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	//			} catch (final Throwable oops) {
	//				throw new RuntimeException(oops);
	//			} finally {
	//				super.rollbackTransaction();
	//			}
	//	}

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
			 * Chapter
			 */
			{
				actorFormC1, null, "area4", null
			}, {
				actorFormC2, null, "area4", javax.validation.ConstraintViolationException.class
			}, {
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
