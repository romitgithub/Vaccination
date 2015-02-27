import java.io.InputStream;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

public class Utils 
{
	static Context context;
	static String host;
	static String tcpHostname;
	static int tcpPort;
	
	public Utils(Context context)
	{
		Utils.host = "http://172.20.185.109:3000";
		Utils.tcpHostname = "172.20.185.109";
		Utils.tcpPort = 3001;
	}
	
	public static byte[] post(String url, AbstractHttpEntity entity)
	{
		try
		{
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 120000);
			
			HttpClient client = new DefaultHttpClient(params);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Connection", "Keep-Alive");

			if(entity != null)
			{
				entity.setChunked(true);
				entity.setContentType("application/json");
				httpPost.setEntity( entity);
			}

			HttpResponse response = client.execute(httpPost);
			byte[] buffer = new byte[1024];
			LinkedList<Byte> contents = new LinkedList<Byte>();
			InputStream inputStream = response.getEntity().getContent();
			int read = -1;
			while((read = inputStream.read(buffer)) != -1)
			{
				for(int i = 0; i < read; i++)
				{
					contents.add(buffer[i]);
				}
			}

			byte[] responseBytes = new byte[contents.size()];
			int i = 0;
			for(Byte b : contents)
			{
				responseBytes[i] = b.byteValue();
				i++;
			}

			contents.clear();

			return responseBytes;
		}
		catch(Exception e)
		{
			Log.e("post", e.toString());

			return null;
		}
	}
}
