public class Cliente implements Runnable{

    private int posicaoNaFila = 0;
    
    public int getPosicaoNaFila() {
        return posicaoNaFila;
    }

    public void setPosicaoNaFila(int posicaoNaFila) {
        this.posicaoNaFila = posicaoNaFila;
    }

    @Override
    public void run() {
        try {
            Operacao();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Operacao() throws InterruptedException{
        while(getPosicaoNaFila() >= 1){
            System.out.println("[Cliente] Espera na fila");
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        System.out.println("[Cliente] Senta na cadeira para ter o cabelo cortado");
    }
    
}
