package com.ingenieriaycomputacionintegral.juegolectura;

import java.util.Random;
import java.util.Vector;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

public class JuegoBarcos extends Activity implements OnTouchListener{
	protected VistaRio vj ;
	private Sonidos sonido;
	private int ancho, alto;
	private Random r;
	boolean haySeleccionado = false;
	int xor=0, yor=0;
	private int ENCONTRADOS = 0, TOTALBARCOS= 3;
	ObjetoRio gAnimal, gBarco;	
	int BIEN, APLAUSO, COLOCA;
	private final static int []IMAGEN= new int[]{
		R.drawable.pez,	R.drawable.pato, R.drawable.vaca, R.drawable.jirafa, R.drawable.leon,
		R.drawable.perro, R.drawable.ave, R.drawable.elefant, R.drawable.panda,
		R.drawable.camello, R.drawable.cebra, R.drawable.pinguino
	};
	private final static String []LETRAS = new String[] {
		"pez", "pato","vaca","jirafa","leon",
		"perro","tucan","elefante","panda",
		"camello","cebra","pinguino"};
	
	MediaPlayer mediaPlayer;
	public ThreadBarcos hilojuego = new ThreadBarcos();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this,R.raw.rainforest);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start(); //Sonido de fondo en la aplicacion */ 		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		LinearLayout rl = new LinearLayout(this.getBaseContext());		
		rl.setOrientation(LinearLayout.VERTICAL);		
		obtenerAncho();		
		sonido = new Sonidos (this.getBaseContext());
		APLAUSO = sonido.load(R.raw.aplausobien);
		BIEN = sonido.load(R.raw.excellent);
		COLOCA = sonido.load(R.raw.colocabarco);
		ENCONTRADOS = 0;
		vj = new VistaRio(this.getBaseContext());		
		vj.setOnTouchListener(this);
		vj.setBackgroundResource(R.drawable.paisaje);	
		vj.setLayoutParams(	new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		vj.setAltoV((int) (alto));
		vj.setAnchoV((int) (ancho ));

		//Toast.makeText(this, "Tamaño pantalla " + ancho + " alto " + alto + " vista x " + vj.getAnchoV(),
		//		Toast.LENGTH_SHORT).show();
		int cual,cuales[]; ;
		r=  new Random();
		cuales = new int[10];	//Añadir opciones aleatorias		
		for (int i=0; i < TOTALBARCOS; i++) {			
			do{	cual = r.nextInt(11);
			}while(existe(cuales,cual, i));		
			cuales[i]=cual;
			vj.addAnimal(cual);			
			//Añadir barco
			vj.addBarco(cual);		
		}	
		rl.addView(vj);
		setContentView( rl );	
		
		//sonido.play(COLOCA);
		Toast.makeText(this, "Coloca el animal en el barquito que lleva su nombre",
				Toast.LENGTH_SHORT).show();
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
		//Toast.makeText(this, "on stop ", Toast.LENGTH_SHORT).show();
	}
	
	public void mensaje(String msj){
		//Toast.makeText(this, msj , Toast.LENGTH_SHORT).show();
		
	}
	
	public boolean existe(int arreglo[], int cual, int cuan){
		for (int i =0; i<=cuan; i++ ){	if (cual == arreglo[i]) return true;	}
		return false;
	}
	
	private void obtenerAncho(){
		int an, al;
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //CONVERTIR PIXELES A DIPS??
        an = dm.widthPixels ; // /(int)getResources().getDisplayMetrics().density ;
        al = dm.heightPixels;  // /(int)getResources().getDisplayMetrics().density ;
        this.ancho = (int) (an * 0.95f );
        this.alto = (int) (al * 0.95f );
		
		 /*Display display = getWindowManager().getDefaultDisplay();
		    DisplayMetrics outMetrics = new DisplayMetrics ();
		    display.getMetrics(outMetrics);

		    float density  = getResources().getDisplayMetrics().density;
		    float dpHeight = outMetrics.heightPixels / density;
		    float dpWidth  = outMetrics.widthPixels / density;
		    this.ancho = (int)dpWidth;
		    this.alto = (int) dpHeight;*/
	}	
	

