package com.kamontat.checkidnumber.model

import com.kamontat.checkidnumber.api.constants.Status
import com.kamontat.checkidnumber.model.strategy.idnumber.IDNumberStrategy
import com.kamontat.checkidnumber.model.strategy.idnumber.ThailandIDNumberStrategy
import java.io.Serializable
import java.util.*

/**
 * object that deal with `id-number` of this program
 *
 * keep id-number by String,s
 * and status of this id-number by using id rule of **Thailand** and you also able to change it by [IDNumber Strategy][IDNumberStrategy]
 *
 * @author kamontat
 * @version 2.2
 * @since 19/8/59 - 20:41
 */
class IDNumber : Serializable {
    private var splitID: CharArray? = null
    private var id: String? = null
    var status: Status = Status.NOT_CREATE
        private set
    val size: Int
        get() {
            return if (id != null) id!!.length else 0
        }

    /**
     * **init** id-number with nothing
     */
    constructor() {
        this.id = null
        splitID = null
        status = Status.NOT_CREATE
    }

    /**
     * **init** id-number by using parameter `id`
     * @param id
     * * 		some id-number
     */
    constructor(id: String) {
        setId(id)
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
        splitID = id.toCharArray()
        status = isIDCorrect
    }

    /**
     * convert first digit in id-number to genre in form of String by using id rule of **Thailand**

     * @return string of genre
     */
    val idGenre: String
        get() {
            when {
                splitID!![0] == '1' -> return "สัญชาติไทย และ แจ้งเกิดทันเวลา"
                splitID!![0] == '2' -> return "สัญชาติไทย และ แจ้งเกิดไม่ทันเวลา"
                splitID!![0] == '3' -> return "คนไทยหรือคนต่างด้าวถูกกฏหมาย\nที่มีชื่ออยู่ในทะเบียนบ้านก่อนวันที่ 31 พฤษภาคม พ.ศ. 2527"
                splitID!![0] == '4' -> return "คนไทยหรือคนต่างด้าวถูกกฏหมายที่ไม่มีเลขประจำตัวประชาชน\nหรือไม่ทันได้เลขประจำตัวก็ขอย้ายบ้านก่อน"
                splitID!![0] == '5' -> return "คนไทยที่ได้รับอนุมัติให้เพิ่มชื่อในกรณีตกสำรวจหรือคนที่ถือ 2 สัญชาติ"
                splitID!![0] == '6' -> return "ผู้ที่เข้าเมืองโดยไม่ชอบด้วยกฎหมาย \nหรือ ผู้ที่เข้าเมืองโดยชอบด้วยกฎหมายแต่อยู่ในลักษณะชั่วคราว"
                splitID!![0] == '7' -> return "บุตรของบุคคลประเภทที่ 6 ซึ่งเกิดในประเทศไทย"
                splitID!![0] == '8' -> return "คนต่างด้าวถูกกฎหมาย ที่ได้รับการให้สัญชาติไทยตั้งแต่หลังวันที่ 31 พฤษภาคม พ.ศ. 2527"
                splitID!![0] == '0' -> return "บุคคลที่ไม่มีสถานะทางทะเบียนราษฎร ไม่มีสัญชาติ"
                else -> return "No Genre"
            }
        }

    /**
     * get address in id-number by using id rule of **Thailand**<br></br>
     * **address** is mean province + district
     * @return address in form of number
     */
    val idAddress: String
        get() = if (splitID != null && splitID!!.isNotEmpty()) String(splitID!!, 1, 4) else ""

    /**
     * get province in id-number by using id rule of **Thailand**
     * @return province in form of number
     */
    val idProvince: String
        get() = if (splitID != null && splitID!!.isNotEmpty()) String(splitID!!, 1, 2) else ""

    /**
     * get district in id-number by using id rule of **Thailand**
     * @return district in form of number
     */
    val idDistrict: String
        get() = if (splitID != null && splitID!!.isNotEmpty()) String(splitID!!, 3, 2) else ""

    /**
     * get ID of **Birth Certificate** in id-number by using id rule of **Thailand**
     * @return Birth Certificate ID in form of number
     */
    val idbc: String
        get() = if (splitID != null && splitID!!.isNotEmpty()) String(splitID!!, 5, 5) else ""

    /**
     * get order in **Birth Certificate** by using id rule of **Thailand**
     * @return order of that in form of number
     */
    val idOrder: String
        get() = if (splitID != null && splitID!!.isNotEmpty()) String(splitID!!, 10, 2) else ""

    /**
     * check id is passing id rule of **Thailand** or not
     * @return status of current id
     */
    val isIDCorrect: Status
        get() = strategy.checking(id)

    override fun toString(): String = if (id != null) id!! else ""

    /**
     * check only id is equal
     * @param other
     * * 		test object
     * *
     * @return true if same
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val number = this.javaClass.cast(other)
        return this.getId() == number.getId()
    }

    override fun hashCode(): Int {
        var result = splitID?.let { Arrays.hashCode(it) } ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }

    companion object {
        public var strategy: IDNumberStrategy = ThailandIDNumberStrategy()
    }
}