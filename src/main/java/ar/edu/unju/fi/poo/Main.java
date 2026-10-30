package ar.edu.unju.fi.poo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	 public static void main(String[] args) throws InterruptedException {
	        int NUM_BARBEROS = 3;
	        int NUM_SILLAS = 4;
	        int totalCliente = 10;
	        
	        Barberia barberia = new Barberia(NUM_SILLAS);
	        
	        // Creamos un pool de hilos fijo para manejar a los barberos en paralelo
	        ExecutorService poolBarberos = Executors.newFixedThreadPool(NUM_BARBEROS);
	        
	        // Inicializamos los barberos
	        for (int i = 1; i <= NUM_BARBEROS; i++) {
	            poolBarberos.execute(new Barbero(i,barberia));
	        }

	        // Simulación de llegada de 10 clientes a lo largo del tiempo
	        for (int i = 1; i <= totalCliente; i++) {
	            final int idCliente = i;
	            
	            new Thread(() -> {
	            	Cliente nuevoCliente = new Cliente(idCliente);
	            	barberia.llegarCliente(nuevoCliente);
	            }).start();
	            
	            // Tiempo aleatorio entre la llegada de un cliente y otro
	            Thread.sleep((long) (Math.random() * 800));
	        }
	 
	        //Apagar el sistema de forma controlada tras finalizar la simulación
	     	Thread.sleep(12000); //Dar tiempo a que terminen los últimos cortes
	     	
	     	poolBarberos.shutdownNow(); //Detiene los hilos de los barberos interrumpibles
	     	
	     	System.out.println("\n--- 🏁 Cerrando la barbería por hoy ---");
	 } 	
}
