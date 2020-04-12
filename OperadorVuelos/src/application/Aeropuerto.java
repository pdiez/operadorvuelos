package application;

public class Aeropuerto {
	
	private boolean activo;
	private String nombre;
	private Ciudad ciudad;
	
	public Aeropuerto(String _nombre, Ciudad _ciudad) {
		this.activo = true;
		this.nombre = _nombre;
		this.ciudad = _ciudad;
	}
	public Aeropuerto(String _nombre, Ciudad _ciudad, boolean _activo) {
		this.activo = _activo;
		this.nombre = _nombre;
		this.ciudad = _ciudad;
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
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	
	public String getNombreCiudad() {
		return this.ciudad.getNombre();
	}

	@Override
	public String toString() {
		return this.getNombre();
	}
	
	
}
