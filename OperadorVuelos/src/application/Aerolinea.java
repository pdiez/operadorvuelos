package application;

public class Aerolinea {

	private boolean activo;
	private String nombre;
	
	public Aerolinea(String _nombre) {
		this.activo = true;
		this.nombre = _nombre;
	}
	
	public Aerolinea(String _nombre, boolean _activo) {
		this.activo = _activo;
		this.nombre = _nombre;
	}
	
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return this.getNombre();
	}
	
	
}
