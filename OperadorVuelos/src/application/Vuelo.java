package application;

public class Vuelo {
	
	private boolean activo;
	private Ruta ruta;
	private int horaSalida;
	private int minutoSalida;
	private Aerolinea aerolinea;
	private String salidaString;
	
	public Vuelo(Ruta _ruta, int _hora, int _minuto, Aerolinea _al) {
		this.activo = true;
		this.ruta = _ruta;
		this.horaSalida = _hora;
		this.minutoSalida = _minuto;
		this.aerolinea = _al;
		this.salidaString = String.format("%02d", this.horaSalida)  + ":" + String.format("%02d", this.minutoSalida);
	}
	public Vuelo(Ruta _ruta, int _hora, int _minuto, Aerolinea _al, boolean _activo) {
		this.activo = _activo;
		this.ruta = _ruta;
		this.horaSalida = _hora;
		this.minutoSalida = _minuto;
		this.aerolinea = _al;
		this.salidaString = String.format("%02d", this.horaSalida)  + ":" + String.format("%02d", this.minutoSalida);
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public Ruta getRuta() {
		return ruta;
	}
	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}
	public int getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(int horaSalida) {
		this.horaSalida = horaSalida;
		this.salidaString = String.format("%02d", this.horaSalida)  + ":" + String.format("%02d", this.minutoSalida);
	}
	public int getMinutoSalida() {
		return minutoSalida;
	}
	public void setMinutoSalida(int minutoSalida) {
		this.minutoSalida = minutoSalida;
		this.salidaString = String.format("%02d", this.horaSalida)  + ":" + String.format("%02d", this.minutoSalida);
	}
	public Aerolinea getAerolinea() {
		return aerolinea;
	}
	public void setAerolinea(Aerolinea aerolinea) {
		this.aerolinea = aerolinea;
	}
	
	public String getNombreOrigen() {
		return this.getRuta().getOrigen().getNombre();
	}
	public String getNombreDestino() {
		return this.getRuta().getDestino().getNombre();
	}
	
	public String getNombreAerolinea() {
		return this.getAerolinea().getNombre();
	}

	public String getSalidaString() {
		return salidaString;
	}

	public void setSalidaString(String salidaString) {
		this.salidaString = salidaString;
	}
	
	
	
	
	
}
