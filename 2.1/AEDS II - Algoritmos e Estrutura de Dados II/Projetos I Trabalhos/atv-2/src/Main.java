import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class No {
    int chave;
    No esquerda, direita;

    public No(int item) {
        chave = item;
        esquerda = direita = null;
    }
}

class ArvoreBinariaBusca {
    No raiz;

    ArvoreBinariaBusca() {
        raiz = null;
    }

    public void inserir(int chave) {
        raiz = inserirRec(raiz, chave);
    }

    private No inserirRec(No raiz, int chave) {
        if (raiz == null) {
            raiz = new No(chave);
            return raiz;
        }
        if (chave < raiz.chave) {
            raiz.esquerda = inserirRec(raiz.esquerda, chave);
        } else if (chave > raiz.chave) {
            raiz.direita = inserirRec(raiz.direita, chave);
        }
        return raiz;
    }

    public void remover(int chave) {
        raiz = removerRec(raiz, chave);
    }

    private No removerRec(No raiz, int chave) {
        if (raiz == null) {
            return raiz;
        }

        if (chave < raiz.chave) {
            raiz.esquerda = removerRec(raiz.esquerda, chave);
        } else if (chave > raiz.chave) {
            raiz.direita = removerRec(raiz.direita, chave);
        } else {
            if (raiz.esquerda == null) {
                return raiz.direita;
            } else if (raiz.direita == null) {
                return raiz.esquerda;
            }
            raiz.chave = encontrarSucessor(raiz.direita);
            raiz.direita = removerRec(raiz.direita, raiz.chave);
        }
        return raiz;
    }

    private int encontrarSucessor(No no) {
        int valorMinimo = no.chave;
        while (no.esquerda != null) {
            valorMinimo = no.esquerda.chave;
            no = no.esquerda;
        }
        return valorMinimo;
    }

    public void imprimirEmOrdem() {
        imprimirEmOrdemRec(raiz);
        System.out.println();
    }
    private void imprimirEmOrdemRec(No no) {
        if (no != null) {
            imprimirEmOrdemRec(no.esquerda);
            System.out.print(no.chave + " ");
            imprimirEmOrdemRec(no.direita);
        }
    }

    public void imprimirPreOrdem() {
        imprimirPreOrdemRec(raiz);
        System.out.println();
    }
    private void imprimirPreOrdemRec(No no) {
        if (no != null) {
            System.out.print(no.chave + " ");
            imprimirPreOrdemRec(no.esquerda);
            imprimirPreOrdemRec(no.direita);
        }
    }

    public int encontrarValorMinimo() {
        if (raiz == null) {
            throw new IllegalStateException("A árvore está vazia!");
        }
        No atual = raiz;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual.chave;
    }

    public int calcularAlturaIterativo() {
        if (raiz == null) return -1;
        Queue<No> fila = new LinkedList<>();
        fila.add(raiz);
        int altura = -1;
        while (true) {
            int contagemNos = fila.size();
            if (contagemNos == 0) return altura;
            altura++;
            while (contagemNos > 0) {
                No no = fila.poll();
                if (no.esquerda != null) fila.add(no.esquerda);
                if (no.direita != null) fila.add(no.direita);
                contagemNos--;
            }
        }
    }

    public List<List<Integer>> obterTodosCaminhosAteFolhas() {
        List<List<Integer>> todosCaminhos = new ArrayList<>();
        encontrarCaminhosRecursivo(raiz, new ArrayList<>(), todosCaminhos);
        return todosCaminhos;
    }

    private void encontrarCaminhosRecursivo(No no, List<Integer> caminhoAtual, List<List<Integer>> todosCaminhos) {
        if (no == null) return;
        caminhoAtual.add(no.chave);
        if (no.esquerda == null && no.direita == null) {
            todosCaminhos.add(new ArrayList<>(caminhoAtual));
        }
        encontrarCaminhosRecursivo(no.esquerda, caminhoAtual, todosCaminhos);
        encontrarCaminhosRecursivo(no.direita, caminhoAtual, todosCaminhos);
        caminhoAtual.remove(caminhoAtual.size() - 1);
    }

    public int contarNosNiveisImpares() {
        if (raiz == null) return 0;
        Queue<No> fila = new LinkedList<>();
        fila.add(raiz);
        int nivel = 1;
        int contador = 0;
        while (!fila.isEmpty()) {
            int tamanhoNivel = fila.size();
            if (nivel % 2 != 0) {
                contador += tamanhoNivel;
            }
            for (int i = 0; i < tamanhoNivel; i++) {
                No noAtual = fila.poll();
                if (noAtual.esquerda != null) fila.add(noAtual.esquerda);
                if (noAtual.direita != null) fila.add(noAtual.direita);
            }
            nivel++;
        }
        return contador;
    }

    public int contarNosChavePar() {
        return contarNosChaveParRec(raiz);
    }

    private int contarNosChaveParRec(No no) {
        if (no == null) return 0;
        int contador = (no.chave % 2 == 0) ? 1 : 0;
        contador += contarNosChaveParRec(no.esquerda);
        contador += contarNosChaveParRec(no.direita);
        return contador;
    }

    public int contarNosComUmFilho() {
        return contarNosComUmFilhoRec(raiz);
    }

    private int contarNosComUmFilhoRec(No no) {
        if (no == null) return 0;
        int contador = ((no.esquerda != null && no.direita == null) || (no.esquerda == null && no.direita != null)) ? 1 : 0;
        contador += contarNosComUmFilhoRec(no.esquerda);
        contador += contarNosComUmFilhoRec(no.direita);
        return contador;
    }
}


public class Main {
    public static void main(String[] args) {
        ArvoreBinariaBusca arvore = new ArvoreBinariaBusca();

        int[] elementos = {33, 15, 41, 38, 34, 47, 43, 49};
        for (int el : elementos) {
            arvore.inserir(el);
        }

        System.out.print("Em Ordem  : ");
        arvore.imprimirEmOrdem();
        System.out.print("Pré Ordem : ");
        arvore.imprimirPreOrdem();


        System.out.println("1. Menor valor na árvore: " + arvore.encontrarValorMinimo());
        System.out.println("4. Altura da árvore: " + arvore.calcularAlturaIterativo());
        System.out.println("5. Caminhos até as folhas: " + arvore.obterTodosCaminhosAteFolhas());
        System.out.println("6. Nós em níveis ímpares (nível da raiz = 1): " + arvore.contarNosNiveisImpares());
        System.out.println("7. Nós com chave par: " + arvore.contarNosChavePar());
        System.out.println("8. Nós com exatamente um filho: " + arvore.contarNosComUmFilho());

        System.out.println("\n--- Demonstração da Remoção ---");
        arvore.remover(34);
        arvore.imprimirEmOrdem();

        arvore.remover(41);
        arvore.imprimirEmOrdem();

        System.out.println("\n--- Estado Final da Árvore ---");
        System.out.print("Pré Ordem : ");
        arvore.imprimirPreOrdem();
    }
}