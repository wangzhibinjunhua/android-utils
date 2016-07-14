package com.example.zzz;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import android.R.color;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.trinea.android.common.util.HttpUtils;

import com.sparkle.webservice.Defaults;
import com.sparkle.webservice.WebServer;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * AUTHER wzb<wangzhibin_x@foxmail.com> 2016-2-18下午04:16:50
 */
public class MainActivity extends Activity implements OnClickListener {

	Button btn1, btn2;
	EditText et1;
	String str1 = "";
	String testString = null;
	StringBuilder sb = null;
	CheckBoxPreference cbp;
	Context mContext;
	TextView tv1;
	WebSocketConnection wsc;
	public BluetoothAdapter mBluetoothAdapter;
	public BluetoothManager bluetoothManager;
	private final int REQUEST_ENABLE_BT = 2;
	private NotificationManager mNotifMan;

	private static String rs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		btn1 = (Button) findViewById(R.id.btn1);
		et1 = (EditText) findViewById(R.id.et1);
		btn1.setOnClickListener(this);

		btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(this);
		tv1 = (TextView) findViewById(R.id.tv1);
		wsc = new WebSocketConnection();
		mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// boolean xxx=setWifiApEnabled(true);
		// Log.d("wzb","xx="+xxx);
		// initWebServer();
		// testTo();
		// byteToBase64();
		// String bgData="550e0311040a0018008c012a0000";
		// int
		// bg=Integer.valueOf(bgData.substring(20,22)+bgData.substring(18,20),16);
		// Log.d("wzb","bg="+bg);
		// DecimalFormat df = new DecimalFormat("#.0");
		// String bg_value=df.format((double)bg/18);
		// Log.d("wzb",""+bg_value);
	}

	void con() {
		try {

			wsc.connect("ws://120.76.47.120:8122",
					new WebSocketConnectionHandler() {

						@Override
						public void onBinaryMessage(byte[] payload) {
							Log.d("wzb", "onBinaryMessage size="
									+ payload.length);
						}

						@Override
						public void onClose(int code, String reason) {
							Log.d("wzb", "onClose reason=" + reason);
						}

						@Override
						public void onOpen() {
							Log.d("wzb", "onOpen");
							// wsc.sendTextMessage("Hello!");
							// wsc.disconnect();
						}

						@Override
						public void onRawTextMessage(byte[] payload) {
							Log.d("wzb", "onRawTextMessage size="
									+ payload.length);
						}

						@Override
						public void onTextMessage(String payload) {
							Log.d("wzb", "onTextMessage" + payload);
						}

					});
		} catch (WebSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void base64StringToByte() {

		String base64String = "SlhKMDAwMDA0MDAwMDAwMDMxNTc2ODIxMTAxNzE1ICAzMjAgjoyJgnt1cnFxcXJyc3R1dHNyb2xqZ2NhYWFgX11cWVZVVFRUVFRTU1JSUVFQT01LSUdFREVGSU9XYm97ipejq7CxrqiimpOQj46MioaBe3d0cm9vcXJzdHV1dnV0cm5raGZjYmJiYWBfXl5dXFtbW1pXVVRTU1NRT05OTU5QVmFxgY6Zo6qwtba1s66noZqVkpKQjoqDfXZzb25tbW5ubm9xcnFycW1pZWFeW1lWVlZWVlZWVlVVVFNSUU9OTk5OTk1KSEdHSlJgcoKQmqKpsLW3t7SwqaSem5iXlZKNhoF7eHVzb25tbG1ucXN0c3FtaWZiYF9eXFtbW1pZWVlXVlVVVVZZWlpXVlRSUVFQT05OT1RebX6Om6aus7e6ure0r6qmo6KjoZyTiX94dXRzcm9ta2pqaWprbGxqaGVhXl1dXFtbWllXV1VVV1paWlpZV1ZVVVRSUVBNSkhGRUVFRkhQXGt7ipeiq7K2urq3samhmZOQkI+MiYR+eXZzb21samlpamtsbGtpZ2ViX11bWldWVlVVVVVTU1JSUlJRUFBQT01LSUZEQ0NFSlRgbn2KlZ6mrbCxr66qpJ2YlZGNh4N/eXZybmxraWZlY2ZobG5vbWpmYV9dWldWVFNSUlJRUVFQT05OTU1LSkpJSEdGRENDQ0NESVNjdomaqLG2uru6trKrpqGbmZqamZaPhn54dnZ1dHJvb29xcnJzdHNxbWhlYV9dXVxbW1pZVlVTUVBPUFBQT05NS0lIR0ZERENDRUtXaHqLmaawt72+vbqzq6Obl5OVk5EA"
				+ "SlhKMDAwMDA0MDAwMDAwMDMxNTc2ODIxMTAxNzE1ICAzMjAgamlnZmViYV9dXFxdYGZueICJkp2osre5t7SzsrOzsrCrp6KZko6MjY6PkJKXmpubmZaSj4yJh4SCgH56eHd1c3FxcXFxcXFubm5ubm5tbW1vcW9ubGtramdjYWBgY2lxeYWQnKizvcPJy8vLysjFwLy3s6+rrq2noZmVkIyLi4yOkJOTlZORjoyLiomFgoB9e3l5eXh3dnZ1dnd3dXJua2hoaGloZmNhX1xaVlRSUFBTW2ZxfouYo6uwsrO0tbSyr6uppqKclo+Jg396d3Vzc3R1dnd3d3Z2dnd3dnJua2ppaWhoZ2dmZWNjYmFgX15cWllaXFxaV1VTUVFQUFBPTk9QU1lhanR/iZCZnqKjo5+bl5WRjYeCf3t5d3Ryb29vb29xcnFvbW1sa2ppZ2VhX15eXVtZWVpcXFxbW1taWVlXVlVTUVBOTk5OT1BRVFxjbXeBi5WbnqGioZ6cmpiVko6Jg397eXd1c3Jyc3V3eHh4dnRycW9ubGlnZWNiYWFgYF9fX19fX19fX15eX15dXFxbWldVVFNUVlphanV/ipafqK2wsLCwr62qpqOfnJmVj4mEgoKCgYKCgYGBgYKCg4KAfnl3dnZ1c3FubGtra2tramloaGdmZWVjY2JgX11bWllZV1ZVVVleaHSAi5ahqbC1t7m6urm5t7a0sq6mnZeQjYyMjIuMjI2NjIqHh4mKi4mFgX57enp6eHZ1dXNxcXNzcnFvbWtqaWdmZWNiYWBfX11cXFxbWVlcY3KBkJ6rtby/v8DBwcC+vbu2sq2mnZOLhH97eXkA";
	}

	void testTo() {
		double[] data = new double[] { 2.2593, 8.3651, 0.3210, 108.4896,
				1004.7120 };
		double[] data1 = new double[] { 3.7128, 13.3239, 0.2625, 180.8847,
				657.0235 };
		double[] data2 = new double[] { 1.6850, 4.9143, 0.3580, 61.1992,
				1515.0510 };
		double[] data3 = new double[] { 1.4004, 4.8400, 0.4047, 63.8586,
				1750.1650 };
		// double
		// y=-36.29597-9.78733*data2[0]+9.95341*data2[1]+256.33560*data2[2]-0.08916*data2[3]+0.03333*data2[4];
		double y = 95.23805 - 4.57543 * data3[0] - 12.31469 * data3[1]
				+ 128.00745 * data3[2] + 1.08628 * data3[3] - 0.01737
				* data3[4];
		Log.d("wzb", "y=" + y);

	}

	void byteToBase64() {
		// int[] odata = new
		// int[]{0x4A,0x58,0x4A,0x30,0x30,0x30,0x30,0x30,0x34,0x30,0x30,0x30,0x30,0x30,0x30,0x30
		// ,0x32,0x39,0x30,0x31,0x37,0x35,0x31,0x39,0x31,0x30,0x31,0x34,0x33,0x31,0x20,0x20
		// ,0x32,0x36,0x32,0x20,
		// 0x71,0x70,0x70,0x70,0x71,0x72,0x73,0x73,0x73,0x72,0x72,0x72,0x72,0x73,0x74,0x74,0x73,0x72,0x72,0x71,0x70,0x6E,0x6E,0x6E,0x6D,0x6D,0x6C,0x6B,0x6A,0x69,0x69,0x6C,0x78,0x90,0xAE,0xC9,0xD6,0xDB,0xDC,0xDC,0xDA,0xD7,0xD2,0xCA,0xC1,0xB6,0xAD,0xA5,0x9F,0x9A,0x95,0x91,0x8D,0x8A,0x86,0x82,0x7F,0x7C,0x7C,0x79,0x81,0x84,0x88,0x8C,0x90,0x94,0x97,0x98,0x98,0x96,0x93,0x8F,0x8C,0x88,0x84,0x81,0x7F,0x7C,0x7A,0x79,0x78,0x77,0x77,0x76,0x76,0x76,0x75,0x75,0x74,0x74,0x74,0x74,0x74,0x75,0x75,0x76,0x78,0x79,0x79,0x79,0x79,0x79,0x78,0x77,0x76,0x75,0x74,0x73,0x72,0x71,0x70,0x70,0x72,0x7B,0x8E,0xAA,0xC6,0xD5,0xDB,0xDC,0xDC,0xDB,0xD9,0xD6,0xCF,0xCA,0xC2,0xB9,0xB0,0xA9,0xA3,0x9F,0x9C,0x99,0x96,0x92,0x8D,0x88,0x84,0x81,0x80,0x82,0x85,0x89,0x8E,0x92,0x95,0x97,0x98,0x97,0x96,0x93,0x90,0x8E,0x8B,0x88,0x86,0x83,0x80,0x7E,0x7C,0x7A,0x79,0x79,0x79,0x79,0x79,0x79,0x79,0x78,0x77,0x77,0x76,0x77,0x78,0x78,0x79,0x79,0x79,0x79,0x79,0x79,0x79,0x7A,0x7A,0x7B,0x7B,0x7B,0x7A,0x79,0x78,0x77,0x76,0x75,0x75,0x76,0x7B,0x8B,0xA6,0xC3,0xD5,0xDC,0xDE,0xDE,0xDD,0xDB,0xD8,0xD4,0xCE,0xC6,0xBE,0xB5,0xAF,0xAA,0xA5,0xA2,0x9F,0x9C,0x97,0x93,0x8E,0x89,0x85,0x82,0x81,0x81,0x83,0x85,0x89,0x8C,0x8E,0x90,0x91,0x91,0x91,0x8F,0x8D,0x89,0x85,0x82,0x7F,0x7C,0x7A,0x79,0x79,0x78,0x78,0x77,0x76,0x75,0x75,0x76,0x77,0x78,0x78,0x79,0x79,0x79,0x7A,0x7B,0x7C,0x7E,0x7F,0x80,0x7F,0x7E,0x7C,0x7B,0x7B,0x7B,0x7B,0x7B,0x7B,0x7A,0x79,0x79,0x77,0x77,0x76,0x76,0x77,0x79,0x80,0x91,0xAD,0xCA,0xD9,0xDD,0xDE,0xDE,0xDD,0xDB,0xD7,0xD3,0xCD,0xC5,0xBD,0xB5,0xAF,0xA9,0xA4,0x9F,0x9C,0x98,0x93,0x8D,0x86,0x80,0x7C,0x7A,0x7B,0x7E,0x83,0x88,0x8C,0x8F,0x8F,0x8F,0x89,0x8B,0x88,0x85,0x82,0x7E,0x7B,0x78,0x75,0x74,0x6E,0x72,0x71,0x71,0x71,0x70,0x6F,0x6E,0x6E,0x6F,0x6E,0x6F,0x6F,0x6F,0x6E,0x6E,0x6E,0x6F,0x70,0x70,0x6E,0x6F,0x6E,0x6D,0x6C,0x6C,0x6C,0x6C,0x6D,0x6C,0x69,0x6A,0x69,0x68,0x68,0x6A,0x74,0x8A,0xAB,0xC9,0xD6,0xDC,0xDC,0xDB,0xD9,0xD5,0xCF,0xC5,0xBB,0xB0,0xA5,0xA0,0x9B,0x98,0x93,0x8D,0x88,0x82,0x7D,0x78,0x77,0x7A,0x7F,0x85,0x8C,0x92,0x96,0x98,0x98,0x96,0x93,0x90,0x8D,0x89,0x85,0x82,0x7F,0x7C,0x7B,0x79,0x79,0x78,0x78,0x77,0x77,0x77,0x77,0x77,0x78,0x79,0x7A,0x7A,0x79,0x78,0x78,0x79,0x78,0x78,0x78,0x79,0x79,0x78,0x76,0x74,0x71,0x6F,0x6D,0x6C,0x6B,0x6C,0x70,0x7B,0x93,0xB4,0xCE,0xD9,0xDD,0xDD,0xDC,0xD9,0xD4,0xCD,0xC3,0xB9,0xB1,0xAB,0xA7,0xA4,0x9F,0x9B,0x96,0x8F,0x87,0x81,0x7C,0x7A,0x7B,0x7E,0x84,0x89,0x8E,0x91,0x92,0x91,0x8F,0x8C,0x8A,0x86,0x82,0x7E,0x7A,0x77,0x74,0x73,0x74,0x74,0x73,0x72,0x71,0x71,0x71,0x6F,0x6E,0x6D,0x6F,0x70,0x71,0x70,0x70,0x70,0x72,0x73,0x74,0x74,0x73,0x72,0x72,0x71,0x70,0x70,0x6F,0x70,0x6F,0x6D,0x6B,0x6A,0x69,0x6A,0x6D,0x7A,0x94,0xB7,0xD1,0xDB,0xDE,0xDE,0xDC,0xDA,0xD7,0xD2,0xCC,0xC4,0xBB,0xB4,0xAE,0xAA,0xA6,0xA3,0xA1,0x9E,0x98,0x90,0x88,0x80,0x7A,0x79,0x7A,0x7D,0x81,0x85,0x88,0x8A,0x89,0x88,0x86,0x83,0x7F,0x7B,0x76,0x73,0x71,0x6F,0x6E,0x6D,0x6C,0x6B,0x6A,0x69,0x69,0x69,0x69,0x69,0x69,0x68,0x67,0x67,0x67,0x67,0x67,0x67,0x68,0x68,0x67,0x67,0x66,0x66,0x66,0x66,0x66,0x65,0x64,0x64,0x63,0x62,0x62,0x60,0x5F,0x5E,0x5D,0x5D,0x5E,0xFF
		// };

		int[] odata = new int[] { 0x4A, 0x58, 0x4A, 0x30, 0x30, 0x30, 0x30,
				0x30, 0x34, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32,
				0x39, 0x30, 0x31, 0x37, 0x35, 0x31, 0x39, 0x31, 0x30, 0x31,
				0x34, 0x33, 0x31, 0x20, 0x20, 0x32, 0x36, 0x32, 0x20, 0x71,
				0x70, 0x70, 0x70, 0x71, 0x72, 0x73, 0x73, 0x73, 0x72, 0x72,
				0x72, 0x72, 0x73, 0x74, 0x74, 0x73, 0x72, 0x72, 0x71, 0x70,
				0x6E, 0x6E, 0x6E, 0x6D, 0x6D, 0x6C, 0x6B, 0x6A, 0x69, 0x69,
				0x6C, 0x78, 0x90, 0xAE, 0xC9, 0xD6, 0xDB, 0xDC, 0xDC, 0xDA,
				0xD7, 0xD2, 0xCA, 0xC1, 0xB6, 0xAD, 0xA5, 0x9F, 0x9A, 0x95,
				0x91, 0x8D, 0x8A, 0x86, 0x82, 0x7F, 0x7C, 0x7C, 0x79, 0x81,
				0x84, 0x88, 0x8C, 0x90, 0x94, 0x97, 0x98, 0x98, 0x96, 0x93,
				0x8F, 0x8C, 0x88, 0x84, 0x81, 0x7F, 0x7C, 0x7A, 0x79, 0x78,
				0x77, 0x77, 0x76, 0x76, 0x76, 0x75, 0x75, 0x74, 0x74, 0x74,
				0x74, 0x74, 0x75, 0x75, 0x76, 0x78, 0x79, 0x79, 0x79, 0x79,
				0x79, 0x78, 0x77, 0x76, 0x75, 0x74, 0x73, 0x72, 0x71, 0x70,
				0x70, 0x72, 0x7B, 0x8E, 0xAA, 0xC6, 0xD5, 0xDB, 0xDC, 0xDC,
				0xDB, 0xD9, 0xD6, 0xCF, 0xCA, 0xC2, 0xB9, 0xB0, 0xA9, 0xA3,
				0x9F, 0x9C, 0x99, 0x96, 0x92, 0x8D, 0x88, 0x84, 0x81, 0x80,
				0x82, 0x85, 0x89, 0x8E, 0x92, 0x95, 0x97, 0x98, 0x97, 0x96,
				0x93, 0x90, 0x8E, 0x8B, 0x88, 0x86, 0x83, 0x80, 0x7E, 0x7C,
				0x7A, 0x79, 0x79, 0x79, 0x79, 0x79, 0x79, 0x79, 0x78, 0x77,
				0x77, 0x76, 0x77, 0x78, 0x78, 0x79, 0x79, 0x79, 0x79, 0x79,
				0x79, 0x79, 0x7A, 0x7A, 0x7B, 0x7B, 0x7B, 0x7A, 0x79, 0x78,
				0x77, 0x76, 0x75, 0x75, 0x76, 0x7B, 0x8B, 0xA6, 0xC3, 0xD5,
				0xDC, 0xDE, 0xDE, 0xDD, 0xDB, 0xD8, 0xD4, 0xCE, 0xC6, 0xBE,
				0xB5, 0xAF, 0xAA, 0xA5, 0xA2, 0x9F, 0x9C, 0x97, 0x93, 0x8E,
				0x89, 0x85, 0x82, 0x81, 0x81, 0x83, 0x85, 0x89, 0x8C, 0x8E,
				0x90, 0x91, 0x91, 0x91, 0x8F, 0x8D, 0x89, 0x85, 0x82, 0x7F,
				0x7C, 0x7A, 0x79, 0x79, 0x78, 0x78, 0x77, 0x76, 0x75, 0x75,
				0x76, 0x77, 0x78, 0x78, 0x79, 0x79, 0x79, 0x7A, 0x7B, 0x7C,
				0x7E, 0x7F, 0x80, 0x7F, 0x7E, 0x7C, 0x7B, 0x7B, 0x7B, 0x7B,
				0x7B, 0x7B, 0x7A, 0x79, 0x79, 0x77, 0x77, 0x76, 0x76, 0x77,
				0x79, 0x80, 0x91, 0xAD, 0xCA, 0xD9, 0xDD, 0xDE, 0xDE, 0xDD,
				0xDB, 0xD7, 0xD3, 0xCD, 0xC5, 0xBD, 0xB5, 0xAF, 0xA9, 0xA4,
				0x9F, 0x9C, 0x98, 0x93, 0x8D, 0x86, 0x80, 0x7C, 0x7A, 0x7B,
				0x7E, 0x83, 0x88, 0x8C, 0x8F, 0x8F, 0x8F, 0x89, 0x8B, 0x88,
				0x85, 0x82, 0x7E, 0x7B, 0x78, 0x75, 0x74, 0x6E, 0x72, 0x71,
				0x71, 0x71, 0x70, 0x6F, 0x6E, 0x6E, 0x6F, 0x6E, 0x6F, 0x6F,
				0x6F, 0x6E, 0x6E, 0x6E, 0x6F, 0x70, 0x70, 0x6E, 0x6F, 0x6E,
				0x6D, 0x6C, 0x6C, 0x6C, 0x6C, 0x6D, 0x6C, 0x69, 0x6A, 0x69,
				0x68, 0x68, 0x6A, 0x74, 0x8A, 0xAB, 0xC9, 0xD6, 0xDC, 0xDC,
				0xDB, 0xD9, 0xD5, 0xCF, 0xC5, 0xBB, 0xB0, 0xA5, 0xA0, 0x9B,
				0x98, 0x93, 0x8D, 0x88, 0x82, 0x7D, 0x78, 0x77, 0x7A, 0x7F,
				0x85, 0x8C, 0x92, 0x96, 0x98, 0x98, 0x96, 0x93, 0x90, 0x8D,
				0x89, 0x85, 0x82, 0x7F, 0x7C, 0x7B, 0x79, 0x79, 0x78, 0x78,
				0x77, 0x77, 0x77, 0x77, 0x77, 0x78, 0x79, 0x7A, 0x7A, 0x79,
				0x78, 0x78, 0x79, 0x78, 0x78, 0x78, 0x79, 0x79, 0x78, 0x76,
				0x74, 0x71, 0x6F, 0x6D, 0x6C, 0x6B, 0x6C, 0x70, 0x7B, 0x93,
				0xB4, 0xCE, 0xD9, 0xDD, 0xDD, 0xDC, 0xD9, 0xD4, 0xCD, 0xC3,
				0xB9, 0xB1, 0xAB, 0xA7, 0xA4, 0x9F, 0x9B, 0x96, 0x8F, 0x87,
				0x81, 0x7C, 0x7A, 0x7B, 0x7E, 0x84, 0x89, 0x8E, 0x91, 0x92,
				0x91, 0x8F, 0x8C, 0x8A, 0x86, 0x82, 0x7E, 0x7A, 0x77, 0x74,
				0x73, 0x74, 0x74, 0x73, 0x72, 0x71, 0x71, 0x71, 0x6F, 0x6E,
				0x6D, 0x6F, 0x70, 0x71, 0x70, 0x70, 0x70, 0x72, 0x73, 0x74,
				0x74, 0x73, 0x72, 0x72, 0x71, 0x70, 0x70, 0x6F, 0x70, 0x6F,
				0x6D, 0x6B, 0x6A, 0x69, 0x6A, 0x6D, 0x7A, 0x94, 0xB7, 0xD1,
				0xDB, 0xDE, 0xDE, 0xDC, 0xDA, 0xD7, 0xD2, 0xCC, 0xC4, 0xBB,
				0xB4, 0xAE, 0xAA, 0xA6, 0xA3, 0xA1, 0x9E, 0x98, 0x90, 0x88,
				0x80, 0x7A, 0x79, 0x7A, 0x7D, 0x81, 0x85, 0x88, 0x8A, 0x89,
				0x88, 0x86, 0x83, 0x7F, 0x7B, 0x76, 0x73, 0x71, 0x6F, 0x6E,
				0x6D, 0x6C, 0x6B, 0x6A, 0x69, 0x69, 0x69, 0x69, 0x69, 0x69,
				0x68, 0x67, 0x67, 0x67, 0x67, 0x67, 0x67, 0x68, 0x68, 0x67,
				0x67, 0x66, 0x66, 0x66, 0x66, 0x66, 0x65, 0x64, 0x64, 0x63,
				0x62, 0x62, 0x60, 0x5F, 0x5E, 0x5D, 0x5D, 0x5E, 0xFF };
		// test
		int count = 0;
		int ret = 0;
		for (int j = odata.length - 600; j < (odata.length - 1); j++) {
			odata[j] = (int) (odata[j] * 1.15);
			// odata[j]=(int) (odata[j]-150);
			if (odata[j] > 255) {
				Log.d("wzb", "j=" + j + " odata=" + odata[j]);
				odata[j] = 255;
				count++;
			}
			if (odata[j] < 1) {
				odata[j] = 1;
				ret++;
			}
		}
		Log.d("wzb", "count=" + count + "ret=" + ret);
		// test
		byte[] ndata = new byte[odata.length];
		for (int i = 0; i < odata.length; i++) {
			ndata[i] = (byte) (odata[i]);
		}
		String base64String = Base64
				.encodeToString(ndata, TRIM_MEMORY_COMPLETE);
		Log.d("wzb", "" + base64String);
	}

	public boolean setWifiApEnabled(boolean enable) {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (enable) {
			wifiManager.setWifiEnabled(false);
		}

		try {
			WifiConfiguration apConfiguration = new WifiConfiguration();
			apConfiguration.SSID = "HYZL-MIFI";
			apConfiguration.preSharedKey = "123123456";
			apConfiguration.status = WifiConfiguration.Status.ENABLED;
			apConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);
			apConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			apConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			apConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			apConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			apConfiguration.allowedProtocols
					.set(WifiConfiguration.Protocol.RSN);
			Method method = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			Log.d("wzb", "1111111");
			return (Boolean) method
					.invoke(wifiManager, apConfiguration, enable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("wzb", "222");
			return false;
		}
	}

	public void initWebServer() {
		Defaults.setPort(8080);
		Defaults.setRoot("/storage/sdcard0/");
		Defaults.setIndexPage("test.php");
		Context context = getApplicationContext();
		WebServer.Start(context);
	}

	public static String string2Unicode(String string) {

		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {

			// 取出每一个字符
			char c = string.charAt(i);

			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}

	public String getSdTotalSize(Context context) {
		StatFs sf = new StatFs("/mnt/sdcard");
		long blockSize = sf.getBlockSize();
		long totalBlocks = sf.getBlockCount();
		return Formatter.formatFileSize(context, blockSize * totalBlocks);
	}

	public String getSdAvailableSize(Context context) {
		StatFs sf = new StatFs("/mnt/sdcard");
		long blockSize = sf.getBlockSize();
		long availableBlocks = sf.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}

	void getUAinfo() {

		WebView mWebView = new WebView(mContext);
		WebSettings mWebSettings = mWebView.getSettings();
		String mUAString = mWebSettings.getUserAgentString();
		Log.d("wzb", "uastring=" + mUAString);
		tv1.setText(mUAString);
	}

	void openfolder() {
		Intent intent = new Intent();

		// 浏览器的主Activity
		intent.setComponent(new ComponentName("com.mediatek.filemanager",
				"com.mediatek.filemanager.FileManagerOperationActivity"));
		String path = "";
		try {
			path = Environment.getExternalStorageDirectory().getCanonicalPath()
					.toString()
					+ "/录音文件";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intent.putExtra("select_path", path);
		startActivity(intent);
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
	};

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data=msg.getData();
			String val=data.getString("value");
			Log.d("wzb","val="+val);
			switch (msg.what) {
			case 12:
				doSnap();
				break;
			case 13:
				// finish();
				// System.exit(0);
				if (mCamera != null) {
					mCamera.stopPreview();
					mCamera.setPreviewCallback(null);
					mCamera.release();
				}
				break;
			case 0xff:
				String msg1 = http_request("http://lib.huayinghealth.com/");
				//Log.d("wzb", "msg1=" + msg1);
				break;
			default:
				break;
			}
		}
	};
	private static int currentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;

	@SuppressLint("NewApi")
	public void setCameraDisplayOrientation() {
		Camera.CameraInfo info = new Camera.CameraInfo();
		mCamera.getCameraInfo(currentCamera, info);
		int rotation = this.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int resultA = 0, resultB = 0;
		if (currentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
			resultA = (info.orientation - degrees + 360) % 360;
			resultB = (info.orientation - degrees + 360) % 360;
			mCamera.setDisplayOrientation(resultA);
		} else {
			resultA = (360 + 360 - info.orientation - degrees) % 360;
			resultB = (info.orientation + degrees) % 360;
			mCamera.setDisplayOrientation(resultA);
		}
		Camera.Parameters params = mCamera.getParameters();
		params.setRotation(resultB);
		mCamera.setParameters(params);
	}

	private ImageView mImageView;
	private Camera mCamera;

	@SuppressLint("NewApi")
	public void doSnap() {
		mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
		if (mCamera == null) {

			Log.d("wzb", "tried to snap when camera was inactive");
			return;
		}
		Log.d("wzb", "do snap=================");
		Camera.Parameters params = mCamera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPictureSizes();
		Camera.Size size = sizes.get(0);
		for (int i = 0; i < sizes.size(); i++) {
			if (sizes.get(i).width > size.width)
				size = sizes.get(i);
		}
		params.setPictureSize(size.width, size.height);
		mCamera.setParameters(params);
		Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream outStream = null;
				try {
					String filename = String.format("/sdcard/img_wear_%d.jpg",
							System.currentTimeMillis());
					outStream = new FileOutputStream(filename);
					outStream.write(data);
					outStream.close();

					Log.d("wzb", "wrote bytes: " + data.length);
					sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
							Uri.parse("file://" + filename)));

					// mCamera.startPreview();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		setCameraDisplayOrientation();
		mCamera.startPreview();
		mCamera.takePicture(null, null, jpegCallback);
		mHandler.sendEmptyMessageDelayed(13, 1000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn1:
			// str1=et1.getText().toString();
			// Log.d("wzb",""+string2Unicode(str1));
			// Context context = getApplicationContext();
			// WebServer.Stop(context);
			// mContext.sendBroadcast(new
			// Intent("com.android.custom.nine_longpress"));
			// doSnap();
			// wsc.sendTextMessage("wsc\n");
			// enable_bt();
			notify_normal_moreLine();
			break;
		case R.id.btn2:
			// getUAinfo();
			// openfolder();
			String msg = http_request("http://lib.huayinghealth.com/");
			Log.d("wzb", "msg=" + msg);

			break;

		default:
			android.os.SystemClock.sleep(1000);
			break;
		}

	}

	private String http_request(final String http_url) {

		Runnable request_task = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//rs = HttpUtils.httpGetString(http_url);
				//Log.d("wzb", "rs rs=" + rs);
				Message msg = new Message();  
	            Bundle data = new Bundle();  
	            data.putString("value", HttpUtils.httpGetString(http_url));  
	            msg.setData(data);  
	           // handler.sendMessage(msg);
	            mHandler.sendMessage(msg);
				mHandler.sendEmptyMessageDelayed(0xff, 3000*10);

			}
		};

		Thread request_thread = new Thread(request_task);
		request_thread.start();
		
		return rs;
	}

	private NotifyUtil currentNotify;
	private int requestCode = (int) SystemClock.uptimeMillis();

	private void notify_normal_moreLine() {
		final Uri uri = Uri.parse("http://www.baidu.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(mContext,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// startActivity(intent);
		// 设置想要展示的数据内容
		// Intent intent = new Intent(mContext, OtherActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// PendingIntent pIntent = PendingIntent.getActivity(mContext,
		// requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// PendingIntent pIntent=null;
		int smallIcon = R.drawable.ic_launcher;
		String ticker = "您有一条新通知";
		String title = "朱立伦请辞国民党主席 副主席黄敏惠暂代党主席";
		String content = "play.google.com/store/apps/developer?id=Microsoft+Corporation据台湾“中央社”报道，国民党主席朱立伦今天(18日)向中常会报告，为败选请辞党主席一职，他感谢各位中常委的指教包容，也宣布未来党务工作由副主席黄敏惠暂代，完成未来所有补选工作。";
		// 实例化工具类，并且调用接口
		NotifyUtil notify2 = new NotifyUtil(mContext, 2);
		notify2.notify_normail_moreline(pIntent, smallIcon, ticker, title,
				content, true, true, false);
		currentNotify = notify2;
	}

	private boolean setBluetoothScanMode(BluetoothAdapter ba, int scanMode) {
		Method method = null;
		// final BluetoothAdapter btAdapter =
		// BluetoothAdapter.getDefaultAdapter();;
		final BluetoothAdapter btAdapter = ba;
		if (!btAdapter.isEnabled()) {
			Log.d("wzb", "BT adapter is off, turning on");
			btAdapter.enable();
		}

		try {
			method = btAdapter.getClass().getMethod("setScanMode", int.class);
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		}

		try {
			method.invoke(btAdapter, scanMode);
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
		boolean start = btAdapter.isDiscovering();
		Log.d("wzb", "start=" + start);

		int xx = btAdapter.getScanMode();
		Log.d("wzb", "xx=" + xx);
		return true;
	}

	@TargetApi(18)
	void enable_bt() {
		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		// 蓝牙是否开启
		if (null == mBluetoothAdapter || !mBluetoothAdapter.isEnabled()) {
			// Intent enableIntent = new Intent(
			// BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			mBluetoothAdapter.enable();
			// Intent discoverableIntent = new Intent(
			// BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE); discoverableIntent
			// .putExtra( BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
			// 60*60*24);
			// startActivityForResult(discoverableIntent, 2);

		}
		mContext.sendBroadcast(new Intent("com.android.custom.bt_discoverable"));
		// boolean
		// ret=setBluetoothScanMode(mBluetoothAdapter,BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
		// Log.d("wzb","ret="+ret);

	}

}
