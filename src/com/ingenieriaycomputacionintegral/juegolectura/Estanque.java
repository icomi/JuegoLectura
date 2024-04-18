package com.ingenieriaycomputacionintegral.juegolectura;

import java.util.Random;
import java.util.Vector;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
 
public class Estanque extends Activity implements OnClickListener, OnTouchListener{
	protected Vector<Botones> boton; 
	protected VistaEstanque vj ;
	private Sonidos sonido;
	private int ancho, alto;
	private Random r;
	int xor=0, yor=0;
	Objeto gTouch;
	
	private final static int []IMAGEN= new int[]{
		R.drawable.pez,	R.drawable.pato,
		R.drawable.abe,	R.drawable.sapo,
		R.drawable.vaca, R.drawable.nube, R.drawable.sol
	};
	private final static int []SONIDOS = new int[] {
		R.raw.pez,  R.raw.pato,
		R.raw.abeja, R.raw.sapo,
		R.raw.vaca,R.raw.nube, R.raw.sol
	};
	private final static String []LETRAS = new String[] {
		"pez", "pato","abeja","sapo","vaca","nube","sol"};
	
	//Formas en que se desplazan los animales, mismo orden q arriba
	private final static int []MODO = new int[] {
		Objeto.NADA, Objeto.NADA, Objeto.VUELA, Objeto.BRINCA,
		Objeto.NO, Objeto.NO, Objeto.NO, Objeto.VUELA};
	MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this,R.raw.rainforest);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start(); //Sonido de fondo en la aplicacion */ 
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		LinearLayout rl = new LinearLayout(this.getBaseContext());
		LinearLayout ll = new LinearLayout(this.getBaseContext());
		LinearLayout ll2 = new LinearLayout(this.getBaseContext());
		LinearLayout ll3 = new LinearLayout(this.getBaseContext());
		
		boton = new Vector<Botones>();
		rl.setOrientation(LinearLayout.VERTICAL);	
		
		setContentView(rl); 
		
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll2.setOrientation(LinearLayout.HORIZONTAL);
		ll3.setOrientation(LinearLayout.HORIZONTAL);
		obtenerAncho();		
		sonido = new Sonidos (this.getBaseContext());		
		ll2.addView(new Button(this.getBaseContext()));
		rl.addView(ll2);	
		int cual,cuales[]; ;
		r=  new Random();
		cuales = new int[10];		
		//Añadir opiones aleatorias		
		//final TableRow fila = new TableRow(this) ;	
		for (int i=0; i <4; i++) {			
			do{	cual = r.nextInt(7);
			}while(existe(cuales,cual, i));		
			cuales[i]=cual;
			Botones b = new Botones(this.getBaseContext(), cual);
			boton.add(b) ;
			b.setOnClickListener(this);
			b.setLayoutParams(new TableRow.LayoutParams((int)(ancho/4),(int)(alto*0.15f))) ;			
			ll.addView(b); 			
		}	
		rl.addView(ll);
		vj = new VistaEstanque(this.getBaseContext());		
		vj.setOnTouchListener(this);
		vj.setLayoutParams(	new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//vj.setLayoutParams(new TableRow.LayoutParams((int)(ancho*0.95f),(int)(alto*0.85f))) ;
		vj.setBackgroundResource(R.drawable.estanque);	
		vj.setAltoV((int) (alto * 0.95f));
		vj.setAnchoV((int)(ancho *0.95f));
		setContentView( rl );		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
	} 

	@Override
	protected void onRestart() {
		super.onRestart();
		//Toast.makeText(this, "on restart ", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Toast.makeText(this, "on dresume", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		//Toast.makeText(this, "on start", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mediaPlayer.stop();
		//Toast.makeText(this, "on stop ", Toast.LENGTH_SHORT).show();
	}
	
	public void mensaje(String msj){
		//Toast.makeText(this, msj , Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public void onClick(View v) {	
		
		int max = boton.size();
		int cual;		
		for (int i = 0; i < max; i++){
			if (v.equals(boton.elementAt(i))){	
				cual = boton.elementAt(i).cual;
				boton.elementAt(i).sonar();			
				vj.addAnimal(IMAGEN[cual], MODO[cual], LETRAS[cual]); //(R.drawable.abe,Objeto.VUELA,"abeja");
				//Toast.makeText(this, LETRAS[cual], Toast.LENGTH_SHORT).show();
				vj.refreshDrawableState();	
				return;
			} 
		}
		if (v.equals(vj)) {
			//Toast.makeText(this, " on click event ", Toast.LENGTH_SHORT).show();
		}
		return ;
	}

	public boolean existe(int arreglo[], int cual, int cuan)
	{
		for (int i =0; i<=cuan; i++ ){
			if (cual == arreglo[i]) return true;
		}
		return false;
	}
	private void obtenerAncho(){
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.ancho = (int) (dm.widthPixels * 0.95f );
        this.alto = (int) (dm.heightPixels * 0.95f );    
	}	
	

class Botones extends Button{	
	int numSonido;
	int voz;
	int cual;
	int imagen;
	int cuantos =0;
	String nombre;
	
	public Botones(Context context, int cual){
		super(context);		
		this.nombre = LETRAS[cual];
		this.imagen = IMAGEN[cual];
		this.cual = cual;
		this.voz = sonido.load(SONIDOS[cual]);
		this.setText(this.nombre);
		this.numSonido = cual;
	}
	
	public void sonar() {		
		 sonido.play(voz);
		return;
	}
	
	public void hacerSonido(Sonidos sonido, int numson){
		int son ;
		 son = sonido.load(numson);
		 sonido.play(son);
		return;
	}
	
	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}
}

@Override
public boolean onTouchEvent(MotionEvent event) {
	int x,y;  
	Objeto g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();
	switch (eventaction) {
    case MotionEvent.ACTION_DOWN: 
    	g= vj.cualTocado(x,y);	    	
    	if (g != null) {
    		gTouch = g;
    	}
        break;
    case MotionEvent.ACTION_MOVE:
    	if (gTouch != null) {
    		gTouch.setPosX(x);// -gPez.getAncho()/2);
    		gTouch.setPosY(y);// -gPez.getAlto()/2 );
    	} 
        break;

    case MotionEvent.ACTION_UP:   
    	//Toast.makeText(this, " soltado ", Toast.LENGTH_SHORT).show();   
    	if (gTouch != null ) {//habia una imagen tocada
    		gTouch.setPosX(x); // -gPez.getAncho()/2);
    		gTouch.setPosY(y); // -gPez.getAlto()/2 );
    		//Toast.makeText(this, gPez.nombre+ " soltado ", Toast.LENGTH_SHORT).show();   
    	}
        break;
		}
	
	return super.onTouchEvent(event);
}

@Override
public boolean onTouch(View view, MotionEvent event) {
	int x,y;  
	Objeto g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();
	switch (eventaction) {
    case MotionEvent.ACTION_DOWN: 
    	g= vj.cualTocado(x,y);	    	
    	if (g != null) {
    		gTouch = g;
    	}
        break;
    }		
	return false;
}


}
