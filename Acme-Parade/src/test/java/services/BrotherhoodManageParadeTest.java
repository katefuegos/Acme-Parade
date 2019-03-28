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

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BrotherhoodManageParadeTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private ParadeService paradeService;

	@Autowired
	// Tests ------------------------------------------------------------------
	@Test
	public void driverManageParade() {
		this.authenticate("brotherhood1");
		final domain.Parade paradeCreated = this.paradeService.create();

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.2 Manage Parade - Create a
				 * Parade b) Positive tests c) analysis of sentence coverage:
				 * 92.3% d) analysis of data coverage - se crea Parade con
				 * atributos title="new title", description="description1",
				 * moment=fecha actual + valor aleatorio. Los demás atributos se
				 * generan automáticamente con el método create siendo
				 * brotherhood1.
				 */
				{ null, paradeCreated, "brotherhood1", "new title", null },
				/*
				 * a) Functional requirements - 10.2 Manage Parade - Create a
				 * Parade b) Negative tests - Business rule: It can not be
				 * modified by another brotherhood. c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se intenta
				 * crear Parade siendo brotherhood2.
				 */
				{ null, paradeCreated, "brotherhood2", "new title",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.2 Manage Parade - Edit a
				 * Parade b) Positive tests - c) analysis of sentence coverage:
				 * 92.3% d) analysis of data coverage - se edita el title de
				 * parade1 con el valor "new title" siendo brotherhood1.
				 */
				{ "parade1", null, "brotherhood1", "new title", null },

				/*
				 * a) Functional requirements - 10.2 Manage Parade - Edit a
				 * Parade b) Negative tests - Business rule: It can not be
				 * modified by another brotherhood. c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se intenta
				 * editar el title de parade1 con el valor "new title" siendo
				 * brotherhood2.
				 */
				{ "parade1", null, "brotherhood2", "new title",
						IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0],
						(domain.Parade) testingData[i][1],
						(String) testingData[i][2], (String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final String nameParade,
			final domain.Parade newParade, final String username,
			final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int paradeId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			domain.Parade parade;
			if (newParade == null) {
				paradeId = super.getEntityId(nameParade);
				parade = this.paradeService.findOne(paradeId);
			} else
				parade = newParade;

			parade.setTitle(newTitle);
			parade.setDescription("description1");
			parade.setMoment(new Date(new Date().getTime() + 315360000000L));

			this.paradeService.save(parade);

			super.unauthenticate();
			this.paradeService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeleteParade() {

		final Object testingData[][] = {

		/*
		 * a) Functional requirements - 12.2 Manage Position - Delete a parade
		 * b) Positive tests - c) analysis of sentence coverage: 92.3% d)
		 * analysis of data coverage - se elimina parade5 siendo brotherhood1.
		 */
		{ "parade5", "brotherhood1", null },

		/*
		 * a) Functional requirements - 12.2 Manage Position - Delete a parade
		 * b) Negative tests - Business rule: Only brotherhood can delete it. c)
		 * analysis of sentence coverage: 92.3% d) analysis of data coverage se
		 * intenta eliminar parade5 siendo admin.
		 */
		{ "parade5", "admin", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDelete((String) testingData[i][0],
						(String) testingData[i][1],
						(Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateDelete(final String nameParade,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.Parade parade = this.paradeService.findOne(super
					.getEntityId(nameParade));

			this.paradeService.delete(parade);

			this.paradeService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