@Override
public boolean onTouchEvent(MotionEvent event) {
	int x,y;  
	ObjetoRio g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();

	switch (eventaction) {
    case MotionEvent.ACTION_DOWN: 
    	haySeleccionado = false;
    	g= vj.cualTocado(x,y);	    	
    	if (g != null) {
    		haySeleccionado = true;
    		gAnimal = g;
    		//posicion original donde estaba el animal
    		xor = x;
    		yor = y; 
    		gAnimal.crecer(1.1);
    	}
        break;
    case MotionEvent.ACTION_MOVE:
    	if (gAnimal != null) {
    		gAnimal.setPosX(x);// -gPez.getAncho()/2);
    		gAnimal.setPosY(y);// -gPez.getAlto()/2 );
    	} 
        break;

    case MotionEvent.ACTION_UP:       	
    	gBarco = vj.cualBarco(x, y);
    	haySeleccionado = false;
    	if (gBarco !=null && gAnimal != null ){
    		//gVocal.nombre = gVocal.dibujo + gVocal.nombre + gPez.dibujo;
    		if( gBarco.dibujo == gAnimal.dibujo){
    			gAnimal.setPosX(gBarco.getPosX()); 
        		gAnimal.setPosY(gBarco.getPosY() -gBarco.getAlto()/4); 
        		//gPez.setModoAndar(ObjetoRio.NADA);
        		gAnimal.crecer(1/1.2); //Hacerlo mas pequeño
        		gAnimal.setEncontrado(true);
        		gBarco.setEncontrado(true);

        		//gBarco.setIncX(gBarco.getIncX() * 10);
        		sonido.play(BIEN);
        		ENCONTRADOS ++;

        		if(ENCONTRADOS >= TOTALBARCOS ){
            		sonido.play(APLAUSO); this.recreate(); }
        		return super.onTouchEvent(event);
        		//subir el animal al barco y despues desaparecer el barco en la siguiente salida
    		}
    	}
    	//Regresar al animal a su posicion original
    	if(gAnimal !=null ) {
    		gAnimal.setPosX(xor); 
        	gAnimal.setPosY(yor -gAnimal.getAlto()/3); 
        	gAnimal.crecer(1/1.1);
        	haySeleccionado = false;
        	gAnimal = null;
    	}	
    
        break;
		}
	
	return super.onTouchEvent(event);
}

@Override
public boolean onTouch(View view, MotionEvent event) {
	int x,y;  
	ObjetoRio g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();
	switch (eventaction) {
    case MotionEvent.ACTION_DOWN:
    	haySeleccionado = false;
    	g= vj.cualTocado(x,y);	    	
    	if (g != null) {
    		gAnimal = g;
    		haySeleccionado = true;
    		//posicion original donde estaba el animal
    		xor = x;
    		yor = y; 
    		gAnimal.crecer(1.1);
    	}
        break;
    }		
	return false;
}

class ObjetoRio {
	private static final int MAX_VELOCIDAD = 90;
	private View view;
	private Drawable drawable;
	protected int dibujo;
	private boolean encontrado = false, visible = true;
	private boolean esDibujo =true;
	private int w, h;  //alto y ancho del dibujo
	private int radioColision;
	private int DIRX=1, DIRY= -1;
	private double posX, posY;
	private double angulo,rotacion;
	private double incX =2, incY =2;
	public final static int NADA = 1,VUELA= 2, BRINCA= 3, CAMINA= 4, NO=5;
	protected int LimY1, LimY2, LimX1, LimX2;
	String nombre="";
	private int modoAndar = 1; //no definido, debe nadar, brincar, caminar o volar
	Resources res;
	
