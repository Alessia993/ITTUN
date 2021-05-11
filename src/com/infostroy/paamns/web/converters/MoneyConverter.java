/**
 * 
 */
package com.infostroy.paamns.web.converters;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.infostroy.paamns.common.enums.Currency;
import com.infostroy.paamns.common.utils.Utils;
import java.util.Map;
import com.infostroy.paamns.persistence.SessionManager;
import com.infostroy.paamns.web.beans.session.SessionBean;

/**
 *
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 *
 */
public class MoneyConverter implements Converter {
	protected transient final Log log = LogFactory.getLog(getClass());

	private final Double DEFAULT_DINARO_EXCHANGE_AMOUNT = 3.39;
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && !String.valueOf(value).isEmpty()) {
			try {

				if (Utils.getLocale() == Locale.ITALIAN) {
					return italianLabel(value);
				} else {
					return frenchLabel(value);
				}

			} catch (Exception e) {
				log.error(e);

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.
	 * FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) throws ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	private String frenchLabel(Object value) {
		DecimalFormat dec = new DecimalFormat("TND ###,###,##0.00", DecimalFormatSymbols.getInstance(Locale.FRENCH));
		dec.setRoundingMode(RoundingMode.HALF_UP);
		return String.valueOf(dec.format(Double.parseDouble(String.valueOf(value))/DEFAULT_DINARO_EXCHANGE_AMOUNT)).replace(" ", "\u00a0");
	}

	private String italianLabel(Object value) {
		DecimalFormat dec = new DecimalFormat("\u20AC ###,###,##0.00",
				DecimalFormatSymbols.getInstance(Locale.ITALIAN));
		dec.setRoundingMode(RoundingMode.HALF_UP);
		return String.valueOf(dec.format(Double.parseDouble(String.valueOf(value)))).replace(" ", "\u00a0");
	}

	public Map<String, Object> getSession() {
		SessionManager sm = SessionManager.getInstance();

		if (sm != null) {
			SessionBean sessionBean = sm.getSessionBean();

			if (sessionBean != null) {
				if (sessionBean.Session != null) {
					return sessionBean.Session;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
