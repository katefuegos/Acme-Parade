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
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;
import domain.Brotherhood;
import forms.ActorForm;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegistrationTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private UserAccountService accountService;

	@Autowired
	private AreaService areaService;

	// Tests ------------------------------------------------------------------

	@Test
	public void driverEditPersonalData() {

		final Integer id = this.getEntityId("brotherhood1");

		final Brotherhood brotherhood = this.brotherhoodService.findOne(id);

		final ActorForm actorForm1 = this.constructActor(
				brotherhood.getUserAccount(), brotherhood.getId(),
				"BROTHERHOOD", brotherhood.getName(),
				brotherhood.getMiddleName(), brotherhood.getSurname(),
				brotherhood.getPhoto(), brotherhood.getEmail(),
				brotherhood.getPhone(), brotherhood.getAddress(),
				brotherhood.getTitle(), brotherhood.getPictures());

		final ActorForm actorForm2 = this.constructActor(
				brotherhood.getUserAccount(), brotherhood.getId(),
				"BROTHERHOOD", brotherhood.getName(),
				brotherhood.getMiddleName(), brotherhood.getSurname(),
				brotherhood.getPhoto(), brotherhood.getEmail(),
				brotherhood.getPhone(), brotherhood.getAddress(), null,
				brotherhood.getPictures());

		final Object testingData[][] = {
				/*
				 * a) Functional requirements 9.2 - Edit personal data b)
				 * Positive tests c) analysis of sentence coverage: 93% with
				 * eclemma d) analysis of data coverage. The actor brotherhood1
				 * is being modified with the following data: area=area1 The
				 * actor in charge is: brotherhood1
				 */
				{ actorForm1, "brotherhood1", "area1", null },
				/*
				 * a) Functional requirements 9.2 - Edit personal data b)
				 * Negative tests - Business rule: Attribute title must not be
				 * null c) analysis of sentence coverage: 93% with eclemma d)
				 * analysis of data coverage. The actor brotherhood1 is being
				 * modified with the following data: area=area1, title=null The
				 * actor in charge is: brotherhood1
				 */
				{ actorForm2, "brotherhood1", "area1",
						javax.validation.ConstraintViolationException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverBrotherhood() {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		UserAccount userAccount = null;
		// Brotherhood
		userAccount = this.accountService.create("Joseph",
				encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB1 = this.constructActor(userAccount, 0,
				"BROTHERHOOD", "Joseph", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", "JoJo's Bizarre Adventure",
				"http://www.picture.com");

		userAccount = this.accountService.create("Joseph",
				encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB2 = this.constructActor(userAccount, 0,
				"BROTHERHOOD", "Joseph", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", null, "http://www.picture.com");

		userAccount = this.accountService.create("Joseph",
				encoder.encodePassword("jojo12345", null), "BROTHERHOOD");
		final ActorForm actorFormB3 = this.constructActor(userAccount, 0,
				"BROTHERHOOD", "Joseph", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", "JoJo's Bizarre Adventure", null);

		final Object testingData[][] = {
				/*
				 * a) Functional requirements 8.1 - Register as a brotherhood
				 * and Requirement 20.1 Select area b) Positive tests c)
				 * analysis of sentence coverage: 93% with eclemma d) analysis
				 * of data coverage. The new brotherhood is being modified with
				 * the following data: useraccount.username=Joseph,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=BROTHERHOOD
				 * ,name="Joseph",middleName="Joestar",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123",title=
				 * "JoJo's Bizarre Adventure",picture= "http://www.picture.com"
				 * The actor in charge is: unauthenticate
				 */
				{ actorFormB1, null, "area1", null },
				/*
				 * a) Functional requirements 8.1 - Register as a brothergood
				 * and Requirement 20.1 Select area b) Negative tests - Business
				 * rule: Attribute title must not be null c) analysis of
				 * sentence coverage: 93% with eclemma d) analysis of data
				 * coverage. The new brotherhood is being modified with the
				 * following data: useraccount.username=Joseph,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=BROTHERHOOD
				 * ,name="Joseph",middleName="Joestar",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123",title= null,picture=
				 * "http://www.picture.com" The actor in charge is:
				 * unauthenticate
				 */
				{ actorFormB2, null, "area1",
						javax.validation.ConstraintViolationException.class },

				/*
				 * a) Functional requirements 8.1 - Register as a brotherhood b)
				 * Negative tests - Business rule: Attribute picture must not be
				 * null c) analysis of sentence coverage: 93% with eclemma d)
				 * analysis of data coverage. The new brotherhood is being
				 * modified with the following data:
				 * useraccount.username=Joseph, useraccount.password=jojo12345,
				 * useraccount
				 * .authority=BROTHERHOOD,name="Joseph",middleName="Joestar"
				 * ,surname= "Joestar",photo= "http://www.photo.com",email=
				 * "jojo@hotmail.com",phone= "654789321",address=
				 * "Calle falsa 123",title= "JoJo's Bizarre Adventure",picture=
				 * null The actor in charge is: unauthenticate
				 */{ actorFormB3, null, "area1",
						javax.validation.ConstraintViolationException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
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

		// Member
		userAccount = this.accountService.create("Jotaro",
				encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM1 = this.constructActor(userAccount, 0,
				"MEMBER", "Jotaro", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jotaro",
				encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM2 = this.constructActor(userAccount, 0,
				"MEMBER", null, "Joestar", "Joestar", "http://www.photo.com",
				"jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jotaro",
				encoder.encodePassword("jojo12345", null), "MEMBER");
		final ActorForm actorFormM3 = this.constructActor(userAccount, 0,
				"MEMBER", "Jotaro", "Joestar", "Joestar", "photo",
				"jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		final Object testingData[][] = {

				/*
				 * a) Functional requirements 8.1 - Register as a member b)
				 * Positive tests c) analysis of sentence coverage: 93% with
				 * eclemma d) analysis of data coverage. The new member is being
				 * modified with the following data:
				 * useraccount.username=Jotaro, useraccount.password=jojo12345,
				 * useraccount
				 * .authority=MEMBER,name="Jotaro",middleName="Joestar",surname=
				 * "Joestar",photo= "http://www.photo.com",email=
				 * "jojo@hotmail.com",phone= "654789321",address=
				 * "Calle falsa 123" The actor in charge is: unauthenticate
				 */
				{ actorFormM1, null, null, null },
				/*
				 * a) Functional requirements 8.1 - Register as a member b)
				 * Negative tests - Business rule: Attribute name must not be
				 * null c) analysis of sentence coverage: 93% with eclemma d)
				 * analysis of data coverage. The new member is being modified
				 * with the following data: useraccount.username=Jotaro,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=MEMBER,name
				 * =null,middleName="Joestar",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123" The actor in charge
				 * is: unauthenticate
				 */
				{ actorFormM2, null, null,
						javax.validation.ConstraintViolationException.class },

				/*
				 * a) Functional requirements 8.1 - Register as a member b)
				 * Negative tests - Business rule: Attribute photo must be a url
				 * c) analysis of sentence coverage: 93% with eclemma d)
				 * analysis of data coverage. The new member is being modified
				 * with the following data: useraccount.username=Jotaro,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=MEMBER,name
				 * ="Jotaro",middleName="Joestar",surname= "Joestar",photo=
				 * "photo",email= "jojo@hotmail.com",phone= "654789321",address=
				 * "Calle falsa 123" The actor in charge is: unauthenticate
				 */
				{ actorFormM3, null, null,
						javax.validation.ConstraintViolationException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
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

		// Administrator
		userAccount = this.accountService.create("Jonathan",
				encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA1 = this.constructActor(userAccount, 0,
				"ADMIN", "Jonathan", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jonathan",
				encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA2 = this.constructActor(userAccount, 0,
				"ADMIN", "Jonathan", "Joestar", "Joestar",
				"http://www.photo.com", "jojo@hotmail.com", "654789321",
				"Calle falsa 123", null, null);

		userAccount = this.accountService.create("Jonathan",
				encoder.encodePassword("jojo12345", null), "ADMIN");
		final ActorForm actorFormA3 = this.constructActor(userAccount, 0,
				"ADMIN", "Jonathan", "Joestar", "Joestar", "photo",
				"jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123",
				null, null);

		final Object testingData[][] = {

				/*
				 * a) Functional requirements 12.1 - Create a user accounts for
				 * a new administrator b) Positive tests c) analysis of sentence
				 * coverage: 93% with eclemma d) analysis of data coverage. The
				 * new admin is being modified with the following data:
				 * useraccount.username=Jonathan,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=ADMIN,name
				 * ="Jonathan",middleName="Joestar",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123" The actor in charge
				 * is: admin
				 */
				{ actorFormA1, "admin", null, null },
				/*
				 * a) Functional requirements 12.1 - Create a user accounts for
				 * a new administrator b) Negative tests - Business rule: You
				 * must be authenticate as a administrator. c) analysis of
				 * sentence coverage: 93% with eclemma d) analysis of data
				 * coverage. The new admin is being modified with the following
				 * data: useraccount.username=Jonathan,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=ADMIN,name
				 * ="Jonathan",middleName="Joestar",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123" The actor in charge
				 * is: unauthenticate
				 */

				{ actorFormA2, null, null, IllegalArgumentException.class },
				/*
				 * a) Functional requirements 12.1 - Create a user accounts for
				 * a new administrator b) Negative tests - Business rule:
				 * Attribute photo must be a url c) analysis of sentence
				 * coverage: 93% with eclemma d) analysis of data coverage. The
				 * new admin is being modified with the following data:
				 * useraccount.username=Jonathan,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=ADMIN,name
				 * ="Jonathan",middleName="Joestar",surname= "Joestar",photo=
				 * "uno dos tres cuatro",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123" The actor in charge
				 * is: admin
				 */
				{ actorFormA3, "admin", null,
						javax.validation.ConstraintViolationException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
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

		// Chapter
		userAccount = this.accountService.create("Josuke",
				encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC1 = this.constructActor(userAccount, 0,
				"CHAPTER", "Dio", "Josuke", "Joestar", "http://www.photo.com",
				"jojo@hotmail.com", "654789321", "Calle falsa 123",
				"JoJo's Bizarre Adventure", null);

		userAccount = this.accountService.create("Josuke",
				encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC2 = this.constructActor(userAccount, 0,
				"CHAPTER", null, "Josuke", "Joestar", "http://www.photo.com",
				"jojo@hotmail.com", "654789321", "Calle falsa 123", null, null);

		userAccount = this.accountService.create("Josuke",
				encoder.encodePassword("jojo12345", null), "CHAPTER");
		final ActorForm actorFormC3 = this.constructActor(userAccount, 0,
				"CHAPTER", "Dio", "Josuke", "Joestar", "http://photo.com",
				"jojo@hotmail.com", "uno dos tres cuatro", "Calle falsa 123",
				null, null);

		final Object testingData[][] = {

				/*
				 * a) Functional requirements 7.1 - Register as a chapter and
				 * 2.1 self-assigned area b) Positive case c) analysis of
				 * sentence coverage: 93% with eclemma d) analysis of data
				 * coverage. The new chapter is being modified with the
				 * following data: useraccount.username=Josuke,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=ADMIN,name
				 * ="Dio",middleName="Josuke",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123",
				 * title="JoJo's Bizarre Adventure" The actor in charge is:
				 * unauthenticate
				 */
				{ actorFormC1, null, "area4", null },
				/*
				 * a) Functional requirements 7.1 - Register as a chapter b)
				 * Case of negative tests - Business rule: Name of actor must
				 * not be null. c) analysis of sentence coverage: 93% with
				 * eclemma d) analysis of data coverage. The new chapter is
				 * being modified with the following data:
				 * useraccount.username=Josuke, useraccount.password=jojo12345,
				 * useraccount
				 * .authority=ADMIN,name=null,middleName="Josuke",surname=
				 * "Joestar",photo= "http://www.photo.com",email=
				 * "jojo@hotmail.com",phone= "654789321",address=
				 * "Calle falsa 123", title=null The actor in charge is:
				 * unauthenticate
				 */
				{ actorFormC2, null, "area4",
						javax.validation.ConstraintViolationException.class },
				/*
				 * a) Functional requirements 7.1 - Register as a chapter and
				 * 2.1 self-assigned area b) Case of negative tests - Business
				 * rule: Title of actor must not be null. c) analysis of
				 * sentence coverage: 93% with eclemma d) analysis of data
				 * coverage. The new chapter is being modified with the
				 * following data: useraccount.username=Josuke,
				 * useraccount.password=jojo12345,
				 * useraccount.authority=ADMIN,name
				 * ="Dio",middleName="Josuke",surname= "Joestar",photo=
				 * "http://www.photo.com",email= "jojo@hotmail.com",phone=
				 * "654789321",address= "Calle falsa 123", title=null The actor
				 * in charge is: unauthenticate
				 */
				{ actorFormC3, null, "area4",
						javax.validation.ConstraintViolationException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((ActorForm) testingData[i][0],
						(String) testingData[i][1], (String) testingData[i][2],
						(Class<?>) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final ActorForm actor, final String username,
			final String area, final Class<?> expected) {
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

	private ActorForm constructActor(final UserAccount userAccount,
			final int id, final String auth, final String name,
			final String middleName, final String surname, final String photo,
			final String email, final String phone, final String address,
			final String title, final String pictures) {
		final ActorForm result = new ActorForm();

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
