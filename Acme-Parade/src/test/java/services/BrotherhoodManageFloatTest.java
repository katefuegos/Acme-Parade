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

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodManageFloatTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private FloaatService floaatService;

	@Autowired
	// Tests ------------------------------------------------------------------
	@Test
	public void driverManage() {
		this.authenticate("brotherhood1");
		final domain.Floaat floaatCreated = this.floaatService.create();

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.1 Manage Float - Create a
				 * float b) Positive tests c) analysis of sentence coverage:
				 * 92.3% d) analysis of data coverage - se crea un float con
				 * atributo title="new title" siendo brotherhood1.
				 */
				{ null, floaatCreated, "brotherhood1", "new title", null },
				/*
				 * a) Functional requirements - 10.1 Manage Float - Create a
				 * float b) Negative tests - Business rule: It can not be
				 * modified by another brotherhood. c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se intenta
				 * crear un float con atributo title="new title" siendo
				 * brotherhood2.
				 */
				{ null, floaatCreated, "brotherhood2", "new title",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.1 Manage Float - Edit a float
				 * b) Positive tests - c) analysis of sentence coverage: 92.3%
				 * d) analysis of data coverage - se edita el title de un float
				 * con el valor "new title" siendo brotherhood1.
				 */
				{ "float1", null, "brotherhood1", "new title", null },

				/*
				 * a) Functional requirements - 10.1 Manage Float - Edit a float
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 92.3%
				 * d) analysis of data coverage - se intenta editar el title de
				 * un float con el valor "new title" siendo brotherhood2.
				 */
				{ "float1", null, "brotherhood2", "new title",
						IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0],
						(domain.Floaat) testingData[i][1],
						(String) testingData[i][2], (String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final String nameFloat,
			final domain.Floaat newFloaat, final String username,
			final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int floatId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			domain.Floaat floaat;
			if (newFloaat == null) {
				floatId = super.getEntityId(nameFloat);
				floaat = this.floaatService.findOne(floatId);
			} else
				floaat = newFloaat;

			floaat.setTitle(newTitle);
			floaat.setDescription("description1");
			floaat.setPictures("http://www.pictures.com");

			this.floaatService.save(floaat);

			super.unauthenticate();
			this.floaatService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
