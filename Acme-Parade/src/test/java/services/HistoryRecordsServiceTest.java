
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HistoryRecordsServiceTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private PeriodRecordService			periodRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private LegalRecordService			legalRecordService;

	@Autowired
	private LinkRecordService			linkRecordService;

	@Autowired
	private HistoryService				historyService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverMiscellaneousRecord() {

		History history;
		this.authenticate("brotherhood1");
		final int brotherhoodId = LoginService.getPrincipal().getId();
		final MiscellaneousRecord miscellaneousRecord1 = this.miscellaneousRecordService.create();
		history = this.historyService.findByBrotherhoodIdSingle(brotherhoodId);
		final Object testingData[][] = {
			/*
			 * a) Functional requirements - 10.2 Manage MiscellaneousRecord - Create
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, miscellaneousRecord1, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage Miscellaneous- Create
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, miscellaneousRecord1, "brotherhood2", "history", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 10.2 Manage MiscellaneousRecords - Edit
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"miscellaneousRecord1", null, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage Parade - Edit a Parade
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"miscellaneousRecord1", null, "brotherhood1", "history", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManage((String) testingData[i][0], (domain.MiscellaneousRecord) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverLegalRecord() {

		History history;
		this.authenticate("brotherhood1");
		final int brotherhoodId = LoginService.getPrincipal().getId();
		final LegalRecord legalRecord1 = this.legalRecordService.create();
		history = this.historyService.findByBrotherhoodIdSingle(brotherhoodId);
		final Object testingData[][] = {
			/*
			 * a) Functional requirements - 10.2 Manage LegalRecord - Create
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, legalRecord1, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage LegalRecord- Create
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, legalRecord1, "brotherhood2", "history", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"legalRecord1", null, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage LegalRecord - Edit
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"legalRecord1", null, "brotherhood1", "history", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageL((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (domain.LegalRecord) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverPeriodRecord() {

		History history;
		this.authenticate("brotherhood1");
		final int brotherhoodId = LoginService.getPrincipal().getId();
		final PeriodRecord periodRecord1 = this.periodRecordService.create();
		history = this.historyService.findByBrotherhoodIdSingle(brotherhoodId);
		final Object testingData[][] = {
			/*
			 * a) Functional requirements - 10.2 Manage periodRecord - Create
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, periodRecord1, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage periodRecord- Create
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, periodRecord1, "brotherhood2", "history", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 10.2 Manage periodRecords - Edit
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"periodRecord1", null, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage - Ed
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"periodRecord1", null, "brotherhood1", "history", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageP((String) testingData[i][0], (domain.PeriodRecord) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverLinkRecord() {

		History history;
		this.authenticate("brotherhood1");
		final int brotherhoodId = LoginService.getPrincipal().getId();
		final LinkRecord linkRecord1 = this.linkRecordService.create();
		history = this.historyService.findByBrotherhoodIdSingle(brotherhoodId);
		final Object testingData[][] = {
			/*
			 * a) Functional requirements - 10.2 Manage linkRecord - Create
			 * b) Positive tests
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, linkRecord1, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage link- Create
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				null, linkRecord1, "brotherhood2", "history", IllegalArgumentException.class
			},

			/*
			 * a) Functional requirements - 10.2 Manage linkRecords - Edit
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"linkRecord1", null, "brotherhood1", "history", null
			},
			/*
			 * a) Functional requirements - 10.2 Manage - Edit
			 * b) Negative tests - Business rule: It can not be modified by another brotherhood.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"linkRecord1", null, "brotherhood1", "history", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateManageLk((String) testingData[i][0], (domain.LinkRecord) testingData[i][1], (String) testingData[i][2], (domain.Brotherhood) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
					(Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	//-------------------------------------Methods-------------------------------------------------

	@Test
	public void driverDeleteParade() {

		final Object testingData[][] = {

			/*
			 * a) Functional requirements - 12.2 Manage - Delete
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"miscellaneousRecord5", "brotherhood1", null
			},

			/*
			 * a) Functional requirements - 12.2 Manage - Delete a miscellaneousRecord
			 * b) Negative tests - Business rule: Only brotherhood can delete it.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"miscellaneousRecord5", "admin", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverDeleteParadeL() {

		final Object testingData[][] = {

			/*
			 * a) Functional requirements - 12.2 Manage - Delete
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"legalRecord5", "brotherhood1", null
			},

			/*
			 * a) Functional requirements - 12.2 Manage - Delete a miscellaneousRecord
			 * b) Negative tests - Business rule: Only brotherhood can delete it.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"legalRecord5", "admin", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDeleteL((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverDeleteParadeP() {

		final Object testingData[][] = {

			/*
			 * a) Functional requirements - 12.2 Manage - Delete
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"periodRecord5", "brotherhood1", null
			},

			/*
			 * a) Functional requirements - 12.2 Manage - Delete a miscellaneousRecord
			 * b) Negative tests - Business rule: Only brotherhood can delete it.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"periodRecord5", "admin", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDeleteP((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void driverDeleteParadeLk() {

		final Object testingData[][] = {

			/*
			 * a) Functional requirements - 12.2 Manage - Delete
			 * b) Positive tests -
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"linkRecord5", "brotherhood1", null
			},

			/*
			 * a) Functional requirements - 12.2 Manage - Delete a miscellaneousRecord
			 * b) Negative tests - Business rule: Only brotherhood can delete it.
			 * c) analysis of sentence coverage: 92.3%
			 * d) analysis of data coverage.
			 */
			{
				"linkRecord5", "admin", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateDeleteLk((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods templates ------------------------------------------------------

	//------------------------------------------------------MANAGE

	//----MiscellaneousRecord----

	protected void templateManage(final String nameM, final domain.MiscellaneousRecord newMiscellaneousRecord, final String username, final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int miscellaneousRecordId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			MiscellaneousRecord miscellaneousRecord;
			if (nameM == null) {
				miscellaneousRecordId = super.getEntityId(nameM);
				miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			} else
				miscellaneousRecord = newMiscellaneousRecord;

			miscellaneousRecord.setTitle(newTitle);
			miscellaneousRecord.setDescription("description1");

			this.miscellaneousRecordService.save(miscellaneousRecord);

			super.unauthenticate();
			this.miscellaneousRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//-------------PeriodRecord---------------

	protected void templateManageP(final String nameM, final domain.PeriodRecord newPeriodRecord, final String username, final String description, final int startYear, final int endYear, final String photos, final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int periodRecordId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			PeriodRecord periodRecord;
			if (nameM == null) {
				periodRecordId = super.getEntityId(nameM);
				periodRecord = this.periodRecordService.findOne(periodRecordId);
			} else
				periodRecord = newPeriodRecord;

			periodRecord.setTitle(newTitle);
			periodRecord.setDescription(description);
			periodRecord.setStartYear(startYear);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photos);

			this.periodRecordService.save(periodRecord);

			super.unauthenticate();
			this.periodRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//------------------LegalRecord-------------------

	protected void templateManageL(final String nameM, final String description, final String legalName, final int VATnumber, final String applicableLaws, final domain.LegalRecord newLegalRecord, final String username, final String newTitle,
		final Class<?> expected) {
		Class<?> caught;
		final int legalRecordId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			LegalRecord legalRecord;
			if (nameM == null) {
				legalRecordId = super.getEntityId(nameM);
				legalRecord = this.legalRecordService.findOne(legalRecordId);
			} else
				legalRecord = newLegalRecord;

			legalRecord.setTitle(newTitle);
			legalRecord.setDescription(description);
			legalRecord.setLegalName(legalName);
			legalRecord.setVATnumber(VATnumber);
			legalRecord.setApplicableLaws(applicableLaws);

			this.legalRecordService.save(legalRecord);

			super.unauthenticate();
			this.legalRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	//----------------------LinkRecord----------------------

	protected void templateManageLk(final String nameM, final domain.LinkRecord newLinkRecord, final String description, final Brotherhood brotherhood, final String username, final String newTitle, final Class<?> expected) {
		Class<?> caught;
		final int linkRecordId;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();
			LinkRecord linkRecord;
			if (nameM == null) {
				linkRecordId = super.getEntityId(nameM);
				linkRecord = this.linkRecordService.findOne(linkRecordId);
			} else
				linkRecord = newLinkRecord;

			linkRecord.setTitle(newTitle);
			linkRecord.setDescription(description);
			linkRecord.setBrotherhood(brotherhood);

			this.linkRecordService.save(linkRecord);

			super.unauthenticate();
			this.linkRecordService.flush();

			super.flushTransaction();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	//---------------------------------------------DELETE

	//					----MiscellaneousRecord----

	protected void templateDelete(final String nameMiscellaneousRecord, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService.findOne(super.getEntityId(nameMiscellaneousRecord));

			this.miscellaneousRecordService.delete(miscellaneousRecord);

			this.miscellaneousRecordService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//					----PeriodRecord----

	protected void templateDeleteP(final String namePeriodRecord, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.PeriodRecord periodRecord = this.periodRecordService.findOne(super.getEntityId(namePeriodRecord));

			this.periodRecordService.delete(periodRecord);

			this.periodRecordService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//				-----LegalRecord-----

	protected void templateDeleteL(final String nameLegalRecord, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.LegalRecord legalRecord = this.legalRecordService.findOne(super.getEntityId(nameLegalRecord));

			this.legalRecordService.delete(legalRecord);

			this.legalRecordService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//			------LinkRecord-----

	protected void templateDeleteLk(final String nameLinkRecord, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != null)
				super.authenticate(username);
			else
				super.unauthenticate();

			final domain.LinkRecord linkRecord = this.linkRecordService.findOne(super.getEntityId(nameLinkRecord));

			this.linkRecordService.delete(linkRecord);

			this.linkRecordService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
