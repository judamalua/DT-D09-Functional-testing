
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Rendezvous extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	name;
	private String	description;
	private Date	moment;
	private String	pictureUrl;
	private String	gpsCoordinates;
	private boolean	finalMode;
	private boolean	deleted;
	private boolean	adultOnly;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@URL
	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(final String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@NotBlank
	public String getGpsCoordinates() {
		return this.gpsCoordinates;
	}

	public void setGpsCoordinates(final String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

	public boolean getFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

	public boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getAdultOnly() {
		return this.adultOnly;
	}

	public void setAdultOnly(final boolean adultOnly) {
		this.adultOnly = adultOnly;
	}


	// Relationships ----------------------------------------------------------
	private User						user;
	private Collection<Question>		questions;
	private Collection<Rendezvous>		rendezvouss;
	private Collection<Announcement>	announcements;
	private Collection<Comment>			comments;


	@Valid
	@NotNull
	@OneToOne(optional = false)
	public User getuser() {
		return this.user;
	}

	public void setuser(final User user) {
		this.user = user;

	}
	@Valid
	@OneToMany
	public Collection<Question> getquestions() {
		return this.questions;
	}

	public void setquestions(final Collection<Question> questions) {
		this.questions = questions;

	}
	@Valid
	@OneToMany
	public Collection<Rendezvous> getrendezvouss() {
		return this.rendezvouss;
	}

	public void setrendezvouss(final Collection<Rendezvous> rendezvouss) {
		this.rendezvouss = rendezvouss;

	}
	@Valid
	@OneToMany
	public Collection<Announcement> getannouncements() {
		return this.announcements;
	}

	public void setannouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;

	}
	@Valid
	@OneToMany
	public Collection<Comment> getcomments() {
		return this.comments;
	}

	public void setcomments(final Collection<Comment> comments) {
		this.comments = comments;

	}
}
