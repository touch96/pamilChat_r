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
	public String id;
	
	@NotNull
	public String f_id;
	
	@CreatedTimestamp
	public Date createDt;
	
	public static Finder<Long,Friends> find = new Finder<Long,Friends>(
		    Long.class, Friends.class
		  ); 

}
