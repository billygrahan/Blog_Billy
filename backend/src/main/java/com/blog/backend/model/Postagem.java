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
    private Integer postagemId;

    @Column(nullable = false, length = 50)
    private String titulo;

    @Column(nullable = false, length = 50)
    private String mensagem;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "imagem", columnDefinition = "LONGBLOB")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] imagem;

    @Transient
    @JsonProperty("imagemBase64")
    public String getImagemBase64() {
        if (this.imagem != null && this.imagem.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(this.imagem);
        }
        return null;
    }
}