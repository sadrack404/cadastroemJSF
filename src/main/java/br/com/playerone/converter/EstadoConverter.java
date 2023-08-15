package br.com.playerone.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.playerone.Estado;
import br.com.playerone.jpautil.JpaUtil;

@FacesConverter(forClass = Estado.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override /* RETORNA OBJETO INTEIRO */
	public Object getAsObject(FacesContext context, UIComponent component, String codigoDoEstado) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		Estado estado = (Estado) entityManager.find(Estado.class, Long.parseLong(codigoDoEstado));

		return estado;
	}

	@Override /* RETORNA O CODIGO EM STRING */
	public String getAsString(FacesContext context, UIComponent component, Object estado) {

		if (estado == null) {
			return null;
		}

		if (estado instanceof Estado) {
			return ((Estado) estado).getId().toString();
		} else {
			return estado.toString();
		}

	}

}
