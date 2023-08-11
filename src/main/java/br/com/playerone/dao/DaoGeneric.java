package br.com.playerone.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.playerone.jpautil.JpaUtil;

public class DaoGeneric<E> {

	public void salvar(E entidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.persist(entidade);

		entityTransaction.commit();
		entityManager.close();
	}

	public E atualizar(E entidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		E entidadeSalvaOuAtualizada = entityManager.merge(entidade);

		entityTransaction.commit();
		entityManager.close();
		return entidadeSalvaOuAtualizada;
	}

	public void remover(E entidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.remove(entidade);

		entityTransaction.commit();
		entityManager.close();
	}

	public void removerPorId(E entidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Object id = JpaUtil.getPrimaryKey(entidade);

		entityManager.createQuery("delete from " + entidade.getClass().getSimpleName() + " where id = " + id)
				.executeUpdate();

		entityTransaction.commit();
	}

	@SuppressWarnings({ "unchecked" })
	public List<E> getListEntity(E entidade) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		EntityTransaction transactional = entityManager.getTransaction();
		transactional.begin();

		List<E> lista = entityManager.createQuery("select u from " + entidade.getClass().getSimpleName() + " u ")
				.getResultList();

		transactional.commit();
		entityManager.close();
		return lista;
	}

}
