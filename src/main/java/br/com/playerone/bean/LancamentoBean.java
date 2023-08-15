package br.com.playerone.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.playerone.Lancamento;
import br.com.playerone.Pessoa;
import br.com.playerone.dao.DaoGeneric;
import br.com.playerone.repository.IdaoLancamento;
import br.com.playerone.repository.IdaoLancamentoImpl;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	private IdaoLancamento daoLancamentos = new IdaoLancamentoImpl();

	public String novo() {
		lancamento = new Lancamento();
		return "";
	}

	public String salvar() {
		Pessoa usuarioLogado = idUsuarioLogado();
		
		lancamento.setUsuario(usuarioLogado);
		lancamento = daoGeneric.merge(lancamento);

		carregarLancamentos();

		return "";
	}

	public String remover() {
		daoGeneric.removerPorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}

	@PostConstruct
	public void carregarLancamentos() {
		Pessoa usuarioLogado = idUsuarioLogado();
		lancamentos = daoLancamentos.listarLancamentos(usuarioLogado.getId());
	}

	private Pessoa idUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();
		Pessoa usuario = (Pessoa) session.getAttribute("usuarioLogado");
		return usuario;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public IdaoLancamento getDaoLancamentos() {
		return daoLancamentos;
	}

	public void setDaoLancamentos(IdaoLancamento daoLancamentos) {
		this.daoLancamentos = daoLancamentos;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamanetos) {
		this.lancamentos = lancamanetos;
	}

}
