package application;

public class Ruta {

	private boolean activo;
	private Aeropuerto origen;
	private Aeropuerto destino;
	
	public Ruta(Aeropuerto _origen, Aeropuerto _destino) {
		this.activo = true;
		this.origen = _origen;
		this.destino = _destino;
	}
	
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public Aeropuerto getOrigen() {
		return origen;
	}
	public void setOrigen(Aeropuerto origen) {
		this.origen = origen;
	}
	public Aeropuerto getDestino() {
		return destino;
	}
	public void setDestino(Aeropuerto destino) {
		this.destino = destino;
	}
	@Override
	public String toString() {
		return this.origen.getNombre() + " -> " + this.destino.getNombre();
		
	}
	
}
