package com.kamontat.checkidnumber.model;

import com.kamontat.checkidnumber.api.constants.Status;
import com.kamontat.checkidnumber.model.strategy.idnumber.IDNumberStrategy;
import com.kamontat.checkidnumber.model.strategy.idnumber.ThailandIDNumberStrategy;

import static com.kamontat.checkidnumber.api.constants.Status.*;

/**
 * object that deal with <code>id-number</code> of this program <br>
 * <p>
 * keep id-number by String, <br>
 * keep update time in form of <code>LocalDateTime</code>, <br>
 * and status of this id-number by using id rule of <b>Thailand</b> <br>
 *
 * @author kamontat
 * @version 2.2
 * @since 19/8/59 - 20:41
 */
public class IDNumber {
	public static IDNumberStrategy strategy = new ThailandIDNumberStrategy();
	private char[] splitID;
	private String id;
	private Status status;
	
	/**
	 * <b>init</b> id-number with nothing
	 */
	public IDNumber() {
		this.id = null;
		splitID = null;
		status = NOT_CREATE;
	}
	
	/**
	 * <b>init</b> id-number by using parameter <code>id</code>
	 *
	 * @param id
	 * 		some id-number
	 */
	public IDNumber(String id) {
		setId(id);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
		splitID = id.toCharArray();
		isIDCorrect();
	}
	
	public Status getStatus() {
		return status;
	}
	
	/**
	 * convert first digit in id-number to genre in form of String by using id rule of <b>Thailand</b>
	 *
	 * @return string of genre
	 */
	@Deprecated
	public String getIDGenre() {
		if (splitID[0] == '1') {
			return "สัญชาติไทย และ แจ้งเกิดทันเวลา";
		} else if (splitID[0] == '2') {
			return "สัญชาติไทย และ แจ้งเกิดไม่ทันเวลา";
		} else if (splitID[0] == '3') {
			return "คนไทยหรือคนต่างด้าวถูกกฏหมาย\nที่มีชื่ออยู่ในทะเบียนบ้านก่อนวันที่ 31 พฤษภาคม พ.ศ. 2527";
		} else if (splitID[0] == '4') {
			return "คนไทยหรือคนต่างด้าวถูกกฏหมายที่ไม่มีเลขประจำตัวประชาชน\nหรือไม่ทันได้เลขประจำตัวก็ขอย้ายบ้านก่อน";
		} else if (splitID[0] == '5') {
			return "คนไทยที่ได้รับอนุมัติให้เพิ่มชื่อในกรณีตกสำรวจหรือคนที่ถือ 2 สัญชาติ";
		} else if (splitID[0] == '6') {
			return "ผู้ที่เข้าเมืองโดยไม่ชอบด้วยกฎหมาย \nหรือ ผู้ที่เข้าเมืองโดยชอบด้วยกฎหมายแต่อยู่ในลักษณะชั่วคราว";
		} else if (splitID[0] == '7') {
			return "บุตรของบุคคลประเภทที่ 6 ซึ่งเกิดในประเทศไทย";
		} else if (splitID[0] == '8') {
			return "คนต่างด้าวถูกกฎหมาย ที่ได้รับการให้สัญชาติไทยตั้งแต่หลังวันที่ 31 พฤษภาคม พ.ศ. 2527";
		} else if (splitID[0] == '0') {
			return "บุคคลที่ไม่มีสถานะทางทะเบียนราษฎร ไม่มีสัญชาติ";
		} else {
			return "No Genre";
		}
	}
	
	/**
	 * get address in id-number by using id rule of <b>Thailand</b><br>
	 * <b>address</b> is mean province + district
	 *
	 * @return address in form of number
	 */
	public String getIDAddress() {
		return String.copyValueOf(splitID, 1, 4);
	}
	
	/**
	 * get province in id-number by using id rule of <b>Thailand</b>
	 *
	 * @return province in form of number
	 */
	public String getIDProvince() {
		return String.copyValueOf(splitID, 1, 2);
	}
	
	/**
	 * get district in id-number by using id rule of <b>Thailand</b>
	 *
	 * @return district in form of number
	 */
	public String getIDDistrict() {
		return String.copyValueOf(splitID, 3, 2);
	}
	
	/**
	 * get ID of <b>Birth Certificate</b> in id-number by using id rule of <b>Thailand</b>
	 *
	 * @return Birth Certificate ID in form of number
	 */
	public String getIDBC() {
		return String.copyValueOf(splitID, 5, 5);
	}
	
	/**
	 * get order in <b>Birth Certificate</b> by using id rule of <b>Thailand</b>
	 *
	 * @return order of that in form of number
	 */
	public String getIDOrder() {
		return String.copyValueOf(splitID, 10, 2);
	}
	
	/**
	 * check id is passing id rule of <b>Thailand</b> or not
	 *
	 * @return if pass return true, otherwise return false
	 */
	private boolean isIDCorrect() {
		if (id.length() <= 0) {
			status = NOT_CREATE;
			return false;
		} else if (id.length() != strategy.getIDLength()) {
			status = UNMATCHED_LENGTH;
			return false;
		}
		status = strategy.checking(id);
		return status == OK;
	}
	
	@Override
	public String toString() {
		//		String text = "id: " + id + "\n";
		//		text += "status: " + status + "\n";
		//		text += "type: " + getLocation().getType() + "\n";
		//		text += "\n\n";
		return id;
	}
	
	/**
	 * check only id is equal
	 *
	 * @param obj
	 * 		test object
	 * @return true if same
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		IDNumber number = this.getClass().cast(obj);
		return this.getId().equals(number.getId());
	}
}