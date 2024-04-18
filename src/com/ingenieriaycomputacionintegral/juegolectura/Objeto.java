package com.ingenieriaycomputacionintegral.juegolectura;

import com.ingenieriaycomputacionintegral.juegolectura.R;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Objeto {

	private static final int MAX_VELOCIDAD = 50;
	private View view;
	private Drawable drawable;
	private boolean reflejada = false;
	private boolean esDibujo =true;
	//private ImageView imagenR = null; 
	private int ancho, alto;  //alto y ancho del dibujo
	private int radioColision;
	private int dibujo;
	private int DIRX=1, DIRY= -1;
	private double posX, posY;
	private double angulo,rotacion;
	private double incX, incY;
	private Sonidos sonido;
	public final static int NADA = 1,VUELA = 2;
	public final static int BRINCA = 3;
	public final static int CAMINA = 4, NO=5;
	protected int LimY1, LimY2, LimX1, LimX2;
	String nombre="";
	private int modoAndar = 2; //no definido, debe nadar, brincar, caminar o volar
	Resources res;
	
	public Objeto(View view, Drawable drawable, int modo, String nombre) {
		this.view = view;
		this.drawable = drawable;
		this.modoAndar = modo;
		this.nombre = nombre;
		
		if (drawable == null) {
			this.drawable =view.getContext().getResources().getDrawable(R.drawable.bt);
			setEsDibujo(false);  //no es un dibujo, es un texto
		}
		ancho = drawable.getIntrinsicWidth();
		alto = drawable.getIntrinsicHeight();
		
		this.setIncX(Math.random()* 4);
		this.setIncY(Math.random()* 4);
		this.setAngulo((int) Math.random()* 180 );
		this.setRotacion((int) Math.random()* 8 -4 );
		radioColision = (alto + ancho) /4;			
		this.setLimites(view.getWidth(),view.getHeight());
	}
	
	public void setLimites(int ANCHO, int ALTO){		
		//¿Es un animal que nada?
		if (this.modoAndar == NADA ){
			LimY1 =(int)(ALTO * 0.7);
			LimY2 = ALTO;
			LimX1 = ancho/2;
			LimX2 =(int)(ANCHO -ancho/2 );
		}
		//¿Es un animal que vuela?
		if (this.modoAndar == VUELA ){
			LimY1 = 1;
			LimY2 =(int)(ALTO /10);
			LimX1 = 1;
			LimX2 =(int)(ANCHO -ancho/2);
		}
		//¿Es un animal que camina?
		if (this.modoAndar == CAMINA ){
			LimY1 = alto; //(int)(ALTO /3);
			LimY2 =(int)(ALTO * 0.5);
			LimX1 = ancho/2;
			LimX2 =(int)(ANCHO -ancho/2);
		}
		if (this.modoAndar == NO ){
			LimY1 =1 ;
			LimY2 = alto*2;
			LimX1 = ancho/2;
			LimX2 =(int)(ANCHO -ancho/2 );
		}else {
			LimY1 = (int)(ALTO * 0.7);
			LimY2 = (int)(ALTO * 0.7) + alto*2; //los que brincan
			LimX1 = 1 ; //ancho/2;
			LimX2 =(int)(ANCHO -ancho/2 );
			this.setIncY(this.getIncY()* 3 );
			this.setIncX(this.getIncX() +2 );
		}
		posX = (int) (Math.random()* LimX2 );
		posY = (int) (Math.random()* LimY2 );
		if (posX < LimX1) { posX = LimX1; }
		if (posY < LimY1 || posY > LimY2) { posY = LimY1 +alto; }
		if (posX > LimX2) { posX = LimX2 -ancho; }
		
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
		canvas.save();
		int x = (int) (posX - ancho/2 );
		int y = (int) (posY - alto/2 );		
		
		if (modoAndar == VUELA ){ 
			canvas.rotate((float) angulo, (float)x, (float) y );
		}	
		if (esDibujo) {
			drawable.setBounds((int)x,(int)y, (int)(posX +ancho), (int)(posY +alto));
			if (reflejada){}
			else	drawable.draw(canvas);
		}
		else{
			canvas.drawCircle(x -ancho/2, y -alto/2, ancho, paint);
			canvas.drawText(this.nombre, x, y, paint);
		}		
		
		canvas.restore();		
		int rInval = (int) Math.hypot(ancho, alto)/2 + MAX_VELOCIDAD;
		view.invalidate(x -rInval, y -rInval, x +rInval, y+rInval);
		
	}	
	
	public void incrementaPos(double factor){		
		//Que no pase del tamaño de la ventana
		//si lega al extremo opuesto, cambiar de direccion
		if (this.modoAndar == NADA) {
			posX += incX * (DIRX) * factor;		
			if (posX < LimX1) {DIRX = 1;}//posX = ancho;}
			if (posX > LimX2) {DIRX =-1;}
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
		
		/*if (DIRX == -1) {
			this.reflejar(imagenR);
		}*/
		
	}
	
	public double distancia(Objeto g){
		return Math.hypot(posX -g.posX, posY -g.posY);
	}
	
	public boolean verificaColision(Objeto g) {
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
		return ancho;
	}
	
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	
	public int getAlto() {
		return alto;
	}
	
	public void setAlto(int alto) {
		this.alto = alto;
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
	public static int getMaxVelocidad() {
		return MAX_VELOCIDAD;
	}
	public Sonidos getSonido() {
		return sonido;
	}
	public void setSonido(Sonidos sonido) {
		this.sonido = sonido;
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
	
	public ImageView reflejar(ImageView imagenR){
		final int refGap = 4;
		
		res = view.getContext().getResources();
		Bitmap orImage = BitmapFactory.decodeResource(res,this.dibujo);
		int width = orImage.getWidth();
		int height = orImage.getHeight();
		
		Matrix matrix = new Matrix();
		//Poner una escala de -1 en Y y con esto los valores se copian pero de manera inversa
		//y se logra el efecto de reflejo, si se va reflejar horizontalmente se pondria X=-1
		matrix.preScale(1, -1); 
		Bitmap refImage = Bitmap.createBitmap(orImage, 0, height/2, width, height/2, matrix, false);
		Bitmap bitmRef = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmRef);
		canvas.drawBitmap(orImage, 0, 0, null);
		Paint defaultPaint = new Paint();
		
		canvas.drawRect(0, height, width, height + refGap, defaultPaint);
		canvas.drawBitmap(refImage,0, height + refGap, null);
		Paint paint = new Paint();
		
		//Se aplica un gradiente alfa para que la imagen reflejada se vea
		//mas transparente al final
		LinearGradient shader = new LinearGradient(0, orImage.getHeight(), 0,
				bitmRef.getHeight() + refGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		
		canvas.drawRect(0, height, width, bitmRef.getHeight() + refGap, paint);
		
		//poner la imagen reflejada en el ImageView que se mando como parametro
		imagenR.setImageBitmap(bitmRef);
		
		reflejada = true;
		return imagenR;
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
	
	
}
