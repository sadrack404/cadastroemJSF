package br.com.playerone.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.playerone.Pessoa;
import br.com.playerone.jpautil.JpaUtil;

public class IdaoPessoaImpl implements IdaoPessoa {
	
	@Override
	public Pessoa buscarUsuario(String login, String senha) {
		Pessoa pessoa = null;
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		
		pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" 
				+ login + "' and p.senha = '" + senha + "'").getSingleResult();
		
		transaction.commit();
		entityManager.close();
		return pessoa;
	}
	
}
