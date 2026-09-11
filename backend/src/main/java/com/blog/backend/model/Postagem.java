package com.blog.backend.model;

import java.util.Base64;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_postagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer postagemId;

    @Column(nullable = false, length = 50)
    private String titulo;

    @Column(nullable = false, length = 50)
    private String mensagem;

    @Lob
    @Column(name = "imagem")
    @JdbcTypeCode(SqlTypes.BINARY)
    @JsonIgnore
    private byte[] imagem;

    // Retorna a imagem formatada em Data URI para o Swagger/Frontend exibir diretamente
    @JsonProperty("imagemBase64")
    public String getImagemBase64() {
        if (this.imagem != null && this.imagem.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(this.imagem);
        }
        return null;
    }
}