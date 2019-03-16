
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Repository-----------------------------------------------

	@Autowired
	private HistoryRepository	historyRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public HistoryService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public History create() {
		final History res = new History();

		final String title = "";
		final String description = "";
		final String photos = "";

		res.setTitle(title);
		res.setDescription(description);
		res.setPhotos(photos);

		return res;
	}

	public Collection<History> findAll() {
		Collection<History> historys;

		historys = this.historyRepository.findAll();

		return historys;
	}

	public History findOne(final Integer historyId) {
		History history;
		history = this.historyRepository.findOne(historyId);
		return history;
	}

	public History save(final History history) {
		Assert.notNull(history);
		final History saved = this.historyRepository.save(history);
		return saved;
	}

	public void delete(final History history) {
		Assert.notNull(history);
		this.historyRepository.delete(history);
	}

}
