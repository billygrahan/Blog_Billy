import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class PostagemService {
    private readonly apiUrl = '/api/postagens';

    constructor(private http: HttpClient) { }

    listar() {
        return this.http.get(this.apiUrl);
    }

    criar(titulo: string, mensagem: string, arquivo?: File) {
        const formData = new FormData();
        formData.append('titulo', titulo);
        formData.append('mensagem', mensagem);

        if (arquivo) {
            formData.append('arquivo', arquivo);
        }

        return this.http.post(this.apiUrl, formData);
    }

    excluir(id: number) {
        const url = `${this.apiUrl}/${id}`;
        console.log('URL de DELETE gerada:', url);
        return this.http.delete(url, { responseType: 'text' });
    }
}