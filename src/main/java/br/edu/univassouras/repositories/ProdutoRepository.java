package br.edu.univassouras.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.univassouras.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	Page<Produto> findByProduto(String nomeProduto, Pageable paginacao);

	Page<Produto> findByProdutoIgnoreCaseContaining(String nomeProduto, Pageable paginacao);
	
}
