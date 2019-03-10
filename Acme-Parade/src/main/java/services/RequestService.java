package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.LoginService;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Repository-----------------------------------------------

	@Autowired
	private RequestRepository requestRepository;

	// Services-------------------------------------------------

	@Autowired
	private MemberService memberService;

	// Constructor----------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Request create() {
		final Request res = new Request();
		res.setStatus("PENDING");
		res.setMember(memberService.findByUserAccountId(LoginService
				.getPrincipal().getId()));
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
		if (request.getStatus().equals("REJECTED")) {
			Assert.notNull(request.getReasonReject(),
					"SI UN REQUEST ES RECHAZADO DEBE DE DECIRSE LA RAZÓN");
		}
		if (request.getStatus().equals("ACCEPTED")) {
			Assert.isTrue(request.getRoow() != null
					&& request.getColuumn() != null,
					"SI SE ACEPTA UN REQUEST SE DEBE INTRODUCIR ROOW Y COLUUMN");
			Assert.isTrue(
					this.findRequestByPosition(request.getRoow(), request
							.getColuumn(), request.getProcession().getId()) == null,
					"ESTA POSICION YA ESTÁ OCUPADA EN ESA PROCESION");
		}
		final Request saved = this.requestRepository.save(request);
		return saved;
	}

	public void delete(final Request request) {
		this.requestRepository.delete(request);
	}

	// Other Methods--------------------------------------------
	public Request findRequestByPosition(int roow, int coluumn, int processionId) {
		return requestRepository.findRequestByPosition(roow, coluumn,
				processionId);
	}

	public Collection<Request> findRequestByProcessionId(int processionId) {
		Assert.notNull(processionId);
		return requestRepository.findRequestByProcessionId(processionId);
	}

	public Collection<Request> findRequestByMemberId(int memberId) {
		Assert.notNull(memberId);
		return requestRepository.findRequestByMemberId(memberId);
	}
}
