
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Slider extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	title_en;
	private String	title_es;
	private String	text_en;
	private String	text_es;
	private String	pictureUrl;
	private String	align;


	@SafeHtml
	@NotBlank
	public String getTitle_en() {
		return this.title_en;
	}

	public void setTitle_en(final String title_en) {
		this.title_en = title_en;
	}
	@SafeHtml
	@NotBlank
	public String getTitle_es() {
		return this.title_es;
	}

	public void setTitle_es(final String title_es) {
		this.title_es = title_es;
	}
	@SafeHtml
	@NotBlank
	public String getText_en() {
		return this.text_en;
	}

	public void setText_en(final String text_en) {
		this.text_en = text_en;
	}
	@SafeHtml
	@NotBlank
	public String getText_es() {
		return this.text_es;
	}

	public void setText_es(final String text_es) {
		this.text_es = text_es;
	}
	@SafeHtml
	@NotBlank
	@URL
	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(final String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	@SafeHtml
	@NotBlank
	@Pattern(regexp = "left|center|right")
	public String getAlign() {
		return this.align;
	}

	public void setAlign(final String align) {
		this.align = align;
	}

}
