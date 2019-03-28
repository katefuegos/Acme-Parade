
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.UserAccount;
import domain.Brotherhood;

@Service
@Transactional
public class BrotherhoodService {

	// Repository-----------------------------------------------

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public BrotherhoodService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Brotherhood create() {
		final Brotherhood res = new Brotherhood();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority("BROTHERHOOD");
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		userAccount.setEnabled(true);
		res.setUserAccount(userAccount);

		res.setIsBanned(false);
		res.setIsSpammer(null);

		final String title = "";
		final Date establishmentDate = new Date();
		final String pictures = "";

		res.setTitle(title);
		res.setEstablishmentDate(establishmentDate);
		res.setPictures(pictures);

		return res;
	}

	public Collection<Brotherhood> findAll() {
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodRepository.findAll();

		return brotherhoods;
	}

	public Brotherhood findOne(final Integer brotherhoodId) {
		Brotherhood brotherhood;
		brotherhood = this.brotherhoodRepository.findOne(brotherhoodId);
		return brotherhood;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		final Brotherhood saved = this.brotherhoodRepository.save(brotherhood);
		return saved;
	}

	public void delete(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		this.brotherhoodRepository.delete(brotherhood);
	}

	// Other Methods--------------------------------------------
	public Brotherhood findByUserAccountId(final int userAccountId) {
		return this.brotherhoodRepository.findByUserAccountId(userAccountId);
	}

	public Collection<Brotherhood> findByBrotherhood(final int areaId) {
		return this.brotherhoodRepository.findByArea(areaId);
	}
}
