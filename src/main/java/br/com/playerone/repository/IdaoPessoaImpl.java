package br.com.playerone.repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;

import br.com.playerone.Estado;
import br.com.playerone.Pessoa;
import br.com.playerone.jpautil.JpaUtil;

public class IdaoPessoaImpl implements IdaoPessoa {

	@Override
	public Pessoa buscarUsuario(String login, String senha) {
		Pessoa pessoa = null;

		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		pessoa = (Pessoa) entityManager
				.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'")
				.getSingleResult();

		transaction.commit();
		entityManager.close();
		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		List<Estado> estados = null;

		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		estados = entityManager.createQuery("from Estado").getResultList();

		for (Estado estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}

		return selectItems;
	}

}
