
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public InceptionRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public InceptionRecord create() {
		final InceptionRecord res = new InceptionRecord();

		final String title = "";
		final String description = "";

		final String photos = "";

		res.setTitle(title);
		res.setDescription(description);
		res.setPhotos(photos);

		return res;
	}

	public Collection<InceptionRecord> findAll() {
		Collection<InceptionRecord> inceptionRecords;

		inceptionRecords = this.inceptionRecordRepository.findAll();

		return inceptionRecords;
	}

	public InceptionRecord findOne(final Integer inceptionRecordId) {
		InceptionRecord inceptionRecord;
		inceptionRecord = this.inceptionRecordRepository.findOne(inceptionRecordId);
		return inceptionRecord;
	}

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);
		final InceptionRecord saved = this.inceptionRecordRepository.save(inceptionRecord);
		return saved;
	}

	public void delete(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);
		this.inceptionRecordRepository.delete(inceptionRecord);
	}

	public Collection<InceptionRecord> findInceptionRecordByHistoryId(final int historyId) {
		Assert.notNull(historyId);
		return this.inceptionRecordRepository.findInceptionRecordByHistoryId(historyId);
	}

}
