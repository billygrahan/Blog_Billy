CREATE TABLE tb_postagens (
    postagem_id SERIAL PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    mensagem VARCHAR(50) NOT NULL,
    imagem BYTEA
);