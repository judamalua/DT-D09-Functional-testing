
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Rendezvous>	createdRendezvouses;
	private Collection<Rendezvous>	rsvpRendezvouses;
	private Collection<Comment>		comments;


	@NotNull
	@OneToMany
	public Collection<Rendezvous> getCreatedRendezvouses() {
		return this.createdRendezvouses;
	}

	public void setCreatedRendezvouses(final Collection<Rendezvous> createdRendezvouses) {
		this.createdRendezvouses = createdRendezvouses;
	}

	@NotNull
	@OneToMany(mappedBy = "user")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;

	}

	@NotNull
	@ManyToMany(mappedBy = "users")
	public Collection<Rendezvous> getRsvpRendezvouses() {
		return this.rsvpRendezvouses;
	}

	public void setRsvpRendezvouses(final Collection<Rendezvous> rsvpRendezvouses) {
		this.rsvpRendezvouses = rsvpRendezvouses;
	}

}
