package com.unipiazza.lotteriaapp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;

public class Utils {

	public static int getPrizeImageByTitle(String title) {
		if (title.equals("Panino") || title.equals("Hamburger"))
			return R.drawable.up_hamburger;
		else if (title.equals("Spritz"))
			return R.drawable.up_spritz;
		else if (title.equals("Caffè"))
			return R.drawable.up_bar;
		else if (title.equals("Cicchetto"))
			return R.drawable.up_club_sandwich;
		else if (title.equals("Stickers"))
			return R.drawable.up_sticker_small;
		else if (title.equals("Frappè") || title.equals("Spremuta"))
			return R.drawable.up_juice;
		else if (title.equals("Birra") || title.equals("Birreria") || title.equals("Pub"))
			return R.drawable.up_beer;
		else if (title.equals("Prosecco"))
			return R.drawable.up_wine;
		else if (title.equals("Cocktail"))
			return R.drawable.up_cocktail;
		else if (title.equals("Toast"))
			return R.drawable.up_toast;
		else if (title.equals("Limoncello"))
			return R.drawable.up_limoncello;
		else if (title.equals("Shottino"))
			return R.drawable.up_shots;
		else if (title.equals("Macedonia"))
			return R.drawable.up_salad;
		else if (title.equals("Menù"))
			return R.drawable.up_star;
		else if (title.equals("Bibita Analcolica"))
			return R.drawable.up_coke;
		else if (title.equals("Trancio di Pizza"))
			return R.drawable.up_pizza_slice;
		else if (title.equals("Tramezzino"))
			return R.drawable.up_sandwich;
		else if (title.equals("Cartoleria") || title.equals("Stampa"))
			return R.drawable.up_print;
		else if (title.equals("Bott. Vino"))
			return R.drawable.up_wine_bottleglass;
		else if (title.equals("Caraffa Spritz"))
			return R.drawable.up_spritz_carafe;
		else if (title.equals("Caraffa Birra"))
			return R.drawable.up_beer_carafe;
		else if (title.equals("Gelato"))
			return R.drawable.up_icecream;
		else if (title.equals("Abbigliamento"))
			return R.drawable.up_clothing;
		else if (title.equals("Sconto"))
			return R.drawable.up_discount;
		else if (title.equals("Maglietta Personalizzabile"))
			return R.drawable.up_shirt;
		else if (title.equals("Penna"))
			return R.drawable.up_pens;
		else if (title.equals("Kebab"))
			return R.drawable.up_kebab;
		else if (title.equals("Cover Smartphone"))
			return R.drawable.up_mobile;
		else if (title.equals("Libro"))
			return R.drawable.up_book;
		else if (title.equals("Pizza"))
			return R.drawable.up_pizza;
		return R.drawable.up_diamond;
	}

	public static AlertDialog createErrorDialog(Context context, int title, int content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(content)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		return builder.create();
	}

	public static AlertDialog createErrorDialog(Context context, int title, String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
				.setMessage(content)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		return builder.create();
	}

	public static String distFrom(double lat1, double lng1, double lat2, double lng2) {
		Location locationA = new Location("point A");
		locationA.setLatitude(lat1);
		locationA.setLongitude(lng1);
		Location locationB = new Location("point B");
		locationB.setLatitude(lat2);
		locationB.setLongitude(lng2);
		int distance = (int) locationA.distanceTo(locationB);
		if (distance > 999) {
			distance = distance / 1000;
			return distance + "km";
		} else {
			return distance + "m";
		}
	}

	public static int distFromInt(double lat1, double lng1, double lat2, double lng2) {
		Location locationA = new Location("point A");
		locationA.setLatitude(lat1);
		locationA.setLongitude(lng1);
		Location locationB = new Location("point B");
		locationB.setLatitude(lat2);
		locationB.setLongitude(lng2);
		return (int) locationA.distanceTo(locationB);
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
				);

		return image;
	}

	public static String formatTime(String created_at) {
		SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date dateStr;
		CharSequence time = "";
		try {
			dateStr = parserSDF.parse(created_at);

			time = DateUtils.getRelativeTimeSpanString(dateStr.getTime(), System.currentTimeMillis()
					, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time.toString().replace("giorni fa", "g").replace("settembre", "sett").replace("novembre", "nov");
	}
}
