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

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Position;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private PositionService positionService;

	// Tests ------------------------------------------------------------------
	@Test
	public void driverManagePosition() {
		final Position position = this.positionService.create();

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 12.2 Manage Position - Create a
				 * Position b) Positive tests c) analysis of sentence coverage:
				 * 92.3% d) analysis of data coverage - se crea un position con
				 * valores "new position" siendo admin.
				 */
				{ null, position, "admin", "new position", null },
				/*
				 * a) Functional requirements - 12.2 Manage Position - Create a
				 * Position b) Negative tests - Business rule: Only
				 * administrators can modify it. c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se intenta
				 * crear un position siendo brotherhood2.
				 */
				{ null, position, "brotherhood2", "new position",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 12.2 Manage Position - Edit a
				 * Position b) Positive tests - c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se edita
				 * position1 con valor = "new position" siendo admin.
				 */
				{ "position1", null, "admin", "new position", null },

				/*
				 * a) Functional requirements - 12.2 Manage Position - Edit a
				 * Position b) Negative tests - Business rule: Only
				 * administrators can modify it. c) analysis of sentence
				 * coverage: 92.3% d) analysis of data coverage - se intenta
				 * editar position1 siendo brotherhood2.
				 */
				{ "position1", null, "brotherhood2", "new position",
						IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0],
						(domain.Position) testingData[i][1],
						(String) testingData[i][2], (String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final String namePosition,
			final domain.Position newPosition, final String username,
			final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int positionId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			domain.Position position;
			if (newPosition == null) {
				positionId = super.getEntityId(namePosition);
				position = this.positionService.findOne(positionId);
			} else
				position = newPosition;

			final Map<String, String> map = new HashMap<String, String>();
			map.put("EN", newTitle);
			map.put("ES", newTitle);
			position.setName(map);

			this.positionService.save(position);

			super.unauthenticate();
			this.positionService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDeletePosition() {

		final Object testingData[][] = {

		/*
		 * a) Functional requirements - 12.2 Manage Position - Delete a Position
		 * b) Positive tests - c) analysis of sentence coverage: 92.3% d)
		 * analysis of data coverage - se elimina position1 siendo admin.
		 */
		{ "position1", "admin", null },

		/*
		 * a) Functional requirements - 12.2 Manage Position - Delete a Position
		 * b) Negative tests - Business rule: Only administrators can modify it.
		 * c) analysis of sentence coverage: 92.3% d) analysis of data coverage
		 * - se intenta eliminar position1 siendo brotherhood2.
		 */
		{ "position1", "brotherhood2", IllegalArgumentException.class },

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

	protected void templateDelete(final String namePosition,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.Position position = this.positionService.findOne(super
					.getEntityId(namePosition));

			this.positionService.delete(position);

			this.positionService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
