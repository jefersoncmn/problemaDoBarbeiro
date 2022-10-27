import java.util.Random;

//Runnable pq é thread
public class Barbeiro implements Runnable {
    private int cadeirasDeEspera; //Quantidade de cadeiras para clientes esperarem o atendimento
    boolean cadeiraOcupada = false;
    int[] clientes; //numero aleatório de clientes
    boolean barbeiroDormindo = false;//true = dorme e false = atende
    private String nomeDaThread;
    private int clientesNovos;//Gera um numero random até o numero maximo de clientes
    int numeroDeClientes; 

    long tempoDeAtendimento = 1000; //O cara corta rápido
    long tempoDeSoneca = 2000; //O cara dorme muito


    Barbeiro(String nomeDaThread, int cadeirasDeEspera, int clientes){
        this.clientesNovos = clientes;//Inicializa o máximo random de clientes
        this.nomeDaThread = nomeDaThread;
        this.cadeirasDeEspera = cadeirasDeEspera;
        System.out.println("O barbeiro "+nomeDaThread+" chegou no salão.");
    }

    public void Clientes(){
        Random numeroAleatorioDeClientes = new Random();
        numeroDeClientes = numeroAleatorioDeClientes.nextInt(clientesNovos);//Gera o total de clientes randomicos
        clientes = new int[numeroDeClientes];//da o tamanho do vetor clientes

        for(int i = 0; numeroDeClientes < clientes.length; i++){
            clientes[i] = i;
        }
    }

    /**
     * Método que se não há clientes do barbeiro espera
     * @throws InterruptedException
     */
    public void BarbeiroDorme() throws InterruptedException{
        System.out.println("Não existe(m) cliente(s) no salão do Barbeiro "+nomeDaThread+".");
        System.out.println("O Barbeiro "+nomeDaThread+" está esperando a chegada de clientes.");
        Thread.sleep(tempoDeSoneca);//Como não tem clientes, o barbeiro vai a mimir. A thread vai esperar 2 segundos.
        System.out.println("A cadeira do Barbeiro "+nomeDaThread+ " está livre");
        Clientes();
    }

    /**
     * Método que atende aos clientes
     * @throws InterruptedException
     */
    public void BarbeiroAtende() throws InterruptedException{
        if(numeroDeClientes != 0){ //Se tiver clientes
            if(numeroDeClientes > 1 && cadeiraOcupada == false){//se tem mais de um cliente e a cadeira não está ocupada
                System.out.println("Entrou(aram) "+numeroDeClientes+" clinte(s) no salão.");
            } else { //Se tem mais de um cliente e tem cadeiras ocupadas, tem clientes esperando
                System.out.println("Existe(m) "+numeroDeClientes+" cliente(s) esperando atendimento "+nomeDaThread);
            }
            //se há clientes, 1 pode já ser atendido
            System.out.println("Um cliente ocupou a cadeira do barbeiro "+nomeDaThread);
            numeroDeClientes--;//Cliente atendido
            System.out.println("Um clinte está sendo atendido pelo barbeiro "+nomeDaThread);
            cadeiraOcupada = true;//a cadeira do barbeiro está ocupada
            Thread.sleep(tempoDeAtendimento);//barbeiro atendendo, a thread espera o atendimento terminar
            
            if(numeroDeClientes > cadeirasDeEspera){
                //verifica quantos clientes irão embora
                int clientesQueVaoEmbora = numeroDeClientes - cadeirasDeEspera;
                //verifica quantos clientes esperam
                numeroDeClientes = numeroDeClientes - clientesQueVaoEmbora;
                //Enquanto o contador for menor que o numero 
                for(int i = 0; i < clientes.length - 1; i++){
                    clientes[i] = 0;
                }
                //Atualiza o total de clientes
                for(int j = 0; j < numeroDeClientes; j++){
                    clientes[j] = j + 1;
                }
                //Mostra quantos clientes foram embora
                System.out.println(clientesQueVaoEmbora + " clientes foram embora! :(");
                //Mostra quantos clientes ficaram
                System.out.println(numeroDeClientes + " clientes estão esperando.");
            }
            //Mostra qual barbeiro já atendeu
            System.out.println("Um cliente já foi atendido pelo barbeiro "+nomeDaThread);
            //Se o numero de cliente for igual a 1

        } else if(numeroDeClientes == 1){
            //Mostra qual barbeiro está livre
            System.out.println("A cadeira do barbeiro "+nomeDaThread+" está livre.");
            //Avisa que o barbeiro vai atender
            System.out.println("A cadeira do Barbeiro "+nomeDaThread+" está ocupada, não existem clientes esperando.");
            Thread.sleep(tempoDeAtendimento);//Thread espera para atender esse cliente
            numeroDeClientes--;//Cliente atendido
            //Mostra qual barbeiro já atendeu
            System.out.println("Um cliente já foi atendido pelo barbeiro "+nomeDaThread);
        } else {
            //Avisa qual barbeiro está livre
            System.out.println("A cadeira do barbeiro "+nomeDaThread+" está livre.");
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
