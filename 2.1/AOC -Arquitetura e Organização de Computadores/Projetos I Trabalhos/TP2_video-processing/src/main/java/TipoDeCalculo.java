public enum TipoDeCalculo {
    MEDIA(0),
    MEDIANA(1),
    MEDIA_E_MEDIANA(2);

    private final int valor;

    TipoDeCalculo(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static TipoDeCalculo fromValor(int valor) {
        for (TipoDeCalculo tipo : TipoDeCalculo.values()) {
            if (tipo.getValor() == valor) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + valor);
    }
}
