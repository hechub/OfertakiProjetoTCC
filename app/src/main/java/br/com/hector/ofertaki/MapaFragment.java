package br.com.hector.ofertaki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,LocationListener{

	private Geocoder geocoder;
	private static GoogleMap map;
	private MapView mapView;
	MenuDeslizante telaPrincipal;
	private Button btnMenu, btnOpcao;
	private HashMap <Marker, Oferta> gradeOfertaMarcador;
	private Handler handler;
	private LatLng RUA, localAtual;
	private ArrayList<Oferta> listaOfertas;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private LocationRequest mLocationRequest;
	private boolean mRequestingLocationUpdates = true;
	private AlertDialog.Builder builder;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Make sure fragment codes match up 
	    if (resultCode == BuscaDialog.REQUEST_CODE_ENDERECO) {
	        String query = data.getStringExtra("query");
	        stopLocationUpdates();
	        atualizaPosicao(query);
	        
	    }else if (resultCode == BuscaDialog.REQUEST_CODE_TITULO){
	    	Fragment f = new ListaResultadoBuscaOfertas();
			FragmentManager fm = getFragmentManager();

			Bundle b = new Bundle();
			b.putString("tituloQuery", data.getStringExtra("query"));
			f.setArguments(b);
			fm.beginTransaction().replace(R.id.frameMapa, f).addToBackStack(null).commit();
	    
	    }
	    }
	
	private void atualizaPosicao (String query){
		
		try {
			List<Address> addresses;
			geocoder = new Geocoder(getActivity());
			addresses = geocoder.getFromLocationName(query, 1);
			if(addresses.size() > 0) {
			    RUA = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
			    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(RUA,18);
			    map.animateCamera(update);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getActivity(), "Endereço não Encontrado", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()){
			case R.id.acao_busca:
				DialogFragment dialog = new BuscaDialog();
				dialog.setTargetFragment(this,0);
				dialog.show(getChildFragmentManager(), "dialog busca");
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void createLocationRequest(){
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(8000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		localAtual = new LatLng(location.getLatitude(), location.getLongitude());	
		atualizaCamera(localAtual);
		
	}
	
	protected synchronized void buildGoogleApiCliente(){
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
										.addConnectionCallbacks(this)
										.addOnConnectionFailedListener(this)
										.addApi(LocationServices.API)
										.build();
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		
		createLocationRequest();
		//Toast.makeText(getActivity(), "Conectado",Toast.LENGTH_LONG).show();
		
		
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		
		if(mLastLocation != null){
			localAtual = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
			atualizaCamera(localAtual);
		}
		
		if(mRequestingLocationUpdates) {
			startLocationUpdates();
		}
		
	}
	
	public void startLocationUpdates(){
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}
	
	protected void stopLocationUpdates(){
		mRequestingLocationUpdates = false;
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Impossível carregar sua posição, verifique o GPS", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Impossível carregar sua posição, verifique o GPS", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		buildGoogleApiCliente();
		super.onCreate(savedInstanceState);
	}
	
	
	//Método para carregar ofertas no mapa.
	public static Marker Marcarmapa(Oferta o){		
			Marker m = map.addMarker(new MarkerOptions()
			.position(o.getGeoPoint())
			.title(o.getTitulo_oferta())
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_principal)));		
			return m;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			View v = inflater.inflate(R.layout.fragmento_mapa, container, false);

	        mapView = (MapView) v.findViewById(R.id.map);
	        mapView.onCreate(savedInstanceState);
	        map = mapView.getMap();
	        map.getUiSettings().setMyLocationButtonEnabled(true);
	        map.setMyLocationEnabled(true);
	        
	        try {
	            MapsInitializer.initialize(this.getActivity());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
				
				@Override
				public boolean onMyLocationButtonClick() {
					startLocationUpdates();
					return false;
				}
			});
	        
	        if (Validacoes.isInternet(getActivity()) && Validacoes.isGpS(getActivity())) {
	        	carregaInformacaoMapa task = new carregaInformacaoMapa();
				mGoogleApiClient.connect();
				task.execute();
				
	        	
			}else{
				builder = new AlertDialog.Builder(getActivity());
				
				builder.setMessage("O GPS está desligado ou você não possui uma conexão de internet ativa, tente novamente")
						.setTitle("Erro na conexão");
				
				builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								getActivity().finish();
								
							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		return v;
		
	}
	
	
	private void atualizaCamera(LatLng novoLocal){
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(novoLocal, 18);
		map.animateCamera(cameraUpdate);
		
	}
	
	private class carregaInformacaoMapa extends AsyncTask<Void,Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			
			listaOfertas = new ArrayList<Oferta>();
			listaOfertas = Oferta.getTodasOfertas();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			gradeOfertaMarcador = new HashMap<Marker, Oferta>();
			for(Oferta o : listaOfertas){					
				Marker mark = MapaFragment.Marcarmapa(o);
				gradeOfertaMarcador.put(mark, o);	
				
				map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker marker) {
						
						
						Oferta o = gradeOfertaMarcador.get(marker);			
						
						Intent i = new Intent(getActivity(),PagerDetalharOfertaActivity.class);
						i.putExtra("ofertaSelecionada", o.getId());
						startActivity(i);
						
						
					}
				});
			}
		}
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
			stopLocationUpdates();
		}
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
	}
	
	@Override
    public void onResume() {
		super.onResume();
        mapView.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
       
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}


