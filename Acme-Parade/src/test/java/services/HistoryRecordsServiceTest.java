
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import utilities.AbstractTest;
import domain.History;
import domain.MiscellaneousRecord;
import forms.MiscellaneousRecordForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HistoryRecordsServiceTest extends AbstractTest {

	// System Under Test ------------------------------------------------------

	@Autowired
	private EveryRecordService			everyRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private AreaService					areaService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverMiscellaneousRecord() {

		History history = null;
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

	//Methods
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

	// Ancillary methods ------------------------------------------------------

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

	private MiscellaneousRecordForm constructMiscellaneousRecord(final History history, final String title, final String description) {

		final MiscellaneousRecordForm result = new MiscellaneousRecordForm();

		final int id = 0;

		result.setId(id);
		result.setHistory(history);

		result.setTitle(title);
		result.setDescription(description);

		return result;
	}

}
