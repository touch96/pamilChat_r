package pc.wsapi.dbs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Friendrequest extends Model {
	@NotNull
	@Id
	public String s_code;
	
	@NotNull
	public String f_code;
	
	public boolean isnew;
	
	/**
	 * 00 : 申請中
	 * 01 : 承認済
	 * 02 : 拒否
	 */
	public String status;

	public String getS_code() {
		return s_code;
	}

	public String getF_code() {
		return f_code;
	}

	public boolean isnew() {
		return isnew;
	}

	public String getStatus() {
		return status;
	}
	
	@CreatedTimestamp
	public Date createdt;
	
	
	
	public Date getCreatedt() {
		return createdt;
	}

	public static Finder<Long,Friendrequest> find = new Finder<Long,Friendrequest>(
		    Long.class, Friendrequest.class
		  ); 


}