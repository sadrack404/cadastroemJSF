package br.com.playerone.repository;

import java.util.List;

import br.com.playerone.Lancamento;

public interface IdaoLancamento {
	
	public List<Lancamento> listarLancamentos(Long codUsuario);

}
