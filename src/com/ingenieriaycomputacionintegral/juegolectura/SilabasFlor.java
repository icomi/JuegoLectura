package com.ingenieriaycomputacionintegral.juegolectura;

import java.util.Vector;

import com.ingenieriaycomputacionintegral.juegolectura.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color; 
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class SilabasFlor extends Activity implements OnClickListener, OnTouchListener {
	private final static int [][]SONIDOS = new int[][] {
		{R.raw.ma,R.raw.me,	R.raw.mi,R.raw.mo,R.raw.mu, R.raw.m},
		{R.raw.sa,R.raw.se,	R.raw.si,R.raw.so,R.raw.su,	R.raw.s},
		{R.raw.pa,R.raw.pe,	R.raw.pi,R.raw.po,R.raw.pu,	R.raw.p},
		{R.raw.la,R.raw.le,	R.raw.li,R.raw.lo,R.raw.lu,	R.raw.l} };
	private final static char[]SILABAS = new char[] {'m','s','p','l'};
	private final static char[]VOCAL = new char[] {'a','e','i','o','u',' '};	
	private final static int[]COLORES = new int []{Color.RED,Color.MAGENTA, Color.CYAN, Color.GREEN};
	private Sonidos sonido;
	protected int ancho, alto;
	protected VistaFlor vf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sonido = new Sonidos(this.getBaseContext());		
		
		setContentView(R.layout.activity_main); 		
		ScrollView  sv = new ScrollView(this);
		LinearLayout ll =new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		obtenerAncho();	
		vf = new VistaFlor(this.getBaseContext());
		vf.setOnTouchListener(this);
		vf.refreshDrawableState();	
		ll.addView(vf);
		ll.setBackgroundResource(R.drawable.fondo);	
		sv.addView(ll);
		setContentView(sv); 
		//Toast.makeText(this.getBaseContext(), " Creada vista ", Toast.LENGTH_SHORT).show();
		
	}
	
	private void obtenerAncho(){
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.ancho = dm.widthPixels /5;
        this.alto = dm.heightPixels /3 ;    
	}
	
	 public void onClick(View view){
		 Intent data = new Intent();
		 data.setData(null);
		 setResult(RESULT_OK,data);		 
		 //Terminar esta actividad
		 finish();
	 }
	
private class Petalo extends ShapeDrawable {
	private String silaba;
	private int archivosonido;
	private int colorP;
	private int posX, posY, radio;
	private boolean centro =false;
	
	Petalo(int sil, int vocal, int colorP, boolean centro, int i, int j, int tam){
		super(new OvalShape());		
		this.silaba= String.valueOf(SILABAS[sil]) + VOCAL[vocal];
		this.colorP = colorP;
		this.centro = centro;
		this.posX= i;
		this.posY= j;
		this.radio = tam;
		this.getPaint().setColor(colorP);
		this.getPaint().setShadowLayer(10, 2, 2, Color.GRAY);
		this.setBounds(i ,j, i + tam*2, j + tam*2 );		
		this.archivosonido = sonido.load(SONIDOS[sil][vocal]);
	}
	
	public boolean fueTocado(int x, int y){
		return this.getBounds().contains(x,y);
	}
	
	public void hacerSonido(){
		 sonido.play(this.archivosonido);
		return;
	}
	
	public void dibuja(Canvas canvas, Paint paint) {
		canvas.save();
		this.setBounds(this.posX ,this.posY, this.posX + this.radio*2, this.posY +this.radio*2 );		
		this.draw(canvas);		
		//color del texto
		paint.setColor(Color.BLACK);
		paint.setTextSize(28);
        paint.setShadowLayer(5, 0, 0, Color.GRAY);
		canvas.drawText(this.silaba, this.posX + this.radio/2,(this.posY + this.radio ), paint);
		canvas.restore();  //Restablecer el canvas ¿para que?
	}

}

private class Flor {
	float cx, cy, radius;
	Vector<Petalo> petalos;
	private int numPetalos;
	String silaba;
	private int colorP;
	private boolean minim = false;
	
