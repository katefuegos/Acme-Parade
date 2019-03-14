
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public MiscellaneousRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public MiscellaneousRecord create() {
		final MiscellaneousRecord res = new MiscellaneousRecord();

		final String title = "";
		final String description = "";

		res.setTitle(title);
		res.setDescription(description);

		return res;
	}

	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> miscellaneousRecords;

		miscellaneousRecords = this.miscellaneousRecordRepository.findAll();

		return miscellaneousRecords;
	}

	public MiscellaneousRecord findOne(final Integer miscellaneousRecordId) {
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);
		return miscellaneousRecord;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		final MiscellaneousRecord saved = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		return saved;
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

}
