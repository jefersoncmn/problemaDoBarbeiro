import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Runnable pq é thread
public class Barbeiro implements Runnable {
    private int cadeirasDeEspera; //Quantidade de cadeiras para clientes esperarem o atendimento
    boolean cadeiraOcupada = false;
    //----------------
    List<Cliente> listaDeClientes;
    List<Thread> clientes;
    //--------------
    boolean barbeiroDormindo = false;//true = dorme e false = atende
    private String nomeDaThread;
    private int numeroMaximoDeClientes = 5;//Gera um numero random até o numero maximo de clientes
    int numeroDeClientes; 

    long tempoDeAtendimento = 4000; 
    long tempoDeSoneca = 2000;


    Barbeiro(String nomeDaThread, int cadeirasDeEspera, int clientes){
        this.nomeDaThread = nomeDaThread;
        this.cadeirasDeEspera = cadeirasDeEspera;
        System.out.println("[Barbeiro] "+nomeDaThread+" chegou no salão.");

        this.listaDeClientes = new ArrayList<Cliente>();
        this.clientes = new ArrayList<Thread>();

        GerarClientes(new Random());//Cria os clientes
    }

    /*
     * Responsável por criar os clientes
     */
    public void GerarClientes(Random numeroAleatorioDeClientes){
        numeroDeClientes = numeroAleatorioDeClientes.nextInt(numeroMaximoDeClientes);//Gera o total de clientes randomicos
        System.out.println("[Barbeiro] Entrou(aram) "+numeroDeClientes+" cliente(s) no salão.");
        for(int i = 0; i< numeroDeClientes; i++){
            Cliente novoCliente = new Cliente();
            novoCliente.setPosicaoNaFila(i + this.listaDeClientes.size()-1);//Defina a posição que cada clienten tem na fila
            this.listaDeClientes.add(novoCliente);
            Thread threadCliente = new Thread(novoCliente);//Cria thread do cliente
            threadCliente.start();//Inicia a thread do cliente
            this.clientes.add(threadCliente);
        }

        
    }

    void atualizarPosicoesDosClientesNaFila(){
        for(int i = 0; i < this.listaDeClientes.size(); i++){
            this.listaDeClientes.get(i).setPosicaoNaFila(i);
        }
    }

    /**
     * Método que se não há clientes do barbeiro espera
     * @throws InterruptedException
     */
    public void BarbeiroDorme() throws InterruptedException{
        System.out.println("[Barbeiro] Não existe(m) cliente(s) no salão do Barbeiro "+nomeDaThread+".");
        System.out.println("[Barbeiro] "+nomeDaThread+" está esperando a chegada de clientes.");
        Thread.sleep(tempoDeSoneca);//Como não tem clientes, o barbeiro vai a mimir. A thread vai esperar 2 segundos.
        System.out.println("[Barbeiro] A cadeira do Barbeiro "+nomeDaThread+ " está livre");
        GerarClientes(new Random());
    }
    
    void RemoverExcessoDeClientes(){
        if(numeroDeClientes > cadeirasDeEspera){
            //verifica quantos clientes irão embora
            int clientesQueVaoEmbora = numeroDeClientes - cadeirasDeEspera;
            //verifica quantos clientes esperam
            numeroDeClientes = numeroDeClientes - clientesQueVaoEmbora;
            //Enquanto o contador for menor que o numero 
            for(int i = cadeirasDeEspera - 1; i < clientes.size() - 1; i++){ //Remove a galera que não arrumou cadeira pra esperar
                // clientes[i] = null;
                clientes.remove(i);
                listaDeClientes.remove(i);
            }
            //Mostra quantos clientes foram embora
            System.out.println("[Barbeiro] "+ clientesQueVaoEmbora + " clientes foram embora! :(");
            //Mostra quantos clientes ficaram
            System.out.println("[Barbeiro] "+numeroDeClientes + " clientes estão esperando.");
        }
    }

    /**
     * Método que atende aos clientes
     * @throws InterruptedException
     */
    public void BarbeiroAtende() throws InterruptedException{
        if(numeroDeClientes != 0){ //Se tiver clientes
            System.out.println("[Barbeiro] Existe(m) "+numeroDeClientes+" cliente(s) esperando atendimento "+nomeDaThread);
            
            synchronized(this.clientes.get(0)){
                try {
                    System.out.println("[Barbeiro] Aguardando o cliente se sentar...");
                    this.clientes.get(0).join();//Faz a Thread do barbeiro parar até terminar essa thread
                    cadeiraOcupada = true;//a cadeira do barbeiro está ocupada
                    System.out.println("[Barbeiro] Um cliente está sendo atendido pelo barbeiro "+nomeDaThread);
                    Thread.sleep(tempoDeAtendimento);//barbeiro atendendo, a thread espera o atendimento terminar
                } catch (Exception e) {
                    System.out.println("Problema no synchronized!");
                } finally {
                    numeroDeClientes--;//Cliente atendido
                    this.clientes.remove(0);
                    this.listaDeClientes.remove(0);
                    atualizarPosicoesDosClientesNaFila();
                    //Mostra qual barbeiro já atendeu
                    System.out.println("[Barbeiro] Um cliente já foi atendido pelo barbeiro "+nomeDaThread);
                    cadeiraOcupada = false;
                }
            }
           
            RemoverExcessoDeClientes();
            
        } else {
            //Avisa qual barbeiro está livre
            System.out.println("[Barbeiro] A cadeira do barbeiro "+nomeDaThread+" está livre.");
            //Libera as cadeiras de espera
            cadeiraOcupada = false;
        }
    }


    @Override
    public void run() {//Método de execução da thread
        while(true){ //Fica verificando o tempo todo
            if(numeroDeClientes <= 0){ //Se não tiver clientes
                try {
                    BarbeiroDorme();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                try {
                    BarbeiroAtende();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }        
    }
    
}
