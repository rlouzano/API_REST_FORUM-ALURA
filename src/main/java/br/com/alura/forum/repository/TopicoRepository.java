package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // O relacionamento aqui está na palavra CursoNome no nome do metodo findByCursoNome ele vai buscar o curso no entidade e o nome
    List<Topico> findByCursoNome(String nomeCurso);

    /*
     * @Query("SELECT t FROM topico t WHERE t.curso.nome = :nomeCurso")
     * List<Topico> QualqueNomeExemplo(@Param("nomeCurso") String nomeCurso);
     */

}
