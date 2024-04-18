
package com.ingenieriaycomputacionintegral.juegolectura;

import java.util.Random;
import java.util.Vector;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
//import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class Laberinto extends Activity implements OnTouchListener, OnClickListener{
	protected VistaAcuario vj ;
	protected String silabaBuscada="sa";
	protected int SON = 0; //Sonido buscado
	protected Sonidos sonido;
	private int ancho, alto;
	private String silabas[][]; //cuales silabas debe encontrar(determinar aleatoriamente al inicio del juego
	private Random r;
	int xor=0, yor=0;
	private int ENCONTRADOS = 0, TOTALSILABAS= 10	;
	ObjetoAcuario gPez, gVocal, gBocina;	
	protected int BIEN, APLAUSO, COLOCA;
	
	//Cuando el drawable sea un -1, buscar en el arreglo correspondiente a letras
	protected final static int PEZ=0,TIBURON=1,BURBUJA=2,OBSTACULO=3,VOCAL=-1,LETRA=-2,BOCINA=4;
	protected final static int []IMAGEN= new int[]{
		R.drawable.yfish, R.drawable.tiburon, 
		R.drawable.burbuja, R.drawable.coral, 
		R.drawable.speaker,R.drawable.pez};
	protected final static String []SLETRA =new String[]{"m","s", "p","l"};
	protected final static String []SVOCAL =new String[]{"a","e","i","o","u"};
	protected final static int []IMVOCAL = new int[]{R.drawable.a, R.drawable.e,R.drawable.i,R.drawable.o,R.drawable.u};
	protected final static int []IMLETRAS = new int[]{R.drawable.m, R.drawable.s,R.drawable.p,R.drawable.l};
	
	protected final static int [][]SONIDOS = new int[][]{
		{R.raw.ma, R.raw.me,R.raw.mi,R.raw.mo,R.raw.mu, R.raw.m},
		{R.raw.sa,R.raw.se,	R.raw.si,R.raw.so,R.raw.su,	R.raw.s},
		{R.raw.pa,R.raw.pe,	R.raw.pi,R.raw.po,R.raw.pu,	R.raw.p},
		{R.raw.la,R.raw.le,	R.raw.li,R.raw.lo,R.raw.lu,	R.raw.l} };
	
	MediaPlayer mediaPlayer;
	public ThreadAcuario hilojuego = new ThreadAcuario();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this,R.raw.rainforest);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start();  		
       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		LinearLayout rl = new LinearLayout(this.getBaseContext());		
		rl.setOrientation(LinearLayout.VERTICAL);		
		obtenerAncho();		
		sonido = new Sonidos (this.getBaseContext());
		APLAUSO = sonido.load(R.raw.aplausobien);
		BIEN = sonido.load(R.raw.hitpipe);
		COLOCA = sonido.load(R.raw.colocabarco);
		ENCONTRADOS = 0;
		vj = new VistaAcuario(this.getBaseContext());		
		vj.setOnTouchListener(this);
		vj.setBackgroundResource(R.drawable.aguamar );	
		vj.setLayoutParams(	new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		vj.setAltoV((int) (alto));
		vj.setAnchoV((int) (ancho ));
		r =  new Random();
		
		this.setTitle( " Creando obstaculos");
		for (int i=0; i < 2; i++)  vj.addObstaculo();
		
		
		this.setTitle( " Creando silabas ");		
		silabas = new String[5][5];
		for (int i=0; i < 4; i++) {
			for (int j=0; j < 5; j++) {
				silabas[i][j] = SLETRA[i] + SVOCAL[j];
			}
		}
			

		this.setTitle( " Creando vocales ");
		for (int i=0; i < 5; i++) 	vj.addVocal(i);	
		ObjetoAcuario oa;
		int j,l;
		this.setTitle( " Creando burbujas");
		for (int i=0; i < 20; i++) {
			oa = vj.addBurbuja();
			l = r.nextInt(4);
			j = r.nextInt(5);
			oa.setNombre(silabas[l][j]);
		}
		
		this.setTitle( " Creando peces ");
		for (int i=0; i < 4; i++) {
			vj.addPez(i);  //Numero de letra que trae este pez			
		}
		vj.addTiburon();
		gBocina = vj.addBocina();
		
		rl.addView(vj);
		setContentView( rl );	
		
		cualSilaba();
		Toast.makeText(this, "Forma la silaba que se indica",
				Toast.LENGTH_SHORT).show();
	}
	
	protected String cualSilaba(){
		int ind = r.nextInt(3);
		int i = r.nextInt(3);
		silabaBuscada = this.silabas[ind][i];
		SON =  sonido.load(SONIDOS[ind][i]);  //sonido de la silaba buscada

		//gBocina.setNombre(silabaBuscada);
		//gBocina.setSONIDO(SON);
		return silabaBuscada;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//mediaPlayer.stop();
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
        this.ancho = an; // (int) (an * 0.95f );
        this.alto = al; //(int) (al * 0.95f );
		
		 /*Display display = getWindowManager().getDefaultDisplay();
		    DisplayMetrics outMetrics = new DisplayMetrics ();
		    display.getMetrics(outMetrics);
		    float density  = getResources().getDisplayMetrics().density;
		    float dpHeight = outMetrics.heightPixels / density;
		    float dpWidth  = outMetrics.widthPixels / density;
		    this.ancho = (int)dpWidth;	    this.alto = (int) dpHeight;*/
	}	
	

