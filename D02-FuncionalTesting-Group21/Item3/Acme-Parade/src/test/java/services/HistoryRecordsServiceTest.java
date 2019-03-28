package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import utilities.AbstractTest;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@Transactional
public class HistoryRecordsServiceTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private PeriodRecordService periodRecordService;

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	@Autowired
	private LegalRecordService legalRecordService;

	@Autowired
	private LinkRecordService linkRecordService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Tests ------------------------------------------------------------------

	@Test
	public void driverMiscellaneousRecord() {

		final int historyId = super.getEntityId("history1");
		final int miscellaneousRecordId = super
				.getEntityId("miscellaneousRecord1");

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Create b) Positive tests c) analysis of sentence coverage:
				 * 95.8% d) analysis of data coverage - se crea un
				 * miscellaneousRecord con title="title" y
				 * description="description", para history1 siendo brotherhood1.
				 */
				{ 0, historyId, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Create b) Negative tests - Business rule: It can not be
				 * modified by another brotherhood. c) analysis of sentence
				 * coverage: 95.8% d) analysis of data coverage - se intenta
				 * crear un miscellaneousRecord para history1 siendo
				 * brotherhood2.
				 */
				{ 0, historyId, 0, "brotherhood2",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Edit b) Positive tests - c) analysis of sentence coverage:
				 * 95.8% d) analysis of data coverage - se edita
				 * miscellaneousRecord1 cambiando su title a "newTitle" siendo
				 * brotherhood1.
				 */
				{ miscellaneousRecordId, 0, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Edit b) Negative tests - Business rule: It can not be
				 * modified by another brotherhood. c) analysis of sentence
				 * coverage: 95.8% d) analysis of data coverage - se intenta
				 * editar el atributo title de miscellaneousRecordId1 a
				 * "newTitle" siendo brotherhood2.
				 */
				{ miscellaneousRecordId, 0, 0, "brotherhood2",
						IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Edit b) Positive test c) analysis of sentence coverage:
				 * 95.8% d) analysis of data coverage - se elimina
				 * miscellaneousRecord1 siendo brotherhood1.
				 */
				{ miscellaneousRecordId, 0, miscellaneousRecordId,
						"brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage MiscellaneousRecord
				 * - Edit b) Negative tests - Business rule: It can not be
				 * deleted by another brotherhood. c) analysis of sentence
				 * coverage: 95.8% d) analysis of data coverage - se intenta
				 * eliminar miscellaneousRecord1 siendo brotherhood2.
				 */
				{ miscellaneousRecordId, 0, miscellaneousRecordId,
						"brotherhood2", IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageMiscellaneousRecord((int) testingData[i][0],
						(int) testingData[i][1], (int) testingData[i][2],
						(String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverLegalRecord() {

		final int historyId = super.getEntityId("history1");
		final int legalRecordId = super.getEntityId("legalRecord1");

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Create
				 * b) Positive tests c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se crea un legalRecord con
				 * title="title" y description="description", VATNumber=21,
				 * laws="laws", legalName="legal name", para history1 siendo
				 * brotherhood1.
				 */
				{ 0, historyId, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord- Create
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta crear un
				 * legalRecord para history1 siendo brotherhood2.
				 */
				{ 0, historyId, 0, "brotherhood2",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Positive tests - c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se edita legalRecord1
				 * cambiando su title a "newTitle" siendo brotherhood1.
				 */
				{ legalRecordId, 0, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta editar el atributo
				 * title de legalRecord1 a "newTitle" siendo brotherhood2.
				 */
				{ legalRecordId, 0, 0, "brotherhood2",
						IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Positive test c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se elimina legalRecord1 siendo
				 * brotherhood1.
				 */
				{ legalRecordId, 0, legalRecordId, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Negative tests - Business rule: It can not be deleted by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta eliminar
				 * legalRecord1 siendo brotherhood2.
				 */
				{ legalRecordId, 0, legalRecordId, "brotherhood2",
						IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageLegalRecord((int) testingData[i][0],
						(int) testingData[i][1], (int) testingData[i][2],
						(String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverPeriodRecord() {

		final int historyId = super.getEntityId("history1");
		final int periodRecordId = super.getEntityId("periodRecord1");

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.2 Manage PeriodRecord -
				 * Create b) Positive tests c) analysis of sentence coverage:
				 * 95.8% d) analysis of data coverage - se crea un periodRecord
				 * con title="title" ,description="description", startYear=2018,
				 * endYear=2019, para history1 siendo brotherhood1.
				 */
				{ 0, historyId, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage PeriodRecord- Create
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta crear un
				 * periodRecord para history1 siendo brotherhood2.
				 */
				{ 0, historyId, 0, "brotherhood2",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.2 Manage PeriodRecord - Edit
				 * b) Positive tests - c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se edita periodRecord1
				 * cambiando su title a "newTitle" siendo brotherhood1.
				 */
				{ periodRecordId, 0, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage PeriodRecord - Edit
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta editar el atributo
				 * title de periodRecord1 a "newTitle" siendo brotherhood2.
				 */
				{ periodRecordId, 0, 0, "brotherhood2",
						IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Positive test c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se elimina periodRecord1 siendo
				 * brotherhood1.
				 */
				{ periodRecordId, 0, periodRecordId, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Negative tests - Business rule: It can not be deleted by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta eliminar
				 * periodRecord1 siendo brotherhood2.
				 */
				{ periodRecordId, 0, periodRecordId, "brotherhood2",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManagePeriodRecord((int) testingData[i][0],
						(int) testingData[i][1], (int) testingData[i][2],
						(String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverLinkRecord() {

		final int historyId = super.getEntityId("history1");
		final int linkRecordId = super.getEntityId("linkRecord1");

		final Object testingData[][] = {
				/*
				 * a) Functional requirements - 10.2 Manage LinkRecord - Create
				 * b) Positive tests c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se crea un linkRecord con
				 * title="title" ,description="description",
				 * brotherhood=brotherhood1, para history1 siendo brotherhood1.
				 */
				{ 0, historyId, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LinkRecord- Create
				 * b) Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta crear un linkRecord
				 * para history1 siendo brotherhood2.
				 */
				{ 0, historyId, 0, "brotherhood2",
						IllegalArgumentException.class },

				/*
				 * a) Functional requirements - 10.2 Manage LinkRecord - Edit b)
				 * Positive tests - c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se edita linkRecord1 cambiando su
				 * title a "newTitle" siendo brotherhood1.
				 */
				{ linkRecordId, 0, 0, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LinkRecord - Edit b)
				 * Negative tests - Business rule: It can not be modified by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta editar el atributo
				 * title de linkRecord1 a "newTitle" siendo brotherhood2.
				 */
				{ linkRecordId, 0, 0, "brotherhood2",
						IllegalArgumentException.class },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Positive test c) analysis of sentence coverage: 95.8% d)
				 * analysis of data coverage - se elimina linkRecord1 siendo
				 * brotherhood1.
				 */
				{ linkRecordId, 0, linkRecordId, "brotherhood1", null },
				/*
				 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
				 * b) Negative tests - Business rule: It can not be deleted by
				 * another brotherhood. c) analysis of sentence coverage: 95.8%
				 * d) analysis of data coverage - se intenta eliminar
				 * linkRecord1 siendo brotherhood2.
				 */
				{ linkRecordId, 0, linkRecordId, "brotherhood2",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageLinkRecord((int) testingData[i][0],
						(int) testingData[i][1], (int) testingData[i][2],
						(String) testingData[i][3],
						(Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// ----MiscellaneousRecord----

	protected void templateManageMiscellaneousRecord(
			final int miscellaneousRecordId, final int historyId,
			final int miscellaneousRecordToDeleteId, final String username,
			final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (miscellaneousRecordId == 0) {
				MiscellaneousRecord mr = miscellaneousRecordService.create();
				mr.setDescription("description");
				mr.setTitle("title");
				mr.setHistory(historyService.findOne(historyId));
				miscellaneousRecordService.save(mr);
			} else if (miscellaneousRecordToDeleteId == 0) {
				MiscellaneousRecord mr = miscellaneousRecordService
						.findOne(miscellaneousRecordId);
				mr.setTitle("new title");
				miscellaneousRecordService.save(mr);
			} else {
				MiscellaneousRecord mr = miscellaneousRecordService
						.findOne(miscellaneousRecordId);
				miscellaneousRecordService.delete(mr);
			}

			super.unauthenticate();
			this.miscellaneousRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	// -------------PeriodRecord---------------

	protected void templateManagePeriodRecord(final int periodRecordId,
			final int historyId, final int periodRecordToDeleteId,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (periodRecordId == 0) {
				PeriodRecord pr = periodRecordService.create();
				pr.setDescription("description");
				pr.setTitle("title");
				pr.setStartYear(2018);
				pr.setEndYear(2019);
				pr.setHistory(historyService.findOne(historyId));
				periodRecordService.save(pr);
			} else if (periodRecordToDeleteId == 0) {
				PeriodRecord pr = periodRecordService.findOne(periodRecordId);
				pr.setTitle("new title");
				periodRecordService.save(pr);
			} else {
				PeriodRecord pr = periodRecordService.findOne(periodRecordId);
				periodRecordService.delete(pr);
			}

			super.unauthenticate();
			this.periodRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	// ------------------LegalRecord-------------------

	protected void templateManageLegalRecord(final int legalRecordId,
			final int historyId, final int legalRecordToDeleteId,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (legalRecordId == 0) {
				LegalRecord lr = legalRecordService.create();
				lr.setDescription("description");
				lr.setTitle("title");
				lr.setApplicableLaws("laws");
				lr.setLegalName("legal name");
				lr.setVATnumber(21);
				lr.setHistory(historyService.findOne(historyId));
				legalRecordService.save(lr);
			} else if (legalRecordToDeleteId == 0) {
				LegalRecord lr = legalRecordService.findOne(legalRecordId);
				lr.setTitle("new title");
				legalRecordService.save(lr);
			} else {
				LegalRecord lr = legalRecordService.findOne(legalRecordId);
				legalRecordService.delete(lr);
			}

			super.unauthenticate();
			this.legalRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	// ----------------------LinkRecord----------------------

	protected void templateManageLinkRecord(final int linkRecordId,
			final int historyId, final int linkRecordToDeleteId,
			final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			if (linkRecordId == 0) {
				LinkRecord lr = linkRecordService.create();
				lr.setDescription("description");
				lr.setTitle("title");
				lr.setBrotherhood(brotherhoodService
						.findByUserAccountId(LoginService.getPrincipal()
								.getId()));
				lr.setHistory(historyService.findOne(historyId));
				linkRecordService.save(lr);
			} else if (linkRecordToDeleteId == 0) {
				LinkRecord lr = linkRecordService.findOne(linkRecordId);
				lr.setTitle("new title");
				linkRecordService.save(lr);
			} else {
				LinkRecord lr = linkRecordService.findOne(linkRecordId);
				linkRecordService.delete(lr);
			}

			super.unauthenticate();
			this.linkRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
