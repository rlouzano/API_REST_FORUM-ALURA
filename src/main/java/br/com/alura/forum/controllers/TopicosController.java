package br.com.alura.forum.controllers;

import br.com.alura.forum.controllers.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controllers.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controllers.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.TopicoDto;
import br.com.alura.forum.repository.CurosRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CurosRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDto.converter(topicos);
        }
    }
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {  // Antes de executar o codigo verifica as validações no bean validation com a anotação: @Valid
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();  //  Esse é o codigo para o caminho da url
        return ResponseEntity.created(uri).body(new TopicoDto(topico)); //  ResponseEntity.created   :   Criando um cenário para gravar as informações no banco de dados POST
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id){
        Optional<Topico> one = topicoRepository.findById(id);
        if(one.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(one.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){

        Optional<Topico> one = topicoRepository.findById(id);
        if(one.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();


    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id){
        Optional<Topico> one = topicoRepository.findById(id);
        if(one.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
