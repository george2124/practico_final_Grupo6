package ar.edu.unju.fi.poo;


public class Barbero implements Runnable {
	
	private final int id;
	private final Barberia barberia;
	
	
	public Barbero(int id, Barberia barberia) {
		super();
		this.id = id;
		this.barberia = barberia;
	}
	
	 public void run() {
         try {
             while (!Thread.currentThread().isInterrupted()) {
                 System.out.println("😴 Barbero " + id + " está durmiendo en su sillón...");
                 
                
                 //Cuando llega un cliente, el primer barbero disponible se despierta.
                 Cliente cliente = barberia.esperarSiguienteCliente(id);
                 
                 System.out.println("💈 Barbero " + id + " se despertó y está cortando el pelo al Cliente " + cliente.getId());
                 //Simula el tiempo del corte de pelo
                 Thread.sleep((long) (Math.random() * 2000 + 1000)); 
                 
                 System.out.println("✅ Barbero " + id + " terminó con el Cliente " + cliente.getId());
             }
         } catch (InterruptedException e) {
             System.out.println("🛑 Barbero " + id + " terminó su turno.");
         }
     }
}
