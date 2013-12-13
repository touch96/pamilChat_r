package pc.wsapi.dbs.sqlbean;

import java.sql.Timestamp;

import javax.persistence.Entity;

import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class MsgHistorySQL {
	public String msghistoryseq;
	
	public String send_code;
	public String target;
	public String img;
	public String type;
	public String sec;
	public Timestamp createdt;
	
	
	
	public Timestamp getCreatedt() {
		return createdt;
	}
	public void setCreatedt(Timestamp createdt) {
		this.createdt = createdt;
	}
	public String getMsghistoryseq() {
		return msghistoryseq;
	}
	public void setMsghistoryseq(String msghistoryseq) {
		this.msghistoryseq = msghistoryseq;
	}
	public String getSend_code() {
		return send_code;
	}
	public void setSend_code(String send_code) {
		this.send_code = send_code;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSec() {
		return sec;
	}
	public void setSec(String sec) {
		this.sec = sec;
	}
	

}
