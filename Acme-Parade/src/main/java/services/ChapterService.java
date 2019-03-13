
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChapterRepository;
import security.Authority;
import security.UserAccount;
import domain.Chapter;

@Service
@Transactional
public class ChapterService {

	// Repository-----------------------------------------------

	@Autowired
	private ChapterRepository	chapterRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public ChapterService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Chapter create() {
		final Chapter res = new Chapter();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority("CHAPTER");
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		userAccount.setEnabled(true);
		res.setUserAccount(userAccount);

		res.setIsBanned(false);
		res.setIsSpammer(null);

		res.setTitle("");

		return res;
	}

	public Collection<Chapter> findAll() {
		Collection<Chapter> chapters;

		chapters = this.chapterRepository.findAll();

		return chapters;
	}

	public Chapter findOne(final Integer chapterId) {
		Chapter chapter;
		chapter = this.chapterRepository.findOne(chapterId);
		return chapter;
	}

	public Chapter save(final Chapter chapter) {
		Assert.notNull(chapter);
		final Chapter saved = this.chapterRepository.save(chapter);
		return saved;
	}

	public void delete(final Chapter chapter) {
		Assert.notNull(chapter);
		this.chapterRepository.delete(chapter);
	}

	// Other Methods--------------------------------------------
	public Chapter findByUserAccountId(final int userAccountId) {
		return this.chapterRepository.findByUserAccountId(userAccountId);
	}

}
