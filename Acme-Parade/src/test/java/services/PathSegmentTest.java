package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Path;
import domain.Segment;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PathSegmentTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private PathService pathService;

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private ParadeService paradeService;

	// Tests ------------------------------------------------------------------
	@Test
	public void driverManage() {
		final int pathId = super.getEntityId("path1");
		final int segmentId = super.getEntityId("segment1");
		final int paradeId = super.getEntityId("parade1");
		final int pathIdForSegment = pathId;
		final double latitudeLongitude = 12345.12345;
		final double aproxTime = 1.5;
		final int segmentIdForEdit = segmentId;
		final int segmentIdForDelete = segmentId;

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 3.3 Create a path. b) Positive
				 * test c) analysis of sentence coverage: 98.2% d) analysis of
				 * data coverage - se crea un path de parade1 siendo
				 * brotherhood1.
				 */
				{ paradeId, "brotherhood1", 0, 0.0, 0.0, 0, 0, 0, null },
				/*
				 * a) Functional requirements - 3.3 Create a path. b) Negative
				 * test - Business rule: Cannot create a path for a foreign
				 * parade. c) analysis of sentence coverage: 98.2% d) analysis
				 * of data coverage - se intenta crear un path de parade1 siendo
				 * brotherhood2.
				 */
				{ paradeId, "brotherhood2", 0, 0.0, 0.0, 0, 0, 0,
						java.lang.IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 3.3 Delete a path. b) Positive
				 * test c) analysis of sentence coverage: 98.2% d) analysis of
				 * data coverage - se elimina path1 siendo brotherhood1.
				 */
				{ 0, "brotherhood1", pathId, 0.0, 0.0, 0, 0, 0, null },
				/*
				 * a) Functional requirements - 3.3 Delete a path. b) Negative
				 * test - Business rule: Cannot delete a path of a foreign
				 * parade. c) analysis of sentence coverage: 98.2% d) analysis
				 * of data coverage - se intenta eliminar path1 siendo
				 * brotherhood2.
				 */
				{ 0, "brotherhood2", pathId, 0.0, 0.0, 0, 0, 0,
						java.lang.IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 3.3 create a segment. b)
				 * Positive test c) analysis of sentence coverage: 98.2% d)
				 * analysis of data coverage - se crea un segment con todas las
				 * longitudes y latitudes = 12345.12345 y todos los tiempor
				 * aproximados = 1.5, todo ello para el path1 siendo
				 * brotherhood1.
				 */
				{ 0, "brotherhood1", 0, latitudeLongitude, aproxTime,
						pathIdForSegment, 0, 0, null },
				/*
				 * a) Functional requirements - 3.3 Create a segment. b)
				 * Negative test - Business rule: Cannot create a segment of a
				 * foreign path c) analysis of sentence coverage: 98.2% d)
				 * analysis of data coverage - se intenta crear un segment para
				 * el path1 siendo brotherhood2.
				 */
				{ 0, "brotherhood2", 0, latitudeLongitude, aproxTime,
						pathIdForSegment, 0, 0,
						java.lang.IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 3.3 Edit a segment. b) Positive
				 * test c) analysis of sentence coverage: 98.2% d) analysis of
				 * data coverage - se edita segment1 con aproxTime = 1.5 siendo
				 * brotherhood1.
				 */
				{ 0, "brotherhood1", 0, 0.0, aproxTime, 0, segmentIdForEdit, 0,
						null },
				/*
				 * a) Functional requirements - 3.3 Edit a segment. b) Negative
				 * test - Business rule: Cannot edit a segment of a foreign path
				 * c) analysis of sentence coverage: 98.2% d) analysis of data
				 * coverage - se intenta editar segment1 siendo brotherhood2.
				 */
				{ 0, "brotherhood2", 0, 0.0, 0.0, 0, segmentIdForEdit, 0,
						java.lang.IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 3.3 Delete a segment. b)
				 * Positive test c) analysis of sentence coverage: 98.2% d)
				 * analysis of data coverage - se elimina segment1 siendo
				 * brotherhood1.
				 */
				{ 0, "brotherhood1", 0, 0.0, 0.0, 0, 0, segmentIdForDelete,
						null },
				/*
				 * a) Functional requirements - 3.3 Delete a segment. b)
				 * Negative test - Business rule: Cannot delete a segment of a
				 * foreign path c) analysis of sentence coverage: 98.2% d)
				 * analysis of data coverage - se intenta eliminar segment1
				 * siendo brotherhood2.
				 */
				{ 0, "brotherhood2", 0, 0.0, 0.0, 0, 0, segmentIdForDelete,
						java.lang.IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((int) testingData[i][0],
						(String) testingData[i][1], (int) testingData[i][2],
						(double) testingData[i][3], (double) testingData[i][4],
						(int) testingData[i][5], (int) testingData[i][6],
						(int) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateManage(final int paradeIdCreate,
			final String username, final int pathId,
			final double latitudeLongitude, final double aproxTime,
			final int pathIdForSegment, final int segmentIdForEdit,
			final int segmentIdForDelete, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			if (paradeIdCreate != 0) {
				Path p = pathService.create();
				p.setParade(paradeService.findOne(paradeIdCreate));
				pathService.save(p);
			} else if (pathId != 0) {
				Path p = pathService.findOne(pathId);
				pathService.delete(p);
			} else if (latitudeLongitude != 0.0) {
				Segment s = segmentService.create();
				s.setApproximateTimeDes(aproxTime);
				s.setApproximateTimeOri(aproxTime);
				s.setDestinationLatitude(latitudeLongitude);
				s.setDestinationLongitude(latitudeLongitude);
				s.setOriginLatitude(latitudeLongitude);
				s.setOriginLongitude(latitudeLongitude);
				s.setPath(pathService.findOne(pathIdForSegment));
				segmentService.save(s);
			} else if (segmentIdForEdit != 0) {
				Segment s = segmentService.findOne(segmentIdForEdit);
				s.setApproximateTimeDes(aproxTime);
				segmentService.save(s);
			} else if (segmentIdForDelete != 0) {
				Segment s = segmentService.findOne(segmentIdForDelete);
				segmentService.delete(s);
			}

			super.unauthenticate();
			this.pathService.flush();
			this.segmentService.flush();
			this.paradeService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
