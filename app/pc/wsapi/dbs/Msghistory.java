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
	
//	public static interface Constants {
//		public String type_noread = "0";
//		public String type_read = "1";
//		public String type_replyed = "2";
//	}
	
	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int msghistoryseq;
	
	@NotNull
	public String send_code;
	@NotNull
	public String target;
	@NotNull
	public String img;
	
	public String type;
	
	public int sec;
	
	public String getSend_code() {
		return send_code;
	}
	public String getTarget() {
		return target;
	}
	public String getImg() {
		return img;
	}
	
	public String getType() {
		return type;
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
