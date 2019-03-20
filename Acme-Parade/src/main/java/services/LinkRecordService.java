
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private LinkRecordRepository	linkRecordRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public LinkRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public LinkRecord create() {
		final LinkRecord res = new LinkRecord();

		final String title = "";
		final String description = "";

		res.setTitle(title);
		res.setDescription(description);

		return res;
	}

	public Collection<LinkRecord> findAll() {
		Collection<LinkRecord> linkRecords;

		linkRecords = this.linkRecordRepository.findAll();

		return linkRecords;
	}

	public LinkRecord findOne(final Integer linkRecordId) {
		LinkRecord linkRecord;
		linkRecord = this.linkRecordRepository.findOne(linkRecordId);
		return linkRecord;
	}

	public LinkRecord save(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		final LinkRecord saved = this.linkRecordRepository.save(linkRecord);
		return saved;
	}

	public void delete(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		this.linkRecordRepository.delete(linkRecord);
	}

	public Collection<LinkRecord> findLinkRecordByHistoryId(final int historyId) {
		Assert.notNull(historyId);
		return this.linkRecordRepository.findLinkRecordByHistoryId(historyId);
	}

}