	public ObjetoRio(View view, int dibujo, int modo) {
		Drawable d;
		if (modo == NADA)
			d = view.getContext().getResources().getDrawable(R.drawable.boat);
		else
			d = view.getContext().getResources().getDrawable(IMAGEN[dibujo]);
		this.view = view;
		this.drawable = d;
		this.modoAndar = modo;
		this.nombre = LETRAS[dibujo];		
		w = drawable.getIntrinsicWidth();
		h = drawable.getIntrinsicHeight();	
		if (modo != NADA){
			if (h > alto /10 )h = alto /10;
			if (w > ancho /10 )w = ancho /10;
		}
		else {
			if (h > alto /10 )h = alto /7;
			if (w > ancho /10 )w = ancho /7;
		}
		this.setIncX(Math.random()* 4);
		this.setIncY(Math.random()* 4);
		this.setAngulo((int) Math.random()* 180 );
		this.setRotacion((int) Math.random()* 8 -4 );
		radioColision = (w + h) /4;			
		this.setLimites(vj.getAnchoV(),vj.getAltoV());
		
	}
	
	public void setLimites(int anchoP, int altoP){		
		//¿Es un animal que nada?
		if (this.modoAndar == NADA ){
			LimY1 =(int)(altoP * 0.66);
			LimY2 =(int)(altoP * 0.90);
			LimX1 = 1;
			LimX2 = anchoP ;
			DIRX =-1;
		}
		else if (this.modoAndar == NO ){
			LimY1 = (int)(altoP * 0.4f);
			LimY2 = (int)(altoP * 0.75f);
			LimX1 = w/2;
			LimX2 =(int)(anchoP -w/2 );
		}else {
			LimY1 = (int)(altoP * 0.7);
			LimY2 = (int)(altoP * 0.7) + alto*2; //los que brincan
			LimX1 = 1 ; //ancho/2;
			LimX2 =(int)(anchoP -w/2 );
			this.setIncY(this.getIncY()* 3 );
			this.setIncX(this.getIncX() +2 );
		}
		if (this.modoAndar == NADA ) {
			posX = (int) (dibujo +1 ) * w  + LimX1; //
			posY =  LimY1 + (int) (dibujo+1)*h*Math.random() ;
		}
		else {posX = (int)(Math.random()* LimX2 );
			posY = (int) LimY1 + (int)( (Math.random()* (LimY2 -LimY1)));
		}
		if (posX < LimX1) { posX = LimX1; }
		if (posY > (LimY2 -h)) { posY = LimY1 +h; }
		if (posX > LimX2) { posX = LimX2 -w; }
		
		//if (this.modoAndar == NADA )posY = LimY1 +(int)Math.random()* h; // + (LimY2 -LimY1)/2;
		this.setPosX(posX);
		this.setPosY(posY);	
	}
	
	public void setModoAndar(int modo)
	{
		this.modoAndar=modo;
	}
	
	public int getModoAndar(){
		return this.modoAndar;
	}
	
	public void dibujaGrafico(Canvas canvas, Paint paint) {
		if (isVisible()) {
			canvas.save();
			int x = (int) (posX - w/2 );
			int y = (int) (posY - h/2 );
			if (modoAndar == VUELA ){ 
				canvas.rotate((float) angulo, (float)x, (float) y );
			}	
			if (esDibujo) {
				drawable.setBounds((int)x,(int)y, (int)(posX +w), (int)(posY +h));
				drawable.draw(canvas);
				if (modoAndar == NADA){ //si nada es porque es barco
					Paint mTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
					//use densityMultiplier to take into account different pixel densities
					final float densityMultiplier = view.getContext().getResources()
					            .getDisplayMetrics().density;  
					mTextPaint.setTextSize(16.0f*densityMultiplier);
					canvas.drawText(this.nombre,x + w/6, y + h/3 , mTextPaint);
				}
			}
			else{
				canvas.drawCircle(x -w/2, y -h/2, w, paint);
				canvas.drawText(this.nombre, x, y, paint);
			}		
			 
			canvas.restore();		
			//int rInval = (int) Math.hypot(w, h)/2 + MAX_VELOCIDAD;
			//view.invalidate(x -rInval, y -rInval, x +rInval, y+rInval);
			int xinv, yinv;
			xinv = w + MAX_VELOCIDAD; 
			yinv = h + MAX_VELOCIDAD;
			view.invalidate(x -xinv, y -yinv, x +xinv, y+ yinv);
		}
	}	
	
