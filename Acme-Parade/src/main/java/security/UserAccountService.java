
package security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class UserAccountService {

	// Repository-----------------------------------------------

	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	//	public UserAccountService() {
	//		super();
	//	}

	// Simple CRUD----------------------------------------------

	public UserAccount create(final String username, final String hashedPass, final String authority) {
		final UserAccount res = new UserAccount();

		final Authority a = new Authority();
		a.setAuthority(authority);
		final List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(a);

		res.setAuthorities(authorities);
		res.setUsername(username);
		res.setPassword(hashedPass);

		return res;
	}

	public UserAccount create() {
		final UserAccount res = new UserAccount();
		final List<Authority> authorities = new ArrayList<Authority>();
		res.setAuthorities(authorities);
		return res;
	}

	public Collection<UserAccount> findAll() {
		return this.userAccountRepository.findAll();
	}

	public UserAccount findOne(final Integer userAccountId) {
		UserAccount userAccount;
		userAccount = this.userAccountRepository.findOne(userAccountId);
		Assert.notNull(userAccount);

		return userAccount;
	}

	public UserAccount save(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		UserAccount saved;

		final String password = userAccount.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		userAccount.setPassword(hashedPassword);

		saved = this.userAccountRepository.saveAndFlush(userAccount);

		return saved;
	}

	public void delete(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		this.userAccountRepository.delete(userAccount);
	}

	// Other Methods--------------------------------------------

	public UserAccount findByUsername(final String username) {
		Assert.notNull(username);
		return this.userAccountRepository.findByUsername(username);
	}

	public UserAccount register(final String username, final String hashedPass, final String type) {
		final UserAccount res = this.create(username, hashedPass, type);
		this.save(res);
		return res;
	}

}
