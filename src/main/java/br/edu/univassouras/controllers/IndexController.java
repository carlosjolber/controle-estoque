package br.edu.univassouras.controllers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.edu.univassouras.models.Produto;
import br.edu.univassouras.service.ProdutoService;

@Controller
public class IndexController {

	private boolean produtoCadastrado = false;
	private Long numeroPaginaAnterior;
	private ProdutoService produtoService;

	public IndexController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@GetMapping("/")
	public String homePage(@RequestParam(name = "numeroPagina", defaultValue = "1") Long numeroPagina,
			@RequestParam(name = "produto", defaultValue = "") String nomeProduto,  Model modelo) {
		Pageable paginacao;
		Page<Produto> page;
		boolean produtoPesquisado = false;

		if (numeroPagina < 1) {
			numeroPagina = (long) 1;
		}

		paginacao = PageRequest.of((int) (numeroPagina - 1), 10);
		if (!nomeProduto.isEmpty()) {
			produtoPesquisado = true;
			page = this.produtoService.listarProdutosPorNome(nomeProduto, paginacao);
		} else {
			page = this.produtoService.listarProdutos(paginacao);

		}
		
		if(numeroPaginaAnterior != numeroPagina) {
			produtoCadastrado = false;
			numeroPaginaAnterior = numeroPagina;
		}

		modelo.addAttribute("produtos", page.getContent());
		modelo.addAttribute("paginaAtual", page.getNumber());
		modelo.addAttribute("totalPaginas", page.getTotalPages());
		modelo.addAttribute("produtoPesquisado", produtoPesquisado);
		modelo.addAttribute("produtoCadastrado", produtoCadastrado);
		
		return "index";
	}

	@PostMapping("/adicionarProduto")
	public String cadastrarProduto(@RequestParam("data") String data, @RequestParam("produto") String produto,
			@RequestParam("quantidade") int quantidade, @RequestParam("setor") String setor,
			@RequestParam("descricao") String descricao, Model modelo) throws ParseException {
		Produto p = null;
		
		
		p = this.produtoService.adicionarProduto(data, produto, quantidade, setor, descricao);
		if(p != null) {
			produtoCadastrado = true;
		}else {
			produtoCadastrado = false;
		}
		
		return "redirect:/";
	}

	@PostMapping("/atualizarProduto")
	public String alterarProduto(@RequestParam("codigo") Long codigo, @RequestParam("data") String data,
			@RequestParam("produto") String produto, @RequestParam("quantidade") int quantidade,
			@RequestParam("setor") String setor, @RequestParam("descricao") String descricao) throws ParseException {

		System.out.println("Data: " + data);
		this.produtoService.alterarProduto(codigo, data, produto, quantidade, setor, descricao);
		return "redirect:/";
	}

	@GetMapping("/deletarProduto")
	public String removerProduto(@RequestParam Long codigo) {
		this.produtoService.deletarProduto(codigo);
		return "redirect:/";
	}

}
