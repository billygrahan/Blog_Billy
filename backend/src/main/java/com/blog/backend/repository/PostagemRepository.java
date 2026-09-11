package com.blog.backend.repository;

import com.blog.backend.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Integer> {
    List<Postagem> findAllByOrderByPostagemIdDesc();
}