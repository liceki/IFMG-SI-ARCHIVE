public class AtvABBHenriqueLeao {

    public static void main(String[] args) {
        ArvoreBinariaDeBusca arvoreBinaria = new ArvoreBinariaDeBusca();
        arvoreBinaria.adicionarNo(8);
        arvoreBinaria.adicionarNo(3);
        arvoreBinaria.adicionarNo(1);
        arvoreBinaria.adicionarNo(6);
        arvoreBinaria.adicionarNo(4);
        arvoreBinaria.adicionarNo(7);
        arvoreBinaria.adicionarNo(10);
        arvoreBinaria.adicionarNo(14);
        arvoreBinaria.adicionarNo(13);

        System.out.println("NAO FOLHA: " + arvoreBinaria.contarNosNaoFolha());
        System.out.println("FOLHA: " + arvoreBinaria.contarNosFolha());
        System.out.println("ALTURA: " + arvoreBinaria.calcularAltura());
        System.out.println("MAIOR Nº : " + arvoreBinaria.encontrarMaiorNumero());
        System.out.println("removendo chaves pares");
        arvoreBinaria.removerChavesPares();
        System.out.println("MAIOR Nº : " + arvoreBinaria.encontrarMaiorNumero());

    }


}


class ArvoreBinariaDeBusca {
    No raiz;
    int numNosNaoFolha;
    int numNosFolha;
    int maiorNumero;


    public ArvoreBinariaDeBusca() {
        this.raiz = null;
        this.numNosNaoFolha = 0;
        this.numNosFolha = 0;
    }

    public void adicionarNo(int valor) {
        raiz = adicionarNoRecursivo(raiz, valor);
        contarNosNaoFolha();
    }

    public No adicionarNoRecursivo(No noAtual, int valor) {
        if (noAtual == null) { // chegamos na folha -> adicionar valor e parar recursividade
            noAtual = new No(valor);
        }

        if (noAtual.valor > valor) {
            noAtual.esq = adicionarNoRecursivo(noAtual.esq, valor);
        } else if (noAtual.valor < valor) {
            noAtual.dir = adicionarNoRecursivo(noAtual.dir, valor);
        }
        return noAtual;
    }



    // ---------------- 1 - Escreva uma função que conte o número de nós não folhas em uma ABB simples.
    public int contarNosNaoFolha() {  // condição nó não folha -> no.esq OU no.dir DIFERENTE de nulo
        numNosNaoFolha = 0; // zerar o contador para nãop acumular com contagens anteriores
        contarNosNaoFolhaRecursivo(raiz);
        return numNosNaoFolha;
    }

    public void contarNosNaoFolhaRecursivo(No noAtual) {
        if (noAtual == null) return;
        if (noAtual.esq == null && noAtual.dir == null) return;

        numNosNaoFolha++;

        contarNosNaoFolhaRecursivo(noAtual.esq);
        contarNosNaoFolhaRecursivo(noAtual.dir);
    }


    // ---------------- 2 - Escreva uma função que conte o número de nós folhas em uma ABB simples.
    public int contarNosFolha() {
        numNosFolha = 0;
        contarNosFolhaRecursivo(raiz);
        return numNosFolha;
    }

    // lógica reversa do metodo de pegar os nos nao folhas
    private void contarNosFolhaRecursivo(No noAtual) {
        if (noAtual == null) return;

        contarNosFolhaRecursivo(noAtual.esq);
        contarNosFolhaRecursivo(noAtual.dir);

        if (noAtual.esq != null || noAtual.dir != null) return;

        numNosFolha++;
    }





    // ---------------- 3 - Escreva uma função que calcule a altura de uma árvore.
    public int calcularAltura() {
        return calculaAlturaRecursivo(raiz);
    }

    private int calculaAlturaRecursivo(No noAtual) {
        if (noAtual == null) return -1;

        int alturaEsquerda = calculaAlturaRecursivo(noAtual.esq);
        int alturaDireita = calculaAlturaRecursivo(noAtual.dir);

        return 1 + Math.max(alturaEsquerda, alturaDireita);
    }


    // ---------------- 4 - Escreva uma função que remova todos os nós com chave par.
    public void removerChavesPares() {
        this.raiz = removerParesRecursivo(this.raiz);
    }

    private No removerParesRecursivo(No noAtual) {
        if (noAtual == null) {
            return null;
        }

        noAtual.esq = removerParesRecursivo(noAtual.esq);
        noAtual.dir = removerParesRecursivo(noAtual.dir);

        if (noAtual.valor % 2 == 0) { // par -> deve ser removido.

            // esq null -> dir substitui
            if (noAtual.esq == null) {
                return noAtual.dir;
            }

            //  dir null -> esq substitui
            if (noAtual.dir == null) {
                return noAtual.esq;
            }

            // tem dois filhos -> o menor nó da sub-árvore direita substitui
            No sucessor = encontrarMenor(noAtual.dir);

            noAtual.valor = sucessor.valor;

            noAtual.dir = remover(noAtual.dir, sucessor.valor);
            return noAtual;

        } else { //  ÍMPAR -> deve ser mantido.
            return noAtual;
        }
    }


    private No encontrarMenor(No no) {
        while (no != null && no.esq != null) {
            no = no.esq;
        }
        return no;
    }


    private No remover(No noAtual, int valor) {
        if (noAtual == null) return null;

        if (valor < noAtual.valor) {
            noAtual.esq = remover(noAtual.esq, valor);
        } else if (valor > noAtual.valor) {
            noAtual.dir = remover(noAtual.dir, valor);
        } else {
            if (noAtual.esq == null) return noAtual.dir;
            if (noAtual.dir == null) return noAtual.esq;

            No sucessor = encontrarMenor(noAtual.dir);
            noAtual.valor = sucessor.valor;
            noAtual.dir = remover(noAtual.dir, sucessor.valor);
        }
        return noAtual;
    }


    // ---------------- 5 - Escreva uma função que encontre o maior número em uma ABB.
    public int encontrarMaiorNumero() {
        maiorNumero = 0;
        encontrarMaiorNumeroRecursivo(raiz);
        return maiorNumero;
    }

    // o maior valor será o que não apresenta nenhum elemento à sua direta
    private void encontrarMaiorNumeroRecursivo(No noAtual) {
        if (noAtual == null) return;

        if (noAtual.dir == null) {
            maiorNumero = noAtual.valor;
        }

        encontrarMaiorNumeroRecursivo(noAtual.dir);
    }


}


class No {
    int valor;
    No esq;
    No dir;

    public No(int valor) {
        this.valor = valor;
        esq = null;
        dir = null;
    }
}