package com.ingenieriaycomputacionintegral.juegolectura;


import java.util.Vector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

public class VistaEstanque extends View {

	private static final long PERIODO_PROCESO = 100;
	private Vector<Objeto> Animales;
	private long ultimoProceso = 0;
	private ThreadJuego threadjuego = new ThreadJuego();	
	Sonidos sonido;
	private final static int CANTMAX = 10;
	private int anchoV, altoV;
	Paint paint = new Paint();
	
	public VistaEstanque(Context context) {
		super(context);	  
		Animales = new Vector<Objeto>();
		sonido = new Sonidos (context);
		//Probar insertando desde el constructor
		//this.addAnimal(R.drawable.sun,Objeto.NO,"sol");
		//Probar insertando desde el constructor 2 animales nuevos
		this.addAnimal(R.drawable.abe,Objeto.VUELA,"abeja");
		}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		for (Objeto animal:Animales) {
			animal.dibujaGrafico(canvas, paint);
		}		
		canvas.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		this.anchoV = w;
		this.altoV = h;		
		for (Objeto animal:Animales){
			animal.setLimites(w,h); //Establecer sus limites y posicion
		} 	
		ultimoProceso =  System.currentTimeMillis();
		threadjuego.start();
	}
	
	
	public void actualizaFisica(){
		long ahora = System.currentTimeMillis();
		 if (ultimoProceso + PERIODO_PROCESO > ahora){
			return ;
		}		
		double retardo =  (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;		
		for (Objeto animal: Animales){
			if (animal.getModoAndar() != Objeto.NO)
				animal.incrementaPos(retardo);
		}			
	}	
	
	public Objeto cualTocado(int x, int y)
	{
		for (Objeto animal: Animales){	
			if (animal.fueTocado(x,y)) return animal;
		}
		return null;
	}
	
	public Objeto addAnimal(int dibujo, int modoAndar, String nombre) {
		if (Animales.size() >= CANTMAX) return null;
		Drawable d;
		d = this.getContext().getResources().getDrawable(dibujo);
		Objeto an =  new Objeto(this, d, modoAndar, nombre);
		an.setDibujo(dibujo); 
		Animales.add(an);
		return an ;
	}
	
	public int addAnimal( Objeto an) {
		int i;
		Animales.add(an);
		i = Animales.size();
		return i ;
	}
	
	public int getCuantos(){
		return Animales.size();
	}
	
	public Vector<Objeto> getAnimales() {
		return Animales;
	}

	public void setAnimales(Vector<Objeto> animales) {
		Animales = animales;
	}
	
	 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
class ThreadJuego extends Thread {
	@Override
	public void run() {
		while (true ) {
			actualizaFisica();
		}
	}  	
}
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public int getAnchoV() {
	return anchoV;
}

public void setAnchoV(int anchoV) {
	this.anchoV = anchoV;
}

public int getAltoV() {
	return altoV;
}

public void setAltoV(int altoV) {
	this.altoV = altoV;
}

@Override
public void destroyDrawingCache() {
	
	super.destroyDrawingCache();
}

	
}
