
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Rendezvous>	createdRendezvouses;
	//private Rendezvous				rendezvous;
	private Collection<Rendezvous>	rsvpsRendezvouses;
	private Collection<Answer>		answers;
	private Collection<Comment>		comments;


	@Valid
	@OneToMany
	public Collection<Rendezvous> getCreatedRendezvouses() {
		return this.createdRendezvouses;
	}

	public void setCreatedRendezvouses(final Collection<Rendezvous> createdRendezvouses) {
		this.createdRendezvouses = createdRendezvouses;

	}
	/*
	 * @Valid
	 * 
	 * @NotNull
	 * 
	 * @OneToOne(optional = false, mappedBy = "rendezvous")
	 * public Rendezvous getrendezvous() {
	 * return this.rendezvous;
	 * }
	 * 
	 * public void setrendezvous(final Rendezvous rendezvous) {
	 * this.rendezvous = rendezvous;
	 * 
	 * }
	 */

	@Valid
	@ManyToMany
	public Collection<Rendezvous> getRsvpsRendezvouses() {
		return this.rsvpsRendezvouses;
	}

	public void setRsvpsRendezvouses(final Collection<Rendezvous> rsvpsRendezvouses) {
		this.rsvpsRendezvouses = rsvpsRendezvouses;

	}

	@Valid
	@OneToMany
	public Collection<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;

	}
	@Valid
	@OneToMany
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;

	}
}
