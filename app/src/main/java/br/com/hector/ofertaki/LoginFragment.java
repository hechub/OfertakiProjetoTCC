package br.com.hector.ofertaki;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginFragment extends Fragment {

	private EditText edtUsuario, edtSenhaLogin;
	private Button btnEntrar;
	private TextView txtNovoUsuario, txtEsqueciMinhaSenha;
	private ProgressDialog progress;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.login, container, false);
		edtUsuario = (EditText)v.findViewById(R.id.edtUsuario);
		edtSenhaLogin = (EditText)v.findViewById(R.id.edtSenhaLogin);
		btnEntrar = (Button)v.findViewById(R.id.btnEntrar);
		txtNovoUsuario = (TextView)v.findViewById(R.id.txtNovoCadastro);
		txtEsqueciMinhaSenha = (TextView)v.findViewById(R.id.txtEsqueciMinhaSenha);
		
		//Parse.initialize(getActivity(),
			//	"uUmWwZ0x0b31l93s3JHKtm4quy8Rlhg3jGU1Mi5b",
				//"56OKLdkeRv2r0YEV8axRi6XR5ONI4PsckwdD3Re9");
		//Parse.enableLocalDatastore(getActivity());
		
		progress = new ProgressDialog(getActivity());
		
		btnEntrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String usuario = edtUsuario.getEditableText().toString();
				String senha = edtSenhaLogin.getEditableText().toString();
				
				progress.show();
				progress.setMessage("Validando Login");
				progress.setCancelable(false);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.show();
				
				
				ParseUser.logInInBackground(usuario,senha, new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					      progress.dismiss();	
					      FragmentManager fm = getFragmentManager();
					      fm.popBackStack();
					      Toast.makeText(getActivity(), "Logado com Sucesso", Toast.LENGTH_SHORT).show();
					      
					      
					    } else {
					      progress.dismiss();	
					      Toast.makeText(getActivity(), "Erro no Login", Toast.LENGTH_SHORT).show();;
					      e.printStackTrace();
					    }
					  }
					});
				
			}
		});
		
		txtNovoUsuario.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				Fragment fragment = new CadastroLojaFragment();
				
				fm.beginTransaction().replace(R.id.frameMapa, fragment).addToBackStack(null).commit();
				
			}
		});
		
		txtEsqueciMinhaSenha.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String email = edtUsuario.getEditableText().toString();
				
				if(!email.equals("")){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

					builder.setMessage("Sua nova senha será enviada para "+email)
					       .setTitle("Esqueci Minha Senha");
					// Add the buttons
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               // User clicked OK button
					        	   ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
					        		   public void done(ParseException e) {
					        		     if (e == null) {
					        		       // An email was successfully sent with reset instructions.
					        		    	 Toast.makeText(getActivity(), "Email enviado com sucesso", Toast.LENGTH_SHORT).show();
					        		     } else {
					        		       // Something went wrong. Look at the ParseException to see what's up.
					        		     }
					        		   }
					        		 });
					           }
					       });
					builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               // User cancelled the dialog
					           }
					       });
					
					AlertDialog dialog = builder.create();
					dialog.show();
					
					
				}else{
					Toast.makeText(getActivity(), "Digite o email para recuperar a senha", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
		});
		
		
		return v;
		
	}
	

}
