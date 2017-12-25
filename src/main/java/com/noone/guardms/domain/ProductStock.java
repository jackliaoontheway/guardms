package com.noone.guardms.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ProductStock {

	private String sku;
	private String name;
	private String produceDate;
	private String price;
	private String rfid;
	private String status;

	// -------------Auto generated start (Fri Dec 08 21:21:12 CST 2017)-------------//

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date createdDate;

	private Long createdBy;

	private Date modifiedDate;

	private Long modifiedBy;

	public void setBaseField(Long operatorId) {
		Date now = new Date();
		if (this.getId() == null) {
			setCreatedBy(operatorId);
			setCreatedDate(now);
		}
		setModifiedBy(operatorId);
		setModifiedDate(now);
	}

	// -------------Auto generated end-------------//

}