@Override
public boolean onTouchEvent(MotionEvent event) {
	int x,y;  
	ObjetoAcuario g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();
	switch (eventaction) {
    case MotionEvent.ACTION_DOWN: 
    	g = vj.cualTocado(x,y);	    	
    	if (g != null) {
    		if (g.tipo ==PEZ ){
    			gPez = g;//posicion original donde estaba el animal
    			xor = x;
    			yor = y; 
    			gPez.crecer(1.1);
    		}
    		else if (g.tipo==BURBUJA){
    			g.explotar(); //Implementar el metodo explotar para las burbujas
    			//el metodo explotar no va a eliminar la burbuja, simplemente 
    			//la manda hasta una posicion
    			//muy abajo en y para que tarde un buen rato en volver a subir
    		}else if (g.tipo==LETRA){
    			g.explotar(); //Hacer algo con las letras vocales
    		}else if (g.tipo==BOCINA){
    			g.hacerSonido();
    		}
    	}
        break;
    case MotionEvent.ACTION_MOVE:
    	if (gPez != null) {
    		gPez.setPosX(x);// -gPez.getAncho()/2);
    		gPez.setPosY(y);// -gPez.getAlto()/2 );
    	} 
        break;

    case MotionEvent.ACTION_UP:       	
    	gVocal = vj.cualVocal(x, y);
    	if (gVocal !=null && gPez != null ){
    		// gPez.indice; //obtener el sonido de la silaba formada
    		//if( gVocal.indice == gBuscada ){
    			gPez.setPosX(gVocal.getPosX()); 
        		gPez.setPosY((int) (gVocal.getPosY() -gVocal.getAlto()/4)); 
        		//gPez.setModoAndar(ObjetoAcuario.NADA);
        		gPez.crecer(1/1.2); //Hacerlo mas pequeño
        		gPez.setEncontrado(true);
        		gVocal.setEncontrado(true);

        		gVocal.setIncX(gVocal.getIncX() * 10);
        		sonido.play(BIEN);
        		ENCONTRADOS ++;

        		if(ENCONTRADOS >= TOTALSILABAS){
            		sonido.play(APLAUSO); this.recreate(); }
        		return super.onTouchEvent(event);        		
    		//}
    	}
    	//Regresar al animal a su posicion original
    	if(gPez !=null ) {
    		gPez.setPosX(xor); 
        	gPez.setPosY(yor -gPez.getAlto()/2); 
        	gPez.crecer(1/1.1);
    	}	    
        break;
		}
	
	return super.onTouchEvent(event);
}

@Override
public boolean onTouch(View view, MotionEvent event) {
	int x,y;  
	ObjetoAcuario g;	//grafico tocado
	int eventaction = event.getAction();
	x = (int) event.getX();
	y = (int) event.getY();
	switch (eventaction) {
    case MotionEvent.ACTION_DOWN: 
    	g = vj.cualTocado(x,y);	    	
    	if (g != null) {
    		 if (g.tipo==PEZ){
    			 gPez = g;
	    		//posicion original donde estaba el animal
	    		xor = x;
	    		yor = y; 
	    		gPez.crecer(1.1);
    		 }else if (g.tipo==BURBUJA){
    			g.explotar(); //Implementar el metodo explotar para las burbujas
    			//el metodo explotar no va a eliminar la burbuja, simplemente 
    			//la manda hasta una posicion
    			//muy abajo en y para que tarde un buen rato en volver a subir
    		}else if (g.tipo==LETRA){
    			g.explotar(); //Hacer algo con las letras vocales
    		}else if (g.tipo==BOCINA){
    			g.hacerSonido();
    		}
    	}else gPez = null;
    	
        break;
    }		
	return false;
}

