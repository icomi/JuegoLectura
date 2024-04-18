package com.ingenieriaycomputacionintegral.juegolectura;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.ingenieriaycomputacionintegral.juegolectura.R;

import android.os.Handler;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Emparejamiento extends Activity {
	
	//public, protected, private
	private final static int []Imagen_dibujo = new int[]{
		R.drawable.gato,
		R.drawable.jose,
		R.drawable.pato,
		R.drawable.perro,
		R.drawable.manzana,
		R.drawable.pera, 
		R.drawable.pina,
		R.drawable.uva,
		R.drawable.platano, 
		R.drawable.fresa,
		R.drawable.leon,
		R.drawable.nube,
		R.drawable.papaya,
		R.drawable.papas,
		R.drawable.sapo,
		R.drawable.sol,
		R.drawable.vaca,
		R.drawable.abe
	};

	private final static int []SONIDOS = new int[] {
		R.raw.gato,
		R.raw.jose,
		R.raw.pato,
		R.raw.perro,
		R.raw.manzana,
		R.raw.pera,
		R.raw.pina,
		R.raw.uva,
		R.raw.platano,
		R.raw.fresa,
		R.raw.leon,
		R.raw.nube,
		R.raw.papaya,
		R.raw.papa,
		R.raw.sapo,
		R.raw.sol,
		R.raw.vaca,
		R.raw.abeja,
		R.raw.aplausobien,
		R.raw.doubleclick,
		R.raw.hitpipe,
		R.raw.ohhh		
	};
	private final static String []LETRAS = new String[] {
		"gato",	"José",	"pato",	"perro","manzana",
		"pera",	"piña",	"uva",	"platano","fresa" ,
		"leon", "nube", "papaya","papa","sapo",
		"sol", "vaca","abeja"};
	private static final int DIBUJO = 2, LETRA=1;
	private int sel = 0;
	private int MAL, BIEN, APLAUSO;
	private final Handler handler = new Handler();
	private Dibujos[] dibujos;
	private Letras[] letras;
	private boolean touchactivo = true;
	private Dibujos dibujoActual = null;
	private Letras letraActual = null;	
	private Sonidos sonido;
	private int tam = 5;
	private int giradas = 0;
	protected float ancho, alto;
	ScrollView sv ;
	LinearLayout ll;
	Vista vista;

	private Random r;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		
		r= new Random();
		do{	tam =  r.nextInt(5);}while(tam < 2);
		
		final TableLayout table = new TableLayout(this) ;		
		final TableLayout table2 = new TableLayout(this) ;	
		
		//setContentView(R.layout.activity_emparejamiento);
		obtenerAncho();	   
		letras = new Letras[tam];
		sonido = new Sonidos(this.getBaseContext());
		MAL = sonido.load(R.raw.mal);
		BIEN = sonido.load(R.raw.ohhh);
		dibujos = crearCeldas(tam) ; 
		APLAUSO = sonido.load(R.raw.aplausobien);
		
		Collections.shuffle(Arrays.asList(dibujos) ) ; //distribuir aleatoriamente las imagenes
		Collections.shuffle(Arrays.asList(letras) ) ; //distribuir aleatoriamente las letras escritas
		for (int i =0; i < tam; i ++){
			final TableRow fila = new TableRow(this) ;
			fila.addView(dibujos[i].boton) ;
			dibujos[i].posicion = i;
			final TableRow fila2 = new TableRow(this) ;
			fila2.addView(letras[i].boton) ;
			letras[i].posicion = i;
			table.addView(fila);
			table2.addView(fila2);
		}
		sv.setBackgroundResource(R.drawable.selva);
		table.setBackgroundColor(Color.TRANSPARENT);
		ll.addView(table);
		vista = new Vista();
		ll.addView( vista ) ;
		table2.setBackgroundColor(Color.TRANSPARENT);
		ll.addView(table2);
		ll.setBackgroundColor(Color.TRANSPARENT);
		sv.addView(ll);
		setContentView(sv); 	
		
	}
	
	private void obtenerAncho(){
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.ancho = dm.widthPixels / 4 ;
        this.alto = dm.heightPixels / tam;
        this.alto = this.alto * 0.9f ;
    
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
		/* Devuelve un arreglo con las dibujos creadas, selecciona (tam) 
		* de la cantidad de dibujos disponibles
		* */
	public boolean existe(int arreglo[], int cual, int cuan)
	{
		for (int i =0; i<=cuan; i++ ){
			if (cual == arreglo[i]) return true;
		}
		return false;
	}
	private Dibujos[] crearCeldas(final int cont ){
			int cual, cuales[];
			final Dibujos[] array = new Dibujos[cont];
			cuales = new int[10];
			
			for (int i=0; i<cont; i++ ){				
				do{	cual = r.nextInt(17);
				}while(existe(cuales,cual, i));				
				cuales[i]=cual;
				array[i] = new Dibujos(Imagen_dibujo[cual],SONIDOS[cual], LETRAS[cual]);
				letras[i] = new Letras(Imagen_dibujo[cual],SONIDOS[cual], LETRAS[cual]);
			}
			return array ;			
		}
	
	public void comparar( ) {			
		if (dibujoActual.imagen == letraActual.imagen ){ //si habia una dibujoActual y se abre otra igual HACEN PAR
				letraActual.boton.setEnabled(false); //Ya no se puede volver a hacer click sobre esta dibujo
				dibujoActual.boton.setEnabled(false);
				this.reproducir(BIEN);
				emparejar(dibujoActual, letraActual );
				this.giradas ++;  //cuantos aciertos lleva
				dibujoActual = null;	
				letraActual = null;
			}
			else {  //Es diferente al dibujo seleccionado? mal
				touchactivo = false;
				this.reproducir(MAL);	
				//Ejecuta las acciones contenidas en el run(), despues de cierto tiempo de espera
				handler.postDelayed(new Runnable(){
					public void run(){	
						if (sel == DIBUJO) dibujoActual = null; 
						if (sel == LETRA ) letraActual = null; 
						touchactivo = true;
					}},1000);				
		}
		//Si la cantidad de aciertos es el total de dibujos, has ganado
		if(giradas >= tam ) {
			this.reproducir(APLAUSO);
			this.recreate();
			}
}

	
	public void emparejar(Dibujos dibujo ) {			
			if (dibujo != null) dibujo.hacerSonido(); 		
			dibujoActual = dibujo;	
			if (letraActual == null) return;
			sel = DIBUJO;
			comparar();
	}

	public void emparejar(Letras letra ) {							
			letraActual = letra;
			if (dibujoActual == null) return;
			sel = LETRA;
			comparar();			
	}
	
	public void emparejar(Dibujos dibujo, Letras letra) {			
		//Poner el sonido de la dibujo seleccionada
		if (dibujo == null || letra == null) return;
		
		float y1,y2 ;
		y1 = (dibujo.posicion ) * this.alto + this.alto/2 ;
		y2 = (letra.posicion ) * this.alto + this.alto/2;
		
		//Dibujar una linea recta de un boton al otro
		vista.addLinea(y1,y2);
		
	}

			
		/* *******************************************************************
		 * Clase privada Dibujos
		 ******************************************************************** */
		private class Dibujos implements OnClickListener{
			private final ImageButton boton;
			private final int imagen;
			int posicion = 0;
			//private final String escrito; //Cual es la palabra escrita que representa ese dibujo
			private int archivosonido;
			private boolean emparejada = false;
			
			Dibujos(final int imag, final int son, final String let){
				this.imagen = imag; 
				this.boton= new ImageButton(Emparejamiento.this);	
				//this.escrito = let;
				this.boton.setLayoutParams(new TableRow.LayoutParams((int)ancho -10,(int)alto -10)) ;
				this.boton.setScaleType(ScaleType.FIT_XY);
				this.boton.setImageResource(imagen); 
				this.boton.setOnClickListener(this);
				this.archivosonido = sonido.load(son);
			}
				
		public void hacerSonido(){
				 sonido.play(this.archivosonido);
				return;
			}
			
		//Si se hace click sobre algun dibujo
		public void onClick(View arg0){
				if (!this.emparejada&&touchactivo){
					emparejar(this);				
				}
			}		
		}
		
		private class Letras implements OnClickListener{
			private final Button boton;
			private final int imagen;
			int posicion;
			private final String escrito; //Cual la palabra escrita que representa esa dibujo
			private int archivosonido;
			private boolean emparejada = false;
			
			Letras(final int imag,  final int son, final String let){
				this.imagen = imag; 
				this.escrito = let;
				this.boton = new Button(Emparejamiento.this);	
				this.boton.setLayoutParams(new TableRow.LayoutParams((int)(ancho * 1.5) , (int)alto -10)) ;
				this.boton.setText(this.escrito);
				
				this.boton.setOnClickListener(this);
				this.archivosonido = sonido.load(son);				
			}
				
			public void hacerSonido(){ 
				 sonido.play(this.archivosonido);
				return;
			}
				
		//Si se hace click sobre alguna dibujo
			public void onClick(View arg0){
				//letrasActual = this;
				if (!this.emparejada&&touchactivo){
					letraActual=this;
					emparejar(this);				
				}
			}		
		}

		private class Vista extends View {
			Paint paint = new Paint();
			float y1[], y2[];
			int cant = 0;
			
			Vista(){
				super(getBaseContext());
				y1 = new float[10];
				y2 = new float[10];
				//this.setBackgroundResource(R.drawable.selva);
				this.setLayoutParams(new TableRow.LayoutParams((int)(ancho * 1.5), (int)alto * tam)) ;
			}
				
			void addLinea (float ly1, float ly2){
				//paint.setStrokeWidth(4);
		        //paint.setColor(Color.BLACK);		        
		        if (cant < 10 ){
		        	this.y1[cant]= ly1; 
		        	this.y2[cant]= ly2;
		        	cant ++;
		        }
				//this.refreshDrawableState();
			}
			
			 @Override
		    public void onDraw(Canvas canvas) {	
			 	float an = canvas.getWidth()/2;			 	
		        paint.setColor(Color.MAGENTA);
				paint.setStrokeWidth(4);				
				for (int i = 0; i < cant; i ++){
					canvas.drawLine(1, y1[i], an , y2[i], paint);
					//canvas.drawText("Encontrado (" +y1[i]+ "," +y2[i] +")", 1, y1[i] +100, paint);
				}			
		    }		 
		}
		
		
	} 

