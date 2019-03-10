
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.Authority;
import security.UserAccount;
import domain.Enrolment;
import domain.Member;
import domain.Request;

@Service
@Transactional
public class MemberService {

	// Repository-----------------------------------------------

	@Autowired
	private MemberRepository	memberRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public MemberService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Member create() {
		final Member res = new Member();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority("MEMBER");
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		userAccount.setEnabled(true);
		res.setUserAccount(userAccount);

		res.setIsBanned(false);
		res.setIsSpammer(null);

		final Collection<Enrolment> enrolments = new ArrayList<Enrolment>();
		res.setEnrolments(enrolments);
		//al no ser obligatorio tener un finder en un member, no se le añade

		return res;
	}

	public List<Member> findAll() {
		return this.memberRepository.findAll();
	}

	public Member findOne(final Integer memberId) {
		Assert.notNull(memberId);
		return this.memberRepository.findOne(memberId);
	}

	public Member save(final Member member) {
		Assert.notNull(member);
		final Member saved = this.memberRepository.save(member);
		return saved;
	}

	public void delete(final Member member) {
		//		final Collection<Request> request = this.findRequestsByMemberId(member.getId());
		//		for (final Request r : request)
		//			this.requestService.delete(r);
		this.memberRepository.delete(member);
	}

	// Other Methods--------------------------------------------

	public Member findByUserAccountId(final int userAccountId) {
		Assert.notNull(userAccountId);
		return this.memberRepository.findByUserAccountId(userAccountId);
	}

	public Member findByEnrolment(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		return this.memberRepository.findByEnrolment(enrolment);
	}

	public Collection<Request> findRequestsByMemberId(final int memberId) {
		return this.memberRepository.findRequestsByMemberId(memberId);
	}
}