class ObjetoAcuario {
	private static final int MAX_VELOCIDAD = 90;
	private View view;
	private Drawable drawable, drawLetra;
	protected int dibujo;
	protected int indice =0;
	protected int tipo=BURBUJA;	
	private boolean encontrado = false, visible = true;
	private int w, h;  //alto y ancho del dibujo
	private int radioColision;
	private int DIRX=1, DIRY= -1;
	private int posX, posY;
	private int angulo,rotacion;
	private double incX =2, incY =2;
	protected int LimY1, LimY2, LimX1, LimX2;
	private String nombre="";
	//Resources res;
	private int SONIDO; 
	
	//Que tipo es y que letra carga en nombre
	public ObjetoAcuario(View view, int tipo, int indice) {
		Drawable d;
		this.indice = indice;
		this.view = view;
		this.tipo = tipo; //tipo de ObjetoAcuario movil
		//this.drawLetra = view.getContext().getResources().getDrawable(R.drawable.m);
		if (tipo >= 0) { 
			//PEZ=0, TIBURON=1, BURBUJA=2, OBSTACULO=3, LETRA=-1;			
			this.dibujo = IMAGEN[tipo];
			if (tipo == PEZ ){
				this.nombre = SLETRA[indice]; //que letra traera este pez	
				int dib = IMLETRAS[indice];
				this.drawLetra = view.getContext().getResources().getDrawable(dib);
			}
			else this.nombre = " Tipo " + indice;
			
		}else { //Es consonante o vocal ?
			if (tipo == VOCAL){ 
				this.dibujo = IMVOCAL[indice];
				this.nombre = SVOCAL[indice];
			}
			else if (tipo == LETRA ) {
				this.dibujo = IMLETRAS[indice];
				this.nombre = SLETRA[indice];
			}			
		}
		if (this.dibujo == 0)  this.dibujo = R.drawable.burbuja;
		d = view.getContext().getResources().getDrawable(this.dibujo);
		this.drawable = d;
		
		this.ajustarAncho();
		this.setLimites(vj.getAnchoV(),vj.getAltoV()); //Poner tamaño y posicion
		
	}
	
	public void ajustarAncho( ){
		w = drawable.getIntrinsicWidth();
		h = drawable.getIntrinsicHeight();
		//ajustar el tamaño si al imagen es muy grande
		if (tipo != OBSTACULO){
			if (h > alto /10 )h = alto /10;
			if (w > ancho /10 )w = ancho /10;
		}
		if (tipo == TIBURON) {
			h = (int)(h*1.5f); w= (int) (w*1.5f);
		}
		radioColision = (w + h) /4;		
	}

	public void explotar( ){
		//Implementar metodo explotar para las burbujas
		hacerSonido();
		setPosY(this.LimY2 *2 );
		this.w = ancho/10;
		this.h = w;
		return ;
	}
	
