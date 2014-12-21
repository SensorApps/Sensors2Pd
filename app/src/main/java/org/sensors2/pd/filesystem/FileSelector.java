package org.sensors2.pd.filesystem;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.sensors2.pd.R;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by thomas on 09.12.14.
 */
public class FileSelector {
	private Activity activity;
	private Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private Boolean firstLvl = true;
	private ListAdapter adapter;

	public FileSelector(Activity activity) {
		this.activity = activity;
	}

	public boolean canLoad() {
		String auxSDCardStatus = Environment.getExternalStorageState();
		if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED))
			return true;
		else if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Toast.makeText(
					activity,
					"Warning, the SDCard it's only in read mode.\nthis does not result in malfunction"
							+ " of the read aplication", Toast.LENGTH_LONG)
					.show();
			return true;
		} else if (auxSDCardStatus.equals(Environment.MEDIA_NOFS)) {
			Toast.makeText(
					activity,
					"Error, the SDCard can be used, it has not a corret format or "
							+ "is not formated.", Toast.LENGTH_LONG)
					.show();
			return false;
		} else if (auxSDCardStatus.equals(Environment.MEDIA_REMOVED)) {
			Toast.makeText(
					activity,
					"Error, the SDCard is not found, to use the reader you need "
							+ "insert a SDCard on the device.",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (auxSDCardStatus.equals(Environment.MEDIA_SHARED)) {
			Toast.makeText(
					activity,
					"Error, the SDCard is not mounted beacuse is using "
							+ "connected by USB. Plug out and try again.",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE)) {
			Toast.makeText(
					activity,
					"Error, the SDCard cant be mounted.\nThe may be happend when the SDCard is corrupted "
							+ "or crashed.", Toast.LENGTH_LONG).show();
			return false;
		} else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED)) {
			Toast.makeText(
					activity,
					"Error, the SDCArd is on the device but is not mounted."
							+ "Mount it before use the app.",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public void show() {
		loadFileList();
		createAdapter();
	}

	private void createAdapter() {
		adapter = new ArrayAdapter<Item>(activity,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);
				int dp5 = (int) (5 * activity.getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);
				return view;
			}
		};
	}

	private void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Toast.makeText(activity, "Unable to read SD Card!", Toast.LENGTH_SHORT).show();
			Log.e("error", "unable to write on the sd card ");
		}
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();
				}
			};
			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.file_icon);
				File sel = new File(path, fList[i]);
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.directory_icon;
				}
			}
			if (!firstLvl) {
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		}
	}

	private class Item {
		public String file;
		public int icon;
		public Item(String file, Integer icon) {
			this.file = file;
			this.icon = icon;
		}
		@Override
		public String toString() {
			return file;
		}
	}
}
