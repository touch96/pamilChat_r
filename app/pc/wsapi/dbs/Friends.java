package pc.wsapi.dbs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Friends extends Model {
	
	@NotNull
	@Id
	public String code;
	
	@NotNull
	public String f_code;
	
//	@ManyToOne(optional=true)
//	@JoinColumn(name="code", referencedColumnName = "code")
//	public Friendrequest friendrequest;
	
	public String getCode() {
		return code;
	}

	public String getF_code() {
		return f_code;
	}

	public Date getCreatedt() {
		return createdt;
	}

	@CreatedTimestamp
	public Date createdt;
	
	public static Finder<Long,Friends> find = new Finder<Long,Friends>(
		    Long.class, Friends.class
		  ); 

}
