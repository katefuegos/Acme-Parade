
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import security.LoginService;
import domain.Actor;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Repository-----------------------------------------------
	@Autowired
	private PositionRepository	positionRepository;

	// Services-------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private EnrolmentService	enrolmentService;


	// Constructor----------------------------------------------
	public PositionService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Position create() {
		final Position position = new Position();
		return position;
	}

	public List<Position> findAll() {
		return this.positionRepository.findAll();
	}

	public Position findOne(final Integer positionId) {
		return this.positionRepository.findOne(positionId);
	}

	public Position save(final Position position) {
		Assert.notNull(position, "position A CREAR/EDITAR NO PUEDE SER NULL");

		// COJO ACTOR ACTUAL
		final Actor actorActual = this.actorService.findActorByUsername(LoginService.getPrincipal().getUsername());
		Assert.notNull(actorActual, "NO HAY ACTOR DETECTADO");

		// COMPRUEBO RESTRICCIONES DE USUARIOS
		if (!actorActual.getUserAccount().getAuthorities().toString().contains("ADMIN"))
			Assert.notNull(null, "SOLO PUEDE CREAR/EDITAR position ADMIN");

		// GUARDO position
		final Position saved = this.positionRepository.save(position);

		return saved;
	}

	public void delete(final Position position) {
		Assert.notNull(position, "position A BORRAR NO PUEDE SER NULL");

		// COJO ACTOR ACTUAL
		final Actor actorActual = this.actorService.findActorByUsername(LoginService.getPrincipal().getUsername());
		Assert.notNull(actorActual, "NO HAY ACTOR DETECTADO");

		// COMPRUEBO RESTRICCIONES DE USUARIOS
		if (!actorActual.getUserAccount().getAuthorities().toString().contains("ADMIN"))
			Assert.notNull(null, "SOLO PUEDE BORRAR position ADMIN");

		// BORRO position
		Assert.isTrue(!this.enrolmentService.deleteRelationshipPosition(position.getId()), "position.error.used");

		this.positionRepository.delete(position);

	}

	// Other Methods--------------------------------------------

	// public Position findPositionByName(final String positionName) {
	// final Position position =
	// this.positionRepository.findpositionByName(positionName);
	// return position;
	// }

}
