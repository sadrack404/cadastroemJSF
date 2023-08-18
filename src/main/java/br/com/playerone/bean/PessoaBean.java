package br.com.playerone.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import br.com.playerone.Cidade;
import br.com.playerone.Estado;
import br.com.playerone.Pessoa;
import br.com.playerone.dao.DaoGeneric;
import br.com.playerone.jpautil.JpaUtil;
import br.com.playerone.repository.IdaoPessoa;
import br.com.playerone.repository.IdaoPessoaImpl;
import jakarta.xml.bind.DatatypeConverter;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daogeneric = new DaoGeneric<Pessoa>();
	private IdaoPessoa daoPessoa = new IdaoPessoaImpl();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	private Part arquivoFoto;

	public String novo() {
		pessoa = new Pessoa();
		carregarUsuarios();
		return null;
	}

	public String limpar() {
		pessoa = new Pessoa();
		carregarUsuarios();
		return null;
	}

	public String salvar() {
		try {
			// Processar Imagem
			// System.out.println(arquivoFoto);
			byte[] imagemByte = null;
			if (arquivoFoto != null) {
				imagemByte = getByteStream(arquivoFoto.getInputStream());
			}
			if (imagemByte != null && imagemByte.length > 0) {
				pessoa.setFotoIconBase64Original(imagemByte); /* Salva imagem original */

				/* Transofmrar em bufferimage */
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));

				/* Descobrir o tipo da imagem */
				int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();

				int largura = 200;
				int altura = 200;

				/* Criar a miniatura */
				BufferedImage resizedImage = new BufferedImage(largura, altura, type);
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(bufferedImage, 0, 0, largura, altura, null);
				g.dispose();

				/* Escrever novamente a imagem em tamanho menor */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String extensao = arquivoFoto.getContentType().split("\\/")[1]; /* Exemplo: image/png */
				ImageIO.write(resizedImage, extensao, baos);

				String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64,"
						+ DatatypeConverter.printBase64Binary(baos.toByteArray());
				// Processar Imagem

				pessoa.setFotoIconBase64(miniImagem);
				pessoa.setExtensao(extensao);
			}

			pessoa = daogeneric.merge(pessoa);
			if (pessoa != null) {
				carregarUsuarios();
				mostrarMsg("Registered successfully!");
			} else {
				mostrarMsg("Could not include record!");
			}
		} catch (Exception e) {
			mostrarMsg("Error adding registry!");
			e.printStackTrace();
		}
		return "";
	}

	public String remover() {
		daogeneric.removerPorId(pessoa);
		pessoa = new Pessoa();
		carregarUsuarios();
		return "";
	}

	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";
			StringBuilder cepBuilder = new StringBuilder();

			while ((cep = reader.readLine()) != null) {
				cepBuilder.append(cep);
			}

			Pessoa gson = new Gson().fromJson(cepBuilder.toString(), Pessoa.class);

			pessoa.setCep(gson.getCep());
			pessoa.setLogradouro(gson.getLogradouro());
			pessoa.setComplemento(gson.getComplemento());
			pessoa.setBairro(gson.getBairro());
			pessoa.setLocalidade(gson.getLocalidade());
			pessoa.setUf(gson.getUf());
			pessoa.setIbge(gson.getIbge());
			pessoa.setGia(gson.getGia());
			pessoa.setDdd(gson.getDdd());
			pessoa.setSiafi(gson.getSiafi());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			mostrarMsg("Erro ao pesquisar CEP!");
		}
	}

	@PostConstruct
	public void carregarUsuarios() {
		pessoas = daogeneric.getListEntity(pessoa);
	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(msg);

		context.addMessage(null, facesMessage);
	}

	public String logar() {

		carregarUsuarios();

		Pessoa usuario = daoPessoa.buscarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (usuario != null) { // encontrou usuario
			// Adiciona o usuário na sessão
			FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
			ExternalContext externalContext = context.getExternalContext();

			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();

			session.setAttribute("usuarioLogado", usuario);

			return "principal.jsf";
		} else {
			FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Usuário ou senha incorretos!"));
		}

		return "index.jsf";
	}

	public String deslogar() {
		// Adiciona o usuário na sessão
		FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		// Controla a sessão do usuário!
		@SuppressWarnings("static-access")
		HttpServletRequest requestSession = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();

		requestSession.getSession().invalidate();

		return "index.jsf";
	}

	public boolean permitirAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();// <- busca informação do jsf
		ExternalContext externalContext = context.getExternalContext();

		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();

		Pessoa usuario = (Pessoa) session.getAttribute("usuarioLogado");

		return usuario.getPerfilUsuario().equals(acesso);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<SelectItem> getEstados() {
		estados = daoPessoa.listaEstados();
		return estados;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public void carregaCidades(AjaxBehaviorEvent event) {
		Estado estado = (Estado) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estado != null) {

			pessoa.setEstado(estado);

			@SuppressWarnings("unchecked")
			List<Cidade> cidades = (List<Cidade>) JpaUtil.getEntityManager()
					.createQuery("from Cidade where estado.id =" + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidade cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			setCidades(selectItemsCidade);
		}

	}

	public void editar() {
		if (pessoa.getCidade() != null) {
			Estado estado = pessoa.getCidade().getEstado();
			pessoa.setEstado(estado);

			@SuppressWarnings("unchecked")
			List<Cidade> cidades = (List<Cidade>) JpaUtil.getEntityManager()
					.createQuery("from Cidade where estado.id =" + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidade cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			setCidades(selectItemsCidade);
		}
	}

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	/* Método de converter um inputstream em array de bites */
	private byte[] getByteStream(InputStream is) throws IOException {
		// Primeiro precisa-se de uma váriavel de controle!
		int len;
		// Tamanho padrão!
		int size = 1024;

		byte[] buf = null;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;
	}

	public void downloadFoto() throws IOException {

		/* O FacesContext() retorna os dados do JSF */
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");

		Pessoa pessoa = daogeneric.buscarPorEntidade(Pessoa.class, Long.valueOf(fileDownloadId));

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();

		FacesContext.getCurrentInstance().responseComplete();

	}

}
