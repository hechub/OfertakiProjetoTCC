package br.com.hector.ofertaki;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends ControlaFragmento {
	private String path;
	private Bitmap bitmap;
	
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		
		
		SharedPreferences pref = getApplication().getSharedPreferences("br.com.ofertaki.preferencias", Context.MODE_PRIVATE);
		Boolean primeiroAcesso = pref.getBoolean("br.com.ofertaki.primeiroacesso", true);
		
		if(primeiroAcesso){
			return new MapaFragment();
		}else{
			return null;
		}
		
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		//Parse.initialize(this, "uUmWwZ0x0b31l93s3JHKtm4quy8Rlhg3jGU1Mi5b", "56OKLdkeRv2r0YEV8axRi6XR5ONI4PsckwdD3Re9");
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	    outState.putString("filepath", path );
	    outState.putParcelable("bitmap", bitmap);
	    super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    path = state.getString("filepath");
	    bitmap = state.getParcelable("bitmap");
	    CadastroOfertaFragment.imvImagemOferta.setImageBitmap(bitmap);
	    super.onRestoreInstanceState(state);
	}
	
	private File savebitmap(Bitmap bmp){
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;
		File file = new File(extStorageDirectory,"temp.png");
		if(file.exists()){
			file.delete();
			file = new File(extStorageDirectory,"temp.png");
		}
		
		try{
			outStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static Bitmap redimensionaBitmap (Bitmap b){
		int nh = (int) ( b.getHeight() * (256.0 / b.getWidth()) );
		Bitmap scaled = Bitmap.createScaledBitmap(b, 256, nh, true);
		return scaled;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			if (requestCode == 1){
				File f = new File(Environment.getExternalStorageDirectory().toString());
				for(File temp : f.listFiles()){
					if (temp.getName().equals("temp.jpg")){
						f = temp;
						File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
						break;
					}
				}
				
				
			try{
				//Bitmap bitmap;
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
				//aplicar imagem ao imageview aki
				CadastroOfertaFragment.imvImagemOferta.setImageBitmap(bitmap);
				
				
				path = android.os.Environment.getExternalStorageDirectory()+File.separator+"Oferta"+File.separator+"default";
				f.delete();
				OutputStream outFile = null;
				
				File file = new File(path,String.valueOf(System.currentTimeMillis())+".jpg");
				try {
					outFile = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
					outFile.flush();
					outFile.close();
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
				
			}
		}else if (requestCode == 2){
			Uri selectedImage = data.getData();
			
			String [] filePath = {MediaStore.Images.Media.DATA};
			Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePath[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
			
			Log.w("path of image from gallery----------",picturePath+"");
			
			//colocar imagem no ImageView aki
			CadastroOfertaFragment.imvImagemOferta.setImageBitmap(thumbnail);
			
			
			
		}}
	}
}