	public void setLimites(int anchoP, int altoP){
		//Todos los animales pueden moverse para donde quieran
		LimY1 = 1;
		LimY2 = altoP;
		LimX1 = 1;
		LimX2 = anchoP ;
		DIRX = 0; 
		DIRY = 0;
		this.setIncX(0); //Avance x 
		this.setIncY(0); //Avance en y		
		if (this.tipo == PEZ){ 
			//Los peces estaran del lado izquierdo la pantalla estaticos			
			this.posX = w/2;
			this.posY =(int) LimY1 + (int)( (Math.random()* (LimY2 -LimY1)));
		}
		else if (this.tipo == BURBUJA ){ //Las burbujas solo se mueven verticalmente
			this.setIncX(0); //Math.random()* 4);
			this.setIncY((int) Math.random()* 6);
			DIRX=0; DIRY= -1;
			this.LimX1 = anchoP/10;
			this.LimX2 = anchoP -this.LimX1;
			this.posY = (int) (LimY2*Math.random()); // + LimY2/10; //Mandarla a nacer mas abajo del limite Y2
			this.posX = (int) LimX1 + (int)( (Math.random()* (LimX2 -LimX1)));
			w=anchoP/10; h=w;
			this.setAngulo((int) Math.random()* 180 );
			this.setRotacion((int) Math.random()* 8 -4 );
		}else if (this.tipo == VOCAL ){ 
			//Los vocales estaran del lado derecho de la pantalla			
			this.posX = LimX2 - w;
			this.posY =(int) LimY1 + (int)( (Math.random()* (LimY2 -LimY1)));
		}
		else if (this.tipo == TIBURON ){ 
			DIRY = -1;
			this.setIncY((int) Math.random()* 6);
			//Los tiburones estaran aleatoriamente en la pantalla			
			this.posX = (int) LimX1 + (int)( (Math.random()* (LimX2 -LimX1)));
			this.posY =(int) LimY1 + (int)( (Math.random()* (LimY2 -LimY1)));
			this.setAngulo((int) Math.random()* 180 );
			this.setRotacion((int) Math.random()* 8 -4 );
		}else if (tipo == BOCINA){
			this.posX = LimX2 /2 + LimX1;
			this.posY = h;
		}else{
			this.posX = (int) LimX1 + (int)( (Math.random()* (LimX2 -LimX1)));
			this.posY =(int) LimY1 + (int)( (Math.random()* (LimY2 -LimY1)));
		}
		this.setPosX(posX);
		this.setPosY(posY);	
	}
	
		
	public void dibujaGrafico(Canvas canvas, Paint paint) {		
			canvas.save();
			int wi =w/2, he=  h/2;
			int x = (int) (posX - wi );
			int y = (int) (posY - he );
			if (tipo == TIBURON || tipo == BURBUJA ){ 
				//posY = posY -h/2;
				canvas.rotate((float) angulo, (float)x, (float) y -h/2 );
			}
			drawable.setBounds((int)x,(int)y, (int)(x +w), (int)(y +h));
			drawable.draw(canvas);
			
			if (tipo == PEZ){ //colocar el letrero en la panza del pez
				drawLetra.setBounds((int)(x +wi/2),(int)(y +he/2), (int)(x +wi), (int)(y +he));
				drawLetra.draw(canvas);
			}
			if (tipo==BURBUJA ){ //las burbujas llevan dibujada una silaba
				Paint mTextPaint = new Paint (Paint.ANTI_ALIAS_FLAG);
				final float densityMultiplier = view.getContext().getResources()
				            .getDisplayMetrics().density;  
				mTextPaint.setColor(Color.BLUE);
				mTextPaint.setTextSize(10.0f*densityMultiplier);
				canvas.drawText(this.nombre,x + w/6, y + h/3 , mTextPaint);
			}
			
			canvas.restore();		
			view.invalidate(x, y, x +wi +MAX_VELOCIDAD, y+he+MAX_VELOCIDAD);
	}	
	
	public void crecer(double fac){	//aplicar un factor de crecimiento para cambiar tamaño imagen
		h = (int) (h * fac );
		w = (int) (w * fac );		
	}
	
	public void incrementaPos(double factor){
		
		//Todos estos tipos de ObjetoAcuarios no se deben mover de su lugar
		if (this.tipo == PEZ || this.tipo == VOCAL || this.tipo == OBSTACULO ) {
			return; 
		}	
		if (this.tipo == BURBUJA || this.tipo == TIBURON ) {
			posY += incY * (DIRY) * factor;
			if (posY <= LimY1 ) {posY = LimY2 + LimY2/5;}
			if (posY >= LimY2 ) {posY = LimY1; }
			//Que vaya creciendo la burbuja
			this.crecer(1.005);
		}
		
		if (this.tipo == TIBURON) {
			//posX += incX
		}
		angulo += rotacion * factor;
		if (posY <= LimY1 ) {posY = LimY2 + LimY2/5;}
		if (posY >= LimY2 ) {posY = LimY1; }
		
	}
	
	public double distancia(ObjetoAcuario g){
		return Math.hypot(posX -g.posX, posY -g.posY);
	}
	
