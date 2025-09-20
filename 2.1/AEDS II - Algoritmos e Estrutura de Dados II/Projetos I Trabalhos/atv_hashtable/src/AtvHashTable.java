
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AtvHashTable {


    public static void main(String[] args) {
        Random random = new Random(42);
        List<Integer> array = new ArrayList<>();

        final int KEY = 100;
        int i;

        for (i = 0; i < 1000; i++) {
            array.add(random.nextInt(0, 100));
        }
        array.add(100);



        long tempoBuscaSemOrdenacao = System.nanoTime();
        i = array.stream()
                .filter(
                        e -> e.equals(KEY))
                .findFirst().orElse(-1);
        tempoBuscaSemOrdenacao = System.nanoTime() - tempoBuscaSemOrdenacao;


        if (i < 0) {
            System.out.println("Não há esse elemento na array");
        }
        System.out.println("Busca Array SEM Ordenação: " + tempoBuscaSemOrdenacao);


        long tempoOrdenacao = System.nanoTime();
        Collections.sort(array);
        tempoOrdenacao = System.nanoTime() - tempoOrdenacao;

        long tempoBuscaComOrdenacao = System.nanoTime();
        i = Collections.binarySearch(array, KEY);
        tempoBuscaComOrdenacao = System.nanoTime() - tempoBuscaComOrdenacao;


        if (i < 0) {
            System.out.println("Não há esse elemento na array");
        }
        System.out.println("Tempo Ordenação: " + tempoOrdenacao);
        System.out.println("Busca Array COM Ordenação: " + tempoBuscaComOrdenacao);


    }


}