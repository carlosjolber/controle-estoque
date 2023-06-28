package br.edu.univassouras.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.edu.univassouras.models.Produto;
import br.edu.univassouras.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
	ProdutoRepository produtoRepository;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@Transactional
	public Produto adicionarProduto(String dataFornecida, String produto, int quantidade, String setor, String descricao) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendario = Calendar.getInstance();
		
		calendario.setTime(format.parse(dataFornecida));
		Produto p = new Produto(calendario, produto, quantidade, setor, descricao);
		Produto produtoStatus = this.produtoRepository.save(p);
		
		return produtoStatus;
	}
	
	@Transactional
	public void alterarProduto(Long codigo, String dataFornecida, String produto, int quantidade, String setor, String descricao) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(format.parse(dataFornecida));
		Produto p = this.listarProdutoPorCodigo(codigo);
		
		p.setData(calendario);
		p.setProduto(produto);
		p.setQuantidade(quantidade);
		p.setSetor(setor);
		p.setDescricao(descricao);
		this.produtoRepository.save(p);
	}
	
	@Transactional
	public void deletarProduto(Long codigo) {
		Produto p = this.listarProdutoPorCodigo(codigo);
		this.produtoRepository.delete(p);
	}
	
	@Transactional
	public Page<Produto> listarProdutos(Pageable paginacao){
		return this.produtoRepository.findAll(paginacao);
	}
	
	@Transactional
	public  Produto listarProdutoPorCodigo(Long codigo){
		return this.produtoRepository.getReferenceById(codigo);
	}

	public Page<Produto> listarProdutosPorNome(String nomeProduto, Pageable paginacao) {
		
		return this.produtoRepository.findByProdutoIgnoreCaseContaining(nomeProduto, paginacao);
	}
	
}
