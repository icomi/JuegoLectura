package com.ingenieriaycomputacionintegral.juegolectura;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends Activity implements OnClickListener{
	protected Button[] boton;
	protected int ancho, alto;
	private final static String []LETRAS = new String[] {
		"Memorama",	"Nombres","Barcos",	"Laberinto", "Estanque"};
	private final static int []SONIDOS = new int[] {
		R.raw.memorama,	R.raw.nombres,R.raw.barcos,R.raw.excellent,R.raw.estanque};
	int SON[];
	private Sonidos sonido;
	//private final static Class clases[]={Memorama.class, Emparejamiento.class};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 		
		obtenerAncho();
		boton = new Button[5];
		sonido= new Sonidos(this.getBaseContext());
		SON = new int[4];
		for (int i=0; i<4; i++){
			SON[i] = sonido.load(SONIDOS[i]);
		}
		final TableLayout table = new TableLayout(this) ;	
		for(int i=0; i<=4; i++){
			boton[i] = new Button(this.getBaseContext());
			boton[i].setLayoutParams(new TableRow.LayoutParams(ancho -10,alto -10)) ;
			boton[i].setText(LETRAS[i]);
			boton[i].setAlpha(25);
			boton[i].setOnClickListener(this);
			final TableRow fila = new TableRow(this) ;	
			fila.addView(boton[i]);
			table.addView(fila);
		}
		//boton[0].setBackgroundResource(R.drawable.flor);
		table.setBackgroundResource(R.drawable.bosque);
		setContentView(table);
	        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void obtenerAncho(){
	 	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.ancho = (int)((dm.widthPixels) * 0.95f);
        this.alto = (int) ((dm.heightPixels/5) * 0.95f);
    
	}
	private void ponerSonidoFondo(){		
		/*MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(this,R.raw.cancion);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start(); //Sonido de fondo en la aplicacion */ 
		return;
	}

	@Override
	public void onClick(View v) {
	
		if (v.equals(boton[0])) {
			this.setTitle(boton[0].getText() + "iniciando ...");
			sonido.play(SON[0]);
			Intent i = new Intent(this, Memorama.class);
			startActivity(i);
			return;		}
		if (v.equals(boton[1])){
			sonido.play(SON[1]);
			this.setTitle("Emparejar Nombres...");
			Intent e = new Intent(this, Emparejamiento.class);
			startActivity(e);
			return;
		}
		if (v.equals(boton[2])){
			this.setTitle(boton[2].getText() + "iniciando ...");
			sonido.play(SON[2]);
			Intent e2 = new Intent(this, JuegoBarcos.class);
			startActivity(e2);
			return;
		}
		if (v.equals(boton[3])){
			this.setTitle(boton[3].getText() + " iniciando ...");
			sonido.play(SON[3]);
			Intent e3 = new Intent(this, Laberinto.class);
			startActivity(e3);
			return;
		}
		if (v.equals(boton[4])){
			sonido.play(SON[3]);
			this.setTitle("Estanque");
			Intent e = new Intent(this, Estanque.class);
			startActivity(e);
			return;
		}
		return ;
	}
		
	



}
