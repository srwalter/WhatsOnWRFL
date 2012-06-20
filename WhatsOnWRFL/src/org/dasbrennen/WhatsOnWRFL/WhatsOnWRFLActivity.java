package org.dasbrennen.WhatsOnWRFL;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.widget.EditText;
import android.widget.TextView;

class NowPlayingData {
	String onAirDJ;
	String nowPlaying;
}

public class WhatsOnWRFLActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		class GetNowPlayingTask extends AsyncTask<Void, Void, NowPlayingData> {
			@Override
			protected NowPlayingData doInBackground(Void... arg0) {
				NowPlayingData data = null;
				
				try {
					org.jsoup.nodes.Document doc = Jsoup.connect("http://wrfl.fm").get();
					org.jsoup.nodes.Element whatsplayingElem = doc.getElementById("whatsplaying");
					org.jsoup.nodes.Element djnameElem = doc.getElementById("djname");
					
					data = new NowPlayingData();
					data.onAirDJ = whatsplayingElem.text();
					data.nowPlaying = djnameElem.text();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return data;
			}

			@Override
			protected void onPostExecute(NowPlayingData result) {
				super.onPostExecute(result);
				
				final TextView nowPlaying = (TextView)findViewById(R.id.CurrentlyPlaying);
				final TextView onAirDJ = (TextView)findViewById(R.id.OnAirDJ);
				
				if (result != null) {
					nowPlaying.setText(result.nowPlaying);
					onAirDJ.setText(result.onAirDJ);
				} else {
					nowPlaying.setText("ERROR");
					onAirDJ.setText("ERROR");
				}
			}
		}
		GetNowPlayingTask t = new GetNowPlayingTask();
		t.execute();
	}
}