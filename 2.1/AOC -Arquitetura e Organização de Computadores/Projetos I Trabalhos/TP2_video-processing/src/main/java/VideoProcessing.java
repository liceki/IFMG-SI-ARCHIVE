
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

public class VideoProcessing {

    /* Carrega a biblioteca nativa (via nu.pattern.OpenCV) assim que a classe é carregada na VM. */
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static byte[][][] carregarVideo(String caminho) {

        VideoCapture captura = new VideoCapture(caminho);
        if (!captura.isOpened()) {
            System.out.println("Vídeo está sendo processado por outra aplicação");
        }

        //tamanho do frame
        int largura = (int) captura.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int altura = (int) captura.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        //não conhecço a quantidade dos frames (melhorar com outra lib) :(
        List<byte[][]> frames = new ArrayList<>();

        //matriz RGB mesmo preto e branco?? - uso na leitura do frame
        Mat matrizRGB = new Mat();

        //criando uma matriz temporária em escala de cinza
        Mat escalaCinza = new Mat(altura, largura, CvType.CV_8UC1); //1 única escala
        byte linha[] = new byte[largura];

        while (captura.read(matrizRGB)) {//leitura até o último frames

            //convertemos o frame atual para escala de cinza
            Imgproc.cvtColor(matrizRGB, escalaCinza, Imgproc.COLOR_BGR2GRAY);

            //criamos uma matriz para armazenar o valor de cada pixel (int estouro de memória)
            byte video[][] = new byte[altura][largura];
            for (int y = 0; y < altura; y++) {
                escalaCinza.get(y, 0, linha);
                for (int x = 0; x < largura; x++) {
                    video[y][x] = (byte) (linha[x] & 0xFF); //shift de correção - unsig
                }
            }
            frames.add(video);
        }
        captura.release();

        /* converte o array de frames em matriz 3D */
        byte cuboPixels[][][] = new byte[frames.size()][][];
        for (int i = 0; i < frames.size(); i++) {
            cuboPixels[i] = frames.get(i);
        }

        return cuboPixels;
    }

    public static void gravarVideo(byte video[][][],
                                   String caminho,
                                   double fps) {

        int qFrames = video.length;
        int altura = video[0].length;
        int largura = video[0][0].length;

        int fourcc = VideoWriter.fourcc('a', 'v', 'c', '1');   // identificação codec .mp4
        VideoWriter escritor = new VideoWriter(
                caminho, fourcc, fps, new Size(largura, altura), true);

        if (!escritor.isOpened()) {
            System.err.println("Erro ao gravar vídeo no caminho sugerido");
        }

        Mat matrizRgb = new Mat(altura, largura, CvType.CV_8UC3); //voltamos a operar no RGB (limitação da lib)

        byte linha[] = new byte[largura * 3];                // BGR intercalado

        for (int f = 0; f < qFrames; f++) {
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    byte g = (byte) video[f][y][x];
                    int i = x * 3;
                    linha[i] = linha[i + 1] = linha[i + 2] = g;     // cinza → B,G,R
                }
                matrizRgb.put(y, 0, linha);
            }
            escritor.write(matrizRgb);
        }
        escritor.release(); //limpando o buffer 
    }

    public static void main(String[] args) {

        String caminhoVideo = "src/main/videos/video5.mp4";
        String caminhoGravar = "src/main/videos/video-pos.mp4";
        double fps = 24.0; //isso deve mudar se for outro vídeo (avaliar metadados ???)

        System.out.println("Carregando o vídeo do diretório: " + caminhoVideo);
        byte video[][][] = carregarVideo(caminhoVideo);

        System.out.printf("Nº de Frames: %d   Resolução: %d x %d \n",
                video.length, video[0][0].length, video[0].length);


        long inicio = System.currentTimeMillis();
        System.out.println("Processamento Salt and Pepper: ");
        video = removerSalPimenta(video, 1); //voce deve implementar esta funcao
        long fim = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + (fim - inicio));

        inicio = System.currentTimeMillis();
        System.out.println("Processamento Remove Borrões Temporários");
        removerBorroesTempo(video); //voce deve implementar esta funcao
        fim = System.currentTimeMillis();
        System.out.println("Tempo de execução: " + (fim - inicio));

        System.out.println("Salvando...  " + caminhoGravar);
        gravarVideo(video, caminhoGravar, fps);
        System.out.println("Término do processamento");
    }

    private static void removerBorroesTempo(byte[][][] video) {
        //
    }


    private static byte[][][] removerSalPimenta(byte[][][] video, int intensidade) {
        //Thread: video, indexInicial, indexFinal
        //Intervalo = numFramesPorThread anterior até numFramesPorThread atual

        //video[247] 246 indices (do 0 ao 246)
        //247 frames para 4 threads
        //numFramesPorThread = 61
        //Resto = 3

        int numThreads = 8;
        int numFramesPorThread = video.length / numThreads;
        int resto = video.length % numThreads;
        int numFramesDespachados = 0;
        Piao p;
        List<Piao> piaos = new ArrayList<>();
        byte[][][] videoPosSalPimenta = new byte[video.length][video[0].length][video[0][0].length];
        int n;
        intensidade = 25;

        // loop que repassa as listas e informa quais frames deverão ser processados por cada thread
        for (int i = 0; i < numThreads; i++) {
            n = numFramesPorThread;
            if (resto > 0) {
                n++;
                resto--;
            }
            p = new Piao(video, videoPosSalPimenta,
                    numFramesDespachados, (numFramesDespachados + n), intensidade,
                    TipoDeCalculo.MEDIA, TipoDeCalculo.MEDIANA);
            numFramesDespachados += n; // 62 124 186
            p.start();
            piaos.add(p);
        }

        for(Piao piao: piaos){
            try {
                System.out.println(piao.getQuantFrames());
                piao.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return videoPosSalPimenta;

    }


}
