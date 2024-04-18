package com.ingenieriaycomputacionintegral.juegolectura;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


/* Añadir sonidos a cada carta cuando sea girada
 * las cartas contendran ma me mi mo mu  */

import android.os.Handler;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class Memorama extends Activity {		
	private final static int [] SONIDOS = new int[] {
		R.raw.ma,R.raw.me,R.raw.mi,R.raw.mo,R.raw.mu,
		R.raw.sa,R.raw.se,R.raw.si,R.raw.so,R.raw.su,
		R.raw.pa,R.raw.pe,R.raw.pi,R.raw.po,R.raw.pu,
		R.raw.la,R.raw.le,R.raw.li,R.raw.lo,R.raw.lu,
	};
	private final static int[]COLORES = new int []
			{Color.RED,Color.MAGENTA, Color.CYAN, Color.BLUE,Color.DKGRAY};
	private final static String []SILABAS = new String[] {
		"ma","me","mi","mo","mu",		
		"sa","se","si","so","su",	
		"pa","pe","pi","po","pu",	
		"la","le","li","lo","lu"		};
	private final int[] FONDOS = new int[]{R.drawable.pasto,R.drawable.acuario, R.drawable.cielo,
			R.drawable.pastocielo,R.drawable.estanque};
	private final int[] IMAGEN = new int[]{R.drawable.hongo,R.drawable.burbuja, R.drawable.nubelluvia,
			R.drawable.habla,R.drawable.pez};
	
	private Animation anim ;
	private int MAL, BIEN, APLAUSO, PICHATARO;
	private final Handler handler = new Handler();
	private Carta[] cartas;
	private boolean touchactivo = true;
	private Carta cartaAbierta = null;
	private Sonidos sonido;
	private int NUMCARTAS=2;
	private int tamx=2, tamy=2;
	private int giradas=0;
	protected int ancho, alto;
	public static int MODELO =0;
	Random r;
	 //MediaPlayer mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
		//ScrollView  sv = new ScrollView(this);		
		LinearLayout rl = new LinearLayout(this.getBaseContext());
		rl.setOrientation(LinearLayout.VERTICAL);		
		setContentView(rl); //R.layout.activity_main); 
		/*mediaPlayer =  MediaPlayer.create(this,R.raw.pichataro);		 
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start(); //Sonido de fondo en la aplicacion */
        
		r = new Random();
		giradas = 0;
		final TableLayout table = new TableLayout(this) ;	
		anim = AnimationUtils.loadAnimation(this, R.anim.aumentar);		
		sonido = new Sonidos(this.getBaseContext());
		MAL = sonido.load(R.raw.mal);
		BIEN = sonido.load(R.raw.ohhh);
		APLAUSO = sonido.load(R.raw.aplausobien);
		PICHATARO = sonido.load(R.raw.pichataro);
		
		int numCartas; //NUMERO DE CARTAS QUE HABRA EN ESTE JUEGO
		do{	numCartas =  r.nextInt(8);}while(numCartas < 2); 
		this.distribuir(numCartas);		
		obtenerAncho();	       
		
		//QUE MODELO DE CARTAS SE USARA?
		MODELO = r.nextInt(4);
		
		cartas = crearCeldas(); 		
		Collections.shuffle(Arrays.asList(cartas) ) ;
		int l=0, tam;
		tam = cartas.length;
		for (int i=0; i<tamx; i++){
			final TableRow fila = new TableRow(this) ;
			for (int j=0; j<tamy; j++){
				if (l >= tam ) break;
				fila.addView(cartas[l].boton);
				l++;
			}
			table.addView(fila);			
		}
		//table.setMinimumHeight(tamy *)
		rl.setBackgroundResource(FONDOS[MODELO]);		
		//sv.addView(rl);
		//setContentView(sv); 
		rl.addView(table);
		setContentView(rl); //table);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//mediaPlayer.stop();
	} 

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	super.onConfigurationChanged(newConfig);
	}
	private int distribuir(int numCartas){		
		
		this.NUMCARTAS =numCartas;
		//como se van a distribuir las cartas dependiendo de cuantas sean
		switch(numCartas){
			case 2:	tamx=2; tamy=2;
				break;
			case 3:	tamx=2; tamy=3;
				break;
			case 4:	tamx=2; tamy=4;
				break;
			case 5:	tamx=2; tamy=5;
				break;
			case 6:	tamx=3; tamy=4;
				break;
			case 7:	tamx=4; tamy=4;
				this.NUMCARTAS = 4;
				break;
			case 8:	tamx=4; tamy=4;
				break;
			default:
				tamx=4; tamy=4;
				this.NUMCARTAS=4;
		}
		return NUMCARTAS;
	}
	
	private void obtenerAncho(){
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //ancho de cada celda del memorama
        this.ancho = (int)((dm.widthPixels/tamx) * 0.95f);// - (dm.heightPixels / (tam *10) );
        this.alto  = (int)((dm.heightPixels/tamy) * 0.95f); // -(dm.heightPixels / (tam *10) );
        
        if (ancho > alto ){
        	int aux = tamy;
        	tamy = tamx;
        	tamx = aux;
        }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memorama, menu);
		return true;
	}
	
	public void reproducir (int son) {
		 sonido.play(son);
		return;
	}
	 public void onClick(View view){
		 Intent data = new Intent();
		 data.setData(null);
		 setResult(RESULT_OK,data);		 
		 //Terminar esta actividad
		 finish();
	 }
	 
	public boolean existe(int arreglo[], int cual, int cuan){
		for (int i =0; i<=cuan; i++ )if (cual == arreglo[i]) return true;
		return false;
		}	
	
	/* Devuelve un arreglo con las cartas creadas
	*  aleatoriamente a partir de las cartas disponibles	* */
	private Carta[] crearCeldas( ){
		//tiene que haber el doble de cartas porque son por parejas
		final Carta[] array = new Carta[this.NUMCARTAS * 2];	
		int cual, cuales[], l=0, color;
		cuales = new int[20];		
		for (int i =0; i<NUMCARTAS; i++ ){
			do{	cual=r.nextInt(20);  //Hay 20 posibles silabas
			}while(existe(cuales,cual,i));				
			cuales[i]=cual;		
			color=COLORES[(int)(cual/5)];
			array[l] = new Carta(cuales[i], color); 
			l++;
			array[l] = new Carta(cuales[i], color); 
			l++;
		}
		return array ;		
	}
	
	/*Mostrar la carta clickeada, si no habia ninguna cartaAbierta
	dejar cartaAbierta la carta actual seleccionada
	Si ya habia alguna cartaAbierta, ver si son iguales 
		y aumentar un punto	a giradas
	Si son diferentes las 2 cartaAbiertas, ocultarlas ambas */
	public void descubrirCarta(final Carta carta) {		
		//Poner el sonido de la carta seleccionada
		if (carta != null) carta.hacerSonido();
		
		if (cartaAbierta == null){ //No hay ninguna carta visible
			cartaAbierta = carta;  //Poner la carta actual como cartaAbierta
			cartaAbierta.setCaraVisible(true);
			}
			else if(cartaAbierta.indice == carta.indice ){ //si habia una cartaAbierta y se abre otra igual HACEN PAR
				cartaAbierta.boton.startAnimation(anim);
				carta.boton.startAnimation(anim);				
				carta.setCaraVisible(true);
				cartaAbierta.boton.setBackgroundResource(R.drawable.bt);
				carta.boton.setBackgroundResource(R.drawable.bt);
				carta.boton.setEnabled(false); //Ya no se puede volver a hacer click sobre esta carta
				cartaAbierta.boton.setEnabled(false);		
									
				cartaAbierta = null;				//No hay ninguna carta cartaAbierta
				this.reproducir(BIEN);
				this.giradas ++;  //cuantos aciertos lleva				
			}
			else {  //Es diferente a la carta abierta? girarla y dejarla tapada
				carta.setCaraVisible(true);
				touchactivo = false;
				//Ejecuta las acciones contenidas en el run(), despues de cierto tiempo de espera
				handler.postDelayed(new Runnable(){
					public void run(){
						carta.setCaraVisible(false);  //ocultar la carta nueva
						cartaAbierta.setCaraVisible(false); //ocultar la carta anterior
						cartaAbierta = null;  //no quedó ninguna visible
						touchactivo = true;
					}},1000);	
				carta.errores ++; 
				this.reproducir(MAL);				
		}
		this.setTitle("giradas " + giradas);
		//Si la cantidad de aciertos es el total de cartas, has ganado
		if(giradas >= NUMCARTAS ){ this.reproducir(PICHATARO);
			this.recreate();
			//this.finish();
		}
	}
	
	/* *******************************************************************
	 * Clase privada Carta
	 ******************************************************************** */
	private class Carta implements OnClickListener{
		private final Button boton;
		private final String silaba;
		private final int fonema; //Cual es el fonema de esta carta
		private int archivosonido, indice;
		protected int errores = 0; //cuantos errores ha cometido con esta carta		
		private boolean caravisible = false;
		
		Carta(final int i, int color){
			this.indice = i;
			this.silaba = SILABAS[i];
			this.fonema = SONIDOS[i];
			this.boton= new Button(Memorama.this);			
			this.boton.setLayoutParams(new TableRow.LayoutParams((int)(ancho*0.8f),(int)(alto*0.8f))) ;
			this.boton.setBackgroundResource(IMAGEN[MODELO]);
			
			//this.boton.set
			//this.boton.setTextSize(44);
			//this.boton.setTextAlignment(0x00000001); //TEXT_ALIGNMENT_GRAVITY );
			//this.boton.setTextColor(color);
			this.boton.setText("");
			this.boton.setOnClickListener(this);
			//this.boton.setSoundEffectsEnabled(true);
			this.archivosonido = sonido.load(this.fonema);
		}
	
		/*Carta(final int i , final int fon){
			//this.silaba = SILABA[i];
			this.imagen = i; 
			this.boton= new ImageButton(Memorama.this);
			this.boton.setLayoutParams(new TableRow.LayoutParams(ancho -10,alto -10)) ;
			this.boton.setScaleType(ScaleType.FIT_XY);
			this.boton.setBackgroundColor(Color.TRANSPARENT);
			
			this.boton.setImageResource(R.drawable.flor);
			this.boton.setOnClickListener(this);
			this.fonema = SONIDOS[i];
			this.archivosonido = sonido.load(this.fonema);
		}*/
				
		public void hacerSonido(){
			sonido.play(this.archivosonido);
			//this.boton.playSoundEffect(this.fonema);
			// this.boton.pl
			return;
		}
	
		
		void setCaraVisible(final boolean cv ){
			this.caravisible = cv ;
			//this.boton.setImageResource(cv? imagen: R.drawable.flor);
			this.boton.setText(cv? this.silaba:" ");
			this.boton.setBackgroundResource(cv? R.drawable.circulo:IMAGEN[MODELO]);			
			if (cv&&errores >1) {			}
	}
		
	//Si se hace click sobre alguna carta
		public void onClick(View arg0){
			if (touchactivo) {
				this.boton.startAnimation(anim);
			}
			if (!this.caravisible&&touchactivo){
				descubrirCarta(this);					
			}
		}		
	}

	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	
} 

