package com.blog.backend.repository;

import com.blog.backend.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Integer> {
    List<Postagem> findAllByOrderByPostagemIdDesc();
}