	public void crecer(double fac){
		//aplicar un factor de crecimiento para q se vea mas 
		//grande la imagen mientras va seleccionada
		h = (int) (h * fac );
		w = (int) (w * fac );
		
	}
	public void incrementaPos(double factor){		
		//Que no pase del tamaño de la ventana
		//si lega al extremo opuesto, cambiar de direccion
		if (this.modoAndar == NADA) {
			posX += incX * (DIRX) * factor;		
			if (posX < LimX1) {posX = ancho +w; DIRX = -1;}
		}	
		if (this.modoAndar == VUELA) {
			posX += incX * (DIRX) * factor;
			//posY += incY
			if (posX < (LimX1 ) ) {posX = LimX2;}
			if (posX > LimX2) {posX = LimX1;}
		}
		if (this.modoAndar== BRINCA) { 
			posY += incY *(DIRY) * factor *2;	
			posX += incX * DIRX * factor;
			if (posY <= LimY1 ) {DIRY = 1; }
			if (posY >= LimY2 ) {DIRY =-1; }
			if (posX < LimX1) {DIRX = 1;}
			if (posX > LimX2) {DIRX =-1;}
		}
		else {
			posX += incX * (DIRX) * factor;		
			if (posX < LimX1) {DIRX = 1;}
			if (posX > LimX2) {DIRX =-1;}
		}		
		angulo += rotacion * factor;
		
		
	}
	
	public double distancia(ObjetoRio g){
		return Math.hypot(posX -g.posX, posY -g.posY);
	}
	
	public boolean verificaColision(ObjetoRio g) {
		return distancia (g) < radioColision +g.radioColision;
	}
	public double getIncX() {
		return incX;
	}
	public void setIncX(double incX) {
		this.incX = incX;
	}
	public double getIncY() {
		return incY;
	}
	public void setIncY(double incY) {
		this.incY = incY;
	}
	public void setRotacion(int i) {
		rotacion = i;
	}
	
	public int getAncho() {
		return w;
	}
	
	public void setAncho(int ancho) {
		this.w = ancho;
	}
	
	public int getAlto() {
		return h;
	}
	
	public void setAlto(int alto) {
		this.h = alto;
	}
	
	public double getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public void setPosX(double posX) {
		this.posX = posX;
	}
	
	public void setPosY(double posY) {
		this.posY = posY;
	}
	public void setAngulo(float d) {
		this.angulo = d;
	}
	public double getRotacion() {
		return rotacion;
	}
	public void setRotacion(float rotacion) {
		this.rotacion = rotacion;
	}
	public double getAngulo() {
		return angulo;
	}
		
	public boolean fueTocado(int x, int y){
		return drawable.getBounds().contains(x,y);
	}
	
	public void invisible(){
		this.drawable.setVisible(false, false);
		return;
	}
	public void hacerSonido(Sonidos sonido, int numson){
		int son ;
		 son = sonido.load(numson);
		 sonido.play(son);
		return;
	}

	public int getDibujo() {
		return dibujo;
	}

	public void setDibujo(int dibujo) {
		this.dibujo = dibujo;
	}

	public boolean isEsDibujo() {
		return esDibujo;
	}

	public void setEsDibujo(boolean esDibujo) {
		this.esDibujo = esDibujo;
	}

	
	public boolean isEncontrado() {
		return encontrado;
	}

