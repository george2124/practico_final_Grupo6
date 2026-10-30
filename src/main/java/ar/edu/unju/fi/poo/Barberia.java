package ar.edu.unju.fi.poo;

import java.util.LinkedList;
import java.util.Queue;

import java.util.concurrent.Semaphore;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

		
public class Barberia {
	private final int totalSillas;
	private int sillasOcupadas = 0;
	
	//Cola interna para saber qué cliente va primero (Protegida por el mutex)
	private final Queue<Cliente> colaDeEspera = new LinkedList<>();
	
	//Semaforos
	private final Semaphore clientes = new Semaphore(0); 	//avisa a los barberos que hay cliente
	private final Semaphore barberos = new Semaphore(0);	//avisa a los cliente que hay barberos libres
	private final Semaphore mutex = new Semaphore(1);		//exclusion mutua para las sillas
	
	public Barberia(int numeroSillas) {
	       this.totalSillas = numeroSillas;
	}

		    // Método que ejecutan los clientes cuando llegan a la barbería
	public void llegarCliente(Cliente cliente) {
		try {
			mutex.acquire();	//aca entra a la seccion critica
			 System.out.println("👤 Cliente " + cliente.getId() + " entra a la barbería.");
			 
			 if(sillasOcupadas < totalSillas) {
				 sillasOcupadas++;
				 colaDeEspera.add(cliente);
				 System.out.println("🪑 Cliente " + cliente.getId() + " se sentó en la sala de espera. Sillas ocupadas: " + colaDeEspera.size());
				 
				 clientes.release();  //aca avisa a un barbero que hay un cliente para atender
				 mutex.release();	  //aca liberael control de las sillas
				 
				 barberos.acquire();  //aca se bloquea al cliente hasta que un barbero este libre y lo llame
				 
			 }else {
				 System.out.println("🚪 [SALA LLENA] Cliente " + cliente.getId() + " se retiró porque no había sillas libres.");
				 mutex.release();  //el mutex se libera si se va el cliente
			 }
		}catch(InterruptedException e) {
			 Thread.currentThread().interrupt();
		}
	}
	
	 // Método que ejecutan los hilos barberos para sacar al siguiente cliente de la cola
    public Cliente esperarSiguienteCliente(int idBarbero) throws InterruptedException {
        System.out.println("😴 Barbero " + idBarbero + " está durmiendo en su sillón...");
        
        clientes.acquire(); //aca el barbero se duerme si no hay cliente
        
        mutex.acquire();   //aca saca al cliente de la cola de espera
        sillasOcupadas--;
        
        Cliente clienteAtender = colaDeEspera.poll();
        mutex.release();
        
        barberos.release();  //aca le avisa al cliente que hay barbero para atenderlo
        
        return clienteAtender; 
    }
		   
}

	


