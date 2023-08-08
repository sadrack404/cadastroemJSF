package br.com.playerone.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.playerone.Lancamento;
import br.com.playerone.jpautil.JpaUtil;

public class IdaoLancamentoImpl implements IdaoLancamento {

	@Override
	public List<Lancamento> listarLancamentos(Long codUsuario) {
		List<Lancamento> lancamentosDoUsuario = null;
		
		EntityManager entityManager =  JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		lancamentosDoUsuario =  entityManager
				.createQuery("from Lancamento where usuario.id = " + codUsuario)
				.getResultList();
		
		transaction.commit();
		entityManager.close();
		return lancamentosDoUsuario;
	}
	
}
