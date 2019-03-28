
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.LoginService;
import domain.Parade;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Repository-----------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Services-------------------------------------------------

	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructor----------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Request create() {
		final Request res = new Request();
		res.setStatus("PENDING");
		res.setMember(this.memberService.findByUserAccountId(LoginService.getPrincipal().getId()));
		return res;
	}

	public List<Request> findAll() {
		return this.requestRepository.findAll();
	}

	public Request findOne(final Integer requestId) {
		Assert.isTrue(requestId != null, "EL ID DE FINDONE NO PUEDE SER NULL");
		return this.requestRepository.findOne(requestId);
	}

	public Request save(final Request request) {
		Assert.notNull(request);
		if (request.getStatus().equals("REJECTED"))
			Assert.notNull(request.getReasonReject(), "SI UN REQUEST ES RECHAZADO DEBE DE DECIRSE LA RAZÓN");
		if (request.getStatus().equals("ACCEPTED")) {
			Assert.isTrue(request.getRoow() != null && request.getColuumn() != null, "SI SE ACEPTA UN REQUEST SE DEBE INTRODUCIR ROOW Y COLUUMN");
			Assert.isTrue(this.findRequestByPosition(request.getRoow(), request.getColuumn(), request.getParade().getId()) == null, "ESTA POSICION YA ESTÁ OCUPADA EN ESA PROCESION");
		}
		final Request saved = this.requestRepository.save(request);
		return saved;
	}

	public void delete(final Request request) {
		this.requestRepository.delete(request);
	}

	// Other Methods--------------------------------------------
	public void flush() {
		this.requestRepository.flush();
	}

	public Request application(final Request request) {

		final domain.Member member = this.memberService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(member);
		Assert.isTrue(request.getParade().getStatus().equals("ACCEPTED"));
		final Collection<Request> requests = this.findRequestByMemberId(member.getId());
		final Collection<Parade> parades = new ArrayList<>();
		if (!requests.isEmpty())
			for (final Request r : requests)
				parades.add(r.getParade());
		Assert.isTrue(!parades.contains(request.getParade()));

		return this.requestRepository.save(request);
	}

	public Request reject(final Request request) {
		Assert.isTrue(request != null);
		Assert.isTrue(request.getParade().getBrotherhood().getId() == this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()).getId());
		Assert.isTrue(!request.getReasonReject().equals(""));
		Assert.isTrue(request.getStatus().equals("PENDING"));

		request.setStatus("REJECTED");

		return this.requestRepository.save(request);
	}
	public Request findRequestByPosition(final int roow, final int coluumn, final int paradeId) {
		return this.requestRepository.findRequestByPosition(roow, coluumn, paradeId);
	}

	public Collection<Request> findRequestByParadeId(final int paradeId) {
		Assert.notNull(paradeId);
		return this.requestRepository.findRequestByParadeId(paradeId);
	}

	public Collection<Request> findRequestByMemberId(final int memberId) {
		Assert.notNull(memberId);
		return this.requestRepository.findRequestByMemberId(memberId);
	}
}
