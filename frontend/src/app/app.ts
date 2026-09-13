import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostagemService } from './services/postagem.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {
  postagens: any[] = [];
  novoTitulo: string = '';
  novaMensagem: string = '';
  arquivoSelecionado?: File;

  constructor(private postagemService: PostagemService) { }

  ngOnInit(): void {
    this.carregarPostagens();
  }

  carregarPostagens(): void {
    this.postagemService.listar().subscribe({
      next: (dados: any) => {
        console.log('Estrutura recebida do backend no GET:', dados);
        this.postagens = Array.isArray(dados) ? dados : [];
      },
      error: (err: any) => {
        console.error('Erro ao carregar postagens:', err);
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.arquivoSelecionado = file;
    }
  }

  salvarPostagem(): void {
    if (!this.novoTitulo || !this.novaMensagem) {
      alert('Preencha o título e a mensagem!');
      return;
    }

    console.log('Enviando POST com:', {
      titulo: this.novoTitulo,
      mensagem: this.novaMensagem,
      arquivo: this.arquivoSelecionado?.name
    });

    this.postagemService.criar(this.novoTitulo, this.novaMensagem, this.arquivoSelecionado).subscribe({
      next: (resposta: any) => {
        alert('Postagem criada com sucesso!');

        // Limpa o formulário e o arquivo selecionado
        this.novoTitulo = '';
        this.novaMensagem = '';
        this.arquivoSelecionado = undefined;

        // Se o backend retornar o novo objeto, insere no topo da lista local. 
        // Caso contrário, recarrega a lista completa.
        if (resposta && typeof resposta === 'object') {
          this.postagens.unshift(resposta);
        } else {
          this.carregarPostagens();
        }
      },
      error: (err: any) => {
        console.error('Erro no POST:', err);
        alert(`Falha no POST (Status ${err.status}). Verifique o servidor.`);
      }
    });
  }

  formatarImagem(item: any): string | null {
    return item?.imagemBase64 ?? null;
  }

  getId(item: any): number | undefined {
    if (!item) return undefined;
    const idEncontrado = item.postagemId ?? item.id;
    return idEncontrado ? Number(idEncontrado) : undefined;
  }

  excluirPostagem(id?: number): void {
    console.log('ID que será enviado para a rota DELETE:', id);

    if (!id || isNaN(id)) {
      alert('Erro: ID da postagem é inválido.');
      return;
    }

    if (confirm(`Deseja realmente excluir a postagem #${id}?`)) {
      this.postagemService.excluir(id).subscribe({
        next: () => {
          alert('Postagem excluída com sucesso!');

          // 🚀 Remove o item da tela imediatamente sem precisar recarregar
          this.postagens = this.postagens.filter(item => this.getId(item) !== id);
        },
        error: (err: any) => {
          console.error('Erro no DELETE:', err);
          alert(`Erro ao excluir postagem. Status: ${err.status}`);
        }
      });
    }
  }
}