	public void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

	
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
		
}

 class VistaRio extends View {

	private static final long PERIODO_PROCESO = 50;
	private Vector<ObjetoRio> Animales;
	private Vector<ObjetoRio> Barcos;
	private long ultimoProceso = 0;
	private final static int CANTMAX = 10;
	private int anchoV, altoV;
	Paint paint = new Paint();
	
	public VistaRio(Context context) {
		super(context);	  
		Animales = new Vector<ObjetoRio>();
		Barcos = new Vector<ObjetoRio>();	
		}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();		
		for (ObjetoRio animal:Animales) {
			animal.dibujaGrafico(canvas, paint);
		}
		
		for (ObjetoRio barco:Barcos) {
			barco.dibujaGrafico(canvas, paint);
		}		
		canvas.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		this.anchoV = w;
		this.altoV = h;		
		/*for (ObjetoRio animal:Animales){
			animal.setLimites(w,h); //Establecer sus limites y posicion
		} */
		for (ObjetoRio barco:Barcos){
			barco.setLimites(this.anchoV,this.altoV);
		} 
		ultimoProceso =  System.currentTimeMillis();
		hilojuego.start();
	}
	
	
	public void actualizaFisica(){
		long ahora = System.currentTimeMillis();
		 if (ultimoProceso + PERIODO_PROCESO > ahora){
			return ;
		}		
		//Si hay un animal seleccionado no mover los barquitos
		if (haySeleccionado ) return;
		
			//Toast.makeText(JuegoBarcos.this, "en el run del Thread ... " + System.currentTimeMillis(),
				//	Toast.LENGTH_SHORT).show();
		double retardo =  (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;		
		
		//Los barcos si se mueven
		for (ObjetoRio barco: Barcos){
			if (barco.isEncontrado()) {
				ObjetoRio animal = buscaAnimal(barco.dibujo);
				if( barco.getPosX() <= (barco.LimX1 + barco.getAncho()) ) {
	        		animal.setVisible(false);
	        		barco.setVisible(false);
					//Animales.remove(animal);
					//Barcos.remove(barco);
					return;
				}
				if (barco.isVisible()) barco.incrementaPos(retardo);
				if (animal != null)	animal.setPosX(barco.getPosX());
			}
			else 
				barco.incrementaPos(retardo);
		}
		return;
	}	
	
	public ObjetoRio buscaAnimal(int dib)
	{
		for (ObjetoRio animal: Animales){	
			if (animal.dibujo == dib) {
				return animal;				}
			}
		return null;
	}
	
	public ObjetoRio cualTocado(int x, int y)
	{
		for (ObjetoRio animal: Animales){	
			if (animal.fueTocado(x,y)) {
				return animal;				}
			}
		return null;
	}
	
	public ObjetoRio cualBarco(int x, int y)
	{
		for (ObjetoRio barco: Barcos){	
			if (barco.fueTocado(x,y)) return barco;
		}
		return null;
	}
	
	public ObjetoRio addAnimal(int dibujo) {
		if (Animales.size() >= CANTMAX) return null;
		ObjetoRio an =  new ObjetoRio(this, dibujo, ObjetoRio.NO);
		an.setDibujo(dibujo); 
		Animales.add(an);
		return an ;
	}
	
	public ObjetoRio addBarco(int dibujo) {
		if (Barcos.size() >= CANTMAX) return null;
		ObjetoRio an =  new ObjetoRio(this, dibujo,ObjetoRio.NADA);
		an.setDibujo(dibujo); 
		Barcos.add(an);
		return an ;
	}
			
	public Vector<ObjetoRio> getAnimales() {
		return Animales;
	}
	
	public Vector<ObjetoRio> getBarcos() {
		return Barcos;
	}
	 
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

	
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
class ThreadBarcos extends Thread {
	@Override
	public void run() {
		while (true ) {
			vj.actualizaFisica();
		}
	}  	
}
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
