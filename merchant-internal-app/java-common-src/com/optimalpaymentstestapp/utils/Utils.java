package com.optimalpaymentstestapp.utils;

import java.security.SecureRandom;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * @author ManishaR
 * 
 */
public class Utils {
	// progress Dialog
	private static ProgressDialog mProgressDialog;

	/**
	 * Check whether or not the String is empty 
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(String source) {
		return (null == source || source.trim().equals("")) ? true : false;
	}
	
	/**
	 * Checks Network Availability Input: context Returns true: Network
	 * available Returns false: Network not available
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isInternetAvailable = false;

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

			if (networkInfo != null && (networkInfo.isConnected())&& networkInfo.isAvailable()) {
				isInternetAvailable = true;
			}
		} catch (Exception exception) {

		}

		return isInternetAvailable;
	}

	/**
	 * This Method uses ConnectivityManager
	 * <p>
	 * checks if <b>connectivity exists</b> or is in the process of <b>being
	 * established</b>.
	 * </P>
	 * <P>
	 * will not <b>guarantee</b> the instant availability.
	 * </P>
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	

	/**
	 * This method is called when start Progress Dialog
	 * 
	 * @return void
	 * 
	 */
	public static void startProgressDialog(Context context,String message) {		
		 mProgressDialog = ProgressDialog.show(context, "",message);
	}

	/**
	 * This method is called when stop Progress Dialog
	 * 
	 * @return void
	 * 
	 */
	public static void stopProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		
		}
	}
	
	/**
	 * show alert dialog
	 * 
	 * @param source
	 * @return void
	 */

	public static void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		  alertDialog.show();
	}
	/**
	 * This method is used for get twelve alphanumeric random number 
	 * 
	 * @return String
	 * 
	 * 
	 */
	public static String twelveDigitRandomAlphanumeric() {
		SecureRandom random = new SecureRandom();
		String strTwelveDigitRandomAlphanumeric = new java.math.BigInteger(60, random).toString(32).toUpperCase();
		return strTwelveDigitRandomAlphanumeric;
	}

	/**
	 * This method is used for get four digit random number
	 * 
	 * @return String
	 * 
	 * 
	 */
	public static String eightDigitRandomNumber() {
		Random rand = new Random();

		int num = rand.nextInt(90000000) + 10000000;
		System.out.println(num);

		return "" + num;

	}
    
}