	public boolean verificaColision(ObjetoAcuario g) {
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
	
	public int getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public void setAngulo(int d) {
		this.angulo = d;
	}
	public int getRotacion() {
		return rotacion;
	}
	public int getAngulo() {
		return angulo;
	}
		
	public boolean fueTocado(int x, int y){
		return drawable.getBounds().contains(x,y);
	}
	
	public void invisible(){
		this.drawable.setVisible(false, false);
		return;
	}
	
	public void hacerSonido(){
		 sonido.play(SONIDO);
		return;
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

	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getSONIDO() {
		return SONIDO;
	}

	public void setSONIDO(int s) {
		SONIDO = sonido.load(s);
	}
		
}

 class VistaAcuario extends View {

	private static final long PERIODO_PROCESO = 100;
	private Vector<ObjetoAcuario> Peces;
	private Vector<ObjetoAcuario> Vocales;
	private Vector<ObjetoAcuario> Burbujas;
	private Vector<ObjetoAcuario> Obstaculos;
	private ObjetoAcuario tiburon, bocina, puntaje;
	private long ultimoProceso = 0;
	private long puntos = 0;
	//private final static int CANTMAX = 10;
	private int anchoV, altoV;
	Paint paint = new Paint();
	
	public VistaAcuario(Context context) {
		super(context);	  
		Peces = new Vector<ObjetoAcuario>();
		Vocales = new Vector<ObjetoAcuario>();
		Burbujas = new Vector<ObjetoAcuario>();
		Obstaculos = new Vector<ObjetoAcuario>();	
		}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();	
		for (ObjetoAcuario o:Obstaculos) {
			o.dibujaGrafico(canvas, paint);
		}
		for (ObjetoAcuario vocal:Vocales) {
			vocal.dibujaGrafico(canvas, paint);
		}
		for (ObjetoAcuario animal:Peces) {
			animal.dibujaGrafico(canvas, paint);
		}
		for (ObjetoAcuario b:Burbujas) {
			b.dibujaGrafico(canvas, paint);
		}
		tiburon.dibujaGrafico(canvas, paint);
		bocina.dibujaGrafico(canvas, paint);
		canvas.restore();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);		
		this.anchoV = w;
		this.altoV = h;		
		for (ObjetoAcuario animal:Peces){
			animal.setLimites(w,h); //Establecer sus limites y posicion
		} 
		for (ObjetoAcuario v:Vocales){
			v.setLimites(this.anchoV,this.altoV);
		} 
		for (ObjetoAcuario b:Burbujas) {
			b.setLimites(this.anchoV,this.altoV);
		}
		tiburon.setLimites(this.anchoV, this.altoV);
		
		ultimoProceso =  System.currentTimeMillis();
		hilojuego.start();
	}
	
	
	public void actualizaFisica(){
		
		long ahora = System.currentTimeMillis();
		 if (ultimoProceso + PERIODO_PROCESO > ahora){
			return ;
		}
		double retardo =  (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;		
		
		for (ObjetoAcuario burbuja: Burbujas){
			burbuja.incrementaPos(retardo);
			burbuja.crecer(1.005);
		}
		tiburon.incrementaPos(retardo);
		
		return;
	}	
	
		public ObjetoAcuario cualTocado(int x, int y)
	{
		for (ObjetoAcuario animal: Peces){	
			if (animal.fueTocado(x,y)) {
				return animal;				}
			}
		for (ObjetoAcuario burb: Burbujas){	
			if (burb.fueTocado(x,y)) {
				return burb;				}
			}
		for (ObjetoAcuario v: Vocales){	
			if (v.fueTocado(x,y)) return v;	
			}
		if (gBocina.fueTocado(x, y)) return gBocina;
		return null;
	}
	
	public ObjetoAcuario cualVocal(int x, int y)
	{
		for (ObjetoAcuario b: Vocales){	
			if (b.fueTocado(x,y)) return b;
		}
		return null;
	}
	
	public ObjetoAcuario addPez(int i) {
		if (Peces.size() >= 5 ) return null;
		ObjetoAcuario an =  new ObjetoAcuario(this, PEZ,Peces.size());
		Peces.add(an);
		return an ;
	}
	
	public ObjetoAcuario addVocal(int i) {
		if (Vocales.size() > 5) return null;
		ObjetoAcuario an =  new ObjetoAcuario(this, VOCAL, i);
		Vocales.add(an);
		return an ;
	}
	public ObjetoAcuario addBurbuja( ) {
		if (Burbujas.size() >= 20 ) return null;
		ObjetoAcuario an =  new ObjetoAcuario(this,BURBUJA, Burbujas.size());
		Burbujas.add(an);
		return an ;
	}
	public ObjetoAcuario addObstaculo( ) {
		if (Obstaculos.size() >= 20 ) return null;
		ObjetoAcuario an =  new ObjetoAcuario(this,OBSTACULO, Obstaculos.size());
		Obstaculos.add(an);
		return an ;
	}
	
	public ObjetoAcuario addTiburon() {
		tiburon =  new ObjetoAcuario(this, TIBURON, 1 );
		return tiburon ;
	}
	
	public ObjetoAcuario addBocina() {
		bocina =  new ObjetoAcuario(this, BOCINA, 1 );
		return bocina ;
	}
	
	public Vector<ObjetoAcuario> getAnimales() {
		return Peces;
	}
	
	public Vector<ObjetoAcuario> getVocales() {
		return Vocales;
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

/**
 * @return the puntos
 */
public long getPuntos() {
	return puntos;
}

/**
 * @param puntos the puntos to set
 */
public void setPuntos(long puntos) {
	this.puntos = puntos;
}

	
}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
class ThreadAcuario extends Thread {
	@Override
	public void run() {
		while (true ) {
			vj.actualizaFisica();
		}
	}  	
}
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

/* (non-Javadoc)
 * @see android.view.View.OnClickListener#onClick(android.view.View)
 */
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	
}

}
