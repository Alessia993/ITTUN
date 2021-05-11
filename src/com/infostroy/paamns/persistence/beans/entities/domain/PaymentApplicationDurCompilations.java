package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;

@Entity
@Table(name = "payment_application_dur_compilations")
public class PaymentApplicationDurCompilations extends PersistentEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2993948864176524297L;

	@Column(name = "payment_application_id")
	private Long paymentApplicationId;

	@Column(name = "durCompilations_id")
	private Long durCompilationsId;

	public PaymentApplicationDurCompilations() {
		super();
	}

	public PaymentApplicationDurCompilations(Long paymentApplicationId, Long durCompilationsId) {
		super();
		this.paymentApplicationId = paymentApplicationId;
		this.durCompilationsId = durCompilationsId;
	}

	public Long getPaymentApplicationId() {
		return paymentApplicationId;
	}

	public void setPaymentApplicationId(Long paymentApplicationId) {
		this.paymentApplicationId = paymentApplicationId;
	}

	public Long getDurCompilationsId() {
		return durCompilationsId;
	}

	public void setDurCompilationsId(Long durCompilationsId) {
		this.durCompilationsId = durCompilationsId;
	}

}