	public Flor(int sil, int cx, int cy, int tam) {
		int nump = 5;
		this.colorP = COLORES[sil];
		this.numPetalos = nump;
		this.silaba = ""+ SILABAS[sil] ;		
		float ang = 1.2566f; //72ª radianes para 5 petalos
		float radiohoja, incx, incy ;
		boolean centro = false;		
		//Encontrar 5 centros que serian las aristas del pentagono		// 72ª
		ang = (float) (((360 /nump) * Math.PI ) / 180); //Tamaño del angulo
		int x,y;
        this.cx = cx; 
		this.cy = cy; 		
		radius = tam/2;
		radiohoja = radius * 0.7f;		
		petalos = new Vector<Petalo>(); 	
		for(int i=0; i<=numPetalos; i++){ //Serian 6 considerando el centro
			incx = (float) (radius * Math.cos(ang * i));
			incy = (float) (radius * Math.sin(ang * i));
			x= (int) (cx+incx -radiohoja);
			y= (int) (cy+incy -radiohoja);
			if (i == 5) { //Centro de la flor ?
				radiohoja= radiohoja * 0.7f;
				x = (int) (cx -radiohoja); y = (int) (cy - radiohoja);
				centro = true;
			}
			Petalo petalo = new Petalo(sil, i, colorP, centro, x, y, (int) radiohoja);
			petalos.add(petalo);
		}
	}
	
	void dibujaPetalos(Canvas canvas, Paint paint){
		if (this.minim ) {			//
			for (Petalo petalo:petalos) { //solo dibujar el centro, cuando la flor este minimizada
				if (petalo.centro){
					petalo.getPaint().setColor(Color.YELLOW);
					petalo.dibuja(canvas, paint);
					petalo.getPaint().setColor(petalo.colorP);
				}
			}
		}
		else{ 	
			for (Petalo petalo:petalos) {
				petalo.dibuja(canvas, paint);
			}
		}
	}
	 	
	}

	private class VistaFlor extends View {
		Paint paint = new Paint();;
		Vector<Flor> flores;
		protected int numFlores =4;
			
		public VistaFlor(Context context) {
			super(context);				
			this.setBottom(100);
			int x = 100;
			int y = alto/2;
			flores = new Vector<Flor>(); 
			//añadiendo las flores en minimizado
			for (int i=0; i<numFlores; i++ ){
				Flor flor = new Flor(i, (i +1)* x, y, 200);
				flores.add(flor);
				
			}
		}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		for (Flor flor:flores) {
			flor.dibujaPetalos(canvas,paint);	
			canvas.drawText(flor.silaba, flor.cx ,flor.cy, paint );
		}
		canvas.drawText("Flores on draw", 200 ,400, paint );
		canvas.restore();
	}
}
	
	  public boolean onTouchEvent(MotionEvent event) {
	    int x = (int) event.getX();
	    int y = (int) event.getY();
	    
	    Toast.makeText(this.getBaseContext(), x+ " tocado en" + y, Toast.LENGTH_SHORT).show();
	    //¿Cual petalo fue tocado? Reproducir su sonido
	    for (Flor flor:vf.flores) {
		    for (Petalo petalo:flor.petalos ) {
				if (petalo.fueTocado(x,y)) {
					petalo.hacerSonido();
					if (petalo.centro) flor.minim = false;
					return true;
				}
		    }
		}
	    return false;
	  
	  }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		Toast.makeText(this.getBaseContext(), x+ " tocado en" + y, Toast.LENGTH_SHORT).show();
	    //¿Cual petalo fue tocado? Reproducir su sonido
	    for (Flor flor:vf.flores) {
		    for (Petalo petalo:flor.petalos ) {
				if (petalo.fueTocado(x,y)) {
					petalo.hacerSonido();
					if (petalo.centro) flor.minim = false;
					return true;
				}
		    }
		}
		return false;
	}	  	
	}

