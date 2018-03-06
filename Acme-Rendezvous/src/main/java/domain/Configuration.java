
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	cookies_eng;
	private String	cookies_es;
	private String	businessName;
	private String	bannerUrl;
	private String	welcomeMessage_eng;
	private String	welcomeMessage_es;
	private Integer	pageSize;


	@SafeHtml
	@NotBlank
	public String getCookies_eng() {
		return this.cookies_eng;
	}

	public void setCookies_eng(final String cookies_eng) {
		this.cookies_eng = cookies_eng;
	}

	@SafeHtml
	@NotBlank
	public String getCookies_es() {
		return this.cookies_es;
	}

	public void setCookies_es(final String cookies_es) {
		this.cookies_es = cookies_es;
	}

	@SafeHtml
	@NotBlank
	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	@SafeHtml
	@NotBlank
	@URL
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@SafeHtml
	@NotBlank
	public String getWelcomeMessage_eng() {
		return this.welcomeMessage_eng;
	}

	public void setWelcomeMessage_eng(final String welcomeMessage_eng) {
		this.welcomeMessage_eng = welcomeMessage_eng;
	}

	@SafeHtml
	@NotBlank
	public String getWelcomeMessage_es() {
		return this.welcomeMessage_es;
	}

	public void setWelcomeMessage_es(final String welcomeMessage_es) {
		this.welcomeMessage_es = welcomeMessage_es;
	}

	@NotNull
	@Range(min = 1)
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(final Integer pageSize) {
		this.pageSize = pageSize;
	}

	// Relationships ----------------------------------------------------------

}
