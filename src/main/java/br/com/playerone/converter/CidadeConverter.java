package br.com.playerone.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.playerone.Cidade;
import br.com.playerone.jpautil.JpaUtil;

@FacesConverter(forClass = Cidade.class, value = "cidadeConverter")
public class CidadeConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigoDaCidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Cidade cidade = (Cidade) entityManager.find(Cidade.class, Long.parseLong(codigoDaCidade));

		return cidade;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {
		if (cidade == null) {
			return null;
		}
		if (cidade instanceof Cidade) {
			return ((Cidade) cidade).getId().toString();
		} else {
			return cidade.toString();
		}
	}

}
