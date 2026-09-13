package com.blog.backend.controller;

import com.blog.backend.model.Postagem;
import com.blog.backend.repository.PostagemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/postagens")
@Tag(name = "Postagens", description = "Endpoints do blog")
@CrossOrigin(
    origins = "*", 
    allowedHeaders = "*", 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class PostagemController {

    private PostagemRepository repository;

    public PostagemController(@Autowired PostagemRepository repository) {
        this.repository = repository;
    }

    // 1. GET: /postagens (Lista todas com imagens em Base64)
    @GetMapping
    @Operation(summary = "Lista todas as postagens")
    public ResponseEntity<?> getAll() {
        var postagens = repository.findAllByOrderByPostagemIdDesc();
        if (postagens.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há postagens.");
        }
        return ResponseEntity.ok(postagens);
    }

    // 2. GET: /postagens/{id} (Obtém uma postagem específica)
    @GetMapping("/{id}")
    @Operation(summary = "Obtém uma postagem por ID")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(postagem -> ResponseEntity.ok((Object) postagem))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Postagem não encontrada."));
    }

    // 3. POST: /postagens (Cria nova postagem)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cria uma nova postagem")
    public ResponseEntity<?> post(
            @RequestParam("titulo") String titulo,
            @RequestParam("mensagem") String mensagem,
            @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {
        
        try {
            Postagem postagem = new Postagem();
            postagem.setTitulo(titulo);
            postagem.setMensagem(mensagem);

            if (arquivo != null && !arquivo.isEmpty()) {
                postagem.setImagem(arquivo.getBytes());
            }

            Postagem novaPostagem = repository.save(postagem);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPostagem);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar imagem.");
        }
    }

    // 4. PUT: /postagens/{id} (Atualiza dados e/ou altera a imagem)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Atualiza uma postagem existente")
    public ResponseEntity<?> put(
            @PathVariable Integer id,
            @RequestParam("titulo") String titulo,
            @RequestParam("mensagem") String mensagem,
            @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {
        
        return repository.findById(id).map(existente -> {
            try {
                existente.setTitulo(titulo);
                existente.setMensagem(mensagem);

                // Se enviou uma nova imagem, atualiza os bytes. Senão, mantém a imagem antiga.
                if (arquivo != null && !arquivo.isEmpty()) {
                    existente.setImagem(arquivo.getBytes());
                }

                Postagem atualizada = repository.save(existente);
                return ResponseEntity.ok((Object) atualizada);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar imagem.");
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Postagem não encontrada."));
    }

    // 5. DELETE: /postagens/{id} (Remove por ID)
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Remove uma postagem por ID")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return repository.findById(id).map(postagem -> {
            repository.delete(postagem);
            return ResponseEntity.ok((Object) postagem);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Postagem não encontrada."));
    }
}