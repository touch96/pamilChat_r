package pc.wsapi.dbs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Msghistory extends Model {
	
	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int msghistoryseq;
	
	@NotNull
	public String send_code;
	@NotNull
	public String recieve_code;
	@NotNull
	public String img;
	
	public boolean isnew;
	
	public int sec;
	
	public String getSend_code() {
		return send_code;
	}
	public String getRecieve_code() {
		return recieve_code;
	}
	public String getImg() {
		return img;
	}
	public boolean isnew() {
		return isnew;
	}
	public int getSec() {
		return sec;
	}
	
	@CreatedTimestamp
	public Date createdt;
	
	public static Finder<Long,Msghistory> find = new Finder<Long,Msghistory>(
		    Long.class, Msghistory.class
		  ); 

	
}
